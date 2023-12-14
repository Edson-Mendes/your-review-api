CREATE TABLE tb_user (
    id bigserial NOT NULL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(320) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT tb_user__f_id__pk PRIMARY KEY (id),
    CONSTRAINT tb_user__f_email__unique UNIQUE (email)
);