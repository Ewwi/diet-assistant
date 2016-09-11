package com.ew.dietassistant.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ew.dietassistant.entity.Food;

public class DatabaseFood
{
	private static final Logger logger = LogManager.getLogger(DatabaseFood.class);

	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite";

	private Connection connection;
	private Statement statement;
	private String databaseFileName;


	DatabaseFood(String databaseFileName)
	{
		this.databaseFileName = databaseFileName;
		try
		{
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}

		createTableIfNotExists();
	}


	private void openConnection()
	{
		try
		{
			connection = DriverManager.getConnection(DB_URL + ":" + databaseFileName);
			statement = connection.createStatement();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void closeConnection()
	{
		try
		{
			statement.close();
		} catch (SQLException e1)
		{
			throw new RuntimeException(e1);
		}
		try
		{
			connection.close();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void createTableIfNotExists()
	{
		openConnection();

		// @formatter:off
		String createTable = "CREATE TABLE IF NOT EXISTS Food "
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "foodName varchar(255),"
				+ "grams INTEGER,"
				+ "caloriesOfFood INTEGER,"
				+ "fatOfFood DOUBLE,"
				+ "proteinOfFood DOUBLE,"
				+ "carboOfFood DOUBLE"
				+ "UNIQUE foodName);";
		// @formatter:on

		try
		{
			statement.execute(createTable);

			logger.info("Table Food created!");
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}

		closeConnection();
	}
	public void dropTable()
	{
		openConnection();
		try
		{
			statement.execute("DROP TABLE Food");

			logger.info("Table Food dropped!");

		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
	}
	public void insertFood(Food food)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into Food values (NULL, ?, ?, ?, ?, ?, ?);");
			preparedStatement.setString(1, food.getProductName().toLowerCase());
			preparedStatement.setInt(2, food.getQuantity());
			preparedStatement.setInt(3, food.getCaloriesOfProduct());
			preparedStatement.setDouble(4, food.getFatOfProduct());
			preparedStatement.setDouble(5, food.getProteinOfProduct());
			preparedStatement.setDouble(6, food.getCarboOfProduct());
			preparedStatement.execute();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
	}
	public List<Food> selectAllFoods()
	{
		openConnection();
		List<Food> foodList = new LinkedList<Food>();
		try
		{
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Food");

			while (resultSet.next())
			{
				int id = resultSet.getInt("id");
				String nameOfProduct = resultSet.getString("foodName");
				int quantity = resultSet.getInt("grams");
				int caloriesOfProduct = resultSet.getInt("caloriesOfFood");
				double fatOfProduct = resultSet.getInt("fatOfFood");
				double proteinOfProduct = resultSet.getInt("proteinOfFood");
				double carboOfProduct = resultSet.getInt("carboOfFood");

				Food food = new Food(id, nameOfProduct, quantity, caloriesOfProduct, fatOfProduct, proteinOfProduct, carboOfProduct);

				foodList.add(food);
			}
			resultSet.close();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
		return foodList;
	}

	public Food selectFoodById(int foodId)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT *"
					+ " FROM Food"
					+ " WHERE id=?");
			preparedStatement.setInt(1, foodId);
			preparedStatement.execute();

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				try
				{
					int id = resultSet.getInt("id");
					String nameOfProduct = resultSet.getString("foodName");
					int quantity = resultSet.getInt("grams");
					int caloriesOfProduct = resultSet.getInt("caloriesOfFood");
					double fatOfProduct = resultSet.getInt("fatOfFood");
					double proteinOfProduct = resultSet.getInt("proteinOfFood");
					double carboOfProduct = resultSet.getInt("carboOfFood");

					Food food = new Food(id, nameOfProduct, quantity, caloriesOfProduct, fatOfProduct, proteinOfProduct, carboOfProduct);

					return food;
				}
				finally
				{
					resultSet.close();
				}
			}
			else
			{
				throw new RuntimeException("Food with id " + foodId + " not found");
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally {
			closeConnection();
		}
	}
	public Food selectFoodByName(String productName)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT *"
					+ " FROM Food"
					+ " WHERE foodName=?");
			preparedStatement.setString(1, productName.toLowerCase());
			preparedStatement.execute();

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				int id = resultSet.getInt("id");
				String nameOfProduct = resultSet.getString("foodName");
				int quantity = resultSet.getInt("grams");
				int caloriesOfProduct = resultSet.getInt("caloriesOfFood");
				double fatOfProduct = resultSet.getInt("fatOfFood");
				double proteinOfProduct = resultSet.getInt("proteinOfFood");
				double carboOfProduct = resultSet.getInt("carboOfFood");

				Food food = new Food(id, nameOfProduct, quantity, caloriesOfProduct, fatOfProduct, proteinOfProduct, carboOfProduct);

				return food;
			}
			resultSet.close();

			throw new RuntimeException("Food with name " + productName + " not found");
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally {
			closeConnection();
		}
	}
	public void removeFood(Food food)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM Food WHERE ID = ?;");
			preparedStatement.setInt(1, food.getIdOfProduct());
			preparedStatement.execute();

		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
	}

}

