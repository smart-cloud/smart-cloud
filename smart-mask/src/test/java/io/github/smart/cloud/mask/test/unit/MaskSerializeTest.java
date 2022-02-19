/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.mask.test.unit;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.smart.cloud.mask.MaskLog;
import io.github.smart.cloud.mask.MaskRule;
import io.github.smart.cloud.mask.util.JacksonMaskUtil;
import io.github.smart.cloud.mask.util.MaskUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class MaskSerializeTest {

    // 手动设置mask属性
    @Test
    void testMaskSerializeSetMaskAttribute() {
        LoginVO loginVO = new LoginVO();
        loginVO.setName("名字测试");
        loginVO.setPassword("13112345678");

        String maskResult = MaskUtil.mask(loginVO);

        LoginVO maskLoginVO = JacksonMaskUtil.parseObject(maskResult, LoginVO.class);
        Assertions.assertThat(maskLoginVO.getName()).isEqualTo(MaskUtil.mask(loginVO.getName(), 1, 1, "###"));
        Assertions.assertThat(maskLoginVO.getPassword()).isEqualTo(MaskUtil.mask(loginVO.getPassword(), 2, 0, "***"));
    }

    // 普通对象
    @Test
    void testMaskSerializeObject() {
        User user = new User();
        user.setId(9L);
        user.setName("名字");
        user.setMobile("13112345678");

        String maskResult = MaskUtil.mask(user);

        JsonNode maskUser = JacksonMaskUtil.parseObject(maskResult);
        String expectedId = MaskUtil.mask(String.valueOf(user.getId()), MaskRule.DEFAULT.getStartLen(),
                MaskRule.DEFAULT.getEndLen(), MaskRule.DEFAULT.getMask());
        Assertions.assertThat(maskUser.get("id").asText()).isEqualTo(expectedId);
        Assertions.assertThat(maskUser.get("name").asText()).isEqualTo(MaskUtil.mask(user.getName(), MaskRule.NAME));
        Assertions.assertThat(maskUser.get("mobile").asText())
                .isEqualTo(MaskUtil.mask(user.getMobile(), MaskRule.MOBILE));
    }

    // 子类对象
    @Test
    void testMaskSerializeSubClass() {
        Student student = new Student();
        student.setId(9L);
        student.setName("名字");
        student.setMobile("13112345678");

        student.setAge(11);
        student.setClassName("高305班");

        String maskResult = MaskUtil.mask(student);

        JsonNode maskStudent = JacksonMaskUtil.parseObject(maskResult);
        String expectedId = MaskUtil.mask(String.valueOf(student.getId()), MaskRule.DEFAULT.getStartLen(),
                MaskRule.DEFAULT.getEndLen(), MaskRule.DEFAULT.getMask());
        Assertions.assertThat(maskStudent.get("id").asText()).isEqualTo(expectedId);

        Assertions.assertThat(maskStudent.get("name").asText())
                .isEqualTo(MaskUtil.mask(student.getName(), MaskRule.NAME));
        Assertions.assertThat(maskStudent.get("mobile").asText())
                .isEqualTo(MaskUtil.mask(student.getMobile(), MaskRule.MOBILE));

        Assertions.assertThat(maskStudent.get("age").asInt()).isEqualTo(student.getAge());
        Assertions.assertThat(maskStudent.get("className").asText())
                .isEqualTo(MaskUtil.mask(student.getClassName(), MaskRule.NAME));
    }

    // 数组对象
    @Test
    void testMaskSerializeArray() {
        User user = new User();
        user.setId(9L);
        user.setName("名字");
        user.setMobile("13112345678");

        User[] users = {user};

        String maskResult = MaskUtil.mask(users);

        JsonNode maskUsers = JacksonMaskUtil.parseObject(maskResult);

        JsonNode maskUser = maskUsers.get(0);

        String expectedId = MaskUtil.mask(String.valueOf(user.getId()), MaskRule.DEFAULT.getStartLen(),
                MaskRule.DEFAULT.getEndLen(), MaskRule.DEFAULT.getMask());
        Assertions.assertThat(maskUser.get("id").asText()).isEqualTo(expectedId);

        Assertions.assertThat(maskUser.get("name").asText()).isEqualTo(MaskUtil.mask(user.getName(), MaskRule.NAME));
        Assertions.assertThat(maskUser.get("mobile").asText())
                .isEqualTo(MaskUtil.mask(user.getMobile(), MaskRule.MOBILE));
    }

    // 嵌套对象
    @Test
    void testMaskSerializeNestedObject() {
        User user = new User();
        user.setId(9L);
        user.setName("名字");
        user.setMobile("13112345678");

        List<User> users = new ArrayList<>();
        users.add(user);

        Source source = new Source();
        source.setIp("12.123.22.33");
        source.setUsers(users);

        String maskResult = MaskUtil.mask(source);

        JsonNode maskSource = JacksonMaskUtil.parseObject(maskResult);
        Assertions.assertThat(maskSource.get("ip").asText()).isEqualTo(MaskUtil.mask(source.getIp(), MaskRule.IP));

        JsonNode maskUser = maskSource.get("users").get(0);

        String expectedId = MaskUtil.mask(String.valueOf(user.getId()), MaskRule.DEFAULT.getStartLen(),
                MaskRule.DEFAULT.getEndLen(), MaskRule.DEFAULT.getMask());
        Assertions.assertThat(maskUser.get("id").asText()).isEqualTo(expectedId);

        Assertions.assertThat(maskUser.get("name").asText()).isEqualTo(MaskUtil.mask(user.getName(), MaskRule.NAME));
        Assertions.assertThat(maskUser.get("mobile").asText())
                .isEqualTo(MaskUtil.mask(user.getMobile(), MaskRule.MOBILE));
    }

    // 泛型对象
    @Test
    void testMaskSerializeGeneric() {
        User user = new User();
        user.setId(9L);
        user.setName("名字");
        user.setMobile("13112345678");

        List<User> users = new ArrayList<>();
        users.add(user);

        Source source = new Source();
        source.setIp("12.123.22.33");
        source.setUsers(users);

        Req<Source> req = new Req<>();
        req.setToken(UUID.randomUUID().toString());
        req.setT(source);

        String maskResult = MaskUtil.mask(req);

        JsonNode maskReq = JacksonMaskUtil.parseObject(maskResult);

        Assertions.assertThat(maskReq.get("token").asText()).isEqualTo(MaskUtil.mask(req.getToken(), MaskRule.DEFAULT));

        JsonNode maskSource = maskReq.get("t");
        Assertions.assertThat(maskSource.get("ip").asText()).isEqualTo(MaskUtil.mask(source.getIp(), MaskRule.IP));

        JsonNode maskUser = maskSource.get("users").get(0);
        String expectedId = MaskUtil.mask(String.valueOf(user.getId()), MaskRule.DEFAULT.getStartLen(),
                MaskRule.DEFAULT.getEndLen(), MaskRule.DEFAULT.getMask());
        Assertions.assertThat(maskUser.get("id").asText()).isEqualTo(expectedId);

        Assertions.assertThat(maskUser.get("name").asText()).isEqualTo(MaskUtil.mask(user.getName(), MaskRule.NAME));
        Assertions.assertThat(maskUser.get("mobile").asText())
                .isEqualTo(MaskUtil.mask(user.getMobile(), MaskRule.MOBILE));
    }

    // 泛型对象（外部字段无mask注解）
    @Test
    void testMaskSerializeGeneric2() {
        User user = new User();
        user.setId(9L);
        user.setName("名字");
        user.setMobile("13112345678");

        List<User> users = new ArrayList<>();
        users.add(user);

        Source source = new Source();
        source.setIp("12.123.22.33");
        source.setUsers(users);

        Req2<Source> req = new Req2<>();
        req.setToken(UUID.randomUUID().toString());
        req.setT(source);

        String maskResult = MaskUtil.mask(req);

        JsonNode maskReq = JacksonMaskUtil.parseObject(maskResult);

        Assertions.assertThat(maskReq.get("token").asText()).isEqualTo(req.getToken());

        JsonNode maskSource = maskReq.get("t");
        Assertions.assertThat(maskSource.get("ip").asText()).isEqualTo(MaskUtil.mask(source.getIp(), MaskRule.IP));

        JsonNode maskUser = maskSource.get("users").get(0);
        String expectedId = MaskUtil.mask(String.valueOf(user.getId()), MaskRule.DEFAULT.getStartLen(),
                MaskRule.DEFAULT.getEndLen(), MaskRule.DEFAULT.getMask());
        Assertions.assertThat(maskUser.get("id").asText()).isEqualTo(expectedId);

        Assertions.assertThat(maskUser.get("name").asText()).isEqualTo(MaskUtil.mask(user.getName(), MaskRule.NAME));
        Assertions.assertThat(maskUser.get("mobile").asText())
                .isEqualTo(MaskUtil.mask(user.getMobile(), MaskRule.MOBILE));
    }

    @Getter
    @Setter
    @ToString
    static class LoginVO implements Serializable {
        @MaskLog(startLen = 1, endLen = 1, mask = "###")
        private String name;
        @MaskLog(startLen = 2)
        private String password;
    }

    @Getter
    @Setter
    @ToString
    static class User implements Serializable {
        @MaskLog
        private Long id;
        @MaskLog(MaskRule.NAME)
        private String name;
        @MaskLog(MaskRule.MOBILE)
        private String mobile;
    }

    @Getter
    @Setter
    @ToString
    static class Student extends User {
        private int age;
        @MaskLog(MaskRule.NAME)
        private String className;
    }

    @Getter
    @Setter
    @ToString
    static class Source implements Serializable {
        @MaskLog(MaskRule.IP)
        private String ip;
        private List<User> users;
    }

    @Getter
    @Setter
    @ToString
    static class Req<T> implements Serializable {
        @MaskLog
        private String token;
        private T t;
    }

    @Getter
    @Setter
    @ToString
    static class Req2<T> implements Serializable {
        private String token;
        private T t;
    }

}