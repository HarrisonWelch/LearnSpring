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

Change Doctor to this

```java
package demo;

public class Doctor implements Staff {

    private String qualification;
    public void assist(){
        System.out.println("Doctor is assisting");
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
```

Spring will try to create the object that is Doctor but it does not know the property. It will make it default to null. We can change this in the XML.

Change the doctor bean to this
```xml
<bean id="doctor" class="demo.Doctor">
    <property name="qualification" value="MBBS"></property>
</bean>
```

Change main to this
```java
ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

Doctor staff = context.getBean(Doctor.class);
staff.assist();
System.out.println(staff.getQualification());
```

output
```
Doctor is assisting
MBBS
```

So it works!

MBBS was injected from the XML

## Inject Objects

Say the doctor has a nurse like so. Doctor.java:
```java
package demo;

public class Doctor implements Staff {

    private String qualification;

    private Nurse nurse;
    public void assist(){
        System.out.println("Doctor is assisting");
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }
}
```
Then we change our spring.xml to this:

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
    <bean id="doctor" class="demo.Doctor">
        <property name="qualification" value="MBBS"></property>
        <property name="nurse" ref="nurse"></property>
    </bean>
    <bean id="nurse" class="demo.Nurse"></bean>
</beans>
```

Nurse must be defined as a bean. Id will match on the prop ref id to the bean id.

## Constructor based

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
    <bean id="doctor" class="demo.Doctor">
        <constructor-arg value="MBBS"></constructor-arg>
    </bean>
    <bean id="nurse" class="demo.Nurse"></bean>
</beans>
```

The object must have a constructor and MBBS will be assigned to the first arg

```java
package demo;

public class Doctor implements Staff {

    private String qualification;

    public Doctor(String qualification) {
        this.qualification = qualification;
    }

    public void assist(){
        System.out.println("Doctor is assisting");
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

}
```
output

```
Doctor is assisting
MBBS
```

works!

## Annotation based config

Change Doctor to use the `@Component` annotation
```java
package demo;

import org.springframework.stereotype.Component;

@Component
public class Doctor implements Staff {

    public void assist(){
        System.out.println("Doctor is assisting");
    }

}
```

Then add the component-scan to the spring.xml

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
    <context:component-scan base-package="demo"></context:component-scan>
</beans>
```

Can't use nurse b/c Nurse is not a component. Add `@Component` and it will work.

## Java configuration

Add the BeanConfig. It will do the component scan instead of XML.

```java
package demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "demo")
public class BeanConfig {

}

```

Change main to use "AnnotationConfigApplicationContext" on the new bean config class.

```java
package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);

        Staff staff = context.getBean(Nurse.class);
        staff.assist();
    }

}
```

output is the same

```
Nurse is assisting
```
Works!

## Define Bean in Java

Change BeanConfig to this

```java
package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.print.Doc;

@Configuration
@ComponentScan(basePackages = "demo")
public class BeanConfig {

    @Bean
    public Doctor doctor() {
        return new Doctor();
    }
}
```

Remove the component from Doctor since we are defining it in the BeanConfig

```java
package demo;

import org.springframework.stereotype.Component;

public class Doctor implements Staff {

    public void assist(){
        System.out.println("Doctor is assisting");
    }

}

```

Then main will work

```java
package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);

        Staff staff = context.getBean(Doctor.class);
        staff.assist();
    }

}
```
output

```
Doctor is assisting
```

Works

## Scopes

We want to define how they are used in Spring Bin. 5 scopes. default is singleton.

1. Singleton
2. Prototype
3. Request
4. Session
5. Global Session

Session and global session are mostly for web. We can see the singleton and prototype here.

Change Doctor to have qualification, g&s, and a toString.

```java
package demo;

import org.springframework.stereotype.Component;

public class Doctor implements Staff {

    private String qualification;

    public void assist(){
        System.out.println("Doctor is assisting");
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "qualification=" + qualification + '\"' +
                '}';
    }
}
```

In main we can demostrate that having the doctor we pulled from the bean bin still gets the MBBS as set on the first doctor object.

```java
package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);

        Doctor doctor = context.getBean(Doctor.class);
        doctor.assist();
        doctor.setQualification("MBBS");
        System.out.println(doctor);

        Doctor doctor1 = context.getBean(Doctor.class); // getting again from container
        System.out.println(doctor1);
    }

}
```
output

```
Doctor is assisting
Doctor{qualification=MBBS"}
Doctor{qualification=MBBS"}
```

Can be changed with `@Scope(scopeName = "...")`.
The default is `singleton`

This can change to prototype to get a new object. Note, this must be a component annotated java file. Needs to be removed from the bean config for now

Doctor.java
```java
package demo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = "prototype")
public class Doctor implements Staff {

    private String qualification;

    public void assist(){
        System.out.println("Doctor is assisting");
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "qualification=" + qualification + '\"' +
                '}';
    }
}

```

BeanConfig.java

```java
package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.print.Doc;

@Configuration
@ComponentScan(basePackages = "demo")
public class BeanConfig {

}
```

Main.java

```java
package demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);

        Doctor doctor = context.getBean(Doctor.class);
        doctor.assist();
        doctor.setQualification("MBBS");
        System.out.println(doctor);

        Doctor doctor1 = context.getBean(Doctor.class); // getting again from container
        System.out.println(doctor1);
    }

}
```
output
```
Doctor is assisting
Doctor{qualification=MBBS"}
Doctor{qualification=null"}
```

2nd one is null because of the scope change to prototype.

## Life cycle

1. Definition of bean
2. Instatiate those beans
    Populate the properties
  * Scope
3. Post-initialize
  * Interfaces will be executed
  * Custom execution
4. (available in container)
5. Pre-destroy
  * release connection or file
  * custom impl
    Bean destroy
6. Custom destroy

// TODO put screenshot here

custome methods
* Default values
* open or close files
* open or close DB connections

50:38
