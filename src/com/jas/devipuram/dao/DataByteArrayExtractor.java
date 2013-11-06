package com.jas.devipuram.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVWriteProc;
import au.com.bytecode.opencsv.CSVWriter;

public class DataByteArrayExtractor implements ResultSetExtractor<Object> {
	
	// define format of CSV file one time and use everywhere
	// human readable configuration
	private static final CSV csv = CSV.separator(',').quote('\'').skipLines(1)
			.charset("UTF-8").create();

	public Object extractData(ResultSet arg0) throws SQLException,
			DataAccessException {

		final ResultSet rs = arg0;

		ByteArrayOutputStream bas = new ByteArrayOutputStream();

		csv.writeAndClose(bas, new CSVWriteProc() {
			public void process(CSVWriter out) {
				try {
					out.writeAll(rs, true);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		

		byte[] data = bas.toByteArray();
		try {
			bas.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;

	
	}

}
