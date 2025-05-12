INSERT INTO dbuser (username, email, password, balance) VALUES 
('Jean964', 'jeanjean@email.com', '$2a$10$Zd6iahffw6N11rTySBO4bOu35lLvdRpbg968nex8L5CvNSuUg44Pm', 100.00),
('Pierre42', 'pierre-berger@email.com', '$2a$10$9mptt2ZUeBUtrE38mmCieOZX6zyQsUdOE.Gy8IuC.rHR3UP/xAcOm', 20.00);

INSERT INTO user_friend (user_id, friend_id) VALUES 
(1, 2), (2, 1);

INSERT INTO dbtransaction (sender_id, receiver_id, description, amount, fee) VALUES 
(1, 2, 'Cadeau de noel', 20.00, 0.10);

INSERT INTO app_wallet (transaction_id, fee_amount)
VALUES (1, 0.10);

UPDATE dbuser SET balance = balance - 20.00 WHERE id = 1;
UPDATE dbuser SET balance = balance + 19.90 WHERE id = 2;
