package com.van.mngr.bt.persistence.repository.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface MyBatisUserRepository {

    Map findByUsername(String username);

    int insert(Map user);

    int deleteAll();
}
