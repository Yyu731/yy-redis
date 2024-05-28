package com.yy.redisbe.web;

import cn.hutool.core.lang.UUID;
import com.yy.redisbe.constant.RedisKey;
import com.yy.redisbe.core.AjaxResult;
import com.yy.redisbe.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * ClassName: StudentController
 * Package: com.yy.redisbe.web
 * Description:
 *
 * @Author yy
 * @Create 2024-05-27 23:36
 * @Version 1.0
 */
@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public AjaxResult list() {
        List<Student> studentList = new ArrayList<>();
        Set<String> keys = redisTemplate.keys(RedisKey.STUDENT_KEY + "*");
        for (String key : keys) {
            Map map = redisTemplate.opsForHash().entries(key);
            Student student = new Student();
            student.setId(key.split(RedisKey.STUDENT_KEY)[1]);
            student.setName((String) map.get(RedisKey.NAME_KEY));
            student.setAge((Integer) map.get(RedisKey.AGE_KEY));
            student.setPhone((String) map.get(RedisKey.PHONE_KEY));
            student.setEmail((String) map.get(RedisKey.EMAIL_KEY));
            student.setBirth((Date) map.get(RedisKey.BITRH_KEY));
            studentList.add(student);
        }
        return AjaxResult.success(studentList);
    }

    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable String id) {
        Map map = redisTemplate.opsForHash().entries(RedisKey.STUDENT_KEY + id);
        Student student = new Student();
        student.setId(id);
        student.setName((String) map.get(RedisKey.NAME_KEY));
        student.setAge((Integer) map.get(RedisKey.AGE_KEY));
        student.setPhone((String) map.get(RedisKey.PHONE_KEY));
        student.setEmail((String) map.get(RedisKey.EMAIL_KEY));
        student.setBirth((Date) map.get(RedisKey.BITRH_KEY));
        return AjaxResult.success(student);
    }

    @PostMapping
    public AjaxResult add(@RequestBody Student student) {
        Map map = new HashMap();
        String id = UUID.randomUUID().toString();
        map.put(RedisKey.NAME_KEY, student.getName());
        map.put(RedisKey.AGE_KEY, student.getAge());
        map.put(RedisKey.PHONE_KEY, student.getPhone());
        map.put(RedisKey.EMAIL_KEY, student.getEmail());
        map.put(RedisKey.BITRH_KEY, student.getBirth());
        redisTemplate.opsForHash().putAll(RedisKey.STUDENT_KEY + id, map);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable String id) {
        Boolean delete = redisTemplate.delete(RedisKey.STUDENT_KEY + id);
        if (delete) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @PutMapping
    public AjaxResult edit(@RequestBody Student student) {
        Map map = new HashMap();
        map.put(RedisKey.NAME_KEY, student.getName());
        map.put(RedisKey.AGE_KEY, student.getAge());
        map.put(RedisKey.PHONE_KEY, student.getPhone());
        map.put(RedisKey.EMAIL_KEY, student.getEmail());
        map.put(RedisKey.BITRH_KEY, student.getBirth());
        redisTemplate.opsForHash().putAll(RedisKey.STUDENT_KEY + student.getId(), map);
        return AjaxResult.success();
    }
}
