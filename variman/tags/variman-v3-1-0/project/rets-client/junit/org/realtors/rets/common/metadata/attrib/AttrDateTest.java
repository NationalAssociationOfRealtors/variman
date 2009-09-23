package org.realtors.rets.common.metadata.attrib;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.AttrType;
import org.realtors.rets.common.util.RetsDateTime;

public class AttrDateTest extends AttrTypeTest {
	public void testAttrDate() throws Exception {
		AttrType parser = new AttrDate();

		assertEquals(parser.getType(), Date.class);
		RetsDateTime cal = new RetsDateTime();
		cal.clear();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.set(2003, Calendar.NOVEMBER, 5, 17, 50, 23);
		assertEquals(cal.getTime(), parser.parse("2003-11-5T17:50:23Z",true));
		assertEquals(cal.getTime(), parser.parse("2003-11-5T17:50:23.0Z", true));
		assertEquals(cal.getTime(), parser.parse("2003-11-5T17:50:23.0+00:00", true));
		assertEquals(cal.getTime(), parser.parse("2003-11-5T17:50:23.0-00:00", true));
		
		assertEquals(new Date(0), parser.parse("1970-1-1T00:00:00+00:00", true));
		assertEquals(new Date(0), parser.parse("1970-1-1T00:00:00-00:00", true));
		assertEquals(new Date(0), parser.parse("1970-1-1T00:00:00Z", true));
		assertEquals(new Date(0), parser.parse("1970-1-1T00:00:00.0+00:00", true));
		
		assertEquals(new Date(0), cal.parse("Thu, 1 Jan 1970 00:00:00 GMT", RetsVersion.RETS_1_5, true));

		assertEquals("wrong day of week but still should parse!", new Date(0), 
						cal.parse("Tue, 1 Jan 1970 00:00:00 GMT", RetsVersion.RETS_1_5, true));

		assertParseException(parser, "2/12/70");
		assertParseException(parser, "12/2/70");
		assertParseException(parser, "2003-1-1");
		// month and date backwards
		assertParseException(parser, "Thu, Jan 1 1970 00:00:00 GMT");
	}
}
