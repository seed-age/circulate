package com.sunnsoft.sloa.util;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sunnsoft.sloa.db.vo.Dictionary;
import com.sunnsoft.sloa.service.DictionaryService;
import com.sunnsoft.util.base.DataDictonary;

import org.gteam.db.helper.hibernate.ScrollEach;
/**
 * 
 * @author llade
 * 
 * 数据库为存储方式的数据字典
 */
@Component("dataDictonary")
public class DbDataDictionary implements DataDictonary {
	
	@Resource
	protected DictionaryService dictionaryService;

	protected ConcurrentMap<String, String> dicMap = new ConcurrentHashMap<String, String>();
	
	@PostConstruct
	public void init(){
		dictionaryService.createHelper().scrollResult(new ScrollEach<Dictionary>(){

			@Override
			public void each(Dictionary bean, long index) {
				
				dicMap.put(bean.getKeyName(), bean.getKeyValue());
				
			}
			
		});
	}


	@Override
	public String replace(String key, String value) {
		String oldValue = this.dicMap.replace(key, value);
		if(value.equals(oldValue)){
			return oldValue;
		}
		persistence(key, value, oldValue);
		
		return oldValue;
	}

	private void persistence(String key, String value, String oldValue) {
		Date now = new Date();
		if(oldValue == null){
			this.dictionaryService.createHelper().bean().create()
			.setKeyName(key)
			.setKeyValue(value)
			.setCreateTime(now)
			.setStatus(true)
			.setUpdateTime(now)
			.insert();
		}else{
			this.dictionaryService.createHelper()
			.getKeyName().Eq(key)
			.bean()
			.setKeyValue(value)
			.setUpdateTime(now)
			.update();
		}
	}
	
	private void deleteKey(String key){
		this.dictionaryService.createHelper().getKeyName().Eq(key).delete();
	}

	@Override
	public String putIfAbsent(String key, String value) {
		String preValue = this.dicMap.putIfAbsent(key, value);
		if(preValue == null){
			this.persistence(key, value, preValue);
		}
		return preValue;
	}

	@Override
	public boolean remove(Object key, Object value) {
		boolean b = this.dicMap.remove(key, value);
		if(b){
			this.deleteKey((String)key);
		}
		return b;
	}

	@Override
	public boolean replace(String key, String oldValue, String newValue) {
		boolean b = this.dicMap.replace(key, oldValue, newValue);
		if(b){
			persistence(key, newValue, oldValue);
		}
		return false;
	}

	@Override
	public void clear() {
		throw new RuntimeException("not support");
	}

	@Override
	public boolean containsKey(Object key) {
		return this.dicMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.dicMap.containsValue(value);
	}

	@Override
	public Set<Map.Entry<String, String>> entrySet() {
		throw new RuntimeException("not support");
	}

	@Override
	public String get(Object key) {
		return this.dicMap.get(key);
	}

	@Override
	public boolean isEmpty() {
		return this.dicMap.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return this.dicMap.keySet();
	}

	@Override
	public String put(String key, String value) {
		String oldValue = this.dicMap.put(key, value);
		if(value.equals(oldValue)){
			return oldValue;
		}
		persistence(key, value, oldValue);
		return oldValue;
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		throw new RuntimeException("not support");
	}

	@Override
	public String remove(Object key) {
		String value = this.dicMap.remove(key);
		if(value != null){
			this.deleteKey((String)key);
		}
		return value;
	}

	@Override
	public int size() {
		return this.dicMap.size();
	}

	@Override
	public Collection<String> values() {
		throw new RuntimeException("not support");
	}

}
