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
| Group         | com.harrison                               |
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

Make a simple hello controller for testing.

```java
package com.harrison.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Welcome to Daily Code Buffer!!";
    }

}
```

Make the securityFilterChain:
* Note I had to change many things about this method to make it work

```java
@Bean
@SuppressWarnings(value = {"deprecated", "removal"})
SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
            .cors()
            .and()
            .csrf()
            .disable()
            .authorizeHttpRequests()
//                .antMatchers(WHITE_LIST_URLS).permitAll(); // Removed in later patches
            .requestMatchers(HttpMethod.GET, WHITE_LIST_URLS).permitAll();
            .requestMatchers(HttpMethod.POST, WHITE_LIST_URLS).permitAll();

    return httpSecurity.build();
}
```

Simple hello test
![sec_hello screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_hello.png)

Now over in PostMan

```json
{
    "firstName":"Shabbir",
    "lastName":"Dawoodi",
    "password":"1234567",
    "email":"shabbir@gmail.com"
}
```

Post user in PostMan

![sec_post_user screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_post_user.png)

DB creates user

![sec_db_user_create screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_db_user_create.png)

But the token did not create:

![sec_db_token_not_made screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_db_token_not_made.png)

Delete the user from the DB (or drop all tables) and restart the app

Now we can see the verification token (user table matches prev)

![sec_db_veri_token screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_db_veri_token.png)

Output:

```
Hibernate: insert into user (email,enabled,first_name,last_name,password,role) values (?,?,?,?,?,?)
Hibernate: insert into verification_token (expiration_time,token,user_id) values (?,?,?)
2023-09-10T17:02:26.519-04:00  INFO 30260 --- [nio-8080-exec-1] .h.c.l.RegistrationCompleteEventListener : Click the link to verify your account: http://localhost:8080verifyRegistration?token=b4497cb9-aa2e-4971-bbc9-ca0b5b548ebb
```

This link will be sent in an email usually, but console will work to mock that route.

Quick correction, onApplicationEvent should have the 

```java
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create the verification token for this User
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        // Save this token on the DB (need to make this entity)
        userService.saveVerificationTokenForUser(user, token);
        // Send Mail to user
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token; // Context path

        // sendVerificationEmail() // Mocking it
        log.info("Click the link to verify your account: {}", url);
    }
```

Now we need to respond to when that user is 

## Respond to the email link

Add a registration controller to respond to the user clicking the link

```java
@GetMapping("/verifyRegistration")
public String verifyRegistration(@RequestParam("token") String token) {
    String result = userService.validateVerficationToken(token);
    if (result.equalsIgnoreCase("valid")) {
        return "User Verifies Sucessfully";
    }
    return "Bad User";
}
```

And then add to the userService

```java
String validateVerficationToken(String token);
```

And impl like this 

```java
@Override
public String validateVerficationToken(String token) {
    VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

    // Does that verification exist at all?
    if (verificationToken == null) {
        return "invalid";
    }

    // Pull user
    User user = verificationToken.getUser();
    Calendar calendar = Calendar.getInstance();

    // Check expiration
    if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
        verificationTokenRepository.delete(verificationToken);
        return "expired";
    }

    // User allowed in system
    user.setEnabled(true);
    userRepository.save(user);

    // Pass back approve
    return "valid";
}
```

Add the `verifyRegistration` to the list URLS

```java
private static final String[] WHITE_LIST_URLS = {
        "/hello/**",
        "/register/**",
        "/verifyRegistration/**"
};
```

Resend the user on a fresh run the application. Delete or drop all tables to reset the DB.

Now click the link in the logs similar to what was seen previously.
* Example: http://localhost:8080/verifyRegistration?token=9eabd30e-0308-42a0-857c-cb294fc7338e

User Verifies as seen here:

![sec_verify_user screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_verify_user.png)

And user is enabled on the DB

![sec_db_user_enabled screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_db_user_enabled.png)

## Resend logic

RegistrationController.java

```java
@GetMapping("/resendVerifyToken")
public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
    VerificationToken verificationToken = userService.generateNewVerification(oldToken);
    User user = verificationToken.getUser();
    resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
    return "Verification link sent.";
}

private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
    // Send Mail to user
    String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken(); // Context path

    // sendVerificationEmail() // Mocking it
    log.info("Click the link to verify your account: {}", url);
}
```

UserService.java:
```java
VerificationToken generateNewVerification(String oldToken);
```

UserServiceImpl.java:
```java
@Override
public VerificationToken generateNewVerification(String oldToken) {
    VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
    verificationToken.setToken(UUID.randomUUID().toString());
    verificationTokenRepository.save(verificationToken);
    return verificationToken;
}
```

Now instead of verifyRegistration on the link we use `resendVerifyToken`

We can use postman for this:
* http://localhost:8080/verifyRegistration?token=780238e7-b537-4dd4-8f91-832ffdcc292c

![sec_resend_token screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_resend_token.png)

We will notice the token changed from 
* c8a2962d-7bf8-4c3d-bdd6-3532b07d7249

To:
* 780238e7-b537-4dd4-8f91-832ffdcc292c

DB reflects this:

![sec_db_token_change_after_resend screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_db_token_change_after_resend.png)

So the resend clearly works!

We can also verify user by clicking the same verifyUser link

![sec_db_veri_resend screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_db_veri_resend.png)

Now we can resend if we didn't get the token the first time.

## Reset password

RegistrationController.java:
```java
@PostMapping("/savePassword")
public String savePassword(@RequestParam("token") String token,
                            @RequestBody PasswordModel passwordModel) {
    String result = userService.validatePasswordResetToken(token);
    if (!result.equalsIgnoreCase("valid")) {
        return "Invalid Token";
    }

    // Token valid, update password
    Optional<User> user = userService.getUserByPasswordResetToken(token);
    if (user.isPresent()) {
        userService.changePassword(user.get(), passwordModel.getNewPassword());
        return "Password Reset Successful";
    }
    return "Invalid Token";
}
```

UserService.java
```java
String validatePasswordResetToken(String token);

Optional<User> getUserByPasswordResetToken(String token);

void changePassword(User user, String newPassword);
```

UserServiceImpl.java
```java
@Override
public String validatePasswordResetToken(String token) {
    PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

    // Does that verification exist at all?
    if (passwordResetToken == null) {
        return "invalid";
    }

    // Pull user
    Calendar calendar = Calendar.getInstance();

    // Check expiration
    if ((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
        passwordResetTokenRepository.delete(passwordResetToken);
        return "expired";
    }

    // Pass back approve
    return "valid";
}

@Override
public Optional<User> getUserByPasswordResetToken(String token) {
    return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
}

@Override
public void changePassword(User user, String newPassword) {
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
}
```

Remember to add to the whitelist in WebSecurityConfig it's currently out of date.
```java
private static final String[] WHITE_LIST_URLS = {
        "/hello/**",
        "/register/**",
        "/verifyRegistration/**",
        "/resendVerifyToken/**",
        "/savePassword/**",
        "/resetPassword/**"
};
```

Going to reset the password for Nikhil.

http://localhost:8080/resetPassword

{
    "email":"nikhil@gmail.com"
}

Example:

sec_reset_password

![sec_reset_password screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_reset_password.png)

DB has that reset token:

![sec_reset_password_token screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_reset_password_token.png)

POST to url: http://localhost:8080/savePassword?token=b7e44c38-f409-45dc-94e3-bfc25e77f565

with raw JSON as the body
```json
{
    "newPassword":"123"
}
```

![sec_reset_password_success screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_reset_password_success.png)

## Change password

RegistrationController.java:
```java
@PostMapping("/changePassword")
public String changePassword(@RequestBody PasswordModel passwordModel) {
    User user = userService.findUserByEmail(passwordModel.getEmail());

    // Check if old is still ok
    if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())) {
        return "Invalid Old Password";
    }

    // Save New Password
    userService.changePassword(user, passwordModel.getNewPassword());

    return "Password Changed Successfully";
}
```

UserService.java
```java
void changePassword(User user, String newPassword);

boolean checkIfValidOldPassword(User user, String oldPassword);
```

UserServiceImpl.java
```java
@Override
public void changePassword(User user, String newPassword) {
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
}

@Override
public boolean checkIfValidOldPassword(User user, String oldPassword) {
    return passwordEncoder.matches(oldPassword, user.getPassword());
}
```

Try to use the wrong old password
```json
{
    "email":"nikhil@gmail.com",
    "oldPassword":"456",
    "newPassword":"789"
}
```

You get the old password invalid error response

![sec_db_invalid_old_password screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_db_invalid_old_password.png)

Enter it correct and it works

![sec_db_change_password screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_db_change_password.png)

Mention of this article for which this tutorial is based.
https://www.baeldung.com/registration-with-spring-mvc-and-spring-security 

## Login functionality - OAuth 2.0 overview
* Stands for Open Authorization is a standard designed to allow a website or application to access resources hosted by other web apps on behalf of the user.
* Access must be given permission

### OAuth 2.0
* Authorization protocol not authentication protocol
* designed primarily as a means of granting access to a set of resources, for example, remote APIs or user's data.
* OAuth 2.0 uses Access Tokens

### OAuth 2.0 Roles
* Resource Owner
* Client
* Authorization Server
* Resource Server

### Resource Owner
* The user or sustem that owns the protected resources and can grant access to them
* That is the user most times. You. The device operator. The rights owner

### Client
* The System that requires access to the protected resources. To access resources, the Client must hold the appropriate Access Token.

### Authorization Server
* This server recieves requests from the Client for Access Tokens and issues them upon sucessful authentication and consent by the Resource Owner.

### Resource Server
* A server that protects the user's resources and receives access requests from the Client. It accepts and validates an Access Token from the Client and returns the appropriate resources to it.

We impl the Auth server, resource server, and the client.

### OAuth 2.0 Scopes
* Important
* Specify the exact reason for which accees to resources may be granted
* Sign in with Google, enter your G credentials, then presented with a screen of what will be granted

## Abstract Protocol Flow
* App requests client for auth rights, User grants given back to app
* App gives Auth grant to the auth server, Auth server gives access token back
* App gives Access token to resource server, protected resource is given back 

![Abstract Protocol Flow chart](https://assets.digitalocean.com/articles/oauth/abstract_flow.png)
- Credit digital Ocean: https://www.digitalocean.com/community/tutorials/an-introduction-to-oauth-2

## Auth playground
* OAuth playground: https://www.oauth.com/playground/
* Register Client -> click register
* Client registration -> Click the register button at the bottom

![sec_auth_playground screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_auth_playground.png)

A new modal screen comes up with client registration

Example:
```
client_id	Qy1LGghTHRRSAoLDJPS4Cml8
client_secret	5QG7k21M9yJlDuALHPOI-hC6GfyOnMdleKtGgg-C-wuD44Qo
login	foolish-rattlesnake@example.com
password	Selfish-Mockingbird-58
```

![sec_auth_register screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_auth_register.png)

Given a link

```sh
https://authorization-server.com/authorize?
  response_type=code
  &client_id=Qy1LGghTHRRSAoLDJPS4Cml8
  &redirect_uri=https://www.oauth.com/playground/authorization-code.html
  &scope=photo+offline_access
  &state=ADZG-smA6_WeQc0t
```

![sec_auth_build_auth_url screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_auth_build_auth_url.png)

Login

![sec_auth_login screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_auth_login.png)

Consent

![sec_auth_consent screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_auth_consent.png)

Matches or Doesnt

![sec_auth_verify screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_auth_verify.png)

Auth Code

```sh
POST https://authorization-server.com/token

grant_type=authorization_code
&client_id=Qy1LGghTHRRSAoLDJPS4Cml8
&client_secret=5QG7k21M9yJlDuALHPOI-hC6GfyOnMdleKtGgg-C-wuD44Qo
&redirect_uri=https://www.oauth.com/playground/authorization-code.html
&code=BbsJfHTnNOXHEUBM2IgG0NYcLNxYKRzW7pfZ5i3_6UppSUfJ
```

![sec_db_auth_code screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_db_auth_code.png)

Token Endpoint Response
```json
{
  "token_type": "Bearer",
  "expires_in": 86400,
  "access_token": "w5urqRIZTlb02FO-bkhgmKF6oezhBQYpvVwMaejciHSogpQRlTUa3Vwjj7d_Yblw6UvwiJax",
  "scope": "photo offline_access",
  "refresh_token": "_cupH5viZlESyMiZkyCqhI9S"
}
```

![sec_auth_token_endpoint_res screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/sec_auth_token_endpoint_res.png)

## Impl of the Authorization server - Setup on the User Service classes

Back to https://start.spring.io/

Link: https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.1.3&packaging=jar&jvmVersion=11&groupId=com.harrison&artifactId=Oauth-authorization-server&name=Oauth-authorization-server&description=Demo%20project%20for%20Spring%20Boot&packageName=com.harrison.oauthserver&dependencies=lombok,data-jpa,mysql,security,web

Table if the link expires:
| Property      | Setting                                |
| ------------- | -------------------------------------- |
| Project       | Maven                                  |
| Language      | Java                                   |
| Spring Boot   | 3.1.3 (latest non-snapshot and non-M*) |
| Group         | com.harrison                               |
| Artifact      | Oauth-authorization-server              |
| Name          | Oauth-authorization-server                 |
| Description   | Demo project for Spring Boot           |
| Package name  | com.harrison.oauthserver                    |
| Packing       | Jar                                    |
| Java          | 11                                     |

Then we are going to add
* Spring Data JPA
* Lombok
* MySQL Driver
* Spring Web
* Spring Security

Add one more dependency in the Oauth-authorization-server module pom.xml
```xml
<!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-oauth2-authorization-server -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-authorization-server</artifactId>
</dependency>
```

Change the application yml

```yml
server:
  port: 9000

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_registration
    username: root
    password: Bingbong123$
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: true
```

User object is copied over from the client

```java
package com.harrison.oauthserver.entity;

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

UserRepository.java is the same way
```java
package com.harrison.oauthserver.repository;

import com.harrison.oauthserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
}
```

CustomUserDetailsService.java is new
```java
package com.harrison.oauthserver.service;

import com.harrison.oauthserver.entity.User;
import com.harrison.oauthserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Username is email
        User user = userRepository.findUserByEmail(email);

        // No user on our DB
        if (user == null) {
            throw new UsernameNotFoundException("No User Found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                getAuthorities(List.of(user.getRole()))
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}

```

Authorization server config
```java
package com.harrison.oauthserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        return httpSecurity.formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // Added one client staticly because we only expect one client
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("api-client") // Name of the client
                .clientSecret(passwordEncoder.encode("secret")) // Hard coded
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // all 3 things we want to grant
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/api-client-oidc") // want to go back to this url
                .redirectUri("http://127.0.0.1:8080/authorized")
                .scope(OidcScopes.OPENID)
                .scope("api.read")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient); // can use the jdbc impl if u want by default
    }


    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /*
    https://stackoverflow.com/questions/75647797/providersettings-class-not-found-it-says-cannot-resolve-symbol-providersettings
     */
    @Bean
    public AuthorizationServerSettings providerSettings() { // Class name changed. SO article above.
        return AuthorizationServerSettings.builder()
                .issuer("http://auth-server:9000")
                .build();
    }

}

```

## Basic Spring Security configuration
* `cat /private/etc/hosts` for Mac/Linux
* Add a layer for the auth-server on the hosts
* For windows C:\Windows\System32\drivers\etc folder

Find hosts:

![hosts screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/hosts.png)

Edit the hosts:

![hosts_edit screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/hosts_edit.png)

## Basic Spring Security configuration

Authenticate all the fields are correct

CustomAuthenticationProvider.java
```java
package com.harrison.oauthserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails user = customUserDetailsService.loadUserByUsername(username);
        return checkPassword(user, password);
    }

    private Authentication checkPassword(UserDetails user, String rawPassword) {
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
        }
        throw new BadCredentialsException("Bad Credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

Bind it in the security config

```java
package com.harrison.oauthserver.config;

import com.harrison.oauthserver.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class DefaultSecurityConfig {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated() // any request has to be authenticated
                )
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Autowired
    public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider); // Bind it
    }
}

```

## Register client
