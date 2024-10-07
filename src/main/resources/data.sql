SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE  FILM_GENRE;

TRUNCATE TABLE  FILM_LIKE;

TRUNCATE TABLE  USER_FRIEND;

TRUNCATE TABLE USERS RESTART IDENTITY;

TRUNCATE TABLE FILMS RESTART IDENTITY;

TRUNCATE TABLE GENRE RESTART IDENTITY;

TRUNCATE TABLE RATING RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO RATING (NAME)
VALUES ('G'),  --1
    ('PG'),    --2
    ('PG-13'), --3
    ('R'),     --4
    ('NC-17'); --5

INSERT INTO GENRE (NAME)
VALUES ('Комедия'),  --1
    ('Драма'),   --2
    ('Мультфильм'),    --3
    ('Триллер'),     --4
    ('Документальный'),      --5
    ('Боевик');  --6

INSERT INTO FILMS (NAME , DESCRIPTION , RELEASE_DATE , DURATION_MINUTES , RATING_ID)
VALUES ('Скотт Пилигрим против всех', 'Скотт Пилигрим нашел девушку своей мечты. Но на его пути стоит более сложная задача: чтобы встречаться с ней, Скотту придется победить ее семерых бывших.', '2010-07-27', 112 , 3),
    ('Звёздные войны: Эпизод 4 – Новая надежда', 'Татуин. Планета-пустыня. Уже постаревший рыцарь Джедай Оби Ван Кеноби спасает молодого Люка Скайуокера, когда тот пытается отыскать пропавшего дроида.', '1977-05-25', 121 , 2),
    ('Мементо', 'Леонард ищет убийцу жены, но ему мешает амнезия. Виртуозный триллер, сделавший Кристофера Нолана суперзвездой', '2000-09-05', 113 , 4),
    ('Человек-швейцарский нож', 'Оказавшись на необитаемом острове, Хэнк уже был готов потерять всякую надежду, но вдруг на берегу он находит труп и называет его Мэнни. Многофункциональность Мэнни поможет Хэнку выжить в диких условиях и заново обрести радость жизни. Вместе они отправятся в эпическое приключение, которое вернет Хэнка к девушке его мечты.', '2016-01-22', 97 , 4);

INSERT INTO FILM_GENRE (FILM_ID , GENRE_ID)
VALUES (1, 3), (1, 5), (1, 2), (1, 1),
    (2, 6), (2, 5), (2, 3), (2, 4),
    (3, 4), (3, 2), (3, 1), (3, 3),
    (4, 3), (4, 6), (4, 1), (4, 5);

INSERT INTO USERS (EMAIL , LOGIN , NAME , BIRTHDATE)
VALUES ('User@gmail.com', 'User01', 'Valentin', '2001-01-01'),   -- 1
    ('Example@example.com', 'Example', 'Primer', '1998-09-01'),       -- 2
    ('Onemore@yandex.ru', 'time', 'Daft', '2000-11-13'),   -- 3
    ('444444444@mail.ru', 'four', 'chetverty', '2004-04-04'),
    ('50cent@gmail.com', 'fifty', '5', '2005-05-05');


INSERT INTO FILM_LIKE (FILM_ID, USER_ID)
VALUES (1, 1), (1, 3),
    (2, 1), (2, 2), (2, 3),
    (3, 2),
    (4, 1), (4, 2), (4, 3);

INSERT INTO USER_FRIEND (USER_ID, FRIEND_ADDED_ID, confirmed)
VALUES (1, 2, false), (1, 3, false),
    (2, 1, false), (2, 3, false),
    (3, 1, false), (3, 2, false);
