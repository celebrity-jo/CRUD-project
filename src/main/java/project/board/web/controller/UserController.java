package project.board.web.controller;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.board.domain.User;
import project.board.web.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

import static project.board.database.ConnectionConst.*;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    /**
     * HikariDataSource 주입
     */
    public UserController(HikariDataSource dataSource) {
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        this.userRepository = new UserRepository(dataSource);
    }

    /**
     * 회원 추가
     */
    @GetMapping("/add")
    public String addUser(@ModelAttribute("user") User user) {
        return "users/addUserForm";
    }

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute User user, BindingResult bindingResult) throws SQLException {

        if (userRepository.findById(user.getUserId()) != null) {
            bindingResult.rejectValue("userId", "required", "중복된 아이디가 이미 존재합니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "users/addUserForm";
        }

        userRepository.save(user);
        return "redirect:/";
    }

    /**
     * 회원 수정
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) throws SQLException {
        User user = userRepository.findById(id);
        model.addAttribute("user", user);
        return "users/editUserForm";
    }

    @PostMapping("/{id}/edit")
    public String edit(@Validated @ModelAttribute User user, BindingResult bindingResult) throws SQLException {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "users/editUserForm";
        }

        userRepository.update(user.getUserId(), user.getPassword(), user.getUserName(), user.getEmail(), user.getAddress());

        return "loginHome";
    }
}
