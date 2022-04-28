INSERT INTO users (first_name, last_name, phone_number,
                   email, role, registration_date)
VALUES ('denis', 'ivaylo', '+38092334455', 'denis@gmail.com', 'barber', '2021-03-02T14:21:23'),
       ('kate', 'bishop', '+38092334411', 'kate@gmail.com', 'barber', '2021-06-02T14:21:23'),
       ('tor', 'odinson', '+38093334422', 'tor@gmail.com', 'barber', '2021-06-02T18:00:00'),
       ('loki', 'odinson', '+38090334499', 'loki@gmail.com', 'barber', '2021-10-02T10:00:00'),
       ('groot', 'tree', '+12098334422', 'iamgroot@gmail.com', 'admin', '2022-03-23T13:34:30'),
       ('frozen', 'girl', '+1111111111', 'frozen@gmail.com', 'user', '2019-08-20T17:34:22'),
       ('Bob', 'Thompson', '+1341177117', 'bob@gmail.com', 'user', '2010-08-20T17:00:01')
;

INSERT INTO barbershop(address, name, phone_number,
                       email, work_time_from, work_time_to, is_active)
VALUES ('Tymoshenka street 9', 'at Tymoshenka', '+380950110000', 'tymoshenka@barbeshop.com', '08:00:00', '19:00:00',
        true),
       ('Bulgaria, Ivaylo street 15', 'BolgariaBarbershop', '+9999999999', 'bolgaria@barbeshop.com', '12:00:00',
        '18:00:00', true),
       ('Kyiv, Lvivska 49', 'BestBarbershop', '+380930939393', 'best@barbeshop.com', '08:00:00', '19:00:00', true),
       ('Lviv, Lvivska 1', 'LvivskaBarbershop', '+380330339333', 'lvivska@barbeshop.com', '10:00:00', '17:00:00', true),
       ('Luhansk, Luhanska street 5', 'LuhanskaBarbershop', '+380110339333', 'luhanska@barbeshop.com', '10:00:00',
        '17:00:00', true)
;

INSERT INTO workspace(user_id, barbershop_id, active)
VALUES (4, 1, true),
       (4, 2, true),
       (4, 3, true),
       (4, 4, true),
       (4, 5, true),
       (3, 2, true),
       (2, 1, true),
       (2, 2, true),
       (1, 3, true),
       (1, 4, true),
       (1, 5, true),
       (1, 2, false)
;

INSERT INTO orders(barbershop_id, barber_id,
                   customer_id, order_date, is_active)
VALUES (3, 1, null, '2022-03-26T14:00:00', true),
       (3, 1, null, '2022-03-27T14:00:00', true),
       (4, 1, 5, '2022-03-30T14:00:00', true),
       (3, 1, 6, '2022-03-30T15:00:00', true),
       (3, 1, 7, '2022-03-30T16:00:00', true),
       (2, 2, null, '2022-03-30T09:00:00', true),
       (2, 2, 7, '2022-03-30T10:00:00', true),
       (2, 2, null, '2022-04-30T13:00:00', true),
       (2, 2, 7, '2022-04-30T17:00:00', true),
       (2, 4, 6, '2022-04-15T17:00:00', true),
       (2, 4, null, '2022-04-16T08:00:00', true),
       (2, 4, 1, '2021-04-16T09:00:00', false),
       (2, 4, 2, '2021-04-16T08:00:00', false)
;


