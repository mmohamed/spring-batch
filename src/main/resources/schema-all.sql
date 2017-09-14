DROP TABLE IF EXISTS person ;

CREATE TABLE person  (
    id  INTEGER(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    reference VARCHAR(20),
    registred_at DATE
);