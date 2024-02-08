package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {

    private final EntityManager em;

    public int findCount() {
        Query query = em.createNativeQuery("select count(*) from board_tb");
        Long boardCount = (Long) query.getSingleResult();

        return boardCount.intValue();
    }

    public List<Board> findAll(int page){
        final int COUNT = 5;
        int value = page * COUNT;
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?, ?", Board.class);
        query.setParameter(1, value);
        query.setParameter(2, COUNT);

        return query.getResultList();
    }

    public Board findById(int id) {
        Query query = em.createNativeQuery("select * from board_tb where id = ?", Board.class);
        query.setParameter(1, id);

        Board board = (Board) query.getSingleResult();
        return board;
    }

    @Transactional // 디비에 인서트 하는 코드이기 때문에 붙여야 함
    public void save(BoardRequest.SaveDTO requestDTO) {
        Query query = em.createNativeQuery("insert into board_tb(author, title, content) values (?, ?, ?)");
        query.setParameter(1, requestDTO.getAuthor());
        query.setParameter(2, requestDTO.getTitle());
        query.setParameter(3, requestDTO.getContent()); // 얘는 세션에서 받은 거임

        query.executeUpdate();
    }

    @Transactional
    public void update(BoardRequest.UpdateDTO requestDTO, int id) {
        Query query = em.createNativeQuery("update board_tb set author = ?, title = ?, content = ? where id = ?");
        query.setParameter(1, requestDTO.getAuthor());
        query.setParameter(2, requestDTO.getTitle());
        query.setParameter(3, requestDTO.getContent());
        query.setParameter(4, id);

        query.executeUpdate();
    }

    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete from board_tb where id = ?");
        query.setParameter(1, id);
        query.executeUpdate();
    }


}
