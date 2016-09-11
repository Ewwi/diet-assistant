package com.ew.dietassistant.pages.welcome;


import static com.ew.dietassistant.entity.person.Gender.FEMALE;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ew.dietassistant.Calculations;
import com.ew.dietassistant.Main;
import com.ew.dietassistant.database.DatabaseDailyMeal;
import com.ew.dietassistant.database.DatabaseHistory;
import com.ew.dietassistant.database.DatabasePerson;
import com.ew.dietassistant.database.DatabaseProvider;
import com.ew.dietassistant.entity.History;
import com.ew.dietassistant.entity.person.Person;
import com.ew.dietassistant.pages.Clock;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WelcomePage
{
	private static final Logger logger = LogManager.getLogger(WelcomePage.class);

	private String cssPath;
	private Stage primaryStage;
	private Scene sceneStart, welcomeScene;
	private GridPane gridPane;
	private int selectedPersonId;
	private DatabasePerson personDatabase = DatabaseProvider.getDatabasePerson();
	private DatabaseHistory databaseHistory = DatabaseProvider.getDatabaseHistory();
	private DatabaseDailyMeal databaseDailyMeal = DatabaseProvider.getDatabaseDailyMeal();


	public WelcomePage(String cssPath, Stage primaryStage, Scene sceneStart)
	{
		this.cssPath = cssPath;
		this.primaryStage = primaryStage;
		this.sceneStart = sceneStart;
	}

	public WelcomePage(String cssPath, Stage primaryStage, Scene sceneStart, int selectedPersonId)
	{
		this.cssPath = cssPath;
		this.primaryStage = primaryStage;
		this.sceneStart = sceneStart;
		this.selectedPersonId = selectedPersonId;
	}


	public void initializePage()
	{
		Person person = personDatabase.selectPersonById(selectedPersonId);
		logger.info("Initializing page for person: {}", person);

		gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(25, 25, 25, 25));
		gridPane.setVgap(10);
		gridPane.setHgap(10);

		gridPane.setGridLinesVisible(Main.GRID_LINES_VISIBLE);

		ColumnConstraints columnContraint1 = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(columnContraint1);

		ColumnConstraints columnContraint2 = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(columnContraint2);

		ColumnConstraints columnContraint3 = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(columnContraint3);

		ColumnConstraints columnContraint4 = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(columnContraint4);

		ColumnConstraints columnContraint5 = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(columnContraint5);

		Clock clock = new Clock();
		clock.createClock();

		gridPane.add(clock.getClockLabel(), 0, 0);

		initializeInfoTextForWelcomePage(person);

		initializeButtons();

		initializeLabels();

		initializeTextForCalculations(person);

		welcomeScene = new Scene(gridPane, 750, 700);
		welcomeScene.getStylesheets().add(cssPath);

		initializeAccordion();

		primaryStage.setScene(welcomeScene);
	}

	private void initializeAccordion()
	{
		AccordionWithDates accordionWithDates = new AccordionWithDates(sceneStart, cssPath, selectedPersonId, primaryStage, welcomeScene);
		accordionWithDates.initializeDaysInAccordion(gridPane, databaseHistory);
	}

	private void initializeTextForCalculations(Person person)
	{
		Calculations calculations = new Calculations();

		int caloriesForThisPerson = (int)calculations.calculateDailyCaloriesIntake(person);
		double proteinForThisPerson = calculations.calculateDailyProteinIntake(person);
		double fatForThisPerson = calculations.calculateDailyFatIntake(person);
		double carboForThisPerson = calculations.calculateDailyCarbohydratesIntake(person);
		double bmiForThisPerson = calculations.calculateBMI(person);

		Text caloriesTxt = new Text(Double.toString(caloriesForThisPerson) + " kcal");
		gridPane.add(caloriesTxt, 1, 2);
		Text fatTxt = new Text(Double.toString(fatForThisPerson) + " g");
		gridPane.add(fatTxt, 1, 3);
		Text carboTxt = new Text(Double.toString(carboForThisPerson) + " g");
		gridPane.add(carboTxt, 3, 2);
		Text proteinTxt = new Text(Double.toString(proteinForThisPerson) + " g");
		gridPane.add(proteinTxt, 3, 3);

		String text = "";

		if (bmiForThisPerson <= 18.5)
		{
			text = ", co oznacza, ¿e masz niedowagê, musisz jeœæ wiêcej.";
		} else if (bmiForThisPerson < 25 && bmiForThisPerson > 18.5)
		{
			text = ". Brawo, masz prawid³ow¹ wagê!";
		} else if (bmiForThisPerson >= 25)
		{
			text = ", wiêc czas siê odchudzaæ...";
		}

		Text notificationBMI = new Text("Twoje BMI wynosi " + bmiForThisPerson + text);
		gridPane.add(notificationBMI, 0, 4);
	}

	private void initializeLabels()
	{
		Label caloriesLabel = new Label("kalorie:");
		gridPane.add(caloriesLabel, 0, 2);
		Label fatLabel = new Label("t³uszcze:");
		gridPane.add(fatLabel, 0, 3);
		Label carbohydratesLabel = new Label("wêglowodany:");
		gridPane.add(carbohydratesLabel, 2, 2);
		Label proteinLabel = new Label("bia³ko:");
		gridPane.add(proteinLabel, 2, 3);
	}

	private void initializeInfoTextForWelcomePage(Person person)
	{
		Text formTitleText = new Text("Witaj" + " " + person.getName() + "!");
		formTitleText.setId("formTitle");
		gridPane.add(formTitleText, 0, 0, 5, 1);
		GridPane.setHalignment(formTitleText, HPos.CENTER);

		String text = "";
		if (person.getGender() == (FEMALE))
		{
			text = "Co dziœ jad³aœ? Twoje dzienne zapotrzebowanie to:";
		}
		else
		{
			text = "Co dziœ jad³eœ? Twoje dzienne zapotrzebowanie to:";
		}

		Text infoSectionText = new Text(text);
		gridPane.add(infoSectionText, 0, 1);
	}

	private void initializeButtons()
	{
		initializeGoBackButton();

		initializeNewButton();

		initializeClearButton();
	}

	private void initializeClearButton()
	{
		Button clearAllButton = new Button("usuñ ca³¹ historiê");
		gridPane.add(clearAllButton, 0, 6, 2, 1);
		clearAllButton.setMinWidth(150);
		GridPane.setHalignment(clearAllButton, HPos.CENTER);
		clearAllButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				List<History> allHistoryForPerson = databaseHistory.selectAllHistoryForPersonId(selectedPersonId);
				for (History history : allHistoryForPerson)
				{
					databaseDailyMeal.removeAllDailyMealsForPersonIdAndDate(selectedPersonId, history.getDate());
				}
				databaseHistory.removeAllHistoryForThisPersonId(selectedPersonId);
				initializePage();
			}
		});
	}

	private void initializeNewButton()
	{
		Button newButton = new Button("stwórz nowy dzieñ");
		gridPane.add(newButton, 2, 6);
		newButton.setMinWidth(150);
		GridPane.setHalignment(newButton, HPos.CENTER);
		newButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				PopupCreateNewDay popUp = new PopupCreateNewDay(cssPath, selectedPersonId, primaryStage);
				popUp.initializePopup();
				popUp.getPopUpStage().showAndWait();
				initializePage();
			}
		});
	}

	private void initializeGoBackButton()
	{
		Button goBackButton = new Button("wróæ");
		gridPane.add(goBackButton, 4, 12);
		goBackButton.setMinWidth(80);
		GridPane.setHalignment(goBackButton, HPos.CENTER);
		goBackButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				primaryStage.setScene(sceneStart);
			}
		});
	}

	public Scene getWelcomeScene()
	{
		return welcomeScene;
	}

	public void setSelectedPersonId(int selectedPersonId)
	{
		this.selectedPersonId = selectedPersonId;
	}
}
