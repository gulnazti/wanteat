## **WantEat!**

The place where you can make a choice for the favorite menu of the day.

REST API using _Hibernate/Spring/SpringMVC_:
- 2 types of users: admin and regular users.
- Admin can input a restaurant and it's lunch menu of the day.
- Menu changes each day (admins do the updates).
- Users can vote on which restaurant they want to have lunch at.
- Only one vote counted per user.
- If user votes again the same day:
   - If it is before 11:00 we assume that he changed his mind.
   - If it is after 11:00 then it is too late, vote can't be changed.
- Each restaurant provides a new menu each day.

#### Run

To launch the app just run the following command:
<pre>mvn clean package -DskipTests=true org.codehaus.cargo:cargo-maven2-plugin:1.8.2:run</pre>

It will be deployed to http://localhost:8080/wanteat

#### cURL

In purpose for testing you can use these commands:

###### vote
<pre>curl -X POST "http://localhost:8080/wanteat/api/v1/restaurants/1003/vote" -H "accept: application/json" --user user@yandex.ru:password</pre>

###### cancel vote
<pre>curl -X DELETE "http://localhost:8080/wanteat/api/v1/restaurants/{id}/vote" -H "accept: application/json" --user user@yandex.ru:password</pre>

###### get votes history
<pre>curl -X GET "http://localhost:8080/wanteat/api/v1/restaurants/votes-history" -H "accept: application/json" --user user@yandex.ru:password</pre>

###### get all restaurants
<pre>curl -X GET "http://localhost:8080/wanteat/api/v1/restaurants" -H "accept: application/json" --user user@yandex.ru:password</pre>

###### get restaurant
<pre>curl -X GET "http://localhost:8080/wanteat/api/v1/restaurants/1003" -H "accept: application/json" --user user@yandex.ru:password</pre>

###### get restaurants with menu
<pre>curl -X GET "http://localhost:8080/wanteat/api/v1/restaurants/with-menu" -H "accept: application/json" --user user@yandex.ru:password</pre>

###### get restaurant with menu
<pre>curl -X GET "http://localhost:8080/wanteat/api/v1/restaurants/1003/with-menu" -H "accept: application/json" --user user@yandex.ru:password</pre>

###### create restaurant
<pre>curl -X POST "http://localhost:8080/wanteat/api/v1/restaurants" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"address\": \"Address\", \"name\": \"Name\"}" --user admin@gmail.com:admin</pre>

###### update restaurant
<pre>curl -X PUT "http://localhost:8080/wanteat/api/v1/restaurants/1003" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"id\": 1003, \"name\": \"Paris\", \"address\": \"France\"}" --user admin@gmail.com:admin</pre>

###### delete restaurant
<pre>curl -X DELETE "http://localhost:8080/wanteat/api/v1/restaurants/1003" -H "accept: application/json" --user admin@gmail.com:admin</pre>

###### get dish
<pre>curl -X GET "http://localhost:8080/wanteat/api/v1/dishes/1007" -H "accept: application/json" --user user@yandex.ru:password</pre>

###### add dish
<pre>curl -X POST "http://localhost:8080/wanteat/api/v1/restaurants/1005/dishes" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"name\": \"Dish\", \"price\": 100, \"created\": \"2020-12-31\"}"  --user admin@gmail.com:admin</pre>

###### update dish
<pre>curl -X PUT "http://localhost:8080/wanteat/api/v1/dishes/1007" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"created\": \"2020-12-31\", \"id\": 1007, \"name\": \"Updated\", \"price\": 500}"  --user admin@gmail.com:admin</pre>

###### delete dish
<pre>curl -X DELETE "http://localhost:8080/wanteat/api/v1/dishes/1010" -H "accept: application/json"  --user admin@gmail.com:admin</pre>

###### get all dishes
<pre>curl -X GET "http://localhost:8080/wanteat/api/v1/dishes" -H "accept: application/json"  --user admin@gmail.com:admin</pre>

To check another commands follow the link http://localhost:8080/wanteat/swagger-ui.html