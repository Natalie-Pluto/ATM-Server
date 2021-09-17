DROP SCHEMA IF EXISTS atmserver CASCADE;
CREATE SCHEMA atmserver;
SET search_path to 'atmserver';

DROP TABLE IF EXISTS Card CASCADE;
CREATE TABLE Card (
    card_number CHAR(5) PRIMARY KEY NOT NULL,
    pin CHAR(4) NOT NULL,
    issue_date DATE NOT NULL,
    exp_date DATE NOT NULL,
    blocked BOOLEAN NOT NULL,
    balance DECIMAL NOT NULL,
    confiscated BOOLEAN NOT NULL
);
