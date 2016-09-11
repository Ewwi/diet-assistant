package com.ew.dietassistant.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ew.dietassistant.entity.DailyMeal;
import com.ew.dietassistant.entity.Food;

public class DatabaseDailyMealTest
{
	@Before
	public void beforeEachMethod()
	{
		DatabaseDailyMeal database = new DatabaseDailyMeal(null, "testDatabase.db");
		database.dropTable();
	}


	@Test
	public void testInsertSelectAll()
	{
		// given
		Food food = new Food(1, "jajko", 60, 100, 90, 80, 70);
		DatabaseFood databaseFoodStub = Mockito.mock(DatabaseFood.class);
		Mockito.when(databaseFoodStub.selectFoodById(1)).thenReturn(food);

		DatabaseDailyMeal database = new DatabaseDailyMeal(databaseFoodStub, "testDatabase.db");
		LocalDate expectedDate = LocalDate.of(2016, 12, 24);
		DailyMeal expectedDailyMeal = new DailyMeal(2, 3, expectedDate, food, 100);

		// when
		database.insertDailyMeal(expectedDailyMeal);
		DailyMeal actualDailyMeal = database.selectAllDailyMeals().get(0);

		// then
		assertEquals(expectedDailyMeal.getDateOfDailyMeal(), actualDailyMeal.getDateOfDailyMeal());
		assertEquals(expectedDailyMeal.getFood(), actualDailyMeal.getFood());
		assertEquals(expectedDailyMeal.getIdOfPerson(), actualDailyMeal.getIdOfPerson());
	}

	@Test
	public void testInsertSelectById()
	{
		// given
		Food food = new Food(1, "jajko", 60, 100, 90, 80, 70);
		DatabaseFood databaseFoodStub = Mockito.mock(DatabaseFood.class);
		Mockito.when(databaseFoodStub.selectFoodById(1)).thenReturn(food);

		int id = 1;
		DatabaseDailyMeal database = new DatabaseDailyMeal(databaseFoodStub, "testDatabase.db");
		LocalDate expectedDate = LocalDate.of(2016, 12, 24);
		DailyMeal expectedDailyMeal = new DailyMeal(2, 3, expectedDate, food, 100);

		// when
		database.insertDailyMeal(expectedDailyMeal);
		DailyMeal actualDailyMeal = database.selectDailyMealById(id);

		// then
		assertEquals(expectedDailyMeal.getDateOfDailyMeal(), actualDailyMeal.getDateOfDailyMeal());
		assertEquals(expectedDailyMeal.getFood(), actualDailyMeal.getFood());
		assertEquals(expectedDailyMeal.getIdOfPerson(), actualDailyMeal.getIdOfPerson());
	}

	@Test
	public void testDatabaseIsEmpty()
	{
		// given
		Food food = new Food(1, "jajko", 60, 100, 90, 80, 70);
		DatabaseFood databaseFoodStub = Mockito.mock(DatabaseFood.class);
		Mockito.when(databaseFoodStub.selectFoodById(1)).thenReturn(food);

		DatabaseDailyMeal database = new DatabaseDailyMeal(databaseFoodStub, "testDatabase.db");

		// when
		List<DailyMeal> foodInDatabase = database.selectAllDailyMeals();

		// then
		assertTrue(foodInDatabase.isEmpty());
	}

	@Test
	public void testDelete()
	{
		// given
		Food food = new Food(1, "jajko", 60, 100, 90, 80, 70);
		LocalDate dateOfDailyMeal = LocalDate.of(2016, 12, 24);
		DatabaseFood databaseFoodStub = Mockito.mock(DatabaseFood.class);
		Mockito.when(databaseFoodStub.selectFoodById(1)).thenReturn(food);

		DatabaseDailyMeal database = new DatabaseDailyMeal(databaseFoodStub, "testDatabase.db");
		assertTrue(database.selectAllDailyMeals().isEmpty());
		DailyMeal dailyMeal = new DailyMeal(1, dateOfDailyMeal, food, 11);

		// when
		int idOfDailyMeal = database.insertDailyMeal(dailyMeal);
		database.removeDailyMealById(idOfDailyMeal);
		List<DailyMeal> dailyMealsInDatabase = database.selectAllDailyMeals();

		// then
		assertTrue(dailyMealsInDatabase.isEmpty());
	}

	@Test
	public void testDeleteAllDailyMealsForPersonIdAndDate()
	{
		// given
		Food food = new Food(1, "jajko", 60, 100, 90, 80, 70);
		DatabaseFood databaseFoodStub = Mockito.mock(DatabaseFood.class);
		Mockito.when(databaseFoodStub.selectFoodById(1)).thenReturn(food);
		DatabaseDailyMeal database = new DatabaseDailyMeal(databaseFoodStub, "testDatabase.db");
		int expectedPersonId = 1;
		int unexpectedPersonId = 2;
		LocalDate expectedDate = LocalDate.of(2016, 12, 24);
		LocalDate unexpectedDate = LocalDate.of(2016, 11, 24);
		DailyMeal expectedDailyMeal = new DailyMeal(expectedPersonId, expectedDate, food, 100); // przejdzie
		DailyMeal expectedDailyMeal2 = new DailyMeal(expectedPersonId, expectedDate, food, 100); // przejdzie
		DailyMeal expectedDailyMeal3 = new DailyMeal(unexpectedPersonId, expectedDate, food, 100);
		DailyMeal expectedDailyMeal4 = new DailyMeal(unexpectedPersonId, expectedDate, food, 100);
		DailyMeal expectedDailyMeal5 = new DailyMeal(expectedPersonId, unexpectedDate, food, 100);
		database.insertDailyMeal(expectedDailyMeal);
		database.insertDailyMeal(expectedDailyMeal2);
		database.insertDailyMeal(expectedDailyMeal3);
		database.insertDailyMeal(expectedDailyMeal4);
		database.insertDailyMeal(expectedDailyMeal5);

		// when
		database.removeAllDailyMealsForPersonIdAndDate(expectedPersonId, expectedDate);

		// then
		List<DailyMeal> itemsInDatabase = database.selectAllDailyMeals();
		assertEquals(3, itemsInDatabase.size());
	}

	@Test
	public void testSearchByPersonIdAndDate()
	{
		// given
		Food food = new Food(1, "jajko", 60, 100, 90, 80, 70);
		DatabaseFood databaseFoodStub = Mockito.mock(DatabaseFood.class);
		Mockito.when(databaseFoodStub.selectFoodById(1)).thenReturn(food);
		DatabaseDailyMeal database = new DatabaseDailyMeal(databaseFoodStub, "testDatabase.db");
		LocalDate expectedDate = LocalDate.of(2016, 12, 24);
		int personId = 3;
		DailyMeal expectedDailyMeal = new DailyMeal(1, personId, expectedDate, food, 100);

		// when
		int idOfInsertedDailyMeal = database.insertDailyMeal(expectedDailyMeal);
		expectedDailyMeal.setId(idOfInsertedDailyMeal);

		// then
		List<DailyMeal> actualDailyMealList = database.selectAllDailyMealsByPersonIdAndDate(personId, expectedDate);
		assertEquals(1, actualDailyMealList.size());
		assertTrue(actualDailyMealList.contains(expectedDailyMeal));
	}

	@Test
	public void testSearchByPersonIdAndDateMultiple()
	{
		// given
		Food food = new Food(1, "jajko", 60, 100, 90, 80, 70);
		DatabaseFood databaseFoodStub = Mockito.mock(DatabaseFood.class);
		Mockito.when(databaseFoodStub.selectFoodById(1)).thenReturn(food);
		DatabaseDailyMeal database = new DatabaseDailyMeal(databaseFoodStub,"testDatabase.db");
		LocalDate expectedDate = LocalDate.of(2016, 12, 24);
		LocalDate expectedDate2 = LocalDate.of(2016, 11, 24);
		int personId = 3;
		DailyMeal expectedDailyMeal = new DailyMeal(personId, expectedDate, food, 100);
		DailyMeal expectedDailyMeal2 = new DailyMeal(personId, expectedDate, food, 100);
		DailyMeal expectedDailyMeal3 = new DailyMeal(personId, expectedDate2, food, 100);

		// when
		database.insertDailyMeal(expectedDailyMeal);
		database.insertDailyMeal(expectedDailyMeal2);
		database.insertDailyMeal(expectedDailyMeal3);

		// then
		List<DailyMeal> databaseDailyMealList = database.selectAllDailyMealsByPersonIdAndDate(personId, expectedDate);
		assertEquals(2, databaseDailyMealList.size());
	}
}
