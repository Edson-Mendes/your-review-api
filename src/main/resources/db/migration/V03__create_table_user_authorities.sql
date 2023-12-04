CREATE TABLE tb_user_authorities (
    user_id int8 NOT NULL,
    authority_id int4 NOT NULL,
    CONSTRAINT tb_user_authorities__f_user_id__fk FOREIGN KEY (user_id) REFERENCES tb_user(id),
    CONSTRAINT tb_user_authorities__f_authority_id__fk FOREIGN KEY (authority_id) REFERENCES tb_authority(id)
);