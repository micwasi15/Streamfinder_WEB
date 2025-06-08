package pl.edu.streamfinder.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        userRepository.save(user);
        return true;
    }
}
