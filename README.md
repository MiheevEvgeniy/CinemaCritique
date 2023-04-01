# java-filmorate
ER-diagram для проекта:<br/>
![ERD-filmorate.png](src%2Fmain%2Fresources%2FERD-filmorate.png)<br/>
URL: https://app.quickdatabasediagrams.com/#/d/KW9QbH
<h3>Описание диаграммы:<br/>
---

<h5> 1) Таблица "film" <br/></h5>
Одна из главных таблиц. Она представляет из себя 
копию класса Film.java, где массив с пользователями, 
которым понравился это фильм вынесены в отдельную таблицу.<br/><br/>
В данной таблице просто хранятся данные о фильмах
<br/>
<h5> 2) Таблица "film_genre" <br/></h5>
Вспомогательная таблица для хранения лайкнувших пользователей и жанров.
<br/>
<h5> 3) Таблица "genre" <br/></h5>
Таблица с id жанров и их именами
<br/>
<h5> 4) Таблица "user" <br/></h5>
Вторая основная таблица. Копия класса User.java с полем friend_id, 
которое ссылается на свою же таблицу, т.к. друзья это другие пользователи
<br/>

---