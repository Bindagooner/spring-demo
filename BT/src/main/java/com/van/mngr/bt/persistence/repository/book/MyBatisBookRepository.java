package com.van.mngr.bt.persistence.repository.book;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MyBatisBookRepository {

    int insert(Map book);

    List<Map> listAll();

    Map findById(String id);

    int deleteAll();
}
