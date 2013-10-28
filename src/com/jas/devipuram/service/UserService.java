package com.jas.devipuram.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.jas.devipuram.dao.DevipuramDAO;
import com.jas.devipuram.model.User;
@Path("/users/")
@Produces("text/xml,application/xml,application/json")
public class UserService  {
	
	
	private DevipuramDAO devipuramDAO;
	
	
	 public DevipuramDAO getDevipuramDAO() {
		return devipuramDAO;
	}


	public void setDevipuramDAO(DevipuramDAO devipuramDAO) {
		this.devipuramDAO = devipuramDAO;
	}


		@GET
	    @Path("/getuser/")
		@Produces({"application/xml","application/json"})
	   public User getUser(){
		 User user = new User();
		 return user;
	 }
		
		
		@POST
	    @Path("/create/")
		@Produces({"application/xml","application/json"})
		@Consumes({MediaType.TEXT_XML,MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	   public User createUser( User user){
		
			long userId = devipuramDAO.createUser(user);
			user.setId(userId);
			return user;
	 }

}
