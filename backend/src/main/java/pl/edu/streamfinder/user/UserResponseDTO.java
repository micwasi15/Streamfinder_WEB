package pl.edu.streamfinder.user;

import lombok.Data;

@Data
public class UserResponseDTO {
    public UserResponseDTO() {}
    public UserResponseDTO(User user) {
        this.email = user.getEmail();
    }

    private String email;
}
