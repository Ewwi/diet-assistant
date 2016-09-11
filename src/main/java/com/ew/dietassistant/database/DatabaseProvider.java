package com.ew.dietassistant.database;

public abstract class DatabaseProvider
{
	private static final String DATABASE_FILE_NAME = "biblioteka.db";

	private static final DatabaseFood databaseFood = new DatabaseFood(DATABASE_FILE_NAME);
	private static final DatabaseDailyMeal databaseDailyMeal = new DatabaseDailyMeal(databaseFood, DATABASE_FILE_NAME);
	private static final DatabaseHistory databaseHistory = new DatabaseHistory(DATABASE_FILE_NAME);
	private static final DatabasePerson databasePerson = new DatabasePerson(DATABASE_FILE_NAME);


	public static DatabaseFood getDatabaseFood()
	{
		return databaseFood;
	}

	public static DatabaseDailyMeal getDatabaseDailyMeal()
	{
		return databaseDailyMeal;
	}

	public static DatabaseHistory getDatabaseHistory()
	{
		return databaseHistory;
	}

	public static DatabasePerson getDatabasePerson()
	{
		return databasePerson;
	}
}
