package project.board.web.repository;

import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.board.domain.User;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static project.board.database.ConnectionConst.*;

class UserRepositoryTest {

    UserRepository repository;

    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repository = new UserRepository(dataSource);
    }

    /*@Test
    void crud() throws SQLException {
        Long id = repository.lastId() + 1;

        // save
        User user = new User("hello", "password");
        user.setId(id);
        repository.save(user);

        // findById
        User findUser = repository.findById(user.getMemberId());
        assertThat(findUser).isEqualTo(user);

        //update: money: 10000 -> 20000
        repository.update(user.getMemberId(), "hello");
        User updateUser = repository.findById(user.getMemberId());
        assertThat(updateUser.getPassword()).isEqualTo("hello");

        //delete
        repository.delete(user.getMemberId());                                            //위의 memberV100을 삭제
        Assertions.assertThatThrownBy(() -> repository.findById(user.getMemberId()))      //repository.findById(member.getMemberId()) 로 조회하면 호출하면 결과 값이 없으니
                .isInstanceOf(NoSuchElementException.class);                                //NoSuchElementException 이 터지면 정상작동

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
*/
}