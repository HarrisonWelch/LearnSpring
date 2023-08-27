# Spring AOP Demo

Companion notes for the project of the same name.

Add dependencies. Viewable here: https://mvnrepository.com/

Allows you to quick paste dependencies from the website. - https://mvnrepository.com/artifact/org.springframework/spring-context/5.3.14

Also need AspectJ Runtime - https://mvnrepository.com/artifact/org.aspectj/aspectjrt/1.9.7

And finally ApspectJ Weaver - https://mvnrepository.com/artifact/org.aspectj/aspectjweaver/1.9.7

After all this, the pom.xml file will look like this:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.harrison</groupId>
  <artifactId>spring-aop-demo</artifactId>
  <version>1.0-SNAPSHOT</version>
  <name>Archetype - spring-aop-demo</name>
  <url>http://maven.apache.org</url>
  <properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.3.14</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjrt -->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjrt</artifactId>
      <version>1.9.7</version>
      <scope>runtime</scope>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.9.7</version>
      <scope>runtime</scope>
    </dependency>

  </dependencies>

</project>

```

## Initial Setup

Shopping Cart
```java
package demo;

import org.springframework.stereotype.Component;

@Component
public class ShoppingCart {
    public void checkout() {
        System.out.println("Checkout Method from Shopping Cart Called");
    }
}

```

BeanConfig
```java
package demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "demo")
public class BeanConfig {
}

```

Main
```java
package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);

        ShoppingCart cart = context.getBean(ShoppingCart.class);
        cart.checkout();
    }
}

```

output
```
Checkout Method from Shopping Cart Called
```

## Now cross cut concerns

```java
public void checkout() {
    // Logging
    // Authentication & Authorization
    // Sanitize the Data
    System.out.println("Checkout Method from Shopping Cart Called");
}
```

Lets use AOP in the next section.
