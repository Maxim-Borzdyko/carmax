delete from user ;

insert into user(id, email, password, status, role, first_name, second_name) values
(1, 'admin@test.com', '$2a$12$c9xhbOGmNbT.8o7ruu1Ky.1qAiVt1mx1GgVG0eJ3dFwp1LLODT.o.', true, 'ADMIN', 'first', 'second'),
(2, 'user1@test.com', '$2a$12$c9xhbOGmNbT.8o7ruu1Ky.1qAiVt1mx1GgVG0eJ3dFwp1LLODT.o.', true, 'USER', 'first', 'second'),
(3, 'user2@test.com', '$2a$12$c9xhbOGmNbT.8o7ruu1Ky.1qAiVt1mx1GgVG0eJ3dFwp1LLODT.o.', true, 'USER', 'first', 'second'),
(4, 'user3@test.com', '$2a$12$c9xhbOGmNbT.8o7ruu1Ky.1qAiVt1mx1GgVG0eJ3dFwp1LLODT.o.', true, 'USER', 'first', 'second');