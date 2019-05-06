package cn.learning.hikaricp.entity;

import lombok.Data;

/**
 * @ClassName User
 * @Description TODO
 * @Author clare
 * @Date 2019/5/6 20:21
 * @Version 1.0
 */
@Data
public class User {
    private int id;
    private String name;
    private int age;
    private double money;

    public User(int id, String name, int age, double money) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.money = money;
    }
}