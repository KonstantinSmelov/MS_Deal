CREATE TABLE clients
(
    id               bigserial primary key,
    first_name       varchar,
    last_name        varchar,
    middle_name      varchar,
    birthdate        date,
    email            varchar,
    gender           varchar,
    marital_status   varchar,
    dependent_amount integer,
    passport_id      bigserial,
    employment_id    bigserial,
    account          varchar,
    foreign key (passport_id) references passports (id),
    foreign key (employment_id) references employments (id)
);