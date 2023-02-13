package com.example.instaCat.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/*Объект этого класса мы будем передавать на сервер при попытке авторизоваться*/

@Data
public class LoginRequest {
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
