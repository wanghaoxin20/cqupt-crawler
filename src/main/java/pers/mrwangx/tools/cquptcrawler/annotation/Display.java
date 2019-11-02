package pers.mrwangx.tools.cquptcrawler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/2
 * \* Time: 1:00
 * \* Description:
 **/
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Display {


    String value() default "";

    boolean display() default true;


    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Separator {
        String value();
    }

}
