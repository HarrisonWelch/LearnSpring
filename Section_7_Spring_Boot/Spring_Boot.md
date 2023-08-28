# Spring Boot

* Basics of the theory
* What is Spring and what is SpringBoot
* How to create a REST API using SpringBoot
* Add exception handling
* Implement unit testing for all layers
* Database config
  * H2 into SQL
* How to monitor application
* How to deploy the application into production

## What is Spring

* Framework to create enterprise application
* There are a lot more configs and tech
* Allows us to do many things
  * SpringWeb, SpringJPA, etc.
  * Hibernate
* A lot of moving parts, so Spring makes it easy
* Conventrate on convention not configuration

## What is spring framework
* Extension layer on to the Spring
* Rapid application development
* Managing dependencies
  * Group all dependencies into a starter template
* Spring testing like JUnit and Mockito
* Auto configuration
* Springboot does the auto config for all those libraries
* We create jar file with a server embedded. Always production ready.
* Microservices make spring boot the way to go.

## Dependency Injection
* Default way for creating objects
* Student s = new Student()
* Inversion of control (IOC) Nothing but to give control from yourself to Spring to create the object itself.
* Spring will create the objects for you.
* Stored in one beans particular container

## Generate the project

start.spring.io

Share code: https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.1.3&packaging=jar&jvmVersion=11&groupId=com.harrison&artifactId=Spring-boot-tutorial&name=Spring-boot-tutorial&description=Demo%20project%20for%20Spring%20Boot&packageName=com.harrison.Springboot.tutorial&dependencies=web,h2

Settings are 
* Project: Maven
* Language: Java
* Spring Boot: 3.1.3 (latest stable (non-SNAPSHOT/M2/etc.))
* Group: com.harrison
* Artifact: Spring-boot-tutorial
* Name: Spring-boot-tutorial
* Description: Demo project for Spring Boot
* Package name: com.harrison.Springboot.tutorial
* Packaging: Jar
* Java: 11

Click "Explore" to preview

Click "Generate" to download the project template

Can open in IntelliJ or STS4 - Spring tool suite 4.

Explore the pom.xml, many dependencies and those have many dependencies.

## The @SpringBootApplication annotation
* Configuration setup. Set for all packages and sub-pckgs.

Run the application
* Note: I had to update my java to 17 (amzn corretto) eventhough I selected java 11 in the generator :)

output
```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.1.3)

2023-08-27T15:34:44.476-04:00  INFO 20724 --- [           main] c.h.S.t.SpringBootTutorialApplication    : Starting SpringBootTutorialApplication using Java 17.0.8.1 with PID 20724 (C:\Users\harri\OneDrive\Desktop\Code\LearnSpring\Section_7_Spring_Boot\Spring-boot-tutorial\target\classes started by harri in C:\Users\harri\OneDrive\Desktop\Code\LearnSpring\Section_7_Spring_Boot\Spring-boot-tutorial)
2023-08-27T15:34:44.480-04:00  INFO 20724 --- [           main] c.h.S.t.SpringBootTutorialApplication    : No active profile set, falling back to 1 default profile: "default"
2023-08-27T15:34:44.987-04:00  INFO 20724 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2023-08-27T15:34:44.992-04:00  INFO 20724 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2023-08-27T15:34:44.992-04:00  INFO 20724 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.12]
2023-08-27T15:34:45.050-04:00  INFO 20724 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2023-08-27T15:34:45.052-04:00  INFO 20724 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 540 ms
2023-08-27T15:34:45.306-04:00  INFO 20724 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2023-08-27T15:34:45.314-04:00  INFO 20724 --- [           main] c.h.S.t.SpringBootTutorialApplication    : Started SpringBootTutorialApplication in 1.039 seconds (process running for 1.272)
```

Tomcat is running on 8080

Loading http://localhost:8080/ will bring back some things, but it will error with blank fallback.

```
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Sun Aug 27 15:42:02 EDT 2023
There was an unexpected error (type=Not Found, status=404).
```

## HelloController

```java
package com.harrison.Springboot.tutorial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController // Component of a particular type, component is added by this annotation
public class HelloController {

    @RequestMapping(value = "/", method = RequestMethod.GET) // Assign default endpoint to / as a GET
    public String helloWorld() {
        return "Welcome too Daily Code Buffer!!";
    }
}

```

Reloading and going into localhost:8080 shows the "Welcome too Daily Code Buffer!!" in basic HTML

## Application.properties

Change the default port away from 8080 for example to 8082.

```properties
server.port = 8082
# Documentation available for each property
```

## Run it

Go to http://localhost:8082/
-> you get "Welcome too Daily Code Buffer!!"

## Now

The request mapping is still verbose

Switch to `@GetMapping("/")` which is simplified of the previous thing.

## Command line run

`mvn spring-boot:run`

## Devtools
* detect changes and restart application

Go back to start.spring.io and add the dependency. Use explore to pull the correct text.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

Add this to the pom.xml

Now open Advanced settings and enable "the auto-make when running" setting.
* Note: In the video it says to go to registry using Ctrl+Shift+A, then enable "compiler.automake.allow.when.app.running" this was moved to advanced settings in 2021. Source: https://youtrack.jetbrains.com/issue/IDEA-274903/In-IntelliJ-2021.2-compiler.automake.allow.when.app.running-disappear.-Unable-to-enable-live-reload-under-Spring-boot

![Compiler allow auto-make](https://youtrack.jetbrains.com/api/files/74-1125614?sign=MTY5MzM1MzYwMDAwMHwxMS0xfDc0LTExMjU2MTR8TUJhYmZmR0NmNTY1cXNzQmU1ZEh1c3V4cTZQ%0D%0ATW9TNF9WLWNMc2kxZHFsOA0K%0D%0A&updated=1627651335092)

Then go to settings -> Build,Execution,Deployment -> Compiler and enable "Build project automatically"

Now run the project, see that the text is still loading, change the text (random characters on the end). The project should appear to run-again, refresh the webpage (F5) and you should see the new text load - all without stopping and starting the Java project.

## Develop application
* Back end application for any number of front ends (Vue, Swift, etc)

Rest API
* GET
* POST
* PUT
* DELETE

Service layer
* Main business layer to write all the logic

Data access repo layer
* DB access and operations

into finally the DB
* H2 in memory DB
* Swith to MySQL DB later

## Add dependency for H2 and JPA

Go to https://start.spring.io/ like before and add the dependency for "Spring Data JPA"

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
No version b/c whatever parent version is should be the same.

Add this to pom.xml.

### H2 config

Add the following 

```properties
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:dcbapp
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

Stop and restart the app (connections need to be opened for the first time)

Got to http://localhost:8082/h2-console

It should load up the H2 console

![H2 screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/h2.png)

Many things should populate from the application.properties
* Note: I had to drop in the JDBC URL from the config.

Type in the password and hit test connection

![H2 test connection screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/h2_test_connection.png)

## Creating components

Create the following packages:
1. department
2. entity
3. repository
4. service

Name make the Deparment Entity

```java
package com.harrison.Springboot.tutorial.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departmentID;
    private String departmentName;
    private String departmentAddress;
    private String departmentCode;

    public Long getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Long departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentAddress() {
        return departmentAddress;
    }

    public void setDepartmentAddress(String departmentAddress) {
        this.departmentAddress = departmentAddress;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Department(Long departmentID, String departmentName, String departmentAddress, String departmentCode) {
        this.departmentID = departmentID;
        this.departmentName = departmentName;
        this.departmentAddress = departmentAddress;
        this.departmentCode = departmentCode;
    }

    public Department() {
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentID=" + departmentID +
                ", departmentName='" + departmentName + '\'' +
                ", departmentAddress='" + departmentAddress + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                '}';
    }
}

```

Department service interface
```java
package com.harrison.Springboot.tutorial.service;

public interface DepartmentService {

}
```
Department service impl

```java
package com.harrison.Springboot.tutorial.service;

import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
}

```

DepartmentController
```java
package com.harrison.Springboot.tutorial.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {
}

```

And Department repository

```java
package com.harrison.Springboot.tutorial.repository;

import com.harrison.Springboot.tutorial.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> { // Entity and primary key type
}

```

Current solution tree looks like this:

![current solution tree 2 13 50 screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/current_solution_tree_2_13_50.png)

## Save API

DepartmentController
```java
package com.harrison.Springboot.tutorial.controller;

import com.harrison.Springboot.tutorial.entity.Department;
import com.harrison.Springboot.tutorial.service.DepartmentService;
import com.harrison.Springboot.tutorial.service.DepartmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {

    @Autowired // Auto wire the service you already have and attach to this controller object
    private DepartmentService departmentService;
    @PostMapping("/departments")
    public Department saveDepartment(@RequestBody Department department) { // JSON converted to Department
        // Spring will convert the data itself
        // Old way where we ask for the control, DepartmentService service = new DepartmentServiceImpl();
        // Invert control by using the Autowired annotation as seen above
        return departmentService.saveDepartment(department);
    }

}

```

DepartmentService.java
```java
package com.harrison.Springboot.tutorial.service;

import com.harrison.Springboot.tutorial.entity.Department;

public interface DepartmentService {

    public Department saveDepartment(Department department);
}
```

DepartmentServiceImpl
```java
package com.harrison.Springboot.tutorial.service;

import com.harrison.Springboot.tutorial.entity.Department;
import com.harrison.Springboot.tutorial.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department); // from the extended JpaRepository
    }
}

```

## Testing the Save API

Here will be using insomnia
* Postman, thunderclient etc also work.

Link: https://insomnia.rest/download

Load the app and H2 will make the department object for you.

![h2 udated with department screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/h2_updated_with_department.png)

Inside insomnia send this JSON

```json
{
	"departmentName":"IT",
	"departmentAddress":"Bangalore",
	"departmentCode":"IT-06"
}
```

it should return the information back and give 200 OK like so

![insomnia test post screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_test_post.png)

And H2 will reflect that


![h2_after_post screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/h2_after_post.png)


Same way with a second record

```json
{
	"departmentName":"CE",
	"departmentAddress":"Bangalore",
	"departmentCode":"CE-07"
}
```

H2 DB will have a 2nd row now.

Save is working great! Now lets work on GET APIs

## Get Mapping

Fetch data from DB

Add to DepartmentController
```java
@GetMapping("/departments")
public List<Department> fetchDepartmentList() {
    return departmentService.fetchDepartmentList(); // no input b/c sending all data back
}
```

Add to department service

```java
public List<Department> fetchDepartmentList();
```

Add to the department service impl
```java
@Override
public List<Department> fetchDepartmentList() {
    return departmentRepository.findAll(); // Done! Easy!
}
```

Restarted the app, need to post 3 department objects

And we get all three back like so:

![insomnia_test_get_all_depts screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_test_get_all_depts.png)

Works!

## Get dept by ID

Add to department controller

```java
@GetMapping("/departments/{id}") // path variable
public Department fetchDepartmentById(@PathVariable("id") Long departmentId) {
    return departmentService.fetchDepartmentById(departmentId);
}
```

Add to department service

```java
public Department fetchDepartmentById(Long departmentId);
```

Add to department service impl

```java
@Override
public Department fetchDepartmentById(Long departmentId) {
    return departmentRepository.findById(departmentId).get();
}
```

Quick test
* Note: need to add the data each time, for some reason its not persistent across app runs. Maybe gets solved later? H2 embed is a good test though.

![insomnia_test_get_dept_by_id screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_test_get_dept_by_id.png)

## Deleting data


Add to department controller

```java
@DeleteMapping("/departments/{id}")
public String deleteDepartmentById(@PathVariable("id") Long departmentId) {
    departmentService.deleteDepartmentById(departmentId);
    return "Department deleted successfully";
}
```

Add to department service

```java
public void deleteDepartmentById(Long departmentId);
```

Add to department service impl

```java
@Override
public void deleteDepartmentById(Long departmentId) {
    departmentRepository.deleteById(departmentId);
}
```

Quick test

![insomnia_test_delete_by_id screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_test_delete_by_id.png)

## Updating data

Add to department controller

```java
@PutMapping("/departments/{id}")
public Department updateDepartment(@PathVariable("id") Long departmentId, @RequestBody Department department) {
    return departmentService.updateDepartment(departmentId, department);
}
```

Add to department service

```java
public Department updateDepartment(Long departmentId, Department department);
```

Add to department service impl

```java
@Override
public Department updateDepartment(Long departmentId, Department department) {
    // Take value from DB
    Department depDB = departmentRepository.findById(departmentId).get();

    // test if incoming dept name is non-null and not empty
    if (Objects.nonNull(department.getDepartmentName()) && !"".equalsIgnoreCase(department.getDepartmentName())){
        depDB.setDepartmentName(department.getDepartmentName());
    }

    // test if incoming dept code is non-null and not empty
    if (Objects.nonNull(department.getDepartmentCode()) && !"".equalsIgnoreCase(department.getDepartmentCode())){
        depDB.setDepartmentCode(department.getDepartmentCode());
    }

    // test if incoming dept addr is non-null and not empty
    if (Objects.nonNull(department.getDepartmentAddress()) && !"".equalsIgnoreCase(department.getDepartmentAddress())){
        depDB.setDepartmentAddress(department.getDepartmentAddress());
    }

    // return the save entity
    return departmentRepository.save(depDB);
}
```

Setup, add 3 departments

![insomnia_test_update_start screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_test_update_start.png)

Update (PUT) on ID 2

```json
{
    "departmentName": "Information Technology",
    "departmentAddress": "Amal",
    "departmentCode": "CS-06"
}
```

![insomnia_test_update_id_2 screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_test_update_id_2.png)

Setup, end

![insomnia_test_update_end screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_test_update_end.png)

H2 reflects this change as well

![insomnia_test_update_end_h2 screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_test_update_end_h2.png)

You can also just update one of the fields by only sending the fields you want changed.

```json
{
    "departmentAddress": "New York"
}
```

![insomnia_test_update_partial screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_test_update_partial.png)

## Fetch data by name

Add to department controller

```java
@GetMapping("/department/name/{name}")
public Department fetchDepartmentByName(@PathVariable("name") String departmentName) {
    return departmentService.fetchDepartmentByName(departmentName);
}
```

Add to department service

```java
public Department fetchDepartmentByName(String departmentName);
```

Add to department service impl

```java
@Override
public Department fetchDepartmentByName(String departmentName) {
    // No default method to get by name, we have to create it our
    return departmentRepository.findByDepartmentNameIgnoreCase(departmentName);
}
```

No default method. Add this to the repository
```java
public Department findByDepartmentName(String departmentName);

public Department findByDepartmentNameIgnoreCase(String departmentName); // Spring keyword search will do this for us
```

Spring does automatic query creation.

https://docs.spring.io/spring-data/jpa/docs/1.6.0.RELEASE/reference/html/jpa.repositories.html

Example would be "Distinct" which converts to adding DISTINCT to the JPQL snippet.

You may have to force with query

```java
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u where u.emailAddress = ?1")
  User findByEmailAddress(String emailAddress);
}
```
?1 for the first param, ?2 for second and so on

## Hibernate Validation

Department name should always be available

Add this to pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Add the `@NotBlank` annotation to the departmentName field of the department.
```java
@NotBlank(message = "Please Add Department Name") // Error message given if blank
private String departmentName;
```

Add the `@Valid` annotation (from Jakarta) to the saveDepartment method of the DepartmentController.

```java
@Autowired // Auto wire the service you already have and attach to this controller object
private DepartmentService departmentService;
@PostMapping("/departments")
public Department saveDepartment(@Valid @RequestBody Department department) { // JSON converted to Department
    // Spring will convert the data itself
    // Old way where we ask for the control, DepartmentService service = new DepartmentServiceImpl();
    // Invert control by using the Autowired annotation as seen above
    // @Valid will check agains the @NotBlank over in the Department class
    return departmentService.saveDepartment(department);
}
```

Look at the defaultMessage in the lower right. "Please Add Department Name"

![pls_add_dept_name screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/pls_add_dept_name.png)

* @Length is there (min and max value)
* @Size - min and max
* @Email validation
* @Positive
* @Negative
* @PositiveOrZero
* @FutureOrPresent - on date fields
* @PastOrPresent

All diff validators. Link below for jakarta validation constraints.

https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/constraints/package-summary.html

## Adding Loggers

SLF4J logging

For department controller, Add the following java

```java
private final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);
```

Implement it in the methods down below like so
```java
@PostMapping("/departments")
public Department saveDepartment(@Valid @RequestBody Department department) { // JSON converted to Department
    // Spring will convert the data itself
    // Old way where we ask for the control, DepartmentService service = new DepartmentServiceImpl();
    // Invert control by using the Autowired annotation as seen above
    // @Valid will check agains the @NotBlank over in the Department class

    LOGGER.info("Inside saveDepartment of Department controller");

    return departmentService.saveDepartment(department);
}

@GetMapping("/departments")
public List<Department> fetchDepartmentList() {
    LOGGER.info("Inside fetchDepartmentList of Department controller");
    return departmentService.fetchDepartmentList(); // no input b/c sending all data back
}
```

Use Insomnia to hit the save dept and get all depts again and you get the below output.

output
```
2023-08-27T22:41:19.883-04:00  INFO 11956 --- [nio-8082-exec-1] c.h.S.t.controller.DepartmentController  : Inside saveDepartment of Department controller
2023-08-27T22:41:49.731-04:00  INFO 11956 --- [nio-8082-exec-2] c.h.S.t.controller.DepartmentController  : Inside fetchDepartmentList of Department controller
```
