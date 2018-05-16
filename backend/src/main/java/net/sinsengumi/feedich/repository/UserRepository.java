package net.sinsengumi.feedich.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.User;
import net.sinsengumi.feedich.model.User.ServiceProvider;

@Repository
public interface UserRepository {

    int create(User user);

    User findByEmail(String email);

    int updateAuthId(@Param("id") int id, @Param("serviceProvider") ServiceProvider serviceProvider, @Param("authId") String authId);

    int deleteById(int id);
}
