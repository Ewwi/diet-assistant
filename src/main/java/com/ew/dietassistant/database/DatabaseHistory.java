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

import com.ew.dietassistant.entity.History;

public class DatabaseHistory
{
	private static final Logger logger = LogManager.getLogger(DatabaseHistory.class);

	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite";

	private Connection connection;
	private Statement statement;
	private String databaseFileName;


	DatabaseHistory(String databaseFileName)
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
		String createTableHistory = "CREATE TABLE IF NOT EXISTS History "
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "date BIGINT,"
				+ "personId INTEGER,"
				+ "UNIQUE(date, personId));";
		// @formatter:on

		try
		{
			statement.execute(createTableHistory);

			logger.info("Table History created!");
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
			statement.execute("DROP TABLE History");

			logger.info("Table History dropped!");

		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
	}
	public int insertHistory(History history)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection
					.prepareStatement("insert into History values (NULL, ?, ?);", RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, history.getDate().toEpochDay());
			preparedStatement.setLong(2, history.getPersonId());
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
	public List<History> selectAllHistory()
	{
		openConnection();
		List<History> historyList = new LinkedList<History>();
		try
		{
			ResultSet resultSet = statement.executeQuery("SELECT * FROM History");

			while (resultSet.next())
			{
				int id = resultSet.getInt("id");
				Long epoch = resultSet.getLong("date");
				int personId = resultSet.getInt("personId");
				LocalDate date = LocalDate.ofEpochDay(epoch);

				History history = new History(id, date, personId);

				historyList.add(history);
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
		return historyList;
	}

	public List<History> selectAllHistoryForPersonId(int idPerson)
	{
		openConnection();
		List<History> list = new LinkedList<History>();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT *"
					+ " FROM History"
					+ " WHERE personId = ?;");
			preparedStatement.setInt(1, idPerson);
			preparedStatement.execute();

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				int id = resultSet.getInt("id");
				Long epoch = resultSet.getLong("date");
				int personId = resultSet.getInt("personId");
				LocalDate date = LocalDate.ofEpochDay(epoch);

				History history = new History(id, date, personId);

				list.add(history);
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

	public History selectHistoryById(int historyId)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT *"
					+ " FROM History"
					+ " WHERE id=?");
			preparedStatement.setInt(1, historyId);
			preparedStatement.execute();

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next())
			{
				try
				{
					int id = resultSet.getInt("id");
					Long epoch = resultSet.getLong("date");
					int personId = resultSet.getInt("personId");
					LocalDate date = LocalDate.ofEpochDay(epoch);

					return new History(id, date, personId);
				}
				finally
				{
					resultSet.close();
				}
			}
			else
			{
				throw new RuntimeException("history with id " + historyId + " not found");
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
	public void removeHistoryById(int id)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM History WHERE ID = ?;");
			preparedStatement.setInt(1, id);
			preparedStatement.execute();

		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		closeConnection();
	}
	public void removeHistoryByDateAndPersonId(LocalDate date, int personId)
	{
		openConnection();
		try
		{
			PreparedStatement preparedStatement = connection
					.prepareStatement("DELETE FROM History WHERE personId = ? AND date =?;");
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
			closeConnection();
		}
	}
	public void removeAllHistoryForThisPersonId(int personId)
	{
		PreparedStatement preparedStatement = null;
		openConnection();
		try
		{
			preparedStatement = connection.prepareStatement("DELETE FROM History WHERE personId = ?;");
			preparedStatement.setInt(1, personId);
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
			finally
			{
				closeConnection();
			}
		}
	}
}
