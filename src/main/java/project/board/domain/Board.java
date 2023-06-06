package project.board.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

@Data
public class Board {

    private Integer id;
    private Integer userId;

    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    private String content;
    private Integer readCount;
    private Timestamp createDate;

    private String userName;

    public Board() {
    }

    public Board(Integer userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }
/*
    public String getTitle() {  //스크립트 방지 (lucy 루시 필터)
        return title.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
*/
}
