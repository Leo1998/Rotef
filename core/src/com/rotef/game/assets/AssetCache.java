package com.rotef.game.assets;

import com.badlogic.gdx.utils.ObjectMap;

public class AssetCache {

	private ObjectMap<String, Object> cache = new ObjectMap<String, Object>();

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
