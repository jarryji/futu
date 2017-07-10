package client.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonUtils {
	private static Gson gson = new Gson();
	/**
	 * 将对象序列化为JSON字符串
	 * 
	 * @param object
	 * @return JSON字符串
	 */
	public static String serialize(Object object) {
		return gson.toJson(object);
	}

	/**
	 * 将JSON字符串反序列化为对象
	 * 
	 * @param object
	 * @return JSON字符串
	 */
	public static <T> T deserialize(String json, Class<T> clazz) {
		try {
			return gson.fromJson(json,clazz);
		} catch (JsonSyntaxException e)
		{
			return null;
		}
	}

}
