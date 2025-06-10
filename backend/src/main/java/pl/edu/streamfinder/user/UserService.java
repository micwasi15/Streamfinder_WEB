package pl.edu.streamfinder.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public boolean addShowToFavorites(String showId, String email) {
        User user = findByEmail(email);
        if (user == null) {
            return false;
        }
        if (user.getFavoriteShowIds().contains(showId)) {
            return false;
        }
        user.getFavoriteShowIds().add(showId);
        userRepository.save(user);
        return true;
    }

    public boolean removeShowFromFavorites(String showId, String email) {
        User user = findByEmail(email);
        if (user == null || !user.getFavoriteShowIds().remove(showId)) {
            return false;
        }
        userRepository.save(user);
        return true;
    }
}
