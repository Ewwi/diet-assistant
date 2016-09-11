package com.ew.dietassistant;

import java.time.LocalDate;

import com.ew.dietassistant.database.DatabaseHistory;
import com.ew.dietassistant.database.DatabaseProvider;
import com.ew.dietassistant.entity.History;
import com.ew.dietassistant.pages.StartPage;
import com.ew.dietassistant.pages.welcome.WelcomePage;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
	public static final boolean GRID_LINES_VISIBLE = false;


	@Override
	public void start(Stage primaryStage)
	{
		String cssPath = this.getClass().getResource("../../../Style.css").toExternalForm();

		StartPage startPage = new StartPage(cssPath, primaryStage);

		startPage.initializePage();
		WelcomePage welcomePage = new WelcomePage(cssPath, primaryStage, startPage.getSceneStart());

		startPage.setWelcomePage(welcomePage);

		primaryStage.show();
	}

	public static void main(String[] args)
	{
		populateDatabases();
		launch(args);
		//		Database database = new Database("biblioteka.db");
		//		database.dropTables();
		//		database.createTablesIfNotExist();
	}

	private static void populateDatabases()
	{
		//		rebuildHistoryDatabaseWithFakeData();
		PopulatingDatabaseFood.populateDatabaseFoodWithProducts();
	}

	private static void rebuildHistoryDatabaseWithFakeData()
	{
		DatabaseHistory databaseHistory = DatabaseProvider.getDatabaseHistory();
		databaseHistory.dropTable();
		databaseHistory.createTableIfNotExists();

		LocalDate person6date1 = LocalDate.of(2010, 12, 24);
		History historyforPerson6 = new History(1, person6date1,6);
		databaseHistory.insertHistory(historyforPerson6);

		LocalDate person6date2 = LocalDate.of(2011, 11, 23);
		History history2forPerson6 = new History(1, person6date2,6);
		databaseHistory.insertHistory(history2forPerson6);

		LocalDate person6date3 = LocalDate.of(2012, 1, 1);
		History history3forPerson6 = new History(1, person6date3,6);
		databaseHistory.insertHistory(history3forPerson6);

		LocalDate person6date4 = LocalDate.of(2012, 6, 6);
		History history4forPerson6 = new History(1, person6date4,6);
		databaseHistory.insertHistory(history4forPerson6);

		LocalDate date = LocalDate.of(2016, 12, 24);
		History historyforPerson2 = new History(1, date,2);
		databaseHistory.insertHistory(historyforPerson2);

		LocalDate date1 = LocalDate.of(2016, 11, 23);
		History history2forPerson2 = new History(1, date1,2);
		databaseHistory.insertHistory(history2forPerson2);

		LocalDate date2 = LocalDate.of(2013, 1, 1);
		History history3forPerson2 = new History(1, date2,2);
		databaseHistory.insertHistory(history3forPerson2);

		LocalDate date3 = LocalDate.of(2012, 6, 6);
		History history4forPerson2 = new History(1, date3,2);
		databaseHistory.insertHistory(history4forPerson2);
	}
}
