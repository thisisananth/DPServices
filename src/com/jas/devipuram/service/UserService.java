package com.jas.devipuram.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.jas.devipuram.dao.DevipuramDAO;
import com.jas.devipuram.dao.EventConstants;
import com.jas.devipuram.model.AuditResponse;
import com.jas.devipuram.model.Error;
import com.jas.devipuram.model.LoginData;
import com.jas.devipuram.model.LoginResponse;
import com.jas.devipuram.model.RegisterData;
import com.jas.devipuram.model.RegisterResponse;
import com.jas.devipuram.model.Status;
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
	   public RegisterResponse createUser( User user){
		RegisterResponse response = new RegisterResponse();
		Status status = new Status();
		RegisterData data = new RegisterData();
			try{
				long userId = devipuramDAO.createUser(user);
				//user.setId(userId);
				devipuramDAO.auditEvent(userId, EventConstants.EVENT_REGISTRATION);
				status.setCode(200);
				status.setDescription("OK");
				data.setLearnerId(userId);
				response.setStatus(status);
				response.setData(data);
				
				
				
			}catch(DuplicateKeyException e){
				e.printStackTrace();
				status.setCode(406);
				status.setDescription("Not Acceptable");
				List errors = new ArrayList();
				Error error= new Error();
				error.setErrorCode(410);
				error.setDescription("The email already exists.Please try again.");
				errors.add(error);
				status.setErrors(errors);
				response.setStatus(status);
				
			}catch(Exception e){
				e.printStackTrace();
				status.setCode(408);
				status.setDescription("Not Acceptable");
				List errors = new ArrayList();
				Error error= new Error();
				error.setErrorCode(411);
				error.setDescription(e.getMessage());
				errors.add(error);
				status.setErrors(errors);
				response.setStatus(status);
			}
			
			return response;
	 }
		

		@POST
	    @Path("/login/")
		@Produces({"application/xml","application/json"})
		@Consumes({MediaType.TEXT_XML,MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	   public LoginResponse login( User user){
		LoginResponse response = new LoginResponse();
		Status status = new Status();
		LoginData data = new LoginData();
			try{
				String password = devipuramDAO.getPassword(user.getEmail());
				
				if(password!=null && password.equals(user.getPassword())){
					
					
					//successful login
					
					long userId = devipuramDAO.getLeanerId(user.getEmail());
					
					// audit login
					devipuramDAO.auditEvent(userId, EventConstants.EVENT_LOGIN);
					status.setCode(200);
					status.setDescription("OK");
					data.setLearnerId(userId);
					response.setStatus(status);
					response.setData(data);
					
				}else{
					status.setCode(406);
					status.setDescription("Not Acceptable");
					List errors = new ArrayList();
					Error error= new Error();
					error.setErrorCode(412);
					error.setDescription("Email and password do not match.Please try again.");
					errors.add(error);
					status.setErrors(errors);
					response.setStatus(status);
				}
				
				
				
			}catch(EmptyResultDataAccessException e){
				
				status.setCode(406);
				status.setDescription("Not Acceptable");
				List errors = new ArrayList();
				Error error= new Error();
				error.setErrorCode(411);
				error.setDescription("Email doesn't exist on file.");
				errors.add(error);
				status.setErrors(errors);
				response.setStatus(status);
				
			}
			
			return response;
	 }
		
		@GET
	    @Path("/audit/")
		@Produces({"application/xml","application/json"})
		public AuditResponse audit (@QueryParam("userId")long userId,@QueryParam("eventId")int eventId){
			
			AuditResponse response = new AuditResponse();
			Status status = new Status();
			try{
				devipuramDAO.auditEvent(userId, eventId);
				status.setCode(200);
				status.setDescription("OK");
				
				response.setStatus(status);
			}catch(DataAccessException e){
				status.setCode(400);
				status.setDescription("Database Error");
				List errors = new ArrayList();
				Error error= new Error();
				error.setErrorCode(412);
				error.setDescription("Sorry, user or event doesn't exist.");
				errors.add(error);
				status.setErrors(errors);
				response.setStatus(status);
			}
			
			
			return response;
			
		}

}
