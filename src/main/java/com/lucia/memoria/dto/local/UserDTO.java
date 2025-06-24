package com.lucia.memoria.dto.local;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private UUID userId;
  private String username;
  private String password;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime lastLogin;
}
