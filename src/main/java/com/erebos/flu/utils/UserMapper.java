package com.erebos.flu.utils;

import com.erebos.flu.utils.pojo.User;
import com.erebos.flu.utils.pojo.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "fullName", expression = "java(user.firstName() + \" \" + user.lastName())")
    @Mapping(target = "ageCategory", expression = "java(user.age() > 18 ? \"Adult\" : \"Minor\")")
    UserDTO userToUserDTO(User user);
}

