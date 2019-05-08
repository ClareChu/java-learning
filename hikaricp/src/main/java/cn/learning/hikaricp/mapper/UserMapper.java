package cn.learning.hikaricp.mapper;

import cn.learning.hikaricp.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description TODO
 * @Author clare
 * @Date 2019/5/6 20:19
 * @Version 1.0
 */
@Mapper
public interface UserMapper {
    /**
     * 通过名字查询用户信息
     */
    @Select("SELECT * FROM user WHERE name = #{name}")
    User findUserByName(@Param("name") String name);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findUserById(@Param("id") int id);

    /**
     * 查询所有用户信息
     */
    @Select("SELECT * FROM user")
    List<User> findAllUser();

    /**
     * 查询所有用户信息
     */
    @Select("SELECT * FROM user where 1=1")
    List<User> findAllUser1();

    /**
     * 插入用户信息
     */
    @Insert("INSERT INTO user(name, age,money) VALUES(#{user.name}, #{user.age}, #{user.money})")
    void insertUser(@Param("user") User user);

    /**
     * 根据 id 更新用户信息
     */
    @Update("UPDATE  user SET money = money + 1 WHERE id = #{user.id}")
    int updateUser(@Param("user") User user);

    /**
     * 根据 id 更新用户信息
     */
    @Update("UPDATE  user SET money= money + 1 WHERE id = #{id}")
    int updateByMoney(@Param("id") int id);

    /**
     * 根据 id 删除用户信息
     */
    @Delete("DELETE from user WHERE id = #{id}")
    void deleteUser(@Param("id") int id);
}
