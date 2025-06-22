package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.dto.local.CardMinimalDTO;
import com.lucia.memoria.dto.local.UserDTO;
import com.lucia.memoria.model.Card;
import com.lucia.memoria.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", source = "userId")
    User toEntity(UserDTO userDTO);

    @Mapping(target = "userId", source = "userId")
    UserDTO toDTO(User user);
}