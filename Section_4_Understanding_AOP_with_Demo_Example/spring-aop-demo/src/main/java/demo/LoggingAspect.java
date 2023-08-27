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
