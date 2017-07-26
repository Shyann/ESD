/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xyz.esd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author me-aydin
 */
public class JDBCConnectionHandler {

	private String pass = "";
	private String user = "root";
	private String url = "jdbc:mysql://localhost:3306/XYZ_Assoc";
	Connection connection = null;
	Statement statement = null;

	public JDBCConnectionHandler() {
	}

	private void createConnection(String dbUrl, String user, String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(dbUrl, user, pass);
		} catch (SQLException | ClassNotFoundException ex) {
			Logger.getLogger(JDBCConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public boolean executeQuery(String query) {
		createConnection(url, user, pass);
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			ps.close();
			closeAll();
			return true;
		} catch (SQLException ex) {
			Logger.getLogger(JDBCConnectionHandler.class.getName()).log(Level.SEVERE, "Error executing query: " + query, ex);
		}
		closeAll();
		return false;
	}

	public ArrayList<HashMap<String, Object>> executeSELECT(String initialStatement, ArrayList<Object> parameters){
		createConnection(url, user, pass);
		try {
			PreparedStatement statement = connection.prepareStatement(initialStatement);
			int j = 1;
			for(Object parameter : parameters){
				if(parameter instanceof String){
					statement.setString(j++, (String)parameter);
				}else if(parameter instanceof Integer){
					statement.setInt(j++, (Integer)parameter);
				}else{
					System.out.println("COULD NOT PROCESS");
				}
			}
			final ResultSet rs = statement.executeQuery();
			if (rs == null) {
				closeAll();
				return null;
			}

			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			try {
				ResultSetMetaData md = rs.getMetaData();
				int columns = md.getColumnCount();
				while (rs.next()) {
					HashMap<String, Object> row = new HashMap<String, Object>(columns);
					for (int i = 1; i <= columns; ++i) {
						row.put(md.getColumnName(i), rs.getObject(i));
					}
					list.add(row);
				}
				rs.close();
			} catch (SQLException ex) {
				Logger.getLogger(JDBCConnectionHandler.class.getName()).log(Level.SEVERE, "Error creating ArrayListHashMap", ex);
			}

			closeAll();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<HashMap<String, Object>> resultSetToHashMapList(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		while (rs.next()) {
			HashMap<String, Object> row = new HashMap<String, Object>(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
		}
		return list;
	}

	private ResultSet select(String query) {
		try {
			statement = connection.createStatement();
			final ResultSet rs = statement.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			System.out.println("way way" + e);
			// results = e.toString();
		}
		return null;
	}

	public ArrayList<HashMap<String, Object>> queryToHashMapList(String query) {
		createConnection(url, user, pass);
		ResultSet rs = select(query);
		if (rs == null) {
			closeAll();
			return null;
		}

		ArrayList<HashMap<String, Object>> list = null;

		try {
			list = resultSetToHashMapList(rs);
			rs.close();
		} catch (SQLException ex) {
			Logger.getLogger(JDBCConnectionHandler.class.getName()).log(Level.SEVERE, "Error creating ArrayListHashMap", ex);
		}

		closeAll();
		return list;
	}

	public void closeAll() {
		try {
			if (statement != null) {
				statement.close();
			}
			
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}
