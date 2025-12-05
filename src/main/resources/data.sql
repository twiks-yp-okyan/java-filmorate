TRUNCATE TABLE rating;

INSERT INTO rating (id, name)
VALUES (1, 'g'),
(2, 'pg'),
(3, 'pg-13'),
(4, 'r'),
(5, 'nc-17')
;

TRUNCATE TABLE genres;

INSERT INTO genres (id, name)
VALUES (1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик')
;