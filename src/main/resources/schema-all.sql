DROP TABLE IF EXISTS batch_job_execution ;
DROP TABLE IF EXISTS batch_job_execution_context ;
DROP TABLE IF EXISTS batch_job_execution_params ;
DROP TABLE IF EXISTS batch_job_execution_seq ;
DROP TABLE IF EXISTS batch_job_instance ;
DROP TABLE IF EXISTS batch_job_seq;
DROP TABLE IF EXISTS batch_step_execution ;
DROP TABLE IF EXISTS batch_step_execution_context ;
DROP TABLE IF EXISTS batch_step_execution_seq ;

DROP TABLE IF EXISTS person ;

CREATE TABLE person  (
    id  INTEGER(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    salary INTEGER(11)  NOT NULL,
    tax INTEGER(11),
    registration_number VARCHAR(20) NOT NULL,
    registred_at DATE NOT NULL
);