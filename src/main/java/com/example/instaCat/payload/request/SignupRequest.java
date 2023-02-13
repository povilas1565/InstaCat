package com.example.instaCat.payload.request;

import com.example.instaCat.annotations.PasswordMatches;
import com.example.instaCat.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/*Объект этого класса мы будем передавать на сервер при попытке зарегистрировать нового пользователя*/

/*Далее - давайте посмотрим на проверки, которые контроллер будет выполнять при регистрации новой учетной записи:
Все обязательные поля заполнены (нет пустых полей)
Электронный адрес действителен (правильно сформирован)
Поле подтверждения пароля совпадает с полем пароля
Аккаунт еще не существует*/

//https://www.baeldung.com/registration-with-spring-mvc-and-spring-security

@Data
@PasswordMatches
public class SignupRequest {
    @Email(message = "It should be email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter your name")
    private String firstname;

    @NotEmpty(message = "Please enter your lastname")
    private String lastname;

    @NotEmpty(message = "Please enter your username (nickname)")
    private String username;

    @NotEmpty(message = "Please enter your password")
    @Size(min = 5, max = 50)
    private String password;

    @NotEmpty(message = "Please enter your password again")
    private String confirmPassword;
//
//    @NotEmpty(message = "Please enter your series again")
//    private String series;
//
//    @NotEmpty(message = "Please enter your number again")
//    private String number;
}
