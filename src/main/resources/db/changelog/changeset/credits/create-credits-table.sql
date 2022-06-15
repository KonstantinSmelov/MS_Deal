CREATE TABLE credits
(
    id                   bigserial NOT NULL primary key,
    amount               decimal,
    term                 integer,
    monthly_payment      decimal,
    rate                 decimal,
    psk                  decimal,
    is_insurance_enabled boolean,
    is_salary_client     boolean,
    credit_status        varchar
);
