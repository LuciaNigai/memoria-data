package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.UserDTO;
import com.lucia.memoria.mapper.UserMapper;
import com.lucia.memoria.model.User;
import com.lucia.memoria.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        // Initialize fields not set from DTO, e.g. createdAt
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(java.time.LocalDateTime.now());
        }
        // You may want to hash password here before saving
        user.setUserId(UUID.randomUUID());

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    @Transactional
    public UserDTO getUserByUserId(UUID userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toDTO(user);
    }

    @Transactional
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}
