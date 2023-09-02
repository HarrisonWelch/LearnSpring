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

share link: https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.1.3&packaging=jar&jvmVersion=11&groupId=com.harrison&artifactId=Spring-boot-tutorial&name=Spring-boot-tutorial&description=Demo%20project%20for%20Spring%20Boot&packageName=com.harrison.Springboot.tutorial&dependencies=web,h2,devtools

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

## Project Lombok

Create a lot of POJOs plain old java objects

Lombok removes boilerplate

Go back to https://start.spring.io/

Search for Lombok and add it

Then grab the pom.xml from the explore button and move it into intellij

Copy paste this into the pom.xml of our project

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

Also grab the plugin here. Note the old plugin should be removed. The entire build tag will look like this:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

You can also search inside IntelliJs plugin manager in the Settings menu

![lombok_already_installed screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/lombok_already_installed.png)

Change department to this

```java
package com.harrison.Springboot.tutorial.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // From Lombok
//@Getter
//@Setter // Can also use these for just the pieces you want
@NoArgsConstructor // Default constructor added
@AllArgsConstructor // All fields used constructor
@Builder // Add a builder pattern
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departmentID;

    @NotBlank(message = "Please Add Department Name") // Error message given if blank
    private String departmentName;
    private String departmentAddress;
    private String departmentCode;
}
```

Annotations guide it all through Lombok

## Exception Handling 

Throw an exception by asking the get all departments to get an ID that does not exist. Example:

![insomnia_exception_example screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_exception_example.png)

Create a new package for error in the tree. Same level as controller, etc.

DepartmentNotFoundException
```java
package com.harrison.Springboot.tutorial.error;

public class DepartmentNotFoundException extends Exception {
    public DepartmentNotFoundException() {
        super();
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }

    public DepartmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepartmentNotFoundException(Throwable cause) {
        super(cause);
    }

    protected DepartmentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
```

Then in the department service impl. Get the optional, detect if present, error if not, return if good.

```java
@Override
public Department fetchDepartmentById(Long departmentId) throws DepartmentNotFoundException {
    Optional<Department> department = departmentRepository.findById(departmentId); // Have to use get b/c it's an optional

    if (!department.isPresent()) {
        throw new DepartmentNotFoundException("Department Not Available");
    }

    return department.get();
}
```

IntelliJ should add the throws itself, then ask to put it on the service interface used.

```java
public Department fetchDepartmentById(Long departmentId) throws DepartmentNotFoundException;
```

Then the department controller needs to have the throws added as well.

```java
@GetMapping("/departments/{id}") // path variable
public Department fetchDepartmentById(@PathVariable("id") Long departmentId) throws DepartmentNotFoundException {
    return departmentService.fetchDepartmentById(departmentId);
}
```

Now we get a meaningful error like so:

![insomnia_better_exception_example screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_better_exception_example.png)

But we still don't want the gargled mess.

We need a class for sending the exception back for the error thrown.

Define a new class `RestResponseEntityExceptionHandler` which can better handle when our custom exception fires.

```java
package com.harrison.Springboot.tutorial.error;

import com.harrison.Springboot.tutorial.entity.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice // Currently for all entities, will run with exception
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ErrorMessage> departmentNotFoundException(DepartmentNotFoundException exception, WebRequest request){
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
```

And we define a new ErrorMessage class

```java
package com.harrison.Springboot.tutorial.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    private HttpStatus status;
    private String message; // error message
}
```

![insomnia_even_better_exception_example screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/insomnia_even_better_exception_example.png)

## Changing H2 over to MySQL

Download MySQL workbench

https://dev.mysql.com/downloads/workbench/

I used the Windows installer and just installed everything and left all settings to default. Note - remember your password.

Now change the `application.properties`

```properties
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/dcbapp
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

# queries in the log
spring.jpa.show-sql: true
```

Now add mysql driver in pom.xml

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

We have to add the scheme `dcbapp` now

Right click -> Create Schema

![mysql_dcbapp screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/mysql_dcbapp.png)

No tables in the beginning

Start application and they should populate

Note: logs should table create

![mysql_table_create screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/mysql_table_create.png)

Run the create dept insomnia then do a quick test on the DB.

```sql
SELECT * FROM dcbapp.department
```

mysql_table_test

![mysql_table_test screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/mysql_table_test.png)

## Unit Testing

Every application should do unit testing

Integration testing in the end to end flow

Test individual components

Concept of mocking for unit testing

Why Mocking?

Consider testing the controller layer, now you are just testing the controller layer not how the data has flowed in from the service layer. Yet its still dependent on that service layer. This is called mocking.

## Unit Testing - Service Layer

Let impliments this

Go to department Service and right click generate

![generate_test screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/generate_test.png)

![generate_test_2 screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/generate_test_2.png)

Then click generate but leave everything blank

![generate_test_3 screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/generate_test_3.png)

A new class should make called `DepartmentServiceTest`

```java
package com.harrison.Springboot.tutorial.service;

import com.harrison.Springboot.tutorial.entity.Department;
import com.harrison.Springboot.tutorial.repository.DepartmentRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @MockBean // From Mockito
    private DepartmentRepository departmentRepository;

//    @BeforeAll // Before all test cases, once (ex 5 cases, 1 call here)
    @BeforeEach // Method called for each test case in this file (ex 5 cases, 5 calls)
    void setUp() {
        Department department = Department.builder()
                                .departmentName("IT")
                                .departmentAddress("Ahmedabad")
                                .departmentCode("IT-06")
                                .departmentID(1L)
                                .build();

        // Mock bean and mock method always available
        Mockito.when(departmentRepository.findByDepartmentNameIgnoreCase("IT"))
                .thenReturn(department);
    }

    @Test
    @DisplayName("Get Data based on Valid Department Name") // Gives it a name
//    @Disabled // Disables a test case
    public void whenValidDepartmentName_thenDepartmentShouldFound() { // Long name is ok, needs to be very unique

        // Need to get department
        String departmentName = "IT";
        Department found = departmentService.fetchDepartmentByName(departmentName);

        assertEquals(departmentName, found.getDepartmentName());

        // Need positive and negative cases
        // We need to mock the layers

    }
}
```

## Unit Testing - Repository Layer

Generate a test. Right click generate on the DepartmentRepository.

This should make the new class DepartmentRepositoryTest. All pkgs done for you.

```java
package com.harrison.Springboot.tutorial.repository;

import com.harrison.Springboot.tutorial.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        Department department =
                Department.builder()
                        .departmentName("Mechanical Engineering")
                        .departmentCode("ME - 011")
                        .departmentAddress("Delhi")
                        .build();

        entityManager.persist(department); // Make data before test case call

        // Won't make persistent changes to the DB.
    }

    @Test
    public void whenFindById_thenReturnDepartment() {
        Department department = departmentRepository.findById(1L).get();
        assertEquals(department.getDepartmentName(), "Mechanical Engineering");
    }
}
```

## Unit Testing - Controller Layer

Controller layer is hit with endpoints. WebMockMVC used.

```java
package com.harrison.Springboot.tutorial.controller;

import com.harrison.Springboot.tutorial.entity.Department;
import com.harrison.Springboot.tutorial.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock the service layer
    @MockBean
    DepartmentService departmentService;

    Department department;

    @BeforeEach
    void setUp() {
        this.department = Department.builder()
                .departmentAddress("Ahmedabad")
                .departmentCode("IT-06")
                .departmentName("IT")
                .departmentID(1L)
                .build();
    }

    @Test
    void saveDepartment() throws Exception {
        // Save dept of service layer
        // Mock it with MockBean

        Department inputDepartment = Department.builder()
                .departmentAddress("Ahmedabad")
                .departmentCode("IT-06")
                .departmentName("IT")
                .build();

        Mockito.when(departmentService.saveDepartment(inputDepartment)).thenReturn(department);

        // Call endpoint
        mockMvc.perform(post("/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"departmentName\":\"IT\",\n" +
                        "\t\"departmentAddress\":\"Ahmedabad\",\n" +
                        "\t\"departmentCode\":\"IT-06\"\n" +
                        "}"
                )).andExpect(status().isOk());
    }

    @Test
    void fetchDepartmentById() throws Exception {
        Mockito.when(departmentService.fetchDepartmentById(1L)).thenReturn(department);

        mockMvc.perform(get("/departments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName")
                        .value(department.getDepartmentName()));
    }
}
```

## Adding config in properies file

Add configs in your config app properties.

```properties
server.port = 8082
# Documentation available for each property

## Add your own configuration

some.config = Some Configuration Data
# Hello Controller
welcome.message = Welcome to Daily Code Buffer!!
# Now you just change the value here and the code will adjust

#spring.h2.console.enabled=true
#spring.datasource.url=jdbc:h2:mem:dcbapp
#spring.datasource.driverClassName=org.h2.Driver
#spring.datasource.username=sa
#spring.datasource.password=password
#spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/dcbapp
spring.datasource.username=root
spring.datasource.password=Bingbong123$
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

# Queries in the log
spring.jpa.show-sql: true
```

Then we can use them in the HelloController (viewed when going to http://localhost:8082/ with the application live)

```java
package com.harrison.Springboot.tutorial.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController // Component of a particular type, component is added by this annotation
public class HelloController {

    @Value("${welcome.message}") // fetch from application.properties and make this var equal to it
    private String welcomeMessage;

    @GetMapping("/") // Very simple now
    public String helloWorld() {
        return welcomeMessage;
    }
}
```

`@Value` can also sepcifiy what prop file you want to take from. The annotation comes from SpringBoot.

## Adding application.yml


