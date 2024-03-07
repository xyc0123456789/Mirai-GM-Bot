package com.king.util;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {

	private static final Gson gson = new GsonBuilder()
			.enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
			.serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
			.setExclusionStrategies(new JsonKit())
			.setVersion(1.0).disableHtmlEscaping()
			.registerTypeAdapter(Map.class,new MapTypeAdapter())
			.create();


	private static final Gson notNullGson = new GsonBuilder()
			.enableComplexMapKeySerialization().disableHtmlEscaping() //支持Map的key为复杂对象的形式
			.setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
			.setVersion(1.0).disableHtmlEscaping().create();

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static String toJsonNotNull(Object obj) {
		return notNullGson.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return gson.fromJson(json, clazz);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public static <T> T fromJsonByType(String json,Type type) {
		try {
			return gson.fromJson(json, type);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public static String json(Object... kv) {
		if(kv == null || kv.length % 2 != 0) {
			throw new RuntimeException("参数长度错误");
		}

		Map<Object, Object> data = new HashMap<>();
		for(int i = 0; i < kv.length / 2; i++) {
			data.put(kv[i * 2], kv[i * 2 +1]);
		}

		return JsonUtil.toJson(data);
	}
}



class MapTypeAdapter extends TypeAdapter<Object> {
	@Override
	public Object read(JsonReader in) throws IOException {
		JsonToken token = in.peek();
		switch (token) {
			case BEGIN_ARRAY:
				List<Object> list = new ArrayList<>();
				in.beginArray();
				while (in.hasNext()) {
					list.add(read(in));
				}
				in.endArray();
				return list;
			case BEGIN_OBJECT:
				Map<String, Object> map = new LinkedTreeMap<>();
				in.beginObject();
				while (in.hasNext()) {
					map.put(in.nextName(), read(in));
				}
				in.endObject();
				return map;
			case STRING:
				return in.nextString();
			case NUMBER:
				/*
				 * 改写数字的处理逻辑，将数字值分为整型与浮点型。
				 */
				String numberStr = in.nextString();
				if (numberStr.contains(".") || numberStr.contains("e")
						|| numberStr.contains("E")) {
					return Double.parseDouble(numberStr);
				}
				if (Long.parseLong(numberStr) <= Integer.MAX_VALUE) {
					return Integer.parseInt(numberStr);
				}
				return Long.parseLong(numberStr);
			case BOOLEAN:
				return in.nextBoolean();
			case NULL:
				in.nextNull();
				return null;
			default:
				throw new IllegalStateException();
		}
	}
	@Override
	public void write(JsonWriter out, Object value) throws IOException {
		// 序列化无需实现
	}
}

@Slf4j
class JsonKit implements ExclusionStrategy {
	public JsonKit() {
	}

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		if (arg0.getName().startsWith("org.glassfish.jersey")){
			log.warn("JsonUtil.toJson: "+arg0.getName() + " has been skipped");
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes arg0) {
		return false;
	}

}