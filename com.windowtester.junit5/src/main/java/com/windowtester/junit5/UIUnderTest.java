package com.windowtester.junit5;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This represents the UI to be tested. Windowtester will take care to make this ui visible.
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface UIUnderTest {

  String title() default "";

  int width() default 400;

  int height() default 300;
}
