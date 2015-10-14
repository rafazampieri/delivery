package com.rafazampieri.delivery.persistence.entity;

import java.util.HashMap;
import java.util.Map;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class MapLocation {
	
	private static final JSONSerializer jsonSerializer = new JSONSerializer().exclude("class");
	
	private Integer id;
	private String location;
	private Map<String, Integer> mapEdges = new HashMap<String, Integer>();
	
	private Integer mapsId;
	
	public MapLocation() {}
	public MapLocation(String location) {
		this.location = location;
	}
	public MapLocation(String location, Integer mapsId) {
		this.location = location;
		this.mapsId = mapsId;
	}
	public MapLocation(Integer id, String location, String jsonEdges) {
		this.id = id;
		this.location = location;
		setJsonEdges(jsonEdges);
	}
	
	public void addEdge(String location, Integer cost){
		location = location.trim().toUpperCase();
		getMapEdges().put(location, cost);
	}
	
	public String getJsonEdges(){
		return jsonSerializer.serialize(mapEdges);
	}

	public void setJsonEdges(String jsonEdges){
		mapEdges = new JSONDeserializer<Map<String, Integer>>().deserialize(jsonEdges);
	}
	
	public Map<String, Integer> getMapEdges(){
		return mapEdges;
	}

	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = (location != null) ? location.toUpperCase() : null;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMapsId() {
		return mapsId;
	}
	public void setMapsId(Integer mapsId) {
		this.mapsId = mapsId;
	}
}