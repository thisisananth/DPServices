package com.jas.devipuram.service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.jas.devipuram.dao.DevipuramDAO;

@Path("/reports/")
@Produces("text/xml,application/xml,application/json")
public class ReportService {
	private DevipuramDAO devipuramDAO;

	public DevipuramDAO getDevipuramDAO() {
		return devipuramDAO;
	}

	public void setDevipuramDAO(DevipuramDAO devipuramDAO) {
		this.devipuramDAO = devipuramDAO;
	}

	@GET
	@Path("/totalregs/")
	@Produces({ "application/csv" })
	public Response getTotalRegistrationCount() {

		byte[] data = getDevipuramDAO().getTotalRegistration();

		return Response
				.ok(getOut(data))
				.header("Content-Disposition",
						"attachment; filename=total_regs.csv").build();

	}

	@GET
	@Path("/regsbetween/")
	@Produces({ "application/csv" })
	public Response getRegistrationBetweenDates(
			@QueryParam("startDate") String startDateStr,
			@QueryParam("endDate") String endDateStr) {

		byte[] data = getDevipuramDAO().getRegsBetweenDates(startDateStr,
				endDateStr);

		return Response
				.ok(getOut(data))
				.header("Content-Disposition",
						"attachment; filename=regs_between.csv").build();

	}

	@GET
	@Path("/loginsperday/")
	@Produces({ "application/csv" })
	public Response getLoginsPerDay() {

		byte[] data = getDevipuramDAO().getLoginsPerDay();

		return Response
				.ok(getOut(data))
				.header("Content-Disposition",
						"attachment; filename=logins_per_day.csv").build();

	}
	
	
	@GET
	@Path("/loginsperweek/")
	@Produces({ "application/csv" })
	public Response getLoginsPerWeek() {

		byte[] data = getDevipuramDAO().getLoginsPerWeek();

		return Response
				.ok(getOut(data))
				.header("Content-Disposition",
						"attachment; filename=logins_per_week.csv").build();

	}
	
	
	@GET
	@Path("/loginsonday/")
	@Produces({ "application/csv" })
	public Response getLoginsOnDay(@QueryParam("date") String date ) {

		byte[] data = getDevipuramDAO().getLoginsOnDate(date);

		return Response
				.ok(getOut(data))
				.header("Content-Disposition",
						"attachment; filename=logins_on_date.csv").build();

	}
	
	
	@GET
	@Path("/loginsforuser/")
	@Produces({ "application/csv" })
	public Response getLoginsForUser(@QueryParam("userId") Long userId,@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {

		byte[] data = getDevipuramDAO().getLoginsForUser(userId, startDate, endDate);

		return Response
				.ok(getOut(data))
				.header("Content-Disposition",
						"attachment; filename=logins_for_user.csv").build();

	}
	
	@GET
	@Path("/videostats/")
	@Produces({ "application/csv" })
	public Response getVideoStats(@QueryParam("videoId") String videoId,@QueryParam("type") String type
			) {
		
		String query="";
		if(videoId!=null && videoId.equals("1") && type!=null && type.equals("start")){
			query = DevipuramDAO.VIDEO_1_STARTS;
			
		}else if(videoId!=null && videoId.equals("2") && type!=null && type.equals("start")){
			query = DevipuramDAO.VIDEO_2_STARTS;
			
		}else if(videoId!=null && videoId.equals("1") && type!=null && type.equals("end")){
			query = DevipuramDAO.VIDEO_1_ENDS;
			
		}else if(videoId!=null && videoId.equals("2") && type!=null && type.equals("end")){
			query = DevipuramDAO.VIDEO_2_ENDS;
			
		}else if(videoId!=null && videoId.equals("all") && type!=null && type.equals("end")){
			query = DevipuramDAO.ALL_VIDEOS_COMP;
			
		}else if(videoId!=null && videoId.equals("all") && type!=null && type.equals("start")){
			throw new WebApplicationException(Response.notAcceptable(null).build());
		}

		byte[] data = getDevipuramDAO().getVideoStats(query);

		return Response
				.ok(getOut(data))
				.header("Content-Disposition",
						"attachment; filename=video_stats.csv").build();

	}
	
	
	
	

	private StreamingOutput getOut(final byte[] excelBytes) {
		return new StreamingOutput() {

			public void write(OutputStream out) throws IOException,
					WebApplicationException {
				out.write(excelBytes);
			}

		};
	}

}
