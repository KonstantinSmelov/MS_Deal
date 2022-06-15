CREATE TABLE employments
(
    id                      bigserial NOT NULL primary key,
    employment_status       varchar,
    employer                varchar,
    salary                  decimal,
    position                varchar,
    work_experience_total   integer,
    work_experience_current integer
);