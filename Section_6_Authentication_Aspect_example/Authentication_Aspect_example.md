## Authentication Aspect example

Can create one package for multiple types of point cuts

AuthenticationAspect.java

```java
package demo;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAspect {

    @Pointcut("within(demo..*)")
    public void authenticatingPointCut() {

    }

    @Pointcut("within(demo..*)")
    public void authorizationPointCut() {

    }

    @Before("authenticatingPointCut() && authorizationPointCut()") // AND operator
    public void authenticate() {
        System.out.println("Authenticating the Request");
    }
}

```
output
```
Authenticating the Request
Before Logger
Checkout Method from Shopping Cart Called
After Logger
```

## After Returning

```java
package demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* demo.ShoppingCart.checkout(..))") // first star required.
    public void beforeLogger(JoinPoint jp) {
        // System.out.println(jp.getSignature()); // --> void demo.ShoppingCart.checkout(String)
        String arg = jp.getArgs()[0].toString();
        System.out.println("Before Logger with Argument: " + arg); // --> Before Logger with Argument: CANCELLED
    }

    @After("execution(* *.*.checkout(..))") // star for any package, any class, but only checkout method
    public void afterLogger() {
        System.out.println("After Logger");
    }

    @Pointcut("execution(* demo.ShoppingCart.quantity(..))")
    public void afterReturningPointCut() {
    }

    @AfterReturning(pointcut = "afterReturningPointCut()", returning = "retVal")
    public void afterReturning(String retVal) {
        System.out.println("After returning : " + retVal);
    }

}
```

