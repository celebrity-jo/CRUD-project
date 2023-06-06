package project.board.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.board.domain.*;
import project.board.web.repository.BoardRepository;
import project.board.web.repository.ReplyRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @GetMapping
    public String board(Model model, @RequestParam(value = "page", defaultValue = "1") int currentPage) throws SQLException {

        List<Board> board = boardRepository.findAll(currentPage - 1);

        int total = boardRepository.countAll();

        model.addAttribute("page", new BoardPage(total, currentPage, 7, 5, board));
        model.addAttribute("total", total);

        model.addAttribute("board", board);
        return "board/board";
    }

    @GetMapping("/{id}")
    public String post(@PathVariable Integer id, Model model) throws SQLException {
        Board post = boardRepository.findById(id);
        List<Reply> reply = replyRepository.findAll(id);
        model.addAttribute("post", post);
        model.addAttribute("reply", reply);
        model.addAttribute("newReply", new Reply());
        boardRepository.viewCountUpdate(id);
        return "board/post";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("post", new Board());
        return "board/addForm";
    }

    @PostMapping("/add")
    public String add(@Validated @ModelAttribute("post") Board board, BindingResult bindingResult,
                      @SessionAttribute(name = "loginUser", required = false) User loginUser,
                      RedirectAttributes redirectAttributes, HttpServletRequest request) throws SQLException {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "board/addForm";
        }

        if(loginUser == null) {     //로그인 된 사용자가 없으면 홈 화면으로 이동
            return "redirect:/";
        }

        Board post = new Board(loginUser.getId(), board.getTitle(),
                board.getContent().replaceAll("<script>", "&lt;script&gt;")
                                  .replaceAll("</script>", "&lt;/script&gt;"));

        Board savedPost = boardRepository.save(post);
        redirectAttributes.addAttribute("id", savedPost.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/board/{id}";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, HttpServletRequest request,
                           @SessionAttribute(name = "loginUser", required = false) User loginUser) throws SQLException {

        Board post = boardRepository.findById(id);

        //로그인 된 사용자가 없거나, 로그인된 사용자가 게시글의 작성자와 다르다면 alert 창 띄우고 게시물로 이동
        if(loginUser == null || !loginUser.getId().equals(post.getUserId())) {
            request.setAttribute("msg", "작성자 외에는 수정이 불가합니다.");
            request.setAttribute("url", "/board/" + id);
            return "alert/editAlert";
        }

        model.addAttribute("post", post);

        return "board/editForm";
    }

    @PostMapping("/{id}/edit")
    public String edit(@Validated @PathVariable Integer id, @ModelAttribute("post") Board board,
                       BindingResult bindingResult) throws SQLException {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "board/{id}/edit";
        }

        boardRepository.update(board.getId(), board.getTitle(), board.getContent());

        return "redirect:/board/{id}";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, HttpServletRequest request,
                         @SessionAttribute(name = "loginUser", required = false) User loginUser) throws SQLException {
        
        Board post = boardRepository.findById(id);

        if(loginUser == null || loginUser.equals(post.getUserId())) {
            request.setAttribute("msg", "작성자 외에는 삭제가 불가합니다.");
            request.setAttribute("url", "/board/" + id);
            return "alert/editAlert";
        }

        boardRepository.delete(id);
        return "redirect:/board";
    }

    @PostMapping("/{id}/reply")
    public String reply(@Validated @PathVariable Integer id, @ModelAttribute("newReply") Reply reply,
                        @SessionAttribute(name = "loginUser", required = false) User loginUser,
                        HttpServletRequest request) throws SQLException {

        if(loginUser == null) {
            request.setAttribute("msg", "로그인을 해주세요");
            request.setAttribute("url", "/board/" + id);
            return "alert/editAlert";
        }

        reply.setUserId(loginUser.getId());
        reply.setBoardId(id);
        reply.setContent(reply.getContent().replaceAll("\r\n", "<br>"));

        replyRepository.save(reply);

        return "redirect:/board/{id}";
    }

    @GetMapping("/test")
    public String test() {
        return "board/test";
    }


}
