INSERT INTO public.users (first_name, last_name, phone_number, email, role, registration_date)
VALUES
       ('denis', 'ivaylo', '+38092', 'denis@gmail.com', 'user', now()),
       ('kate', 'bishop', '+38093', 'kate@gmail.com', 'admin', now()),
       ('tor', 'odinson', '+38094', 'tor@gmail.com', 'barber', now()),
       ('loki', 'odinson', '+38095', 'loki@gmail.com', 'user', now()),
       ('groot', 'tree', '+38096', 'groot@gmail.com', 'admin', now());

INSERT INTO public.barbershop (address, name, phone_number, email, work_time_from, work_time_to)
VALUES
    ('address1', 'barbershop1', '+3809111111', 'barbershop1@gmail.com', current_time, current_time),
    ('address2', 'barbershop2', '+3809222222', 'barbershop2@gmail.com', current_time, current_time),
    ('address3', 'barbershop3', '+3809333333', 'barbershop3@gmail.com', current_time, current_time),
    ('address4', 'barbershop4', '+3809444444', 'barbershop4@gmail.com', current_time, current_time);