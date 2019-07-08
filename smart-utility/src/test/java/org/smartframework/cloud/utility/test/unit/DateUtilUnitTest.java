package org.smartframework.cloud.utility.test.unit;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.DateUtil;

import junit.framework.TestCase;

public class DateUtilUnitTest extends TestCase {

	public void testNow() {
		Assertions.assertThat(DateUtil.now()).isNotNull();
	}

	public void testGetCurrentDate() {
		String currentDateStr = DateUtil.getCurrentDate();
		Assertions.assertThat(currentDateStr).isNotBlank();
		Assertions.assertThat(currentDateStr.length()).isEqualTo(DateUtil.FOROMAT_DATE.length());
	}

	public void testGetCurrentDateTime() {
		String currentDateStr = DateUtil.getCurrentDateTime();
		Assertions.assertThat(currentDateStr).isNotBlank();
		Assertions.assertThat(currentDateStr.length()).isEqualTo(DateUtil.FOROMAT_DATETIME.length());
	}

	public void testGetCurrentDateTimeWithFormat() {
		Assertions.assertThat(DateUtil.getCurrentDateTime(DateUtil.FOROMAT_DATE_YY_MM).length())
				.isEqualTo(DateUtil.FOROMAT_DATE_YY_MM.length());
		Assertions.assertThat(DateUtil.getCurrentDateTime(DateUtil.FOROMAT_DATE).length())
				.isEqualTo(DateUtil.FOROMAT_DATE.length());
		Assertions.assertThat(DateUtil.getCurrentDateTime(DateUtil.FOROMAT_DATETIME).length())
				.isEqualTo(DateUtil.FOROMAT_DATETIME.length());
		Assertions.assertThat(DateUtil.getCurrentDateTime(DateUtil.FOROMAT_DATETIME_HH_MM_SS_SSS).length())
				.isEqualTo(DateUtil.FOROMAT_DATETIME_HH_MM_SS_SSS.length());
		Assertions.assertThat(DateUtil.getCurrentDateTime(DateUtil.FOROMAT_DATETIME_HH).length())
				.isEqualTo(DateUtil.FOROMAT_DATETIME_HH.length());
		Assertions.assertThat(DateUtil.getCurrentDateTime(DateUtil.FOROMAT_DATETIME_HH_MM).length())
				.isEqualTo(DateUtil.FOROMAT_DATETIME_HH_MM.length());
	}

	public void testFormat() {
		Assertions.assertThat(DateUtil.format(new Date(), DateUtil.FOROMAT_DATE_YY_MM).length())
				.isEqualTo(DateUtil.FOROMAT_DATE_YY_MM.length());
	}

	public void testFormatDate() {
		Assertions.assertThat(DateUtil.formatDate(new Date()).length()).isEqualTo(DateUtil.FOROMAT_DATE.length());
	}

	public void testFormatDateTime() {
		Assertions.assertThat(DateUtil.formatDateTime(new Date()).length()).isEqualTo(DateUtil.FOROMAT_DATETIME.length());
	}

	public void testToDate() {
		Assertions.assertThat(DateUtil.toDate("2019-01")).isNotNull();
		Assertions.assertThat(DateUtil.toDate("2019-01-01")).isNotNull();
		Assertions.assertThat(DateUtil.toDate("2019-01-01 11:11:11")).isNotNull();
		Assertions.assertThat(DateUtil.toDate("2019-01-01 11:11:11.111")).isNotNull();
		Assertions.assertThat(DateUtil.toDate("2019-01-01 11")).isNotNull();
		Assertions.assertThat(DateUtil.toDate("2019-01-01 11:11")).isNotNull();
	}

}