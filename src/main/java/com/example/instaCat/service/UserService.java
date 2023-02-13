package com.example.instaCat.service;

import com.example.instaCat.dto.UserDTO;
import com.example.instaCat.entity.User;
import com.example.instaCat.entity.enums.ERole;
import com.example.instaCat.exceptions.UserAlreadyExistException;
import com.example.instaCat.payload.request.SignupRequest;
import com.example.instaCat.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //https://habr.com/ru/post/334636/

    /*В основном стоит избегать внедрения через поля. Как альтернативу для внедрения следует использовать сеттеры или конструкторы.
      У каждого из них есть свои преимущества и недостатки в зависимости от ситуации. Однако так как эти подходы можно смешивать,
      это не выбор «или-или» и вы можете в одном классе комбинировать инъекцию и через сеттер, и через конструктор.
      Конструкторы больше подходят для обязательных зависимостей и при нужде в неизменяемых объектах.
      Сеттеры лучше подходят для опциональных зависимостей.*/
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User createUser(SignupRequest userIn) {
        User user = new User();
//        Passport passport = new Passport();
        user.setEmail(userIn.getEmail());
        user.setFirstname(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

//        passport.setSeries(userIn.getSeries());
//        passport.setNumber(userIn.getNumber());
//        user.setPassport(passport);


        try {
            LOG.info("Saving user {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error registration {}", e.getMessage());
            throw new UserAlreadyExistException("The user " + user.getEmail() + " already exist!");
        }
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setInfo(userDTO.getInfo());
        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    public User getUserById(Long userId){
        return userRepository.findUserById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found "));
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }
}
