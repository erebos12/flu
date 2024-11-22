package com.erebos.flu.utils;

import com.erebos.flu.utils.pojo.User;
import com.erebos.flu.utils.pojo.UserDTO;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

    @Test
    void testMappingUser() {
        User user = new User("John", "Doe", 25);

        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);

        System.out.println("Full Name: " + userDTO.fullName());
        System.out.println("Age Category: " + userDTO.ageCategory());
    }
}
