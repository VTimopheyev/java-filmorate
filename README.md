# java-filmorate
Filmorate project
![диаграмма Filmorate](Schema.png)

Пример 1: запрос всех Users
select *
from user;

Пример 2: запрос всех Likes для фильма
select user_id
from Likes
where film_id = xxx;
