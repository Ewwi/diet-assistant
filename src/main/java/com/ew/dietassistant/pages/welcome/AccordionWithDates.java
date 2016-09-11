package com.ew.dietassistant.pages.welcome;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import com.ew.dietassistant.Calculations;
import com.ew.dietassistant.DateTimeLookup;
import com.ew.dietassistant.Main;
import com.ew.dietassistant.database.DatabaseDailyMeal;
import com.ew.dietassistant.database.DatabaseHistory;
import com.ew.dietassistant.database.DatabasePerson;
import com.ew.dietassistant.database.DatabaseProvider;
import com.ew.dietassistant.entity.DailyMeal;
import com.ew.dietassistant.entity.History;
import com.ew.dietassistant.entity.person.Person;
import com.ew.dietassistant.pages.DailyMealContentPage.DailyMealContentPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccordionWithDates
{
	private Stage primaryStage;
	private VBox boxMain, boxYear, boxMonth;
	private TitledPane yearTitledPane, monthTitledPane, dayTitledPane;
	private GridPane gridPaneAccordion;
	private Scene welcomeScene, sceneStart;
	private String cssPath;
	private int selectedPersonId;
	private DatabasePerson databasePerson = DatabaseProvider.getDatabasePerson();
	private DatabaseHistory databaseHistory = DatabaseProvider.getDatabaseHistory();


	public AccordionWithDates(Scene sceneStart, String cssPath, int selectedPersonId, Stage primaryStage, Scene welcomeScene)
	{
		this.selectedPersonId = selectedPersonId;
		this.primaryStage = primaryStage;
		this.welcomeScene = welcomeScene;
		this.cssPath = cssPath;
		this.sceneStart = sceneStart;
	}


	public void initializeDaysInAccordion(GridPane gridPane, DatabaseHistory databaseHistory)
	{
		StackPane stackPane = new StackPane();
		Accordion accordion = new Accordion();
		ScrollPane scrollPane = new ScrollPane();

		stackPane.setMinSize(690, 200);
		stackPane.setMaxSize(690, 400);
		stackPane.getChildren().add(accordion);

		accordion.setMinSize(690, 200);
		accordion.setMaxSize(690, 400);

		scrollPane.setMinSize(690, 200);
		scrollPane.setMaxSize(690, 400);

		TitledPane titledPaneMain = new TitledPane("Twój roczny dziennik posi³ków", scrollPane);

		accordion.getPanes().add(titledPaneMain);
		accordion.setExpandedPane(titledPaneMain);

		scrollPane.setVvalue(1.0);

		gridPane.add(stackPane, 0, 5);

		List<History> historyList = databaseHistory.selectAllHistoryForPersonId(selectedPersonId);

		historyList.sort(Comparator.comparing(History::getDate));

		boxMain = new VBox(1);

		int previousYear = 0;
		int previousMonth = 0;

		for (History history : historyList)
		{
			int listIndex = historyList.indexOf(history);
			int listIndexPrev = historyList.indexOf(history);

			if (listIndex == 0)
			{
				previousMonth = 99;
				previousYear = 99;
			}
			else
			{
				listIndexPrev = listIndex - 1;
				previousYear = historyList.get(listIndexPrev).getDate().getYear();
				previousMonth = historyList.get(listIndexPrev).getDate().getMonthValue();
			}

			int currentYear = historyList.get(listIndex).getDate().getYear();
			int currentMonth = historyList.get(listIndex).getDate().getMonthValue();
			int currentDay = historyList.get(listIndex).getDate().getDayOfMonth();

			DateTimeLookup dateTimeLookup = new DateTimeLookup();
			LocalDate dateTime = LocalDate.of(currentYear, currentMonth, currentDay);

			String month = dateTimeLookup.getMonthName(dateTime);
			String year = "" + currentYear;
			String day = dateTimeLookup.getNameOfDayOfTheWeek(dateTime);

			if (currentYear!= previousYear)
			{
				createYearPaneInAccordion(year);
			}

			if (currentMonth != previousMonth || currentYear != previousYear)
			{
				createMonthPaneInAccordion(month);
			}

			createAccordionSlotContent(history);

			createDayPaneInAccordion(currentDay, day);

			setExpansionOfPanesInAccordion(historyList, listIndex);

			scrollPane.setContent(boxMain);
		}
	}

	private void setExpansionOfPanesInAccordion(List<History> historyList, int listIndex)
	{
		if (listIndex == historyList.size() - 1)
		{
			dayTitledPane.setExpanded(true);
			monthTitledPane.setExpanded(true);
			yearTitledPane.setExpanded(true);
		}
		else
		{
			dayTitledPane.setExpanded(false);
			monthTitledPane.setExpanded(false);
			yearTitledPane.setExpanded(false);
		}
	}

	private void createDayPaneInAccordion(int dayOfMonth, String dayOfWeek)
	{
		String dayTitle = dayOfWeek + " " + DateTimeLookup.addLeadingZeroIfHasOnlyOneDigit(dayOfMonth);
		dayTitledPane = new TitledPane(dayTitle, gridPaneAccordion);
		dayTitledPane.setExpanded(false);
		dayTitledPane.setMinWidth(500);
		dayTitledPane.setMaxSize(500, 150);
		boxMonth.getChildren().add(dayTitledPane);
	}

	private void createMonthPaneInAccordion(String month)
	{
		boxMonth = new VBox(1);
		monthTitledPane = new TitledPane(month, boxMonth);
		monthTitledPane.setExpanded(false);
		monthTitledPane.setMinWidth(600);
		monthTitledPane.setMaxSize(600, 150);
		boxYear.getChildren().add(monthTitledPane);
	}

	private void createYearPaneInAccordion(String year)
	{
		boxYear = new VBox(1);
		yearTitledPane = new TitledPane("" + year, boxYear);
		yearTitledPane.setExpanded(false);
		yearTitledPane.setMinWidth(670);
		yearTitledPane.setMaxSize(670, 150);
		boxMain.getChildren().add(yearTitledPane);
	}

	private void createAccordionSlotContent(History history)
	{
		gridPaneAccordion = new GridPane();
		gridPaneAccordion.setAlignment(Pos.CENTER);
		gridPaneAccordion.setPadding(new Insets(25, 25, 25, 25));
		gridPaneAccordion.setVgap(10);
		gridPaneAccordion.setHgap(10);

		gridPaneAccordion.setGridLinesVisible(Main.GRID_LINES_VISIBLE);

		ColumnConstraints columnContraint1 = new ColumnConstraints(150);
		gridPaneAccordion.getColumnConstraints().add(columnContraint1);

		ColumnConstraints columnContraint2 = new ColumnConstraints(150);
		gridPaneAccordion.getColumnConstraints().add(columnContraint2);

		Calculations calculations = new Calculations();
		Person person = databasePerson.selectPersonById(history.getPersonId());
		DatabaseDailyMeal databaseDailyMeal = DatabaseProvider.getDatabaseDailyMeal();

		List<DailyMeal> someDailyMeals = databaseDailyMeal.selectAllDailyMealsByPersonIdAndDate(person.getId(), history.getDate());
		int caloriesSummary = calculations.calculateDailyCaloriesPercentIntake(someDailyMeals, person);
		double proteinSummary = calculations.calculateDailyProteinPercentIntake(someDailyMeals, person);
		double fatSummary = calculations.calculateDailyFatPercentIntake(someDailyMeals, person);
		double carboSummary = calculations.calculateDailyCarboPercentIntake(someDailyMeals, person);

		Text calories = new Text("kalorie: " + caloriesSummary + "%");
		gridPaneAccordion.add(calories, 0, 1);
		Text protein = new Text("bia³ko: " + (int)proteinSummary + "%");
		gridPaneAccordion.add(protein, 0, 2);
		Text fat = new Text("t³uszcz: " + (int)fatSummary + "%");
		gridPaneAccordion.add(fat, 1, 1);
		Text carbo = new Text("wêglowodany: " + (int)carboSummary + "%");
		gridPaneAccordion.add(carbo, 1, 2);

		Button editButton = new Button("edytuj");
		gridPaneAccordion.add(editButton, 0, 3);
		editButton.setMinWidth(80);
		GridPane.setHalignment(editButton, HPos.CENTER);

		editButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				DailyMealContentPage dailyMealContent = new DailyMealContentPage(cssPath, selectedPersonId, history, welcomeScene, primaryStage, sceneStart);
				dailyMealContent.initializeScene();
				primaryStage.setScene(dailyMealContent.getAccordionDayContentScene());
			}
		});
		Button clearButton = new Button("usuñ");
		gridPaneAccordion.add(clearButton, 1, 3);
		clearButton.setMinWidth(80);
		GridPane.setHalignment(clearButton, HPos.CENTER);
		clearButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				removeDayFromHistory(history);
			}
		});
	}

	private void removeDayFromHistory(History history)
	{
		DatabaseDailyMeal databaseDailyMeal = DatabaseProvider.getDatabaseDailyMeal();
		databaseDailyMeal.removeAllDailyMealsForPersonIdAndDate(selectedPersonId, history.getDate());
		databaseHistory.removeHistoryByDateAndPersonId(history.getDate(), selectedPersonId);
		WelcomePage welcomePage = new WelcomePage(cssPath, primaryStage, sceneStart, selectedPersonId);
		welcomePage.initializePage();
	}
}
