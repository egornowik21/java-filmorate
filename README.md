# java-filmorate
Filmorate project.
Ссылка на базу данных:
[DataBase](https://github.com/egornowik21/java-filmorate/blob/main/QuickDBD-export.png)

примеры запросов:
SELECT name
FROM film
WHERE rating = 'NC-17'

SELECT ganre_name 
from Ganre as a
join Film_ganre as b on a.ganre_id = b.ganre_id
join Film as f on f.film_id = b.film_id
where name = 'Титаник'

select count(userid) 
from Likes as l
join Film as f on f.Filmid = l.Film_id
where name = 'Аватар' and duration>120
 
