package com.ew.dietassistant.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ew.dietassistant.entity.Food;

public class DatabaseFoodTest
{
	@Before
	public void beforeEachMethod()
	{
		DatabaseFood database = new DatabaseFood("testDatabase.db");
		database.dropTable();
	}


	@Test
	public void testInsertSelectAll()
	{
		// given
		DatabaseFood database = new DatabaseFood("testDatabase.db");
		Food expected = new Food(1, "jajko", 60, 100, 90, 80, 70);

		// when
		database.insertFood(expected);
		Food actual = database.selectAllFoods().get(0);

		// then
		assertFoodsEqual(expected, actual);
	}

	private void assertFoodsEqual(Food expected, Food actual)
	{
		assertEquals(expected.getIdOfProduct(), actual.getIdOfProduct());
		assertEquals(expected.getProductName().toLowerCase(), actual.getProductName().toLowerCase());
		assertEquals(expected.getQuantity(), actual.getQuantity());
		assertEquals(expected.getCaloriesOfProduct(), actual.getCaloriesOfProduct());
		assertEquals(expected.getFatOfProduct(), actual.getFatOfProduct(), 0.001);
		assertEquals(expected.getProteinOfProduct(), actual.getProteinOfProduct(), 0.001);
		assertEquals(expected.getCarboOfProduct(), actual.getCarboOfProduct(), 0.001);
	}

	@Test
	public void testInsertSelectById()
	{
		// given
		int id = 1;
		DatabaseFood database = new DatabaseFood("testDatabase.db");
		Food expected = new Food(id, "jajko", 60, 100, 90, 80, 70);

		// when
		database.insertFood(expected);
		Food actual = database.selectFoodById(id);

		// then
		assertFoodsEqual(expected, actual);
	}

	@Test
	public void testSelectByName()
	{
		// given
		int id = 1;
		DatabaseFood database = new DatabaseFood("testDatabase.db");
		Food expected = new Food(id, "Jajko", 60, 100, 90, 80, 70);

		// when
		database.insertFood(expected);
		Food actual = database.selectFoodByName("jajko");

		// then
		assertFoodsEqual(expected, actual);
	}

	@Test
	public void testDelete()
	{
		// given
		DatabaseFood database = new DatabaseFood("testDatabase.db");
		Food food = new Food(1, "Jajko", 60, 100, 90, 80, 70);

		// when
		database.insertFood(food);
		database.removeFood(food);

		// then
		List<Food> foodInDatabase = database.selectAllFoods();
		assertTrue(foodInDatabase.isEmpty());
	}
}
