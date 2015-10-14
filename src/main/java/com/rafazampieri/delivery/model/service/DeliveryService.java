package com.rafazampieri.delivery.model.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafazampieri.delivery.model.algorithm.MinorPathAlgorithm;
import com.rafazampieri.delivery.model.algorithm.MinorPathAlgorithm.ResultMinorPathAlgorithm;
import com.rafazampieri.delivery.model.exception.DeliveryException;
import com.rafazampieri.delivery.model.to.MinorPathTO;
import com.rafazampieri.delivery.persistence.dao.MapLocationDAO;
import com.rafazampieri.delivery.persistence.dao.MapsDAO;
import com.rafazampieri.delivery.persistence.entity.MapLocation;
import com.rafazampieri.delivery.persistence.entity.Maps;

@Service
public class DeliveryService {
	
	private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);
	
	private MapsDAO mapsDAO;
	private MapLocationDAO mapLocationDAO;
	
	@Autowired
	public DeliveryService(MapsDAO mapsDAO, MapLocationDAO mapLocationDAO) {
		this.mapsDAO = mapsDAO;
		this.mapLocationDAO = mapLocationDAO;
	}
	
	public void addLocationToMap(String mapName, String locationA, String locationB, Integer cost){
		logger.debug("params: [mapName:{},locationA:{},locationB:{},cost:{}]", mapName, locationA, locationB, cost);
		
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
	
	public MinorPathTO calculateMinorPath(String mapName, String locationBegin, String locationEnd, Integer fuelAutonomy, Double fuelCost) throws DeliveryException{
		logger.debug("params: [mapName:{},locationBegin:{},locationB:{},fuelAutonomy:{},fuelCost:{}]", mapName, locationBegin, locationEnd, fuelAutonomy, fuelCost);
		
		Maps maps = mapsDAO.findByNameIgnoreCase(mapName);
		if(maps == null){
			logger.info("mapName '{}' not found", mapName);
			throw new DeliveryException("Not found mapName.");
		}
		
		ResultMinorPathAlgorithm resultMinorPathAlgorithm = new MinorPathAlgorithm(mapLocationDAO).calculate(maps.getId(), locationBegin, locationEnd);
		
		MinorPathTO minorPathTO = new MinorPathTO(resultMinorPathAlgorithm);
		minorPathTO.setFuelAutonomy(fuelAutonomy);
		minorPathTO.setFuelCost(fuelCost);
		minorPathTO.setMapName(mapName);
		
		return minorPathTO;
	}
}
