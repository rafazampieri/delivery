package com.walmart.delivery.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walmart.delivery.controller.uri.ProjectRestURIConstants;
import com.walmart.delivery.model.service.DeliveryService;
import com.walmart.delivery.model.to.MinorPathTO;
import com.walmart.delivery.persistence.entity.Maps;

@Controller
public class DeliveryController {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);
	
	private DeliveryService deliveryService;

	@Autowired
	public DeliveryController(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	// ex: http://localhost:8080/delivery/rest/delivery/addLocationToMap/SP/B/F/5
	@RequestMapping(value = ProjectRestURIConstants.DeliveryController.POST_ADD_LOCATION_TO_MAP, method = RequestMethod.POST)
	public @ResponseBody Boolean addLocationToMap(@PathVariable("mapName") String mapName, 
			@PathVariable("locationBegin") String locationBegin, @PathVariable("locationEnd") String locationEnd, 
			@PathVariable("cost") Integer cost) {
		try{
			deliveryService.addLocationToMap(mapName, locationBegin, locationEnd, cost);
			return true;
		} catch (Throwable t){
			t.printStackTrace();
			return false;
		}
	}
	
	// ex: http://localhost:8080/delivery/rest/delivery/calculateMinorPath/SP/A/D/10/2.5
	@RequestMapping(value = ProjectRestURIConstants.DeliveryController.GET_CALCULATE_MINOR_PATH, method = RequestMethod.GET)
	public @ResponseBody MinorPathTO calculateMinorPath(@PathVariable("mapName") String mapName, 
			@PathVariable("locationBegin") String locationBegin, @PathVariable("locationEnd") String locationEnd, 
			@PathVariable("fuelAutonomy") Integer fuelAutonomy, @PathVariable("fuelCost") Double fuelCost) throws Exception{
		return deliveryService.calculateMinorPath(mapName, locationBegin, locationEnd, fuelAutonomy, fuelCost);
	}

}
