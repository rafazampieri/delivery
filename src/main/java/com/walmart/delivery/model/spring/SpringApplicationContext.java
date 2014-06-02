package com.walmart.delivery.model.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walmart.delivery.model.service.DeliveryService;
import com.walmart.delivery.persistence.dao.MapLocationDAO;
import com.walmart.delivery.persistence.dao.MapsDAO;

public class SpringApplicationContext {
	
	private static final ConfigurableApplicationContext context;
	public static final Service SERVICE;
	public static final Dao DAO;
	
	static {
		context = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
		SpringApplicationContext springApplicationContext = new SpringApplicationContext();
		SERVICE = springApplicationContext.new Service();
		DAO = springApplicationContext.new Dao();
	}
	public static void closeApplicationContext(){
		context.close();
	}
	
	public class Service{
		private Service(){}
		
		public DeliveryService getDeliveryService(){
			return context.getBean("deliveryService", DeliveryService.class);
		}
	}
	
	public class Dao{
		private Dao(){}
		
		public MapsDAO getMapsDAO(){
			return context.getBean("mapsDAO", MapsDAO.class);
		}
		
		public MapLocationDAO getMapLocationDAO(){
			return context.getBean("mapLocationDAO", MapLocationDAO.class);
		}
	}
	

}
