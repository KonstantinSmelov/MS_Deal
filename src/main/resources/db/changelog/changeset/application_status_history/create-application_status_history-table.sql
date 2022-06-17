CREATE TABLE application_status_history
(
    id             bigserial primary key,
    status         varchar,
    time           date,
    change_type    varchar,
    application_id bigserial,
    foreign key (application_id) references applications (id)
);