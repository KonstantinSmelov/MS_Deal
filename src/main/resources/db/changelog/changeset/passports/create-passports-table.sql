CREATE TABLE passports
(
    id              bigserial primary key,
    passport_series varchar,
    passport_number varchar,
    issue_date      date,
    issue_branch    varchar
);