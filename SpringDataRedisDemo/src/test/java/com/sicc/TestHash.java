package com.sicc;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class TestHash {

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Test
	public void testSetValue(){//存入值
		redisTemplate.boundHashOps("namehash").put("a", "唐僧");
		redisTemplate.boundHashOps("namehash").put("b", "悟空");
		redisTemplate.boundHashOps("namehash").put("c", "八戒");
		redisTemplate.boundHashOps("namehash").put("d", "沙僧");
	}
	
	@Test
	public void testGetKeys(){//提取所有的KEY
		Set s = redisTemplate.boundHashOps("namehash").keys();		
		System.out.println(s);		
	}

	@Test
	public void testGetValues(){//提取所有的值
		List values = redisTemplate.boundHashOps("namehash").values();
		System.out.println(values);		
	}
	
	@Test
	public void testGetValueByKey(){//根据KEY提取值
		Object object = redisTemplate.boundHashOps("namehash").get("b");
		System.out.println(object);
	}
	
	@Test
	public void testRemoveValueByKey(){//根据KEY移除值
		redisTemplate.boundHashOps("namehash").delete("c");
	}
}
