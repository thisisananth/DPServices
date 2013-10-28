package com.jas.devipuram.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.jas.devipuram.model.User;

public class DevipuramDAO {
	

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

}
