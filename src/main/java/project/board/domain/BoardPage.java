package project.board.domain;

import java.util.List;

public class BoardPage {

    //전체 글의 행의 수
    private int total;
    //현재 페이지 번호
    private int currentPage;
    //현재 페이지 개수
    private int totalPages;
    //시작 페이지 번호
    private int startPage;
    //종료 페이지 번호
    private int endPage;
    //페이징 개수
    private int pagingCount;
    //게시글 데이터
    private List<Board> content;

    //size : 보여질 행의 수
    public BoardPage(int total, int currentPage, int size, int pagingCount, List<Board> content) {
        this.total = total;
        this.currentPage = currentPage;
        this.pagingCount = pagingCount;
        this.content = content;

        if(total == 0) {
            totalPages = 0;
            startPage = 0;
            endPage = 0;
        } else {
            totalPages = total / size;

            if(total % size > 0) {
                totalPages += 1;
            }

            startPage = currentPage / pagingCount * pagingCount + 1;

            if(currentPage % pagingCount == 0) {
                startPage -= pagingCount;
            }

            endPage = startPage + pagingCount - 1;

            if(endPage > totalPages)
                endPage = totalPages;
        }
    }

    public int getTotal() {
        return this.total;
    }

    //게시물이 없는가? total == 0 이면 true
    public boolean hasNoPost() {
        return this.total == 0;
    }

    //게시물이 있는가? total > 0 이면 게시물이 있다는 것 true
    public boolean hasPost() {
        return this.total > 0;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Board> getContent() {
        return this.getContent();
    }

    public int getStartPage() {
        return this.startPage;
    }

    public int getEndPage() {
        return this.endPage;
    }
}
