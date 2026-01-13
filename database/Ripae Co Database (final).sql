DROP DATABASE IF EXISTS RIPAE_CO;
CREATE DATABASE RIPAE_CO;
USE RIPAE_CO;
CREATE TABLE CUSTOMER
(
	Customer_ID  BIGINT  NOT NULL ,
	Customer_Name  VARCHAR(255) NOT NULL ,
	Customer_Type  VARCHAR(255) NOT NULL ,
	Notes  VARCHAR(255)  NULL 
);

ALTER TABLE CUSTOMER
	ADD CONSTRAINT  XPKCUSTOMER PRIMARY KEY (Customer_ID);

CREATE TABLE ACCOUNT
(
	Account_ID  BIGINT  NOT NULL ,
	Customer_ID  BIGINT  NOT NULL ,
	Account_Name  VARCHAR(255) NOT NULL ,
	Account_Type  VARCHAR(50) NOT NULL ,
	Remarks  VARCHAR(255) NULL 
);


ALTER TABLE ACCOUNT
	ADD CONSTRAINT  XPKACCOUNT PRIMARY KEY (Account_ID);
    ALTER TABLE ACCOUNT
    ADD CONSTRAINT FK_ACCOUNT_CUSTOMER
    FOREIGN KEY(Customer_ID)
    REFERENCES customer(Customer_ID);


ALTER TABLE ACCOUNT
ADD UNIQUE KEY UK_ACCOUNT_ID (Account_ID);



CREATE TABLE Account__Balance
(
	Balance_Amount DECIMAL(15,2) NOT NULL ,
	Balance_Date  DATE NOT NULL ,
	Customer_ID  BIGINT  NOT NULL ,
	Account_ID  BIGINT  NOT NULL ,
	Remarks  VARCHAR(255)  NULL ,
	CR_DR_Status CHAR(2) CHECK (CR_DR_Status IN ('CR','DR')),
	CR_DR_AMOUNT  DECIMAL(15,2)  NULL 
);

ALTER TABLE Account__Balance
    ADD COLUMN Balance_ID BIGINT NOT NULL;

ALTER TABLE Account__Balance    
    ADD CONSTRAINT PK_Account__Balance PRIMARY KEY(Balance_ID);
    
ALTER TABLE Account__Balance
MODIFY Balance_ID INT NOT NULL AUTO_INCREMENT;    


ALTER TABLE Account__Balance
    ADD CONSTRAINT FK_ACCOUNT__BALANCE_ACCOUNT
    FOREIGN KEY (Account_ID)
    REFERENCES account(Account_ID);

    
CREATE TABLE CUSTOMER_KYC
(
	Customer_ID  BIGINT NOT NULL ,
	Address_1  VARCHAR(255) NULL ,
	Address_2  VARCHAR(255)  NULL ,
	Mail_ID  VARCHAR(255)  NULL ,
	DOB  DATE  NULL ,
	Notes VARCHAR(25)  NULL 
);

ALTER TABLE CUSTOMER_KYC
    ADD COLUMN KYC_ID BIGINT NOT NULL;
      

ALTER TABLE CUSTOMER_KYC    
    ADD CONSTRAINT PK_CUSTOMER_KYC PRIMARY KEY(KYC_ID);
    
ALTER TABLE CUSTOMER_KYC
MODIFY KYC_ID INT NOT NULL AUTO_INCREMENT;      
    
ALTER TABLE CUSTOMER_KYC
    ADD CONSTRAINT FK_CUSTOMER_KYC_CUSTOMER
    FOREIGN KEY (Customer_ID)
    REFERENCES customer(Customer_ID);

CREATE TABLE STATUS
(
    STATUS_ID VARCHAR(10) NOT NULL,
    Customer_ID  BIGINT NOT NULL ,
	REQUEST_STATUS  VARCHAR(25)  NULL 
);

    
ALTER TABLE STATUS
    ADD CONSTRAINT XPKSTATUS PRIMARY KEY (STATUS_ID);  
    

ALTER TABLE STATUS
    ADD CONSTRAINT FK_STATUS_CUSTOMER
    FOREIGN KEY (Customer_ID)
    REFERENCES customer(Customer_ID);

CREATE TABLE Transactions
(
	Transaction_ID  BIGINT  NOT NULL ,
	Transaction_Type  VARCHAR(255) NOT NULL ,
	Transaction_Amount  DECIMAL (15,2) NOT  NULL ,
	Transaction_Date  DATE NOT NULL ,
	Remarks  VARCHAR(255)  NULL ,
	Source_Customer_ID  BIGINT  NULL ,
	Source_Account_ID  BIGINT  NULL ,
	Target_Customer_ID  BIGINT  NULL ,
	Target_Account_ID  BIGINT  NULL 
);


ALTER TABLE Transactions
	ADD CONSTRAINT  XPKTransactions PRIMARY KEY (Transaction_ID);
       
ALTER TABLE Transactions
ADD CONSTRAINT FK_TXN_SRC_ACCOUNT
FOREIGN KEY (Source_Account_ID)
REFERENCES account(Account_ID);


ALTER TABLE Transactions
ADD CONSTRAINT FK_TXN_TGT_ACCOUNT
FOREIGN KEY (Target_Account_ID)
REFERENCES account(Account_ID);


ALTER TABLE Transactions
ADD CONSTRAINT FK_TXN_SRC_CUSTOMER
FOREIGN KEY (Source_Customer_ID)
REFERENCES customer(Customer_ID);


ALTER TABLE Transactions
ADD CONSTRAINT FK_TXN_TGT_CUSTOMER
FOREIGN KEY (Target_Customer_ID)
REFERENCES customer(Customer_ID);


CREATE TABLE APP_USER
(
	USER_ID  BIGINT  NOT NULL ,
	USER_Name  VARCHAR(255)  NULL ,
	USER_TYPE  VARCHAR(255)  NULL ,
	Remarks  VARCHAR(25)  NULL ,
	PASSWORD_HASH  VARCHAR(255) NOT NULL ,
	Customer_ID  BIGINT NOT NULL 
);


ALTER TABLE APP_USER
	ADD CONSTRAINT  XPKAPP_USER PRIMARY KEY (USER_ID);
    

ALTER TABLE APP_USER
    ADD CONSTRAINT FK_APP_USER_CUSTOMER
    FOREIGN KEY(Customer_ID)
    REFERENCES customer(Customer_ID);


ALTER TABLE ACCOUNT
	ADD (CONSTRAINT  R_1 FOREIGN KEY (Customer_ID) REFERENCES CUSTOMER(Customer_ID));


ALTER TABLE Account__Balance
	ADD (CONSTRAINT  R_3 FOREIGN KEY (Customer_ID,Account_ID) REFERENCES ACCOUNT(Customer_ID,Account_ID));


ALTER TABLE CUSTOMER_KYC
	ADD (CONSTRAINT  R_2 FOREIGN KEY (Customer_ID) REFERENCES CUSTOMER(Customer_ID));


ALTER TABLE STATUS
	ADD (CONSTRAINT  R_6 FOREIGN KEY (Customer_ID) REFERENCES CUSTOMER(Customer_ID));


ALTER TABLE APP_USER
	ADD (CONSTRAINT  R_7 FOREIGN KEY (Customer_ID) REFERENCES CUSTOMER(Customer_ID));

INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Customer_Type, Notes)
VALUES 
(001, 'Brian Jacob', 'INDIVIDUAL','Premium Customer'),
(002,'Ripae Technologies','CORPORATION','Corporate Customer'),
(003, 'David Solomon', 'ADMIN', 'Bank Admin');

INSERT INTO ACCOUNT (Customer_ID, Account_ID, Account_Name, Account_Type, Remarks)
VALUES
(001, 101, 'Primary Savings Account','SAVINGS','Primary Savings Account'),
(001,102,'Secondary Savings','SAVINGS','Transfer Account'),
(002,201,'Corporate Account','CURRENT','Business Transactions');

INSERT INTO CUSTOMER_KYC (Customer_ID, Address_1, Address_2, Mail_ID, DOB, Notes)
VALUES
(001,'MG Road', 'Bangalore','brianj@example.com','2003-01-02','Passport Verified'),
(002,'Indira Nagar','Chennai','ripae.co@example.com','2015-02-03','Company KYC Complete');

INSERT INTO STATUS (Customer_ID, STATUS_ID, REQUEST_STATUS)
VALUES
(001,'S001','Pending'),
(001,'S002','Account Approved'),
(002,'S003','Pending'),
(002,'S004', 'Account Rejected');

INSERT INTO APP_USER (USER_ID, USER_NAME, USER_TYPE, Remarks, PASSWORD_HASH, Customer_ID)
VALUES
(1001,'Brian Jacob','Individual Customer','Customer','Brian123',001),
(1002,'Ripae Technologies','Corporate Customer','Customer','Ripae123',002),
(1003,'David Solomon','Bank Admin','Admin','Admin123',003);

INSERT INTO TRANSACTIONS (Transaction_ID, Transaction_Type, Transaction_Amount, Transaction_Date, Remarks, Source_Customer_ID, Source_Account_ID, Target_Customer_ID, Target_Account_ID)
VALUES
(2001,'DEPOSIT',5000.00,'2026-01-01','Initial Deposit', NULL, NULL, 001, 101),
(2002,'TRANSFER', 1500.00, '2026-01-03','Transfer to Account #2', 001, 101, 001, 102),
(2003, 'TRANSFER', 25000.00, '2026-01-05', 'Client Payment', 002, 201, 001, 101);

INSERT INTO Account__Balance (Balance_Amount, Balance_Date, Customer_ID, Account_ID, Remarks, CR_DR_Status, CR_DR_AMOUNT)
VALUES
(5000.00, '2026-01-01', 001, 101, 'Initial Deposit', 'CR', 5000.00),
(3500.00, '2026-01-02', 001, 101, 'Transfer to Account #2', 'DR', 1500.00),
(1500.00, '2026-01-02', 001, 102, 'Received from savings', 'CR', 1500.00),
(25000.00, '2026-01-03', 002, 201, 'Client Outgoing Transfer', 'DR', 25000.00);

SELECT * FROM CUSTOMER;
SELECT * FROM ACCOUNT;
SELECT * FROM Transactions;
SELECT * FROM Account__Balance;

SELECT A.Customer_ID, A.Account_ID, C.Customer_Name
FROM ACCOUNT A
JOIN CUSTOMER C ON A.Customer_ID = C.Customer_ID;

SELECT B.Customer_ID, B.Account_ID, B.Balance_Date, B.Balance_Amount
FROM Account__Balance B
JOIN ACCOUNT A
  ON B.Customer_ID = A.Customer_ID
 AND B.Account_ID = A.Account_ID;

SELECT Transaction_ID, Source_Customer_ID, Source_Account_ID,
       Target_Customer_ID, Target_Account_ID
FROM Transactions;

SELECT * FROM Account__Balance
WHERE CR_DR_Status NOT IN ('CR','DR');

SELECT Customer_ID, Account_ID, Balance_Date, Balance_Amount
FROM Account__Balance
ORDER BY Customer_ID, Account_ID, Balance_Date;

