package org.fst.backup.service

import org.junit.Assert;

class IncrementDateExtractorService {
	
	Date extractDate(String increment) {
		String trimmed = increment.trim()
		if (trimmed.endsWith('directory')) {
			String[] secondsAndDirectory = increment.trim().split()
			String secondsOfEpoch = secondsAndDirectory[0]
			long millisOfEpoch = secondsOfEpoch.toLong() * 1000
			return new Date(millisOfEpoch)
			
		} else {
			throw new InvalidIncrementException()
		}
		
	}

}
