package com.rafazampieri.delivery.persistence.entity;

import java.util.List;

public class Maps {
	
	private Integer id;
	private String name;
	
	private List<MapLocation> listDeliveryMapLocations;
	
	public Maps() {}
	public Maps(String name) {
		this.name = name;
	}

	public List<MapLocation> getListDeliveryMapLocations() {
		return listDeliveryMapLocations;
	}

	public void setListDeliveryMapLocations(List<MapLocation> listDeliveryMapLocations) {
		this.listDeliveryMapLocations = listDeliveryMapLocations;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
