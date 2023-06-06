package project.board.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Reply {
    private Integer id;
    private Integer userId;
    private Integer boardId;
    private String content;
    private Timestamp createDate;
    private String userName;
}
