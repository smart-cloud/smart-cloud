package org.smartframework.cloud.utility.test.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.CollectionUtil;

import junit.framework.TestCase;

public class CollectionUtilUnitTest extends TestCase {

	public void testCollectionIsEmpty() {
		List<String> list = null;
		Assertions.assertThat(CollectionUtil.isEmpty(list)).isTrue();
		
		list = new ArrayList<>();
		Assertions.assertThat(CollectionUtil.isEmpty(list)).isTrue();
		
		list.add("test");
		Assertions.assertThat(CollectionUtil.isEmpty(list)).isFalse();
	}

	public void testCollectionIsNotEmpty() {
		List<String> list = null;
		Assertions.assertThat(CollectionUtil.isNotEmpty(list)).isFalse();
		
		list = new ArrayList<>();
		Assertions.assertThat(CollectionUtil.isNotEmpty(list)).isFalse();
		
		list.add("test");
		Assertions.assertThat(CollectionUtil.isNotEmpty(list)).isTrue();
	}
	
	public void testMapIsEmpty() {
		Map<String, String> map = null;
		Assertions.assertThat(CollectionUtil.isEmpty(map)).isTrue();
		
		map = new HashMap<>();
		Assertions.assertThat(CollectionUtil.isEmpty(map)).isTrue();
		
		map.put("test", "test");
		Assertions.assertThat(CollectionUtil.isEmpty(map)).isFalse();
	}
	
	public void testMapIsNotEmpty() {
		Map<String, String> map = null;
		Assertions.assertThat(CollectionUtil.isNotEmpty(map)).isFalse();
		
		map = new HashMap<>();
		Assertions.assertThat(CollectionUtil.isNotEmpty(map)).isFalse();
		
		map.put("test", "test");
		Assertions.assertThat(CollectionUtil.isNotEmpty(map)).isTrue();
	}

}