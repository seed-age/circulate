package com.sunnsoft.sloa.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sunnsoft.util.base.DataDictonary;

@Component("userDictonary")
public class UserDbDataDictionary implements DataDictonary {
	
	@Resource
	private DbDataDictionary dataDictionary;
	
	public static final String KEY_SEPARATOR = ":";
	
	private Serializable getCurrentUserId(){
		return UserUtils.getCurrentUser().getUserId();
	}

	@Override
	public String putIfAbsent(String key, String value) {
		return this.dataDictionary.putIfAbsent(key + KEY_SEPARATOR + this.getCurrentUserId(), value);
	}

	@Override
	public boolean remove(Object key, Object value) {
		return this.dataDictionary.remove(key + KEY_SEPARATOR + this.getCurrentUserId(), value);
	}

	@Override
	public String replace(String key, String value) {
		return this.dataDictionary.replace(key + KEY_SEPARATOR + this.getCurrentUserId(), value);
	}

	@Override
	public boolean replace(String key, String oldValue, String newValue) {
		return this.dataDictionary.replace(key + KEY_SEPARATOR + this.getCurrentUserId(), oldValue, newValue);
	}

	@Override
	public void clear() {
		this.dataDictionary.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.dataDictionary.containsKey(key + KEY_SEPARATOR + this.getCurrentUserId());
	}

	@Override
	public boolean containsValue(Object value) {
		return this.dataDictionary.containsValue(value);
	}

	@Override
	public Set<Map.Entry<String, String>> entrySet() {
		return this.dataDictionary.entrySet();
	}

	@Override
	public String get(Object key) {
		String value = this.dataDictionary.get(key + KEY_SEPARATOR + this.getCurrentUserId());
		return value == null ? this.dataDictionary.get(key) : value;
	}

	@Override
	public boolean isEmpty() {
		return this.dataDictionary.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return this.dataDictionary.keySet();
	}

	@Override
	public String put(String key, String value) {
		return this.dataDictionary.put(key + KEY_SEPARATOR + this.getCurrentUserId(), value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		this.dataDictionary.putAll(m);
	}

	@Override
	public String remove(Object key) {
		return this.dataDictionary.remove(key + KEY_SEPARATOR + this.getCurrentUserId());
	}

	@Override
	public int size() {
		return this.dataDictionary.size();
	}

	@Override
	public Collection<String> values() {
		return this.dataDictionary.values();
	}

}
