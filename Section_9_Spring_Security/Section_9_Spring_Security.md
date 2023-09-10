# Section 9 Spring Security

* Very important
* User has to be authenticated and authorized
* Register user to system.
* How to let a user into your system
* OAuth open ID
* Create User with REST API
* Confirmation email after signing
* Forgot password API - Email setup token
* OAuth 2 and OpenID - Seend with "Login with Google" or "Login with FB"

Create the spring-security-tutorial in the IntelliJ idea.

Make the client with https://start.spring.io/

Click this link to auto-import: https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.1.3&packaging=jar&jvmVersion=11&groupId=com.harrison&artifactId=spring-security-client&name=spring-security-client&description=Demo%20project%20for%20Spring%20Boot&packageName=com.harrison.client&dependencies=web,lombok,data-jpa,mysql

Table if the link expires:
| Property      | Setting                                |
| ------------- | -------------------------------------- |
| Project       | Maven                                  |
| Language      | Java                                   |
| Spring Boot   | 3.1.3 (latest non-snapshot and non-M*) |
| Group         | com.name                               |
| Artifact      | spring-security-client                 |
| Name          | spring-security-client                 |
| Description   | Demo project for Spring Boot           |
| Package name  | com.harrison.client                    |
| Packing       | Jar                                    |
| Java          | 11                                     |

Then we are going to add
* Spring Data JPA
* Lombok
* MySQL Driver
* Spring Web

Generate and extract that into

In the outer project adjust the pom.xml to add this:
```xml
  <packaging>pom</packaging>

  <modules>
    <module>spring-security-client</module>
  </modules>
```

Now dig into the client project that we just labeled as a module to the outer project

Rename the application.properties to applcation.yml. Then put this in there:

```yml
Server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_registration
    username: root
    password: Bingbong123$
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update

```

Now create that schema in MySQL workbench

![sec_make_schema screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_make_schema.png)

Now build the following packages:
1. controller
2. config
3. entity
4. model
5. repository
6. service

![sec_pkg_struct screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_pkg_struct.png)

## Create Basic structure

RegistrationController.java
```java
package com.harrison.client.controller;

import com.harrison.client.entity.User;
import com.harrison.client.model.UserModel;
import com.harrison.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel) {
        User user = userService.registerUser(userModel);
        return null;
    }
}
```

UserRepository.java
```java
package com.harrison.client.repository;

import com.harrison.client.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
```

UserService.java
```java
package com.harrison.client.service;

import com.harrison.client.entity.User;
import com.harrison.client.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);
}
```

UserServiceImpl:
```java
package com.harrison.client.service;

import com.harrison.client.entity.User;
import com.harrison.client.model.UserModel;
import com.harrison.client.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(userModel.getPassword());

        return null;
    }
}
```

User.java:
```java
package com.harrison.client.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @Column(length = 60)
    private String password; // Will be encrypted later
    private String role;
    private boolean enabled = false; // default behavior
}
```

UserModel.java:
```java
package com.harrison.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String matchingPassword;
}
```

Now for the security part

WebSecurityConfig.java (start of it)
```java
package com.harrison.client.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSecurityConfig {
}

```

Now we go back to the https://start.spring.io/ and enter the security and open the Explore tab at the bottom. From there, this is what you want to put in the pom.xml (inner project for the client):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

Then add to the Web

```java
package com.harrison.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig {

    @Bean // So we can autowire it
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
```

Change the UserService to use the encoder
```java
    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);
        return user;
    }
```

Change the registration controller to return success
```java
    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel) {
        User user = userService.registerUser(userModel);
        return "Success";
    }
```

## Send user an email on registration

First we need to setup the actual event that will fire the email.

RegistrationCompleteEvent.java:

```java
package com.harrison.client.event;

import com.harrison.client.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public RegistrationCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
```

RegistrationCompleteEventListener.java:
```java
package com.harrison.client.listener;

import com.harrison.client.entity.User;
import com.harrison.client.event.RegistrationCompleteEvent;
import com.harrison.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create the verification token for this User
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        // Save this token on the DB (need to make this entity)
        userService.saveVerificationTokenForUser(user, token);
        // Send Mail to user
    }
}
```

VerificationToken.java
```java
package com.harrison.client.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
public class VerificationToken {

    // Expiration time is 10 minutes
    private static final int EXPIRATION_TIME = 10; // minutes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public VerificationToken (User user, String token) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = calculationExpirationTime(EXPIRATION_TIME);
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = calculationExpirationTime(EXPIRATION_TIME);
    }

    private Date calculationExpirationTime(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
```

VerificationTokenRepository.java
```java
package com.harrison.client.repository;


import com.harrison.client.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
```

UserService interface gets a new method
```java
void saveVerificationTokenForUser(User user, String token);
```

UserServiceImpl gets this impl of that method:
```java
@Override
public void saveVerificationTokenForUser(User user, String token) {
    VerificationToken verificationToken = new VerificationToken(user, token);

    verificationTokenRepository.save(verificationToken);
}
```

In this example the email will be put to the console.

Now over to the email part

RegistrationCompleteEventListener:
```java
@Override
public void onApplicationEvent(RegistrationCompleteEvent event) {
    // Create the verification token for this User
    User user = event.getUser();
    String token = UUID.randomUUID().toString();
    // Save this token on the DB (need to make this entity)
    userService.saveVerificationTokenForUser(user, token);
    // Send Mail to user
    String url = event.getApplicationUrl() + "verifyRegistration?token=" + token; // Context path

    // sendVerificationEmail() // Mocking it
    log.info("Click the link to verify your account: {}", url);
}
```

Change the RegistrationController to pass through this request:
```java
@PostMapping("/register")
public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
    User user = userService.registerUser(userModel);
    publisher.publishEvent(new RegistrationCompleteEvent(
        user,
        applicationUrl(request)
    )); // Build Url later
    return "Success";
}

private String applicationUrl(HttpServletRequest request) {
    return "http://" +
            request.getServerName() +
            ":" +
            request.getServerPort() +
            request.getContextPath();
}
```

Now go to http://localhost:8080/ which should fwd you to the login page

![sec_login screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_login.png)

Spring will give us a default password but we want a custom impl

Output
```
...
Using generated security password: f7a4dd85-811d-4601-afb1-39f575008f2f
...
```

We want to create a user that bypass the login. We have to impl this.

## Login Bypass
