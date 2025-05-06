INSERT INTO `user` (username, email, password) VALUES 
('Jean964', 'jeanjean@email.com', 'mouton56'),
('Pierre42', 'pierre-berger@email.com', 'jaimelesfleurs');

INSERT INTO transaction (sender_id, receiver_id, description, amount) VALUES 
(1, 2, 'Cadeau de noel', 120.50);

INSERT INTO user_connection (user_id, connection_id) VALUES 
(1, 2), (2, 1);
