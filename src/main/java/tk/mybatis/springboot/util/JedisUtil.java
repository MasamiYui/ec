package tk.mybatis.springboot.util;


import redis.clients.jedis.Jedis;

public class JedisUtil {
	public static final Jedis jedis = new Jedis("123.207.35.82",6379);
	//public static final JedisPool jedisPool = new JedisPool("127.0.0.1",6379); 
	
	public static String add(String key, String value, int seconds){
		return jedis.setex(key, seconds, value);
	}
	
	public static String get(String key){
		return jedis.get(key);
	}
	
	public static void main(String[] args) {
	}
	

}
