package project.board.web.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import project.board.domain.Board;
import project.board.domain.Reply;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ReplyRepository {

    private final DataSource dataSource;

    public ReplyRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Reply reply) throws SQLException {
        String sql = "insert into reply(userId, boardId, content, createDate) values(?, ?, ?, NOW())";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, reply.getUserId());
            pstmt.setInt(2, reply.getBoardId());
            pstmt.setString(3, reply.getContent());

            pstmt.executeUpdate();
        } catch (Exception e) {
            log.error("DB REPLY INSERT ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public List<Reply> findAll(Integer boardId) throws SQLException {
        StringBuffer sb = new StringBuffer();

        sb.append("select r.id, r.userId, r.boardId, r.content, r.createDate, u.userName ");
        sb.append("from reply r inner join users u ");
        sb.append("on r.userId = u.id ");
        sb.append("where boardId=?");

        String sql = sb.toString();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Reply> replyList = new ArrayList<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, boardId);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                do {
                    Reply reply = new Reply();
                    reply.setId(rs.getInt("id"));
                    reply.setUserId(rs.getInt("userId"));
                    reply.setBoardId(rs.getInt("boardId"));
                    reply.setContent(rs.getString("content"));
                    reply.setCreateDate(rs.getTimestamp("createDate"));
                    reply.setUserName(rs.getString("userName"));
                    replyList.add(reply);
                }
                while (rs.next());
            }

            return replyList;

        } catch (Exception e) {
            log.error("DB REPLY findAll ERROR", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }

    }

    public void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeConnection(con);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeResultSet(rs);
    }

    public Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}
