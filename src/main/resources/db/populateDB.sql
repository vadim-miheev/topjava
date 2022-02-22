DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories)
VALUES (100000, '2022-02-22 17:10:00', 'New 01', 500),
       (100000, '2022-02-22 18:20:00', 'New 02', 1050),
       (100001, '2022-02-22 17:10:00', 'New 03', 950),
       (100001, '2022-02-22 20:30:00', 'New 04', 1150),
       (100002, '2022-02-22 17:10:00', 'New 05', 2000),
       (100002, '2022-02-22 18:40:00', 'New 06', 500),
       (100002, '2022-02-22 19:00:00', 'New 07', 500);
