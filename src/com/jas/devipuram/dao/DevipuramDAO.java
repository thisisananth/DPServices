package com.jas.devipuram.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import au.com.bytecode.opencsv.CSV;

import com.jas.devipuram.model.User;

public class DevipuramDAO {

	Logger log = Logger.getLogger(this.getClass().getName());

	private JdbcTemplate jdbcTemplate;

	// Total registrations

	private static String TOTAL_REG = "select count(id) from learner";

	// define format of CSV file one time and use everywhere
	// human readable configuration
	private static final CSV csv = CSV.separator(',').quote('\'').skipLines(1)
			.charset("UTF-8").create();

	// Registrations between two dates
	private static String REG_BW_TWO_DATES = "select * from learner where date_created between to_timestamp(?, 'DD-Mon-YYYY')"
			+ " and 	to_timestamp(?, 'DD-Mon-YYYY') + interval '1 day'";

	// count of logins per day

	private static String LOGINS_PER_DAY = "SELECT date_trunc('day', date_created) AS day_start, "
			+ "COUNT(id) AS user_count FROM evt_audit where evt_id=2  GROUP BY date_trunc('day', date_created)";

	// average logins per week
	private static String LOGINS_PER_WEEK = "WITH weekly_count AS (SELECT date_trunc('week', date_created) AS week_start,"
			+ "COUNT(id) AS user_count FROM evt_audit where evt_id=2 GROUP BY date_trunc('week', date_created)) "
			+ "SELECT AVG(user_count) FROM weekly_count";

	// logins on a day
	private static String LOGINS_ON_DATE = "select * from evt_audit where date_trunc('day',date_created) = "
			+ "to_date(?, 'DD-Mon-YYYY') and evt_id=2";

	// Logins for a user between two dates
	private static String USER_LOGIN_BW_DATES = "select * from evt_audit where date_trunc('day',date_created)"
			+ " between to_date(?, 'DD-Mon-YYYY') and to_date(?, 'DD-Mon-YYYY') and evt_id=2 and learner_id=?";

	// Number of people started video 1
	public static String VIDEO_1_STARTS = "select learner_id,date_created from evt_audit where evt_id=3";

	// Number of people started video 2
	public static String VIDEO_2_STARTS = "select learner_id,date_created from evt_audit where evt_id=4";

	// Number of people completed video 1
	public static String VIDEO_1_ENDS = "select learner_id,date_created from evt_audit where evt_id=5";

	// Number of people completed video 2
	public static String VIDEO_2_ENDS = "select learner_id,date_created from evt_audit where evt_id=6";

	// Number of people who completed both videos
	public static String ALL_VIDEOS_COMP = "select learner_id from evt_audit where evt_id=5 INTERSECT  "
			+ "select learner_id from evt_audit where evt_id=6";

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public long createUser(final User user) {

		final String INSERT_SQL = "insert into learner(email,password,date_created) values(?,?,current_timestamp)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL,
						new String[] { "id" });
				ps.setString(1, user.getEmail());
				ps.setString(2, user.getPassword());

				return ps;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();

		return key.longValue();

	}

	public String getPassword(String email) {
		String loginQuery = "select password from learner where email= ? ";
		String password = "";

		password = jdbcTemplate.queryForObject(loginQuery,
				new Object[] { email }, String.class);

		return password;

	}

	public long getLeanerId(String email) {
		String loginQuery = "select id from learner where email= ? ";
		long id = 0;

		id = jdbcTemplate.queryForLong(loginQuery, new Object[] { email });

		return id;

	}

	public void auditEvent(long userId, int eventId) {
		String query = "insert into evt_audit(learner_id,evt_id,date_created) values(?,?,current_timestamp)";

		jdbcTemplate.update(query, new Object[] { userId, eventId });

	}

	// Total registrations
	public byte[] getTotalRegistration() {

		byte[] data = (byte[]) jdbcTemplate.query(TOTAL_REG,
				new DataByteArrayExtractor());

		return data;

	}

	// Registrations between two dates
	public byte[] getRegsBetweenDates(String startDate, String endDate) {

		byte[] data = (byte[]) jdbcTemplate.query(REG_BW_TWO_DATES,
				new Object[] { startDate, endDate },
				new DataByteArrayExtractor());

		return data;

	}

	// count of logins per day
	public byte[] getLoginsPerDay() {

		byte[] data = (byte[]) jdbcTemplate.query(LOGINS_PER_DAY,
				new DataByteArrayExtractor());

		return data;

	}

	// average logins per week
	public byte[] getLoginsPerWeek() {
		byte[] data = (byte[]) jdbcTemplate.query(LOGINS_PER_WEEK,
				new DataByteArrayExtractor());

		return data;
	}

	// logins on a day
	public byte[] getLoginsOnDate(String date) {
		byte[] data = (byte[]) jdbcTemplate.query(LOGINS_ON_DATE,
				new Object[] { date }, new DataByteArrayExtractor());

		return data;
	}

	// Logins for a user between two dates
	public byte[] getLoginsForUser(long userId, String startDate, String endDate) {
		byte[] data = (byte[]) jdbcTemplate.query(USER_LOGIN_BW_DATES,
				new Object[] { startDate, endDate, userId },
				new DataByteArrayExtractor());

		return data;
	}

	// Number of people started video 1
	// Logins for a user between two dates
	public byte[] getVideoStats(String query) {

		log.info("Query is" + query);

		byte[] data = (byte[]) jdbcTemplate.query(query, new Object[] {},
				new DataByteArrayExtractor());

		return data;
	}

	// Insert into payment_audit table.

	public long createPaymentAudit(final String paymentId, final Integer quantity,
			final String status, final String offerTitle,
			final String buyerEmail, final Float unitPrice, final Float amount,
			final Float fees, final String macAddr) {
		
		log.info("Came to this method");
		final String INSERT_SQL = "insert into payment_audit(buyer_email,payment_id,quantity,"
				+ "status,offer_title,unit_price,amount,fees,payment_date,mac) values(?,?,?,?,?,?,?,?,current_timestamp,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL,
						new String[] { "id" });
				ps.setString(1, buyerEmail);
				ps.setString(2, paymentId);
				ps.setObject(3, quantity);
				ps.setString(4, status);
				ps.setString(5, offerTitle);
				ps.setObject(6, unitPrice);
				ps.setObject(7, amount);
				ps.setObject(8, fees);
				ps.setString(9, macAddr);

				return ps;
			}
		}, keyHolder);
		Number key = keyHolder.getKey();

		return key.longValue();
	}
	
	public void saveCheckoutCode(final long paymentAudId,final String checkoutCode){
		final String sql = "insert into payments(coupon_code,payment_aud_id,date_created) values(?,?,current_timestamp)";
		jdbcTemplate.update(sql, new Object[] { checkoutCode, paymentAudId });
	}
	
	
	
	
	public void linkExistingEmail(String email,String checkoutCode){
		final String sql  = "select id from learner where email=?";
		long userId=0;
		try{
			 userId = jdbcTemplate.queryForLong(sql, new Object[]{email});
		}catch(IncorrectResultSizeDataAccessException rse){
			log.info("User not found with the email.");
		}
		if(userId!=0){
			updateCheckout(userId, checkoutCode);
		}
	}
	
	
	
	
	public boolean verifyCheckoutCode(String checkOutCode){
		
		boolean valid = false;
		String query = "select learner_id,id from payments where coupon_code=?";
		Map result = jdbcTemplate.queryForMap(query, new Object[]{});
		
		if(result!=null){
			Long id = (Long) result.get("id");
			Long userId=(Long)result.get("learner_id");
			if(id!=null && userId==null){
				valid=true;
			}
		}
		
		
		return valid;
		
	}
	
	
	public void updateCheckout(long userId,String checkoutCode){
		final String sql = "update payments set learner_id = ? where coupon_code = ? ";
		jdbcTemplate.update(sql,new Object[]{userId,checkoutCode});
	}

}
