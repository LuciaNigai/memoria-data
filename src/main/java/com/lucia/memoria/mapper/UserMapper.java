package com.lucia.memoria.mapper;


import com.lucia.memoria.dto.local.UserDTO;
import com.lucia.memoria.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = DeckMapper.class,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    List<UserDTO> toDTO(List<User> userList);
    List<User> toEntity(List<UserDTO> userDTOList);
}
