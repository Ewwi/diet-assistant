package com.ew.dietassistant.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ew.dietassistant.entity.History;

public class DatabaseHistoryTest
{
	@Before
	public void beforeEachMethod()
	{
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");
		database.dropTable();
	}


	@Test
	public void testInsert()
	{
		// given
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");

		LocalDate expectedDate = LocalDate.of(2016, 12, 24);

		int somePersonId = 1;
		History history = new History(expectedDate, somePersonId);

		// when
		database.insertHistory(history);
		List<History> actualList = database.selectAllHistory();
		History historyActualDate = actualList.get(0);
		LocalDate actualDate = historyActualDate.getDate();

		// then
		assertEquals(expectedDate, actualDate);
		assertEquals(actualList.size(), 1);
	}

	@Test
	public void testDatabaseIsEmpty()
	{
		// given
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");

		// when
		List<History> historyInDatabase = database.selectAllHistory();

		// then
		assertTrue(historyInDatabase.isEmpty());
	}

	@Test
	public void testSelectAllHistoryForGivenPerson()
	{
		// given
		int id = 1;
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");
		LocalDate expectedDate = LocalDate.of(2016, 12, 24);
		History expected = new History(id, expectedDate, 1);
		expected.setDate(expectedDate);

		// when
		database.insertHistory(expected);
		List<History> actualHistory = database.selectAllHistoryForPersonId(1);

		// then
		for (History history : actualHistory)
		{
			assertEquals(expected.getDate(), history.getDate());
			assertEquals(expected.getPersonId(), history.getPersonId());
		}
	}

	@Test
	public void testInsertingAccordionCellMultiples()
	{
		// given
		int personId1 = 1;
		int personId2 = 2;
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");
		LocalDate expectedDate = LocalDate.of(2016, 12, 24);
		History testItem1 = new History(1, expectedDate, personId1);
		History testItem2 = new History(2, expectedDate, personId2);

		// when
		database.insertHistory(testItem1);
		database.insertHistory(testItem2);
		List<History> historyForPerson1 = database.selectAllHistoryForPersonId(1);
		List<History> historyForPerson2 = database.selectAllHistoryForPersonId(2);

		// then
		for (History history : historyForPerson1)
		{
			assertEquals(testItem1.getDate(), history.getDate());
			assertEquals(testItem1.getPersonId(), history.getPersonId());

		}
		for (History history : historyForPerson2)
		{
			assertEquals(testItem2.getDate(), history.getDate());
			assertEquals(testItem2.getPersonId(), history.getPersonId());
		}
	}

	@Test(expected=RuntimeException.class)
	public void testPairContraint()
	{
		// given
		int personId1 = 1;
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");
		LocalDate expectedDate = LocalDate.of(2016, 12, 24);
		History testItem1 = new History(1, expectedDate, personId1);
		History testItem2 = new History(2, expectedDate, personId1);

		// when
		database.insertHistory(testItem1);
		database.insertHistory(testItem2);

		// then
		// expected exception
	}

	@Test
	public void testDeleteHistoryByDateAndPersonId()
	{
		// given
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");
		LocalDate dateToRemove = LocalDate.of(2016, 12, 24);
		LocalDate dateToSave = LocalDate.of(2016, 11, 24);
		History historyToRemove1 = new History(1, dateToRemove, 3);
		History historyToRemove2 = new History(3, dateToRemove, 4);
		History historyToSave = new History(2, dateToSave, 3);

		// when
		database.insertHistory(historyToRemove1);
		database.insertHistory(historyToRemove2);
		int historyToSaveId = database.insertHistory(historyToSave);
		historyToSave.setId(historyToSaveId);

		database.removeHistoryByDateAndPersonId(dateToRemove, 3);
		database.removeHistoryByDateAndPersonId(dateToRemove, 4);

		// then
		List<History> history = database.selectAllHistory();
		assertEquals(historyToSave, history.get(0));
	}

	@Test
	public void testDeleteAllHistoryForThisPerson()
	{
		// given
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");
		int expectedPersonId = 1;
		int unexpectedPersonId = 2;
		LocalDate date1 = LocalDate.of(2016, 12, 24);
		LocalDate date2 = LocalDate.of(2016, 11, 24);
		LocalDate date3 = LocalDate.of(2016, 11, 23);
		History expectedHistory = new History(1, date1, expectedPersonId); // przejdzie
		History expectedHistory1 = new History(2, date2, expectedPersonId); // przejdzie
		History expectedHistory2 = new History(3, date3, expectedPersonId); // przejdzie
		History unexpectedHistory = new History(4, date3, unexpectedPersonId);
		database.insertHistory(expectedHistory);
		database.insertHistory(expectedHistory2);
		database.insertHistory(expectedHistory1);
		database.insertHistory(unexpectedHistory);

		// when
		database.removeAllHistoryForThisPersonId(expectedPersonId);

		// then
		List<History> itemsInDatabase = database.selectAllHistory();
		assertEquals(1, itemsInDatabase.size());
	}

	@Test
	public void testRemoveHistoryById()
	{
		// given
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");
		int expectedHistoryId = 1;
		LocalDate date = LocalDate.of(2016, 12, 24);
		History history = new History(expectedHistoryId, date, 1); // przejdzie

		// when
		database.insertHistory(history);
		database.removeHistoryById(history.getId());

		// then
		List<History> historyInDatabase = database.selectAllHistory();
		assertTrue(historyInDatabase.isEmpty());
	}

	@Test
	public void testSelectHistoryById()
	{
		// given
		DatabaseHistory database = new DatabaseHistory("testDatabase.db");
		LocalDate date = LocalDate.of(2016, 12, 24);
		History expected = new History(date, 1); // przejdzie
		int historyId = database.insertHistory(expected);
		expected.setId(historyId);

		// when
		History actual = database.selectHistoryById(historyId);

		// then
		assertEquals(expected, actual);
	}
}
