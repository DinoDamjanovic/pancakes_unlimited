## Instructions

* Make sure your Docker is running
* Open Terminal and place yourself in the project directory
* Run `docker-compose up` command and wait until Docker initializes a Container with MySQL database
* Start the Spring Boot application

---

Flyway will create the tables in the database and populate them with data.<br>
All tables except `orders` table will be populated with data. This table is left empty for you to test.<br>
To test the orders easier there are 15 premade pancakes for you in the `pancakes` table:
* Pancakes with id 1-5 have > 75% healthy ingredients. 
* Pancakes with id 6-10 have <= 75% healthy ingredients. 
* Pancakes with id 11-14 are invalid pancakes (missing base ingredient, stuffing etc.)

Postman tests for each Resource in the application can be found [here](https://www.postman.com/satellite-geoscientist-87027700/workspace/pancakes-unlimited-testing).

---

To connect to MySQL database in Docker container via MySQL Workbench, use the following:
* Hostname: localhost 
* Port: 3308 
* Password: root

