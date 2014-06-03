package com.walmart.delivery.persistence.dao.postgresql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.walmart.delivery.persistence.dao.MapsDAO;
import com.walmart.delivery.persistence.entity.MapLocation;
import com.walmart.delivery.persistence.entity.Maps;

@SuppressWarnings("all")
@Repository
public class MapsDAOPostgreSQL implements MapsDAO {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public MapsDAOPostgreSQL(DataSource dataSource){
		this.dataSource = dataSource;
	}

	private Integer getNextVal(){
		String sql = "SELECT NEXTVAL('seq_maps')"; 
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		return (Integer) jdbcTemplate.queryForObject(sql, new Object[]{}, Integer.class);
	}

	public void insert(Maps maps) {
		String sql = "INSERT INTO maps (ID, NAME) VALUES (?, ?)";
		maps.setId(getNextVal());

		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, new Object[] { maps.getId(), maps.getName() });
	}

	public Maps get(Integer id) {
		String sql = "SELECT * FROM maps WHERE ID = ?";
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		return (Maps) jdbcTemplate.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper(Maps.class));
	}
	
	public Maps findByNameIgnoreCase(String name){
		String sql = "SELECT * FROM maps WHERE upper(name) = upper(?)";
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[]{name});
		
		List<Maps> listMaps = new ArrayList<Maps>();
		for (Map row : rows) {
			Maps mapLocation = new Maps();
			mapLocation.setId( Integer.parseInt(String.valueOf(row.get("ID"))) );
			mapLocation.setName( (String) row.get("NAME") );
			return mapLocation;
		}
		
		return null;
	}

}
