package io.hyte.h2.test.v20206;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.hyte.h2.test.BaseH2ExclusiveModeTest;

public class H2ExclusiveModeTest extends BaseH2ExclusiveModeTest {

	@Override
	protected void setExclusiveMode(Connection connection, int exclusiveMode) throws SQLException {
		if (!EXCLUSIVE_MODES.contains(exclusiveMode)) {
			throw new IllegalArgumentException("Unsupported exclusive mode: " + exclusiveMode);
		}
		
		String sql = "SET EXCLUSIVE " + exclusiveMode;
		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.execute();
		}
	}
	
	@Override
	protected boolean getExclusiveMode(Connection connection) throws SQLException{
		boolean exclusiveMode = false;
		
		String sql = "SELECT SETTING_VALUE FROM INFORMATION_SCHEMA.Settings WHERE SETTING_NAME = 'EXCLUSIVE'";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				exclusiveMode = result.getBoolean("SETTING_VALUE");
			}
		}
		
		return exclusiveMode;
	}
}
