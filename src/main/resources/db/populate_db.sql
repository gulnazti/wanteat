DELETE FROM user_roles;
DELETE FROM votes;
DELETE FROM users;
DELETE FROM dishes;
DELETE FROM restaurants;
ALTER SEQUENCE global_seq RESTART WITH 1000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Dev', 'dev@yandex.ru', '{noop}devpassword'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1000),
       ('USER', 1001),
       ('ADMIN', 1002);

INSERT INTO restaurants (name, address)
VALUES ('Claude Monet', 'Kazan, Peterburgskaya st., 5'),
       ('Voyage', 'Kazan, Baumana st., 17'),
       ('Java Coffee', 'Kazan, Pushkina st., 5'),
       ('Marinad', 'Kazan, Universitetskaya st., 22');

INSERT INTO dishes (name, price, restaurant_id, created)
VALUES ('Soupe à loseille', 670, 1003, DATE '2020-12-24'),
       ('Saint Jacques', 900, 1003, DATE '2020-12-24'),
       ('Pecheur', 480, 1004, DATE '2020-12-24'),
       ('Tagliatelles', 330, 1004, DATE '2020-12-24'),
       ('Spaghetti', 200, 1005, DATE '2020-12-24'),
       ('Beans with Fish', 360, 1005, DATE '2020-12-24'),
       ('Tomatoes in Juice', 220, 1006, DATE '2020-12-24'),
       ('Cucumber Roll', 200, 1006, DATE '2020-12-24');

INSERT INTO dishes (name, price, restaurant_id)
VALUES ('Boeuf Bourguignon', 700, 1003),
       ('Blanquette de Veau', 850, 1003),
       ('Cassoulet', 450, 1004),
       ('Confit de Canard', 530, 1004),
       ('Spring Dish', 260, 1005),
       ('Beans with Salad', 310, 1005),
       ('Tomatoes', 180, 1006),
       ('Cucumber Roll', 200, 1006);

INSERT INTO votes (user_id, restaurant_id, date)
VALUES (1000, 1003, DATE '2020-12-24'),
       (1001, 1005, DATE '2020-12-24');

INSERT INTO votes (user_id, restaurant_id)
VALUES (1001, 1005),
       (1002, 1005);
