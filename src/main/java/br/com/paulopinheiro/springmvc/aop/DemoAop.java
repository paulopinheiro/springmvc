package br.com.paulopinheiro.springmvc.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class DemoAop {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext();
        context.scan("br.com.paulopinheiro.springmvc.aop");
        context.refresh();

        User user = (User) context.getBean("user-aop");

        System.out.println("==== First Method Demo ====");
        System.out.println(user.getFirstName());

        System.out.println("==== Seconde Method Demo ====");
        user.throwException();
    }
}
