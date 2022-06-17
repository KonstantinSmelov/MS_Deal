CREATE TABLE applications
(
    id               bigserial primary key,
    client_id        bigserial,
    credit_id        bigserial,
    status           varchar,
    creation_date    date,
    applied_offer_id bigserial,
    sign_date        date,
    ses_code         integer
);