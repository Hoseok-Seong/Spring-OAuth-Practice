package shop.mtcoding.getintherelogin.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {
    public User findByEmail(String email);

    public int insert(@Param("username") String username, @Param("password") String password,
            @Param("email") String email, @Param("provider") String provider);
}
