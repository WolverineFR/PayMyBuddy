DROP TABLE IF EXISTS user_connection;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    description VARCHAR(255),
    amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES user(id),
    FOREIGN KEY (receiver_id) REFERENCES user(id)
);

CREATE TABLE user_connection (
    user_id INT,
    connection_id INT,
    PRIMARY KEY (user_id, connection_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (connection_id) REFERENCES user(id)
);

