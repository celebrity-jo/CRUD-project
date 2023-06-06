package project.board.web.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import project.board.domain.Board;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Repository
public class BoardRepository {

    private final DataSource dataSource;

    public BoardRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //게시물 저장
    public Board save(Board board) throws SQLException {
        String sql = "insert into board(userId, title, content, createDate) values(?, ?, ?, NOW())";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, board.getUserId());
            pstmt.setString(2, board.getTitle());
            pstmt.setString(3, board.getContent());
            pstmt.executeUpdate();
            board.setId(lastId());
            return board;
        } catch (SQLException e) {
            log.error("DB BOARD INSERT ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    //특정 게시물 추출
    public Board findById(Integer id) throws SQLException {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT b.id, b.userId, b.title, b.content, b.readCount, b.createDate, u.userName ");
        sb.append("FROM board b inner join users u ");
        sb.append("on b.userId = u.id ");
        sb.append("where b.id = ?");

        String sql = sb.toString();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Board board = new Board();
                board.setId(rs.getInt("id"));
                board.setUserId(rs.getInt("userId"));
                board.setTitle(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setReadCount(rs.getInt("readCount"));
                board.setCreateDate(rs.getTimestamp("createDate"));
                board.setUserName(rs.getString("userName"));
                return board;
            } else {
                throw new NoSuchElementException("board not found id = " + id);
            }
        } catch (Exception e) {
            log.error("DB BOARD findById ERROR", e);
            throw e;
        }finally {
            close(con, pstmt, rs);
        }
    }

    //모든 게시물 추출
    public Integer countAll() throws SQLException {
        String sql = "select count(*) count from board";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int count = 0;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                count = rs.getInt("count");
            }

            return count;

        } catch (SQLException e) {
            log.error("DB BOARD findAll ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<Board> findAll(int currentPage) throws SQLException {
        currentPage = currentPage * 7;
        String sql = "select * from board order by id desc limit ?, 7";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ArrayList<Board> boards = new ArrayList<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, currentPage);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                do {
                    Board board = new Board();
                    board.setId(rs.getInt("id"));
                    board.setUserId(rs.getInt("userId"));
                    board.setTitle(rs.getString("title"));
                    board.setContent(rs.getString("content"));
                    board.setReadCount(rs.getInt("readCount"));
                    board.setCreateDate(rs.getTimestamp("createDate"));
                    boards.add(board);
                }
                while (rs.next());
            }

            return boards;

        } catch (SQLException e) {
            log.error("DB BOARD findAll ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    //게시글 수정
    public void update(Integer id, String title, String content) throws SQLException {
        String sql = "update board set title=?, content=? where id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("DB BOARD UPDATE ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "delete from board where id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("DB BOARD DELETE ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    //가장 최근에 생성된 게시글 id 추출
    public Integer lastId() throws SQLException {
        String sql = "select MAX(id) as id from board";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new NoSuchElementException("not found last id");
            }

        } catch (Exception e) {
            log.error("DB BOARD ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void viewCountUpdate(Integer id) throws SQLException {
        String sql = "update board set readCount = (select readCount from board where id=?)+1 where id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("DB BOARD ViewCountUpdate ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}
