package neospider.mngr.bt.persistence.repositories;

import neospider.mngr.bt.persistence.entities.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MyBatisUserRepository {

//    @Select("SELECT * FROM user WHERE username = #{username}")
    UserEntity findByUsername(String username);

//    @Insert("INSERT INTO user(id, username, password, role) " +
//            " VALUES (#{id}, #{username}, #{password}, #{role})")
    int insert(UserEntity userEntity);
}
