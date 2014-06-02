package com.walmart.delivery.model.service;

import java.util.ArrayList;
import java.util.List;

import com.walmart.delivery.model.algorithm.MinorPathAlgorithm;
import com.walmart.delivery.model.algorithm.MinorPathAlgorithm.ResultMinorPathAlgorithm;
import com.walmart.delivery.model.to.MinorPathTO;
import com.walmart.delivery.persistence.dao.MapLocationDAO;
import com.walmart.delivery.persistence.dao.MapsDAO;
import com.walmart.delivery.persistence.entity.MapLocation;
import com.walmart.delivery.persistence.entity.Maps;

public class DeliveryService {
	
	private MapsDAO mapsDAO;
	private MapLocationDAO mapLocationDAO;
	
	public DeliveryService(MapsDAO mapsDAO, MapLocationDAO mapLocationDAO) {
		this.mapsDAO = mapsDAO;
		this.mapLocationDAO = mapLocationDAO;
	}
	
	public void addLocationToMap(String mapName, String locationA, String locationB, Integer cost){
		locationA = locationA.trim().toUpperCase();
		locationB = locationB.trim().toUpperCase();
		
		Maps maps = mapsDAO.findByNameIgnoreCase(mapName);
		if(maps == null){
			maps = new Maps( mapName.trim() );
			mapsDAO.insert(maps);
		}
		
		List<String> listNames = new ArrayList<String>();
		listNames.add( locationA );
		listNames.add( locationB );
		
		List<MapLocation> listMapLocations = mapLocationDAO.findByMapsIdAndLocationInList(maps.getId(), listNames);
		if( listMapLocations.isEmpty() ){
			MapLocation mapLocationA = new MapLocation( locationA, maps.getId() );
			mapLocationDAO.insert(mapLocationA);
			listMapLocations.add(mapLocationA);
			
			MapLocation mapLocationB = new MapLocation( locationB, maps.getId() );
			mapLocationDAO.insert(mapLocationB);
			listMapLocations.add(mapLocationB);
		} else if( listMapLocations.size() == 1 ){
			boolean existLocationA = false;
			for (MapLocation mapLocation: listMapLocations) {
				if(mapLocation.getLocation().equals(locationA)){
					existLocationA = true;
				}
			}
			
			if(existLocationA == false){
				MapLocation mapLocationA = new MapLocation( locationA, maps.getId() );
				mapLocationDAO.insert(mapLocationA);
				listMapLocations.add(mapLocationA);
			} else {
				MapLocation mapLocationB = new MapLocation( locationB, maps.getId() );
				mapLocationDAO.insert(mapLocationB);
				listMapLocations.add(mapLocationB);
			}
		}
		
		for (MapLocation mapLocation : listMapLocations) {
			if(mapLocation.getLocation().equals(locationA)){
				mapLocation.addEdge(locationB, cost);
			} else if(mapLocation.getLocation().equals(locationB)){
				mapLocation.addEdge(locationA, cost);
			}
			mapLocationDAO.update(mapLocation);
		}
	}
	
	public MinorPathTO calculateMinorPath(String mapName, String locationBegin, String locationEnd, Integer fuelAutonomy, Double fuelCost) throws Exception{
		Maps maps = mapsDAO.findByNameIgnoreCase(mapName);
		if(maps == null){
			throw new Exception("Not found mapName.");
		}
		
		ResultMinorPathAlgorithm resultMinorPathAlgorithm = new MinorPathAlgorithm(mapLocationDAO).calculate(maps.getId(), locationBegin, locationEnd);
		
		MinorPathTO minorPathTO = new MinorPathTO(resultMinorPathAlgorithm);
		minorPathTO.setFuelAutonomy(fuelAutonomy);
		minorPathTO.setFuelCost(fuelCost);
		minorPathTO.setMapName(mapName);
		
		return minorPathTO;
	}
}
