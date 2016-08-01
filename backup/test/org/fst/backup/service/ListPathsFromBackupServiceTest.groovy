package org.fst.backup.service

import static org.junit.Assert.*

import org.fst.backup.service.exception.NoBackupDirectoryException;
import org.fst.backup.test.AbstractFileSystemTest
import org.fst.backup.test.RDiffBackupHelper

class ListPathsFromBackupServiceTest extends AbstractFileSystemTest {

	RDiffBackupHelper helper =  new RDiffBackupHelper()

	ListPathsFromBackupService service = new ListPathsFromBackupService()

	void testNotExisitingBackupDir() {
		shouldFail (NoBackupDirectoryException) {service.retrieveAllPathsFromBackupDir(new File(targetPath), nowAsSecondsSinceTheEpoch())}
	}

	void testFirstPathIsCurrentDir1() {
		String[] paths = createBackupAndRetrievePaths()
		assert 3 == paths.length
		assert '.' == paths[0]
	}

	void testFirstPathIsCurrentDir2() {
		helper.createEmptyBackup(sourePath, targetPath)

		Calendar c = Calendar.getInstance()
		c.set(Calendar.MILLISECOND, 0)

		String[] paths = service.retrieveAllPathsFromBackupDir(new File(targetPath), nowAsSecondsSinceTheEpoch())
		assert '.' == paths[0]
	}

	void testAllPathsAreRetrieved() {
		String[] paths = createBackupAndRetrievePaths()

		assert helper.file1.getName() == paths[1]
		assert helper.file2.getName() == paths[2]
	}

	void testPathsAreAlwaysRelative() {
		String[] paths = createBackupAndRetrievePaths()
		assert paths.every { !it.contains(targetPath) }
	}

	void testPathsAreRetrievedByIncrementDate() {
		helper.createTwoIncrements(sourePath, targetPath)
		ListIncrementsService listIncrementsService = new ListIncrementsService()
		List<String> increments = listIncrementsService.listIncrements(new File(targetPath))
		IncrementDateService incrementDateService = new IncrementDateService()
		long timeOfFirstBackup = incrementDateService.secondsSinceTheEpoch(incrementDateService.extractDate(increments.getAt(0)))

		String[] paths = service.retrieveAllPathsFromBackupDir(new File(targetPath), timeOfFirstBackup)
		assert 2 == paths.length
		assert '.' == paths[0]
		assert helper.file1.getName() == paths[1]

		long timeOfSecondBackup = incrementDateService.secondsSinceTheEpoch(incrementDateService.extractDate(increments.getAt(1)))
		paths = service.retrieveAllPathsFromBackupDir(new File(targetPath), timeOfSecondBackup)
		assert 3 == paths.length
		assert '.' == paths[0]
		assert helper.file1.getName() == paths[1]
		assert helper.file2.getName() == paths[2]
	}

	private String[] createBackupAndRetrievePaths() {
		helper.createTwoIncrements(sourePath, targetPath)
		String[] paths = service.retrieveAllPathsFromBackupDir(new File(targetPath), nowAsSecondsSinceTheEpoch())
		return paths
	}

	private long nowAsSecondsSinceTheEpoch() {
		Calendar c = Calendar.getInstance()
		c.set(Calendar.MILLISECOND, 0)
		return c.timeInMillis / 1000
	}
}
