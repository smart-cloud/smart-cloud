package org.smartframework.cloud.mask.test.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.mask.MaskLog;
import org.smartframework.cloud.mask.MaskRule;
import org.smartframework.cloud.mask.util.MaskUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class MaskSerializeTest extends TestCase {

	// 手动设置mask属性
	public void testMaskSerializeSetMaskAttribute() {
		LoginVO loginVO = new LoginVO();
		loginVO.setName("名字测试");
		loginVO.setPassword("13112345678");

		String maskResult = MaskUtil.mask(loginVO);

		LoginVO maskLoginVO = JSON.parseObject(maskResult, LoginVO.class);
		Assertions.assertThat(maskLoginVO.getName()).isEqualTo(MaskUtil.mask(loginVO.getName(), 1,1, "###"));
		Assertions.assertThat(maskLoginVO.getPassword()).isEqualTo(MaskUtil.mask(loginVO.getPassword(), 2,0,"***"));
	}

	// 普通对象
	public void testMaskSerializeObject() {
		User user = new User();
		user.setId(9L);
		user.setName("名字");
		user.setMobile("13112345678");

		String maskResult = MaskUtil.mask(user);

		User maskUser = JSON.parseObject(maskResult, User.class);
		Assertions.assertThat(maskUser.getId()).isEqualTo(user.getId());
		Assertions.assertThat(maskUser.getName()).isEqualTo(MaskUtil.mask(user.getName(), MaskRule.NAME));
		Assertions.assertThat(maskUser.getMobile()).isEqualTo(MaskUtil.mask(user.getMobile(), MaskRule.MOBILE));
	}

	// 数组对象
	public void testMaskSerializeArray() {
		User user = new User();
		user.setId(9L);
		user.setName("名字");
		user.setMobile("13112345678");

		User[] users = { user };

		String maskResult = MaskUtil.mask(users);

		User[] maskUsers = JSON.parseObject(maskResult, User[].class);
		User maskUser = maskUsers[0];
		Assertions.assertThat(maskUser.getId()).isEqualTo(user.getId());
		Assertions.assertThat(maskUser.getName()).isEqualTo(MaskUtil.mask(user.getName(), MaskRule.NAME));
		Assertions.assertThat(maskUser.getMobile()).isEqualTo(MaskUtil.mask(user.getMobile(), MaskRule.MOBILE));
	}

	// 嵌套对象
	public void testMaskSerializeNestedObject() {
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

		Source maskSource = JSON.parseObject(maskResult, Source.class);

		Assertions.assertThat(maskSource.getIp()).isEqualTo(MaskUtil.mask(source.getIp(), MaskRule.IP));

		User maskUser = maskSource.getUsers().get(0);
		Assertions.assertThat(maskUser.getId()).isEqualTo(user.getId());
		Assertions.assertThat(maskUser.getName()).isEqualTo(MaskUtil.mask(user.getName(), MaskRule.NAME));
		Assertions.assertThat(maskUser.getMobile()).isEqualTo(MaskUtil.mask(user.getMobile(), MaskRule.MOBILE));
	}

	// 泛型对象
	public void testMaskSerializeGeneric() {
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

		Req<Source> maskReq = JSON.parseObject(maskResult, new TypeReference<Req<Source>>() {
		});

		Assertions.assertThat(maskReq.getToken()).isEqualTo(MaskUtil.mask(req.getToken(), MaskRule.DEFAULT));

		Source maskSource = maskReq.getT();
		Assertions.assertThat(maskSource.getIp()).isEqualTo(MaskUtil.mask(source.getIp(), MaskRule.IP));

		User maskUser = maskSource.getUsers().get(0);
		Assertions.assertThat(maskUser.getId()).isEqualTo(user.getId());
		Assertions.assertThat(maskUser.getName()).isEqualTo(MaskUtil.mask(user.getName(), MaskRule.NAME));
		Assertions.assertThat(maskUser.getMobile()).isEqualTo(MaskUtil.mask(user.getMobile(), MaskRule.MOBILE));
	}
	
	@Getter
	@Setter
	@ToString
	public static class LoginVO {
		@MaskLog(startLen = 1, endLen = 1, mask = "###")
		private String name;
		@MaskLog(startLen = 2)
		private String password;
	}

	@Getter
	@Setter
	@ToString
	public static class User {
		private Long id;
		@MaskLog(MaskRule.NAME)
		private String name;
		@MaskLog(MaskRule.MOBILE)
		private String mobile;
	}

	@Getter
	@Setter
	@ToString
	public static class Source {
		@MaskLog(MaskRule.IP)
		private String ip;
		private List<User> users;
	}

	@Getter
	@Setter
	@ToString
	public static class Req<T> {
		@MaskLog
		private String token;
		private T t;
	}

}