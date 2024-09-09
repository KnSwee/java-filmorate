# java-filmorate
Template repository for Filmorate project.

[Screenshot of a DB ER diagram](./diagram.png)


FilmController:

findAll()
SELECT f.film_id, f.name, f.description, f.release_date, f.duration_minutes, f.rating
FROM film f
JOIN rating r ON f.rating_id = r.rating_id

SELECT g.genre
FROM film_genre fg
JOIN genre g ON fg genre_id = g.genre_id
WHERE film_id = ...


