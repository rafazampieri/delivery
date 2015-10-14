package com.rafazampieri.delivery.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rafazampieri.delivery.controller.uri.ProjectURIConstants;
import com.rafazampieri.delivery.model.exception.DeliveryException;
import com.rafazampieri.delivery.model.service.DeliveryService;
import com.rafazampieri.delivery.model.to.MinorPathTO;

@RestController
@RequestMapping(value=ProjectURIConstants.DeliveryRestController.PREFIX_MAPPING)
public class DeliveryRestController {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryRestController.class);
	
	private DeliveryService deliveryService;

	@Autowired
	public DeliveryRestController(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	// ex: http://localhost:8080/delivery/rest/delivery/addLocationToMap/SP/B/F/5
	@RequestMapping(value = ProjectURIConstants.DeliveryRestController.POST_ADD_LOCATION_TO_MAP, method = RequestMethod.POST)
	public Boolean addLocationToMap(@PathVariable("mapName") String mapName, 
			@PathVariable("locationBegin") String locationBegin, @PathVariable("locationEnd") String locationEnd, 
			@PathVariable("cost") Integer cost) {
		logger.debug("params: [mapName:{},locationA:{},locationB:{},cost:{}]", mapName, locationBegin, locationEnd, cost);
		try{
			deliveryService.addLocationToMap(mapName, locationBegin, locationEnd, cost);
			return true;
		} catch (Throwable t){
			t.printStackTrace();
			return false;
		}
	}
	
	// ex: http://localhost:8080/delivery/rest/delivery/calculateMinorPath/SP/A/D/10/2.5
	@RequestMapping(value = ProjectURIConstants.DeliveryRestController.GET_CALCULATE_MINOR_PATH, method = RequestMethod.GET)
	public MinorPathTO calculateMinorPath(@PathVariable("mapName") String mapName, 
			@PathVariable("locationBegin") String locationBegin, @PathVariable("locationEnd") String locationEnd, 
			@PathVariable("fuelAutonomy") Integer fuelAutonomy, @PathVariable("fuelCost") Double fuelCost) throws DeliveryException {
		return deliveryService.calculateMinorPath(mapName, locationBegin, locationEnd, fuelAutonomy, fuelCost);
	}

}
