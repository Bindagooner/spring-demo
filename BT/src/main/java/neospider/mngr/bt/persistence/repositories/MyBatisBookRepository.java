package neospider.mngr.bt.persistence.repositories;

import neospider.mngr.bt.persistence.entities.BookEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyBatisBookRepository {

    int insert(BookEntity book);

    List<BookEntity> listAll();

    BookEntity findById(String id);
}
