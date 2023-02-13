package com.example.instaCat.annotations;

import com.example.instaCat.validators.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

//http://www.seostella.com/ru/article/2012/05/19/annotacii-v-java-vvedenie.html - открыть и прочитать студентам

//https://www.baeldung.com/registration-with-spring-mvc-and-spring-security

/*Далее - давайте проверим адрес электронной почты и убедимся, что он правильно сформирован.
Мы собираемся создать для этого кастомный валидатор, а также кастомную аннотацию валидации - назовем это @ValidEmail.

Небольшое примечание: мы запускаем нашу собственную аннотацию вместо @Email Hibernate, потому что Hibernate считает допустимым
старый формат адресов интранете: myaddress@myserver
(см. Статью Stackoverflow https://stackoverflow.com/questions/4459474/hibernate-validator-email-accepts-askstackoverflow-as-valid), что не очень хорошо.
Вот аннотация для проверки электронной почты и настраиваемый валидатор:*/

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE}) //Обратите внимание, что мы определили аннотацию на уровне FIELD, поскольку именно здесь она применяется конкретно к полю Email
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {
    String message() default "Invalid email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
