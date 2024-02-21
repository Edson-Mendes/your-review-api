-- create table tb_user
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

-- create table tb_authority
CREATE TABLE tb_authority (
    id serial NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT tb_authority__f_id__pk PRIMARY KEY (id),
    CONSTRAINT tb_authority__f_name__unique UNIQUE (name)
);

-- create table tb_user_authorities
CREATE TABLE tb_user_authorities (
    user_id int8 NOT NULL,
    authority_id int4 NOT NULL,
    CONSTRAINT tb_user_authorities__f_user_id__fk FOREIGN KEY (user_id) REFERENCES tb_user(id),
    CONSTRAINT tb_user_authorities__f_authority_id__fk FOREIGN KEY (authority_id) REFERENCES tb_authority(id)
);

-- insert authorities USER and ADMIN
INSERT INTO tb_authority(name) VALUES ('USER'), ('ADMIN');

-- create table tb_movie_votes
CREATE TABLE tb_movie_votes(
    id bigserial NOT NULL,
    movie_id varchar(100) NOT NULL,
    vote_total bigint NOT NULL,
    vote_count bigint NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT tb_movie_votes__f_id__pk PRIMARY KEY (id),
    CONSTRAINT tb_movie_votes__f_movie_id__unique UNIQUE (movie_id)
);

-- create table tb_review
CREATE TABLE tb_review (
    id bigserial NOT NULL,
    vote int4 NOT NULL,
    opinion text NULL,
    user_id bigint NOT NULL,
    movie_votes_id bigint NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT tb_review__f_id__pk PRIMARY KEY (id),
    CONSTRAINT tb_review__f_user_id__pk FOREIGN KEY (user_id) REFERENCES tb_user(id),
    CONSTRAINT tb_review__f_movie_votes_id__pk FOREIGN KEY (movie_votes_id) REFERENCES tb_movie_votes(id)
);