#!/bin/bash

if [ -z "$CRT" ] || [ -z "$KEY" ]; then
	echo "TLS CRT/KEY environment value not found !"
	exit 1
fi

commitID=$(git rev-parse --short HEAD)

if [ $? != 0 ] || [ -z "$commitID" ]; then
	echo "Unable to determinate CommitID !"
	exit 1
fi

echo "Deploy for CommitID : ${commitID}"

bddep=$(kubectl get deployment mysql -n springbatch)
if [ $? != 0 ]; then
	# create new deploy
	kubectl apply -f mysql.yaml
	if [ $? != 0 ]; then
		echo "Unable to deploy database !"
		exit 1
	fi	
fi

# wait for ready
attempts=0
rolloutStatusCmd="kubectl rollout status deployment/mysql -n springbatch"
until $rolloutStatusCmd || [ $attempts -eq 60 ]; do
  $rolloutStatusCmd
  attempts=$((attempts + 1))
  sleep 10
done

appdep=$(kubectl get deployment springbatch -n springbatch)
if [ $? != 0 ]; then
	# create new deploy
	sed -i "s|{{crt}}|`echo $CRT`|g" app.yaml
	sed -i "s|{{key}}|`echo $KEY`|g" app.yaml
	sed -i "s|{{host}}/springbatch.medinvention.dev/g" app.yaml
	sed -i "s|{{commit}}|`echo $commitID`|g" app.yaml

	kubectl apply -f app.yaml
	if [ $? != 0 ]; then
		echo "Unable to deploy application !"
		exit 1
	fi	
else
	# patch it
	kubectl patch deployment springbatch -n springbatch -p "{"spec":{"template":{"metadata":{"labels":{"commit":"$commitID"}}}}}"
	if [ $? != 0 ]; then
		echo "Unable to patch application deploy !"
		exit 1
	fi	
fi

# wait for ready
attempts=0
rolloutStatusCmd="kubectl rollout status deployment/springbatch -n springbatch"
until $rolloutStatusCmd || [ $attempts -eq 60 ]; do
  $rolloutStatusCmd
  attempts=$((attempts + 1))
  sleep 10
done