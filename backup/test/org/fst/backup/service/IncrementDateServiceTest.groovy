package org.fst.backup.service

import static org.junit.Assert.*

import org.fst.backup.service.exception.InvalidIncrementDateException;
import org.fst.backup.service.exception.InvalidIncrementException;

class IncrementDateServiceTest extends GroovyTestCase {

	IncrementDateService service = new IncrementDateService()

	void testNonParsableIncrement1() {
		shouldFail(InvalidIncrementException) { service.extractDate('Current mirror: Tue Jul 05 21:53:46 2016') }
	}

	void testNonParsableIncrement2() {
		shouldFail(InvalidIncrementException) { service.extractDate('Found 0 increments:') }
	}

	void testNonParsableIncrement3() {
		shouldFail(InvalidIncrementException) { service.extractDate('increments.2016-07-04T23:25:22+02:00.dir   Mon Jul 04 23:25:22 2016') }
	}

	void testDateIsExtractedFromParsableIncrement() {
		Date actualDate = service.extractDate('1467750198 directory')
		Date expectedDate = new Date()
		expectedDate.clearTime()
		expectedDate.set(year:2016, month:6, dayOfMonth: 5, hourOfDay:22, minute:23, second:18)
		assert actualDate.equals(expectedDate)
	}

	void testExtractedDateHasNoMillis() {
		Date actualDate = service.extractDate('1467750198 directory')
		assert 0 == actualDate.getAt(Calendar.MILLISECOND)
	}

	void testSecondsAreReturnedFromValidIncrementDate() {
		Date expectedDate = new Date()
		expectedDate.clearTime()
		expectedDate.set(year:2016, month:6, dayOfMonth: 5, hourOfDay:22, minute:23, second:18)
		assert 1467750198 == service.secondsSinceTheEpoch(expectedDate)
	}

	void testInvalidIncrementDate() {
		Calendar c = Calendar.getInstance()
		c.set(Calendar.MILLISECOND, 1234)
		shouldFail(InvalidIncrementDateException) { service.secondsSinceTheEpoch(c.getTime()) }
	}
}


