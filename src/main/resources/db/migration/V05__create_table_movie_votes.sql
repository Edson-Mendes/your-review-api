CREATE TABLE tb_movie_votes(
    id bigserial NOT NULL,
    movie_id varchar(100) NOT NULL,
    vote_total bigint NOT NULL,
    vote_count bigint NOT NULL,
    created_at timestamp NOT NULL,
    CONSTRAINT tb_movie_votes__f_id__pk PRIMARY KEY (id),
    CONSTRAINT tb_movie_votes__f_movie_id__unique UNIQUE (movie_id)
);