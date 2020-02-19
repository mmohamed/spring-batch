

#### Create
```
# Copy
kubectl get secret front-tls -n=front --export -o yaml | kubectl apply -n=springbatch -f -
# Create
CRT=$(cat front/tls/cert | base64)
KEY=$(cat front/tls/key | base64)
```

```
sed -i "s/{{crt}}/`echo $CRT`/" app.yaml
sed -i "s/{{key}}/`echo $KEY`/" app.yaml
sed -i "s/{{host}}/[YOUR-HOSTNAME]/" app.yaml
sed -i "s/{{commit}}/[GIT_COMMIT]/" app.yaml
```

```
kubectl apply -f mysql.yaml
kubectl apply -f app.yaml
```


#### Update
```
kubectl patch deployment springbatch -n springbatch -p "{"spec":{"template":{"metadata":{"labels":{"commit":"$commitID"}}}}}"
```
