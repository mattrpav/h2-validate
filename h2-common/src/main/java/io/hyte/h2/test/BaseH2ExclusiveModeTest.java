package io.hyte.h2.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;


public abstract class BaseH2ExclusiveModeTest {
	
	protected static final List<Integer> EXCLUSIVE_MODES = Arrays.asList(0, 1, 2);
	
	@Test
	public void testSetExclusiveGetExclusive() throws SQLException {

		Connection connection = DriverManager.getConnection("jdbc:h2:file:./target/exclusivedb", "sa", "");
		
		assertFalse(getExclusiveMode(connection));
		
		setExclusiveMode(connection, 1);
		assertTrue(getExclusiveMode(connection));
		
		setExclusiveMode(connection, 0);
		assertFalse(getExclusiveMode(connection));
		
		setExclusiveMode(connection, 2);
		assertTrue(getExclusiveMode(connection));
		
		setExclusiveMode(connection, 0);
		assertFalse(getExclusiveMode(connection));
	}
	
	@Test
	public void testSetExclusiveGetExclusiveFails14200() throws SQLException {

		Connection connection = DriverManager.getConnection("jdbc:h2:file:./target/exclusivedb", "sa", "");
		
		assertFalse(getExclusiveMode(connection));
		
		setExclusiveMode(connection, 1);
		assertTrue(getExclusiveMode(connection));
		
		setExclusiveMode(connection, 0);
		assertFalse(getExclusiveMode(connection));
		
		setExclusiveMode(connection, 1);
		assertTrue(getExclusiveMode(connection));
		
		// Setting to existing mode throws exception
		setExclusiveMode(connection, 2);
		assertTrue(getExclusiveMode(connection));
		
		setExclusiveMode(connection, 0);
		assertFalse(getExclusiveMode(connection));
	}
	
	
	protected void setExclusiveMode(Connection connection, int exclusiveMode) throws SQLException {
		if (!EXCLUSIVE_MODES.contains(exclusiveMode)) {
			throw new IllegalArgumentException("Unsupported exclusive mode: " + exclusiveMode);
		}
		
		String sql = "SET EXCLUSIVE " + exclusiveMode;
		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.execute();
		}
	}
	
	protected boolean getExclusiveMode(Connection connection) throws SQLException{
		boolean exclusiveMode = false;
		
		String sql = "SELECT VALUE FROM INFORMATION_SCHEMA.Settings WHERE NAME = 'EXCLUSIVE'";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				exclusiveMode = result.getBoolean("VALUE");
			}
		}
		
		return exclusiveMode;
	}
}
