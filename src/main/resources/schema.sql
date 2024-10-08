CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(40) NOT NULL,
    name VARCHAR(40),
    birthdate DATE
);

CREATE TABLE IF NOT EXISTS rating
(
    rating_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS films
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    release_date DATE,
    duration_minutes INT,
    rating_id INT,
    foreign key (rating_id) references rating(rating_id)
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id INT,
    genre_id INT,
    foreign key (film_id) references films(id),
    foreign key (genre_id) references genre(genre_id)
);

CREATE TABLE IF NOT EXISTS film_like
(
    film_id INT,
    user_id INT,
    foreign key (film_id) references films(id),
    foreign key (user_id) references users(id),
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS user_friend
(
    user_id INT,
    friend_added_id INT,
    confirmed BOOLEAN DEFAULT false,
    foreign key (user_id) references users(id),
    foreign key (friend_added_id) references users(id)
);