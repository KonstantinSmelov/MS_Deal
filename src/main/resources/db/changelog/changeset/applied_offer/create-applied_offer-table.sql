CREATE TABLE applied_offer
(
    id                   bigserial NOT NULL primary key,
    requested_amount     decimal,
    total_amount         decimal,
    term                 integer,
    monthly_payment      decimal,
    rate                 decimal,
    is_insurance_enabled boolean,
    is_salary_client     boolean
);