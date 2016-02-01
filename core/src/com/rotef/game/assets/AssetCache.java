package com.rotef.game.assets;

import java.util.HashMap;
import java.util.Map;

public class AssetCache {

	private Map<String, Object> cache = new HashMap<String, Object>();

	public <T> void add(String key, T asset) {
		cache.put(key, asset);
	}

	public boolean canLocate(String key) {
		return cache.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T locate(String key) {
		Object o = cache.get(key);

		return (T) o;
	}

	public void clear() {
		cache.clear();
	}

}
