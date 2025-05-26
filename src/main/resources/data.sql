INSERT INTO db_user (username, email, password, balance, role) VALUES 
('Jean964', 'jeanjean@email.com', '$2a$10$Zd6iahffw6N11rTySBO4bOu35lLvdRpbg968nex8L5CvNSuUg44Pm', 100.00,'USER'),
('Pierre42', 'pierre-berger@email.com', '$2a$10$9mptt2ZUeBUtrE38mmCieOZX6zyQsUdOE.Gy8IuC.rHR3UP/xAcOm', 20.00,'USER'),
('admin','admin@email.com','$2a$10$udSlyPlByzgQPkxaZn.C8Og5TNKnykKSrmZfPQSY.p7wzupA0Y01q',00.00,'ADMIN');

INSERT INTO user_friend (user_id, friend_id) VALUES 
(1, 2), (2, 1);

INSERT INTO db_transaction (sender_id, receiver_id, description, amount, fee) VALUES 
(1, 2, 'Cadeau de noel', 20.00, 0.10);


UPDATE db_user SET balance = balance - 20.10 WHERE id = 1;
UPDATE db_user SET balance = balance + 20.00 WHERE id = 2;
