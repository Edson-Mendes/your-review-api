-- insert user
INSERT INTO tb_user(name, email, password, status, created_at) VALUES
    ('John Doe', 'john.doe@email.com', '{bcrypt}$2a$10$65gCOyzktXSMmHdtuNsSHuqP6Ttp9VaNQD/0/yOdmkSIgRlp.7CkO', 'ENABLED', '2024-02-18T10:00:00'),
    ('Jane Doe', 'jane.doe@email.com', '{bcrypt}$2a$10$65gCOyzktXSMmHdtuNsSHuqP6Ttp9VaNQD/0/yOdmkSIgRlp.7CkO', 'ENABLED', '2024-02-18T10:00:00');

-- insert authority para o user acima
INSERT INTO tb_user_authorities(user_id, authority_id) VALUES (1, 1), (2, 1);

-- insert movie votes
INSERT INTO tb_movie_votes(movie_id, vote_total, vote_count, created_at) VALUES
    ('120', 19, 2, '2024-02-18T11:00:00');

-- insert review
INSERT INTO tb_review(vote, opinion, user_id, movie_votes_id, created_at) VALUES
    (10, 'lorem ipsum dolor sit amet', 1, 1, '2024-02-18T12:00:00'),
    (9, 'lorem ipsum dolor sit amet', 2, 1, '2024-02-18T12:00:00');