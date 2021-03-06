package com.rafazampieri.delivery.model.to;

import java.io.Serializable;
import java.util.List;

import com.rafazampieri.delivery.model.algorithm.MinorPathAlgorithm.ResultMinorPathAlgorithm;

public class MinorPathTO implements Serializable{
	private static final long serialVersionUID = 5295145406705669752L;
	
	private String mapName;
	private Integer distance;
	private Integer fuelAutonomy;
	private Double fuelCost;
	private List<String> listPaths; 
	
	public MinorPathTO() {}
	public MinorPathTO(ResultMinorPathAlgorithm resultMinorPathAlgorithm) {
		this.listPaths = resultMinorPathAlgorithm.getListPaths();
		this.mapName = resultMinorPathAlgorithm.getMapName();
		this.distance = resultMinorPathAlgorithm.getDistance();
	}
	
	public Double getTotalCost(){
		return (((double)distance / (double)fuelAutonomy) * fuelCost);
	}
	
	public Integer getFuelAutonomy() {
		return fuelAutonomy;
	}
	public void setFuelAutonomy(Integer fuelAutonomy) {
		this.fuelAutonomy = fuelAutonomy;
	}
	public Double getFuelCost() {
		return fuelCost;
	}
	public void setFuelCost(Double fuelCost) {
		this.fuelCost = fuelCost;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public List<String> getListPaths() {
		return listPaths;
	}
	public void setListPaths(List<String> listPaths) {
		this.listPaths = listPaths;
	}

}
