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

import com.ew.dietassistant.entity.person.Activity;
import com.ew.dietassistant.entity.person.Gender;
import com.ew.dietassistant.entity.person.Person;

public class DatabasePerson
{
	private static final Logger logger = LogManager.getLogger(DatabasePerson.class);

	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite";

	private Connection connection;
	private Statement statement;
	private String databaseFileName;


	DatabasePerson(String databaseFileName)
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
		String createTable = "CREATE TABLE IF NOT EXISTS Person "
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "name varchar(255),"
				+ "gender varchar(7),"
				+ "age INTEGER,"
				+ "height INTEGER,"
				+ "weight INTEGER,"
				+ "activity varchar(20));";
		// @formatter:on

		try
		{
			statement.execute(createTable);

			logger.info("Table Person created!");
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
			statement.execute("DROP TABLE Person");

			logger.info("Table Person dropped!");

		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
	}
	public void insertPerson(Person person)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into Person values (NULL, ?, ?, ?, ?, ?, ?);");
			preparedStatement.setString(1, person.getName());
			preparedStatement.setString(2, person.getGender().name());
			preparedStatement.setInt(3, person.getAge());
			preparedStatement.setInt(4, person.getHeight());
			preparedStatement.setInt(5, person.getWeight());
			preparedStatement.setString(6, person.getActivity().name());
			preparedStatement.execute();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
	}
	public List<Person> selectAllPersons()
	{
		openConnection();
		List<Person> persons = new LinkedList<Person>();
		try
		{
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Person");

			while (resultSet.next())
			{
				int id = resultSet.getInt("id");
				String imie = resultSet.getString("name");
				Gender p³eæ = Gender.valueOf(resultSet.getString("gender"));
				int wiek = resultSet.getInt("age");
				int wzrost = resultSet.getInt("height");
				int waga = resultSet.getInt("weight");
				Activity activity = Activity.valueOf(resultSet.getString("activity"));

				Person person = new Person(id, imie, p³eæ, wiek, wzrost, waga, activity);

				persons.add(person);
			}
			resultSet.close();
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
		return persons;
	}

	public Person selectPersonById(int personId)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT *"
					+ " FROM Person"
					+ " WHERE id=?");
			preparedStatement.setInt(1, personId);
			preparedStatement.execute();

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				try
				{
					int id = resultSet.getInt("id");
					String name = resultSet.getString("name");
					Gender gender = Gender.valueOf(resultSet.getString("gender"));
					int age = resultSet.getInt("age");
					int height = resultSet.getInt("height");
					int weight = resultSet.getInt("weight");
					Activity activity = Activity.valueOf(resultSet.getString("activity"));

					return new Person(id, name, gender, age, height, weight, activity);
				}
				finally
				{
					resultSet.close();
				}
			}
			else
			{
				throw new RuntimeException("Person with id " + personId + " not found");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			throw new RuntimeException(e);
		}
		finally {
			closeConnection();
		}
	}
	public void removeByPersonId(int id)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM Person WHERE ID = ?;");
			preparedStatement.setInt(1, id);
			preparedStatement.execute();

		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
	}

}
