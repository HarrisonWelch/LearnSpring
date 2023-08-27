# Logging Aspect example

Continuing with the same project from Section 4 "spring-aop-demo"

Create logging Aspect
* Note: it must be a component

```java
package demo;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* demo.ShoppingCart.checkout())")
    public void logger() {
        // This is a point-cut
        System.out.println("Loggers here");
    }

}
```

And the BeanConfig needs to enable auto proxy

```java
package demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "demo")
@EnableAspectJAutoProxy
public class BeanConfig {
}

```
Then we get the right output

```
Loggers here
Checkout Method from Shopping Cart Called
```

## Different point-cuts
* Before
* After
* After return
* After throw
* Around the method

## Example of "after"

```java

package demo;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* demo.ShoppingCart.checkout())") // first star required.
    public void beforeLogger() {
        // This is a point-cut
        System.out.println("Before Logger");
    }

    @After("execution(* *.*.checkout())") // star for any package, any class, but only checkout method
    public void afterLogger() {
        System.out.println("After Logger");
    }

}

```
output
```
Before Logger
Checkout Method from Shopping Cart Called
After Logger
```

If the ShoppingCart changes signature, ie add another param, then apsect loggers wont work

```java
public void checkout(String status) {...}
```
output
```
Checkout Method from Shopping Cart Called
```

To fix that, add two dots in the method execute string in the logging aspect

```java
@Before("execution(* demo.ShoppingCart.checkout(..))") // first star required.
public void beforeLogger() {
    // This is a point-cut
    System.out.println("Before Logger");
}

@After("execution(* *.*.checkout(..))") // star for any package, any class, but only checkout method
public void afterLogger() {
    System.out.println("After Logger");
}
```
And it works again
```output
Before Logger
Checkout Method from Shopping Cart Called
After Logger
```

