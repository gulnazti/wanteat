DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM restaurants;
ALTER SEQUENCE global_seq RESTART WITH 1000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1000),
       ('ADMIN', 1001);

INSERT INTO restaurants (name, address)
VALUES ('Claude Monet', 'Kazan, Peterburgskaya st., 5'),
       ('Voyage', 'Kazan, Baumana st., 17'),
       ('Java Coffee', 'Kazan, Pushkina st., 5'),
       ('Marinad', 'Kazan, Universitetskaya st., 22');