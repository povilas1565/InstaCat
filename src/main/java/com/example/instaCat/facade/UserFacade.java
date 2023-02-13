package com.example.instaCat.facade;

import com.example.instaCat.dto.UserDTO;
import com.example.instaCat.entity.User;
import org.springframework.stereotype.Component;


/*Класс для мапинга данных и передачи их на контроллер.
* Берем только необходимые поля!*/
@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());
        userDTO.setInfo(user.getInfo());

        return userDTO;
    }
}
