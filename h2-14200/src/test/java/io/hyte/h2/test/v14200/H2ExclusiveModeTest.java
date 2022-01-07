package io.hyte.h2.test.v14200;

import java.sql.SQLException;
import org.junit.Test;
import io.hyte.h2.test.BaseH2ExclusiveModeTest;

public class H2ExclusiveModeTest extends BaseH2ExclusiveModeTest {

	
	@Override
	@Test(expected = org.h2.jdbc.JdbcSQLNonTransientConnectionException.class)
	public void testSetExclusiveGetExclusiveFails14200() throws SQLException {
		super.testSetExclusiveGetExclusiveFails14200();
	}
}
