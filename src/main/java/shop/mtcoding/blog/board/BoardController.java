package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardRepository boardRepository;

    @GetMapping("/") // 홈 화면
    public String index(HttpServletRequest request) {
        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("boardList", boardList);

        return "index";
    }

    @GetMapping("/board/saveForm") // 글쓰기 화면
    public String saveForm() {

        return "board/saveForm";
    }

    @PostMapping("/board/save") // 글쓰기 저장
    public String save(BoardRequest.SaveDTO requestDTO){
        boardRepository.save(requestDTO);

        return "redirect:/";
    }

    @GetMapping("/board/{id}/updateForm") // 글쓰기 수정
    public String updateForm(@PathVariable int id) {

        return "board/updateForm";
    }

    @PostMapping("/board/{id}/update") // 수정 완료
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO){
        boardRepository.update(requestDTO, id);

        return "redirect:/";
    }

    @PostMapping("/board/{id}/delete") // 글 삭제
    public String delete(@PathVariable int id){

        return "redirect:/";
    }
}
