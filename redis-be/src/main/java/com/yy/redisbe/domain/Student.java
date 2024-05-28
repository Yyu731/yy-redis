package com.yy.redisbe.domain;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: Student
 * Package: com.yy.redisbe.domain
 * Description:
 *
 * @Author yy
 * @Create 2024-05-27 23:29
 * @Version 1.0
 */
@Data
public class Student {
    private String id;
    private String name;
    private Integer age;
    private String phone;
    private String email;
    private Date birth;
}
