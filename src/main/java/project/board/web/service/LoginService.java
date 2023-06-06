package project.board.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.board.domain.User;
import project.board.web.repository.UserRepository;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public User login(String userId, String password) throws SQLException {
        User user = userRepository.findById(userId);

        if (user.getPassword().equals(password))
            return user;
        else
            return null;
    }
}
