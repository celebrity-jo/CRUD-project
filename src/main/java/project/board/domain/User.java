package project.board.domain;

import lombok.Data;

import javax.validation.constraints.*;
import java.sql.Timestamp;

@Data
public class User {

    private Integer id;

    @NotBlank      //빈값 + 공백만 있는 경우를 허용하지 않는다.
    private String userId;

//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])[a-z]{4,16}$", message = "비밀번호는 4~16자리수여야 합니다.")
    private String password;

    @NotBlank
    private String userName;

    @NotBlank
    @Email          //이메일 형식 이어야한다.
    private String email;
    private String address;
    private String userRole;
    private Timestamp createDate;

    public User() {
    }

    public User(String userId, String password, String userName, String email, String address) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.address = address;
    }
}
