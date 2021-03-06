package com.rafazampieri.delivery.persistence.dao;

import java.util.List;

import com.rafazampieri.delivery.persistence.entity.MapLocation;

public interface MapLocationDAO {

	public void insert(MapLocation mapLocation);
	public void update(MapLocation mapLocation);
	public MapLocation findByMapsIdAndLocation(Integer mapsId, String location);
	public List<MapLocation> findByMapsIdAndLocationInList(Integer mapsId, List<String> listLocations);
	
}
