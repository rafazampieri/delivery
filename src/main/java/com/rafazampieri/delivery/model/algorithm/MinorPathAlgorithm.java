package com.rafazampieri.delivery.model.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rafazampieri.delivery.model.exception.DeliveryException;
import com.rafazampieri.delivery.persistence.dao.MapLocationDAO;
import com.rafazampieri.delivery.persistence.entity.MapLocation;

public class MinorPathAlgorithm {
	
	public MinorPathAlgorithm(MapLocationDAO mapLocationDAO){
		this.mapLocationDAO = mapLocationDAO;
	}
	
	private MapLocationDAO mapLocationDAO;
	
	private List<String> nextLocationsForSearch = new ArrayList<String>();
	private Map<String, DistanceAndPath> mapLocationAndDistance = new HashMap<String, DistanceAndPath>();
	
	private Integer mapsId;
	private DistanceAndPath finalDistanceAndPath;
	
	private String locationB;
	
	private class DistanceAndPath{
		DistanceAndPath(Integer countCost, Integer cost) {
			this.countCost = countCost;
			this.cost = cost;
		}
		Integer cost;
		Integer countCost;
		List<String> listPaths = new ArrayList<String>();
	}
	
	public ResultMinorPathAlgorithm calculate(Integer mapsId, String locationA, String locationB) throws DeliveryException{
		if(locationA.equals(locationB)){
			throw new DeliveryException("Location A equals Location B");
		}
		this.mapsId = mapsId;
		this.locationB = locationB;
		
		MapLocation mapLocationA = mapLocationDAO.findByMapsIdAndLocation(this.mapsId, locationA);
		MapLocation mapLocationB = mapLocationDAO.findByMapsIdAndLocation(this.mapsId, locationB);
		if(mapLocationA == null || mapLocationB == null){
			throw new DeliveryException("Not found one or two locations.");
		}
		
		DistanceAndPath costVsRout = new DistanceAndPath(0, 0);
		costVsRout.listPaths.add(locationA);
		mapLocationAndDistance.put(locationA, costVsRout);
		prepareNextQuery(mapLocationA);
		
		List<MapLocation> listMapLocations = mapLocationDAO.findByMapsIdAndLocationInList(mapsId, nextLocationsForSearch);
		startSearch(listMapLocations);
		
		Integer totalDistance = 0;
		for(String location: finalDistanceAndPath.listPaths){
			totalDistance += mapLocationAndDistance.get(location).cost;
		}
		
		ResultMinorPathAlgorithm resultMinorPathAlgorithm = new ResultMinorPathAlgorithm();
		resultMinorPathAlgorithm.setMapsId(mapsId);
		resultMinorPathAlgorithm.setDistance(totalDistance);
		resultMinorPathAlgorithm.setListPaths( finalDistanceAndPath.listPaths );
		
		return resultMinorPathAlgorithm;
	}

	private void startSearch(List<MapLocation> listMapLocations) {
		for (MapLocation mapLocation: listMapLocations) {
			prepareNextQuery(mapLocation);
		}
		if(nextLocationsForSearch.size() > 0){
			List<MapLocation> list = mapLocationDAO.findByMapsIdAndLocationInList(mapsId, nextLocationsForSearch);
			nextLocationsForSearch.clear();
			startSearch( list );
		}
	}

	private void prepareNextQuery(MapLocation mapLocation) {
		for (String location: mapLocation.getMapEdges().keySet()) {
			if(location.equals(locationB)){
				DistanceAndPath currentLocation = mapLocationAndDistance.get(mapLocation.getLocation());
				
				DistanceAndPath newDistanceAndPath = new DistanceAndPath(currentLocation.countCost + mapLocation.getMapEdges().get(location), mapLocation.getMapEdges().get(location));
				newDistanceAndPath.listPaths = new ArrayList<String>( currentLocation.listPaths );
				newDistanceAndPath.listPaths.add(location);
				
				if(finalDistanceAndPath == null || finalDistanceAndPath.countCost > newDistanceAndPath.countCost){
					mapLocationAndDistance.put(location, newDistanceAndPath);
					finalDistanceAndPath = newDistanceAndPath;
				}
			}
			
			DistanceAndPath existCostVsRout = mapLocationAndDistance.get(location);
			if( existCostVsRout == null ){
				nextLocationsForSearch.add(location);
				
				DistanceAndPath currentLocation = mapLocationAndDistance.get(mapLocation.getLocation());
				
				DistanceAndPath newDistanceAndPath = new DistanceAndPath(currentLocation.countCost + mapLocation.getMapEdges().get(location), mapLocation.getMapEdges().get(location));
				newDistanceAndPath.listPaths = new ArrayList<String>( currentLocation.listPaths );
				newDistanceAndPath.listPaths.add(location);
				
				mapLocationAndDistance.put(location, newDistanceAndPath);
			} else {
				DistanceAndPath currentLocation = mapLocationAndDistance.get(mapLocation.getLocation());
				int currentCost = currentLocation.countCost + mapLocation.getMapEdges().get(location);
				if(currentCost < existCostVsRout.countCost){
					DistanceAndPath newDistanceAndPath = new DistanceAndPath(currentLocation.countCost + mapLocation.getMapEdges().get(location), mapLocation.getMapEdges().get(location));
					newDistanceAndPath.listPaths = new ArrayList<String>( currentLocation.listPaths );
					newDistanceAndPath.listPaths.add(location);
						
					mapLocationAndDistance.put(location, newDistanceAndPath);
				}
			}
		}
	}
	
	public class ResultMinorPathAlgorithm{
		private Integer mapsId;
		private String mapName;
		private Integer distance;
		private List<String> listPaths;
		
		public Integer getMapsId() {
			return mapsId;
		}
		public void setMapsId(Integer mapsId) {
			this.mapsId = mapsId;
		}
		public List<String> getListPaths() {
			return listPaths;
		}
		public void setListPaths(List<String> listPaths) {
			this.listPaths = listPaths;
		}
		public Integer getDistance() {
			return distance;
		}
		public void setDistance(Integer distance) {
			this.distance = distance;
		}
		public String getMapName() {
			return mapName;
		}
		public void setMapName(String mapName) {
			this.mapName = mapName;
		}
	}

}
