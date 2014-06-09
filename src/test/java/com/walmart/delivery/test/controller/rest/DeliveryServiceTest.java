package com.walmart.delivery.test.controller.rest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.walmart.delivery.model.exception.DeliveryException;
import com.walmart.delivery.model.service.DeliveryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/spring-context.xml" })
public class DeliveryServiceTest {

	@Autowired
	private DeliveryService deliveryService;

	@Test
	public void testAddingNewLocationWithSuccess() {
		try {
			deliveryService.addLocationToMap("SP", "A", "B", 10);
		} catch (Exception e) {
			Assert.fail("not expected");
		}
	}
	
	@Test
	public void testCalculateMinorPath(){
		try {
			deliveryService.calculateMinorPath("SP", "A", "B", 10, 2.5);
		} catch (DeliveryException e) {
			Assert.fail("not expected");
		}
	}

}
