CREATE TABLE tb_review (
    id bigserial NOT NULL,
    vote int4 NOT NULL,
    opinion text NULL,
    user_id bigint NOT NULL,
    movie_votes_id bigint NOT NULL,
    CONSTRAINT tb_review__f_id__pk PRIMARY KEY (id),
    CONSTRAINT tb_review__f_user_id__pk FOREIGN KEY (user_id) REFERENCES tb_user(id),
    CONSTRAINT tb_review__f_movie_votes_id__pk FOREIGN KEY (movie_votes_id) REFERENCES tb_movie_votes(id)
);