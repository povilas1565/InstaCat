package com.example.instaCat.controller;

import com.example.instaCat.exceptions.UserAlreadyExistException;
import com.example.instaCat.payload.request.LoginRequest;
import com.example.instaCat.payload.request.SignupRequest;
import com.example.instaCat.payload.response.JWTSuccessResponse;
import com.example.instaCat.payload.response.MessageResponse;
import com.example.instaCat.security.SecurityConstants;
import com.example.instaCat.security.jwt.JWTProvider;
import com.example.instaCat.service.UserService;
import com.example.instaCat.validators.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private UserService userService;

    //api/auth/signup
    //метод который будет принимать данные пользователя чтобы он мог зарегистрироваться

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        //Для начала проверим наличие ошибок:
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        //пробуем создать юзера
        try {
            userService.createUser(signupRequest);
            return ResponseEntity.ok(new MessageResponse("Registration successfully completed"));
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }

    }


    //api/auth/signin
    //метод который будет принимать данные пользователя чтобы он мог авторизоваться

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        //Для начала проверим наличие ошибок:
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTSuccessResponse(true, jwt));
    }
}
