package com.ew.dietassistant;

import com.ew.dietassistant.database.DatabaseFood;
import com.ew.dietassistant.database.DatabaseProvider;
import com.ew.dietassistant.entity.Food;

public class PopulatingDatabaseFood
{
	private static DatabaseFood databaseFood = DatabaseProvider.getDatabaseFood();

	public static void populateDatabaseFoodWithProducts()
	{
		//		Food(nazwa, kalorie, bia³ko, t³uszcz, wêgle, gramy)

		databaseFood.dropTable();
		databaseFood.createTableIfNotExists();

		Food boiledEgg = new Food("jajko gotowane", 155, 12.6, 10.6, 1.1, 100);
		databaseFood.insertFood(boiledEgg);

		Food whiteBread = new Food("bia³y chleb", 245, 8, 0.3, 52.7, 100);
		databaseFood.insertFood(whiteBread);

		Food butter = new Food("mas³o", 735, 0.7, 82.5, 0.7, 100);
		databaseFood.insertFood(butter);

		Food mayonaise = new Food("majonez", 663, 1, 71.8, 3.1, 100);
		databaseFood.insertFood(mayonaise);

		Food cookedChickenBreast = new Food("ugotowana pierœ z kurczaka", 151, 32.9, 2, 0, 100);
		databaseFood.insertFood(cookedChickenBreast);

		Food potato = new Food("ziemniak", 77, 1.9, 0.1, 18.3, 100);
		databaseFood.insertFood(potato);

	}
}
