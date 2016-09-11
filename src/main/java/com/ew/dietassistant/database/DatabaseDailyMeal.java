package com.ew.dietassistant.database;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ew.dietassistant.entity.DailyMeal;
import com.ew.dietassistant.entity.Food;

public class DatabaseDailyMeal
{
	private static final Logger logger = LogManager.getLogger(DatabaseDailyMeal.class);

	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite";

	private Connection connection;
	private Statement statement;
	private String databaseFileName;
	private DatabaseFood databaseFood;


	DatabaseDailyMeal(DatabaseFood databaseFood, String databaseFileName)
	{
		this.databaseFood = databaseFood;
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
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void closeConnection()
	{
		try
		{
			statement.close();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
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
		String createTable = "CREATE TABLE IF NOT EXISTS DailyMeal "
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "idOfPerson INTEGER,"
				+ "dateOfDailyMeal BIGINT,"
				+ "idOfFood INTEGER,"
				+ "grams INTEGER);";
		// @formatter:on

		try
		{
			statement.execute(createTable);

			logger.info("Table DailyMeal created!");
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
			statement.execute("DROP TABLE DailyMeal");

			logger.info("Table DailyMeal dropped!");

		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
	}
	public int insertDailyMeal(DailyMeal dailyMeal)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into DailyMeal values (NULL, ?, ?, ?, ?);", RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, dailyMeal.getIdOfPerson());
			preparedStatement.setLong(2, dailyMeal.getDateOfDailyMeal().toEpochDay());
			preparedStatement.setInt(3, dailyMeal.getFood().getIdOfProduct());
			preparedStatement.setInt(4, dailyMeal.getGrams());
			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating dailymeal failed, no rows affected.");
			}

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return (int) generatedKeys.getLong(1);
				}
				else {
					throw new SQLException("Creating dailymeal failed, no ID obtained.");
				}
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			closeConnection();
		}
	}
	public List<DailyMeal> selectAllDailyMeals()
	{
		openConnection();
		List<DailyMeal> list = new LinkedList<DailyMeal>();
		try
		{
			ResultSet resultSet = statement.executeQuery("SELECT * FROM DailyMeal");

			while (resultSet.next())
			{
				int id = resultSet.getInt("id");
				int idOfPerson = resultSet.getInt("idOfPerson");
				Long epoch = resultSet.getLong("dateOfDailyMeal");
				LocalDate dateOfDailyMeal = LocalDate.ofEpochDay(epoch);
				int idOfFood = resultSet.getInt("idOfFood");
				int grams = resultSet.getInt("grams");

				Food food = databaseFood.selectFoodById(idOfFood);

				DailyMeal dailymeal = new DailyMeal(id, idOfPerson, dateOfDailyMeal, food, grams);

				list.add(dailymeal);
			}
			resultSet.close();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
		return list;
	}

	public DailyMeal selectDailyMealById(int productId)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT *"
					+ " FROM DailyMeal"
					+ " WHERE id=?");
			preparedStatement.setInt(1, productId);
			preparedStatement.execute();

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				try
				{
					int id = resultSet.getInt("id");
					int idOfPerson = resultSet.getInt("idOfPerson");
					Long epoch = resultSet.getLong("dateOfDailyMeal");
					LocalDate dateOfDailyMeal = LocalDate.ofEpochDay(epoch);
					int idOfFood = resultSet.getInt("idOfFood");
					int grams = resultSet.getInt("grams");

					Food food = databaseFood.selectFoodById(idOfFood);

					DailyMeal dailyMeal = new DailyMeal(id, idOfPerson, dateOfDailyMeal, food, grams);

					return dailyMeal;
				}
				finally
				{
					resultSet.close();
				}
			}
			else
			{
				throw new RuntimeException("Item with id " + productId + " not found");
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
	public List<DailyMeal> selectAllDailyMealsByPersonIdAndDate(int personId, LocalDate date)
	{
		openConnection();
		List<DailyMeal> list = new LinkedList<DailyMeal>();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT *"
					+ " FROM DailyMeal"
					+ " WHERE idOfPerson = ? AND dateOfDailyMeal = ?;");
			preparedStatement.setInt(1, personId);
			preparedStatement.setLong(2, date.toEpochDay());
			preparedStatement.execute();

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				int id = resultSet.getInt("id");
				int idOfPerson = resultSet.getInt("idOfPerson");
				Long epoch = resultSet.getLong("dateOfDailyMeal");
				LocalDate dateOfDailyMeal = LocalDate.ofEpochDay(epoch);
				int idOfFood = resultSet.getInt("idOfFood");
				int grams = resultSet.getInt("grams");

				Food food = databaseFood.selectFoodById(idOfFood);

				DailyMeal dailyMeal = new DailyMeal(id, idOfPerson, dateOfDailyMeal, food, grams);
				list.add(dailyMeal);
			}
			resultSet.close();

			return list;
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally {
			closeConnection();
		}
	}
	public void removeDailyMealById(int id)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM DailyMeal WHERE id = ?;");
			preparedStatement.setInt(1, id);
			preparedStatement.execute();

		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			closeConnection();
		}
	}
	public void removeAllDailyMealsForPersonIdAndDate(int personId, LocalDate date)
	{
		PreparedStatement preparedStatement = null;
		openConnection();
		try
		{
			preparedStatement = connection
					.prepareStatement("DELETE FROM DailyMeal WHERE idOfPerson = ? AND dateOfDailyMeal = ?;");
			preparedStatement.setInt(1, personId);
			preparedStatement.setLong(2, date.toEpochDay());
			preparedStatement.execute();
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
			closeConnection();
		}
	}
}
