SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user_friend;
DROP TABLE IF EXISTS db_transaction;
DROP TABLE IF EXISTS db_user;
DROP TABLE IF EXISTS app_wallet;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE db_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0
);

CREATE TABLE db_transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    description VARCHAR(255),
    amount DECIMAL(10,2) NOT NULL,
    fee DECIMAL(10,2) NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES db_user(id),
    FOREIGN KEY (receiver_id) REFERENCES db_user(id)
);

CREATE TABLE user_friend (
    user_id INT,
    friend_id INT,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES db_user(id),
    FOREIGN KEY (friend_id) REFERENCES db_user(id)
);

CREATE TABLE app_wallet (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    fee_amount DECIMAL(10,2) NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES db_transaction(id)
);
