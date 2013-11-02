package com.jas.devipuram.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.jas.devipuram.model.User;

public class DevipuramDAO {
	
	Logger log = Logger.getLogger(this.getClass().getName());

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	public long createUser(final User user){
		
		final String INSERT_SQL =  "insert into learner(email,password,date_created) values(?,?,current_timestamp)";
		

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps =
		                connection.prepareStatement(INSERT_SQL, new String[] {"id"});
		            ps.setString(1, user.getEmail());
		            ps.setString(2, user.getPassword());
		            
		            return ps;
		        }
		    },
		    keyHolder);
		Number key = keyHolder.getKey();
		
		
		
		return key.longValue();
		
	}
	
	
	public String getPassword(String email){
		String loginQuery = "select password from learner where email= ? ";
		String password ="";
		
			password = jdbcTemplate.queryForObject(loginQuery, new Object[]{email}, String.class);
		
		 return password;
		 
		 
		
		
	}
	
	public long getLeanerId(String email){
		String loginQuery = "select id from learner where email= ? ";
		long id =0;
		
			id = jdbcTemplate.queryForLong(loginQuery, new Object[]{email});
		
		 return id;
		 
		 
		
		
	}
	
	
	
	public void auditEvent(long userId,int eventId){
		String query = "insert into evt_audit(learner_id,evt_id,date_created) values(?,?,current_timestamp)";
		
		jdbcTemplate.update(query,new Object[]{userId,eventId});
		
	}
	
	

}
