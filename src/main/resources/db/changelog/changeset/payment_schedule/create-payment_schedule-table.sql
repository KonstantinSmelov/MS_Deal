CREATE TABLE payment_schedule
(
    id               bigserial NOT NULL primary key,
    number           integer,
    date             date,
    total_payment    decimal,
    interest_payment decimal,
    debt_payment     decimal,
    remaining_debt   decimal,
    credit_id        bigserial,
    foreign key (credit_id) references credits (id)
);
