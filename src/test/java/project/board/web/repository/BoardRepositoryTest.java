package project.board.web.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.board.domain.Board;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static project.board.database.ConnectionConst.*;

@Slf4j
class BoardRepositoryTest {

    BoardRepository repository;

    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repository = new BoardRepository(dataSource);
    }

    @Test
    void crud() throws SQLException {
        Integer id = repository.lastId() + 1;

        Board board = new Board(2, "제목", "내용");
        board.setId(id);
        repository.save(board);

        // findById
        Board findMember = repository.findById(board.getId());
        assertThat(findMember).isEqualTo(board);

/*
        //update: money: 10000 -> 20000
        repository.update(board.getMemberId(), "hello");
        Member updateMember = repository.findById(member.getMemberId());
        assertThat(updateMember.getPassword()).isEqualTo("hello");
*/

/*
        //delete
        repository.delete(member.getMemberId());                                            //위의 memberV100을 삭제
        Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId()))      //repository.findById(member.getMemberId()) 로 조회하면 호출하면 결과 값이 없으니
                .isInstanceOf(NoSuchElementException.class);                                //NoSuchElementException 이 터지면 정상작동
*/

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}