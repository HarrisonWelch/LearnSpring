## SpringFrameWorkDemo

Notes for the companion Intellij project "SpringFrameWorkDemo"

Setup the initial state

Main.java
```java
package demo;

public class Main {

    public static void main(String[] args) {
        Doctor doctor = new Doctor();
        doctor.assist();
    }
}
```

Doctor.java
```java
package demo;

public class Doctor {

    public void assist() {
        System.out.println("Doctor is assisting.");
    }
}
```

output
```
Doctor is assisting.
```

## What if

* New doctor
* New objects added to class
* Suppose new classes are created and those objects are needed here



Doctor.java
```java
package demo;

public class Doctor {
    
    Qualification qualification;

    public void assist() {
        System.out.println("Doctor is assisting.");
    }
}
```

Doctor and Qualification are tightly coupled

Have to create a loose coupling.

We can use Doctor without a qualification (null)

* Loose coupling comes in from Spring
* Spring will try to reference all the classes
* "Reference graph"
* Don't have to use everything new
* "Might need" this class and this object and Spring will take care of everyhting.
* Inversion of control
* Dependency injection
  * Constructor
  * Setter base
  * annotation base

## pom.xml

```xml
  <dependencies>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.3.14</version>
    </dependency>
  </dependencies>
```

Right click -> Maven -> reload project

download all the jar files

Spring will create all the beans. Spring will load all the beans and store all the beans in the container. From that we use all the beans that are loaded and we don't have to create another object.

## Beans definition

Under resources define Spring.xml

Spring.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context.xsd">
    <bean>

    </bean>
</beans>
```
Then Main.java changed to this
```java
package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        Doctor doctor = context.getBean(Doctor.class);
        doctor.assist();
    }

}
```

Without a Doctor def it will fail thought will this error output
```
Exception in thread "main" org.springframework.beans.factory.parsing.BeanDefinitionParsingException: Configuration problem: Unnamed bean definition specifies neither 'class' nor 'parent' nor 'factory-bean' - can't generate bean name
Offending resource: class path resource [spring.xml]
```

We have to define the Doctor in the xml itself.

Change the spring.xml to this:
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context.xsd">
    <bean id="doctor" class="demo.Doctor"></bean>
</beans>
```

Running it again and it works!
```
Doctor is assisting
```

Now lets create a Nurse

Nurse.java
```java
package demo;

public class Nurse {
    public void assist(){
        System.out.println("Nurse is assisting");
    }
}

```

Lets put this in the main

```java
package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        Nurse doctor = context.getBean(Nurse.class);
        doctor.assist();
    }

}
```

Define the bean (place directly below the doctor bean)

```xml
<bean id="nurse" class="demo.Nurse"></bean>
```

And you get output
```
Nurse is assisting
```
All good!

You get get the ID as well

```java
Nurse doctor = (Nurse) context.getBean("nurse");
```

We don't often use this.

## Works for interfaces too

Make Staff.java
```java
package demo;

public interface Staff {
    void assist();
}
```

Attached that onto the Doctor and Nurse
```java
public class Doctor implements Staff {...}
```
```java
public class Nurse implements Staff {...}
```

And now main can use it like so

```java
package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        Staff staff = context.getBean(Doctor.class);
        staff.assist();
    }

}
```
Output
```
Doctor is assisting
```
This can switch to nurse with one line

```java
Staff staff = context.getBean(Nurse.class);
```

Output
```
Nurse is assisting
```

## Properties

What if we have some properties?


