package com.walmart.delivery.persistence.dao.postgresql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.walmart.delivery.persistence.dao.MapLocationDAO;
import com.walmart.delivery.persistence.entity.MapLocation;

@SuppressWarnings("all")
public class MapLocationDAOPostgreSQL implements MapLocationDAO {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public MapLocationDAOPostgreSQL(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	private Integer getNextVal(){
		String sql = "SELECT NEXTVAL('seq_map_location')"; 
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		return (Integer) jdbcTemplate.queryForObject(sql, new Object[]{}, Integer.class);
	}

	public void insert(MapLocation mapLocation) {
		String sql = "INSERT INTO map_location (ID, LOCATION, JSON_EDGES, MAPS_ID) VALUES (?, ?, ?, ?)";
		mapLocation.setId(getNextVal());

		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, new Object[] { mapLocation.getId(), mapLocation.getLocation(), mapLocation.getJsonEdges(), mapLocation.getMapsId() });
	}

	public void update(MapLocation mapLocation) {
		String sql = "UPDATE map_location SET LOCATION = ?, JSON_EDGES = ? WHERE ID = ?";
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, new Object[] { mapLocation.getLocation(), mapLocation.getJsonEdges(), mapLocation.getId() });
	}

	public MapLocation findByMapsIdAndLocation(Integer mapsId, String location) {
		String sql = "SELECT * FROM map_location WHERE maps_id = ? and upper(location) = upper(?)";
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		return (MapLocation) jdbcTemplate.queryForObject(sql, new Object[] { mapsId, location }, new BeanPropertyRowMapper(MapLocation.class));
	}

	public List<MapLocation> findByMapsIdAndLocationInList(Integer mapsId, List<String> listLocations) {
		String sql = "SELECT * FROM map_location WHERE MAPS_ID = :id AND LOCATION IN (:locations)";
		
		RowMapper<MapLocation> rowMapper = new RowMapper<MapLocation>() {
			public MapLocation mapRow(ResultSet rs, int arg1) throws SQLException {
				MapLocation mapLocation = new MapLocation();
				mapLocation.setId( rs.getInt("ID") );
				mapLocation.setLocation( rs.getString("LOCATION") );
				mapLocation.setJsonEdges( rs.getString("JSON_EDGES") );
				mapLocation.setMapsId( rs.getInt("MAPS_ID") );
				return mapLocation;
			}
		};
		
		jdbcTemplate = new JdbcTemplate(dataSource);
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("id", mapsId);
		parameters.addValue("locations", listLocations);
		List<MapLocation> listMapLocations = namedParameterJdbcTemplate.query(sql, parameters, rowMapper);
		
		return listMapLocations;
	}

}
