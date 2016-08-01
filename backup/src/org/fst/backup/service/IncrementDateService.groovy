package org.fst.backup.service

import org.fst.backup.service.exception.InvalidIncrementDateException;
import org.fst.backup.service.exception.InvalidIncrementException;


class IncrementDateService {

	Date extractDate(String increment) {
		String trimmed = increment.trim()
		if (trimmed.endsWith('directory')) {
			String[] secondsAndDirectory = increment.trim().split()
			String secondsSinceTheEpoch = secondsAndDirectory[0]
			long millisSinceTheEpoch = secondsSinceTheEpoch.toLong() * 1000
			return new Date(millisSinceTheEpoch)
		} else {
			throw new InvalidIncrementException()
		}
	}

	long secondsSinceTheEpoch(Date incrementDate) {
		if (0 == incrementDate.getAt(Calendar.MILLISECOND)) {
			return incrementDate.time / 1000
		} else {
			throw new InvalidIncrementDateException()
		}
	}
}
