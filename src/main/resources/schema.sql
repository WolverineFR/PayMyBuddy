DROP TABLE IF EXISTS user_friend;
DROP TABLE IF EXISTS dbtransaction;
DROP TABLE IF EXISTS dbuser;
DROP TABLE IF EXISTS app_wallet;

CREATE TABLE dbuser (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0
);

CREATE TABLE dbtransaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    description VARCHAR(255),
    amount DECIMAL(10,2) NOT NULL,
    fee DECIMAL(10,2) NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES dbuser(id),
    FOREIGN KEY (receiver_id) REFERENCES dbuser(id)
);

CREATE TABLE user_friend (
    user_id INT,
    friend_id INT,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES dbuser(id),
    FOREIGN KEY (friend_id) REFERENCES dbuser(id)
);

CREATE TABLE app_wallet (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    fee_amount DECIMAL(10,2) NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES dbtransaction(id)
);
