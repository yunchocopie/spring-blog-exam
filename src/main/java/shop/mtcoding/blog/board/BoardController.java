package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardRepository boardRepository;

    @GetMapping("/") // 홈 화면
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        List<Board> boardList = boardRepository.findAll(page);
        request.setAttribute("boardList", boardList);

        int currentPage = page;
        int nextPage = currentPage + 1;
        int prevPage = currentPage - 1;
        request.setAttribute("nextPage", nextPage);
        request.setAttribute("prevPage", prevPage);

        // prevPage
        boolean first = (currentPage == 0) ? true : false;
        request.setAttribute("first", first);

        // nextPage
        int totalCount = boardRepository.findCount(); // 전체 개시물 수
        int pagingCount = 5; // 페이지 당 표시할 게시물 수
        int totalPage; // 게시물을 다 나타내려면 필요한 페이지 수
        if (totalCount % pagingCount == 0) {
            totalPage = totalCount / pagingCount;
        } else {
          totalPage = (totalCount / pagingCount) + 1;
        }
        boolean last = currentPage == (totalPage - 1) ? true : false;
        request.setAttribute("last", last);

        // pageIndex
        int pageIndex;
        if (totalCount % pagingCount == 0) {
            pageIndex = totalCount / pagingCount;
        } else {
            pageIndex = (totalCount / pagingCount) + 1;
        }

        ArrayList pageIndexList = new ArrayList<>();
        for (int i = 0; i < pageIndex; i++) {
            pageIndexList.add(i);
        }

        request.setAttribute("pageIndex", pageIndexList);


        return "index";
    }

    @GetMapping("/board/saveForm") // 글쓰기 화면
    public String saveForm() {

        return "board/saveForm";
    }

    @PostMapping("/board/save") // 글쓰기 저장
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request){

        if (requestDTO.getTitle().length() > 20) {
            request.setAttribute("status", 400);
            request.setAttribute("msg","title의 길이가 20자를 초과하면 안돼요.");
            return "error/40x"; // BadRequest
        }

        if (requestDTO.getContent().length() > 20) {
            request.setAttribute("status", 400);
            request.setAttribute("msg","content의 길이가 20자를 초과하면 안돼요.");
            return "error/40x"; // BadRequest
        }

        boardRepository.save(requestDTO);

        return "redirect:/";
    }

    @GetMapping("/board/{id}/updateForm") // 글쓰기 수정
    public String updateForm(@PathVariable int id, HttpServletRequest request) {

        Board board = boardRepository.findById(id);
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    @PostMapping("/board/{id}/update") // 수정 완료
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO, HttpServletRequest request){

        if (requestDTO.getTitle().length() > 20) {
            request.setAttribute("status", 400);
            request.setAttribute("msg","title의 길이가 20자를 초과하면 안돼요.");
            return "error/40x"; // BadRequest
        }

        if (requestDTO.getContent().length() > 20) {
            request.setAttribute("status", 400);
            request.setAttribute("msg","content의 길이가 20자를 초과하면 안돼요.");
            return "error/40x"; // BadRequest
        }

        boardRepository.update(requestDTO, id);

        return "redirect:/";
    }

    @PostMapping("/board/{id}/delete") // 글 삭제
    public String delete(@PathVariable int id){
        boardRepository.deleteById(id);

        return "redirect:/";
    }
}
