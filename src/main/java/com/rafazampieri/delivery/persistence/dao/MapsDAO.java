package com.rafazampieri.delivery.persistence.dao;

import com.rafazampieri.delivery.persistence.entity.Maps;

public interface MapsDAO {

	public void insert(Maps maps);
	public Maps get(Integer id);
	public Maps findByNameIgnoreCase(String mapName);
	
}
