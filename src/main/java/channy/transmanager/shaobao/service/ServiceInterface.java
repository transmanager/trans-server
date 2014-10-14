package channy.transmanager.shaobao.service;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import channy.util.ChannyException;

public interface ServiceInterface<T> {
	//public T add(Object ... args);
	
	public T getById(long id);
	//public T getByName(String name);
	public void update(T entity);
	public void remove(T entity);
	public void removeById(long id) throws ChannyException;
	
	public boolean exists(long id);
	//public boolean exits(String name);
	public int getCount() throws ChannyException;
	
	public JSONObject query(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException;
	public JSONObject select(int page, int pageSize, Map<String, Object> filter) throws ChannyException, JSONException;
}
