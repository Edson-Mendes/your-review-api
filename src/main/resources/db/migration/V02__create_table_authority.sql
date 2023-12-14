CREATE TABLE tb_authority (
    id serial NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT tb_authority__f_id__pk PRIMARY KEY (id),
    CONSTRAINT tb_authority__f_name__unique UNIQUE (name)
);