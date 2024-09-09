# java-filmorate
Template repository for Filmorate project.  

[Screenshot of a DB ER diagram](./diagram.png)


FilmController:

# findAll()
SELECT f.film_id, f.name, f.description, f.release_date, f.duration_minutes, f.rating  
FROM film f  
JOIN rating r ON f.rating_id = r.rating_id  
  
SELECT g.genre  
FROM film_genre fg  
JOIN genre g ON fg genre_id = g.genre_id  
WHERE film_id = ...  

# getFilm(long filmId)
SELECT f.film_id, f.name, f.description, f.release_date, f.duration_minutes, f.rating  
FROM film f  
JOIN rating r ON f.rating_id = r.rating_id  
WHERE f.film_id = filmId  
  
SELECT g.genre  
FROM film_genre fg  
JOIN genre g ON fg genre_id = g.genre_id  
WHERE film_id = filmId  

# getRatedFilms(int count)
SELECT f.film_id, f.name, f.description, f.release_date, f.duration_minutes, f.rating    
FROM film f   
JOIN rating r ON f.rating_id = r.rating_id   
WHERE f.film_id IN (SELECT DISTINCT film_id  
                      FROM film_like  
                    GROUP BY film_id  
                    ORDER BY COUNT(user_id) DESC  
                    LIMIT count)  

SELECT g.genre  
FROM film_genre fg  
JOIN genre g ON fg genre_id = g.genre_id  
WHERE film_id IN (...)

# findAll()
SELECT *  
FROM user  

# getUser(long userId)
SELECT *
FROM user
WHERE user_id = userId

# getFriends(long userId)
SELECT friend_added_id
FROM user_friend
WHERE user_id = userId

# getMutualFriends(long id, long otherId)
SELECT friend_added_id  
FROM user_friend,  
  (SELECT friend_added_id  
    FROM user_friend   
    WHERE user_id = id) AS s1,  
  (SELECT friend_added_id   
    FROM user_friend   
    WHERE user_id = otherId) AS s2  
WHERE friend_added_id IN s1  
  AND friend_added_id IN s2  









