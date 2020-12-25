DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM dishes;
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

INSERT INTO dishes (name, price, restaurant_id, created)
VALUES ('Boeuf Bourguignon', 700, 1002, DATE '2020-12-25'),
       ('Blanquette de Veau', 850, 1002, DATE '2020-12-25'),
       ('Cassoulet', 450, 1003, DATE '2020-12-24'),
       ('Confit de Canard', 530, 1003, DATE '2020-12-24'),
       ('Spring Dish', 260, 1004, DATE '2020-12-24'),
       ('Beans with Salad', 310, 1004, DATE '2020-12-24'),
       ('Tomatoes', 180, 1005, DATE '2020-12-24'),
       ('Cucumber Roll', 200, 1005, DATE '2020-12-24');