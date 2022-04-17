package ru.sstu.hobbyboard.servicies;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.entities.User;
import ru.sstu.hobbyboard.repositories.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = findByEmail(s);
        if (user == null) throw new UsernameNotFoundException("User with email " + s + " not found");

        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean validateUser(User user) {
        return findByNickname(user.getNickName()) == null || findByNickname(user.getNickName()).getId() == user.getId();
    }

    private User findByNickname(String nickName) {
        return userRepository.findByNickName(nickName);
    }

    public List<Integer> findTop3() {
        return userRepository.findTop3ByPurchases();
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}
