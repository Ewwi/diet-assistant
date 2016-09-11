package com.ew.dietassistant.pages.DailyMealContentPage;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ew.dietassistant.Calculations;
import com.ew.dietassistant.Main;
import com.ew.dietassistant.database.DatabaseDailyMeal;
import com.ew.dietassistant.database.DatabaseFood;
import com.ew.dietassistant.database.DatabasePerson;
import com.ew.dietassistant.database.DatabaseProvider;
import com.ew.dietassistant.entity.DailyMeal;
import com.ew.dietassistant.entity.Food;
import com.ew.dietassistant.entity.History;
import com.ew.dietassistant.entity.person.Person;
import com.ew.dietassistant.pages.Clock;
import com.ew.dietassistant.pages.welcome.WelcomePage;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DailyMealContentPage
{
	private static final Logger logger = LogManager.getLogger(DailyMealContentPage.class);

	private Scene welcomeScene, dailyMealContentScene, sceneStart;
	private int selectedPersonId;
	private String cssPath;
	private Stage primaryStage;
	private GridPane gridPane;
	private TableView<DailyMeal> table;
	private Person person;
	private Calculations calculations = new Calculations();
	private Button addButton;
	private History history;
	private DatabasePerson personDatabase = DatabaseProvider.getDatabasePerson();
	private DatabaseFood databaseFood = DatabaseProvider.getDatabaseFood();
	private DatabaseDailyMeal databaseDailyMeal = DatabaseProvider.getDatabaseDailyMeal();


	public DailyMealContentPage(String cssPath, int selectedPersonId, History history, Scene welcomeScene, Stage primaryStage, Scene SceneStart)
	{
		this.cssPath = cssPath;
		this.selectedPersonId = selectedPersonId;
		this.history = history;
		this.welcomeScene = welcomeScene;
		this.primaryStage = primaryStage;
		this.sceneStart = SceneStart;
	}


	public void initializeScene()
	{
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

		person = personDatabase.selectPersonById(selectedPersonId);

		Clock clock = new Clock();
		clock.createClock();

		gridPane.add(clock.getClockLabel(), 0, 0);

		initializePageButtons();

		initializeSearchTextField();

		initializeTableView();

		initializeCalculations(person, calculations);

		dailyMealContentScene = new Scene(gridPane, 750, 700);
		dailyMealContentScene.getStylesheets().add(cssPath);
	}

	private void initializeTableView()
	{
		table = new TableView<DailyMeal>();
		table.setMinWidth(690);
		table.setMaxHeight(350);

		gridPane.add(table, 0, 2);

		TableColumn<DailyMeal, String> name = new TableColumn<>("nazwa produktu");
		name.setMinWidth(190);

		name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DailyMeal, String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<DailyMeal, String> param)
			{
				DailyMeal dailyMeal = param.getValue();
				String productName = dailyMeal.getFood().getProductName();

				return new ReadOnlyObjectWrapper<>(productName);
			}
		});

		name.setCellFactory(TextFieldTableCell.forTableColumn());

		TableColumn<DailyMeal, Integer> quantity = new TableColumn<>("iloœæ");
		quantity.setMinWidth(100);

		quantity.setCellValueFactory(new PropertyValueFactory<DailyMeal, Integer>("grams"));

		TableColumn<DailyMeal, Integer> calories = new TableColumn<>("kalorie");
		calories.setMinWidth(100);

		calories.setCellValueFactory(new Callback<CellDataFeatures<DailyMeal, Integer>, ObservableValue<Integer>>()
		{
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<DailyMeal, Integer> cellDataFeatures)
			{
				DailyMeal dailyMeal = cellDataFeatures.getValue();
				double calc = dailyMeal.getFood().getCaloriesOfProduct() * (dailyMeal.getGrams()/(double)100);
				int calories = (int) calc;

				return new ReadOnlyObjectWrapper<>(calories);
			}
		});

		TableColumn<DailyMeal, Double> protein = new TableColumn<>("bia³ko");
		protein.setMinWidth(100);

		protein.setCellValueFactory(new Callback<CellDataFeatures<DailyMeal, Double>, ObservableValue<Double>>()
		{
			@Override
			public ObservableValue<Double> call(CellDataFeatures<DailyMeal, Double> cellDataFeatures)
			{
				DailyMeal dailyMeal = cellDataFeatures.getValue();
				double calc = dailyMeal.getFood().getProteinOfProduct() * (dailyMeal.getGrams()/(double)100);
				calc = Math.round(calc);
				double protein = calc;

				return new ReadOnlyObjectWrapper<Double>(protein);
			}
		});

		TableColumn<DailyMeal, Double> fat = new TableColumn<>("t³uszcz");
		fat.setMinWidth(100);

		fat.setCellValueFactory(new Callback<CellDataFeatures<DailyMeal, Double>, ObservableValue<Double>>()
		{
			@Override
			public ObservableValue<Double> call(CellDataFeatures<DailyMeal, Double> cellDataFeatures)
			{
				DailyMeal dailyMeal = cellDataFeatures.getValue();
				double calc = dailyMeal.getFood().getFatOfProduct() * (dailyMeal.getGrams()/(double)100);
				calc = Math.round(calc);
				double fat = calc;

				return new ReadOnlyObjectWrapper<Double>(fat);
			}
		});

		TableColumn<DailyMeal, Double> carbo = new TableColumn<>("wêglowodany");
		carbo.setMinWidth(100);

		carbo.setCellValueFactory(new Callback<CellDataFeatures<DailyMeal, Double>, ObservableValue<Double>>()
		{
			@Override
			public ObservableValue<Double> call(CellDataFeatures<DailyMeal, Double> cellDataFeatures)
			{
				DailyMeal dailyMeal = cellDataFeatures.getValue();
				double calc = dailyMeal.getFood().getCarboOfProduct() * (dailyMeal.getGrams()/(double)100);
				calc = Math.round(calc);
				double carbo = calc;

				return new ReadOnlyObjectWrapper<Double>(carbo);
			}
		});

		table.getColumns().addAll(Arrays.asList(name, quantity, calories, protein, fat, carbo));

		populateTableView(databaseFood);
		table.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				logger.trace("DailyMeal table clicked!");
			}
		});
	}

	public void initializeSearchTextField()
	{
		ComboBox<Food> comboBox = initializeSearchComboBox();

		addButton = new Button("dodaj");

		TextField gramsTextField = new TextField();
		gramsTextField.setMaxWidth(50);

		addButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				String text = gramsTextField.getText();
				int grams = Integer.parseInt(text);
				String productName = comboBox.getEditor().textProperty().getValue();
				Food foodSelectedByName = databaseFood.selectFoodByName(productName);
				DailyMeal dailyMeal = new DailyMeal(1, selectedPersonId, history.getDate(), foodSelectedByName, grams);
				databaseDailyMeal.insertDailyMeal(dailyMeal);

				initializeTableView();
				initializeCalculations(person, calculations);

				comboBox.setValue(null);
			}
		});

		Label searchLabel = new Label("Wyszukaj:  ");
		Label gramsLabel = new Label("g");

		HBox hboxSearch = new HBox(5, searchLabel, comboBox, gramsTextField, gramsLabel, addButton);
		hboxSearch.setPadding(new Insets(5));
		gridPane.add(hboxSearch, 0, 1, 4, 1);

	}

	private ComboBox<Food> initializeSearchComboBox()
	{
		ComboBox<Food> comboBox = new ComboBox<Food>();
		comboBox.setEditable(true);

		List<Food> allProducts = databaseFood.selectAllFoods();
		ObservableList<Food> items = FXCollections.observableArrayList(allProducts);

		FilteredList<Food> filteredItems = new FilteredList<Food>(items, p -> true);
		comboBox.setItems(filteredItems);

		comboBox.setOnKeyReleased(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				String textInComboBox = comboBox.getEditor().textProperty().getValue();
				logger.debug("current text is: {}", textInComboBox);

				comboBox.show();

				if(event.getCode().equals(KeyCode.ENTER))
				{
					Food selectedFood = findFoodByName(allProducts, textInComboBox);
					logger.debug("selected: {}", selectedFood);
				}
				else if(event.getCode().equals(KeyCode.UP))
				{
					logger.debug("arrow up pressed");
				}
				else if(event.getCode().equals(KeyCode.DOWN))
				{
					logger.debug("arrow down pressed");
				}
				else if (event.getCode().isLetterKey()
						|| event.getCode().isDigitKey()
						|| event.getCode().equals(KeyCode.BACK_SPACE)
						|| event.getCode().equals(KeyCode.SPACE))
				{
					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							Predicate<Food> predicate = new Predicate<Food>()
							{
								@Override
								public boolean test(Food food)
								{
									boolean predicateResult = food.getProductName().toUpperCase().contains(textInComboBox.toUpperCase());
									logger.debug("returning predicateResult: {}", predicateResult);
									return predicateResult;
								}
							};
							filteredItems.setPredicate(predicate);
						}
					});
				}
			}
		});
		return comboBox;
	}

	private Food findFoodByName(List<Food> allProducts, String name)
	{
		for (Food food : allProducts)
		{
			if (food.getProductName().equals(name))
			{
				return food;
			}
		}
		throw new IllegalArgumentException("product not found!");
	}

	private void populateTableView(DatabaseFood databaseFood)
	{
		List<DailyMeal> databaseDailyMealList = databaseDailyMeal.selectAllDailyMealsByPersonIdAndDate(selectedPersonId, history.getDate());
		table.setItems(FXCollections.observableArrayList(databaseDailyMealList));
	}

	private void initializePageButtons()
	{
		Button removeButton = new Button("usuñ");
		removeButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				databaseDailyMeal.removeDailyMealById(table.getSelectionModel().getSelectedItem().getId());
				initializeTableView();
				initializeCalculations(person, calculations);
			}
		});

		Button removeAllButton = new Button("usuñ wszystko");
		removeAllButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				databaseDailyMeal.removeAllDailyMealsForPersonIdAndDate(selectedPersonId, history.getDate());
				initializeTableView();
				initializeCalculations(person, calculations);
			}
		});

		HBox hboxWithRemoveButtons = new HBox(15, removeButton, removeAllButton);
		hboxWithRemoveButtons.setPadding(new Insets(5));
		gridPane.add(hboxWithRemoveButtons, 0, 4, 4, 1);

		Button backButton = new Button("wróæ");
		backButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				primaryStage.setScene(welcomeScene);
				WelcomePage welcomePage = new WelcomePage(cssPath, primaryStage, sceneStart, selectedPersonId);
				welcomePage.initializePage();
			}
		});
		gridPane.add(backButton, 4, 9);
		backButton.setMinWidth(100);
		GridPane.setHalignment(backButton, HPos.CENTER);
	}
	private void initializeCalculations(Person selectPerson, Calculations calculations)
	{
		String personNeedsHeading = "twoje dzienne zapotrzebowanie";
		String percentHeading = "procent dziennego zapotrzebowania";
		String sumHeading = "suma sk³adników od¿ywczych";

		TableView<CustomRow> tableSum = new TableView<>();
		tableSum.setMinWidth(690);
		tableSum.setMaxHeight(84);
		tableSum.getStyleClass().add("tableview-header-hidden");
		tableSum.getStyleClass().add("tableSum");

		List<DailyMeal> someDailyMeals = databaseDailyMeal.selectAllDailyMealsByPersonIdAndDate(selectPerson.getId(),
				history.getDate());
		ObservableList<CustomRow> sumColumnContent = FXCollections.observableArrayList(
				new CustomRow(personNeedsHeading, (int) calculations.calculateDailyCaloriesIntake(selectPerson),
						calculations.calculateDailyProteinIntake(selectPerson),
						calculations.calculateDailyFatIntake(selectPerson),
						calculations.calculateDailyCarbohydratesIntake(selectPerson)),
				new CustomRow(sumHeading, calculations.calculateCaloriesForThisDay(someDailyMeals),
						calculations.calculateProteinSumForThisDay(someDailyMeals),
						calculations.calculateFatSumForThisDay(someDailyMeals),
						calculations.calculateCarboSumForThisDay(someDailyMeals)),
				new CustomRow(percentHeading,
						calculations.calculateDailyCaloriesPercentIntake(someDailyMeals, selectPerson),
						calculations.calculateDailyProteinPercentIntake(someDailyMeals, selectPerson),
						calculations.calculateDailyFatPercentIntake(someDailyMeals, selectPerson),
						calculations.calculateDailyCarboPercentIntake(someDailyMeals, selectPerson)));

		TableColumn<CustomRow, String> nameSumColumn = new TableColumn<>();
		nameSumColumn.setMinWidth(290);

		nameSumColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CustomRow, String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<CustomRow, String> param)
			{
				CustomRow customRow = param.getValue();
				String rowHeading = customRow.getRowHeading();

				return new ReadOnlyObjectWrapper<>(rowHeading);
			}
		});

		TableColumn<CustomRow, Integer> caloriesSumColumn = new TableColumn<>();
		caloriesSumColumn.setMinWidth(100);

		caloriesSumColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CustomRow, Integer>, ObservableValue<Integer>>()
		{
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<CustomRow, Integer> param)
			{
				CustomRow customRow = param.getValue();
				Integer calories = customRow.getCalories();

				return new ReadOnlyObjectWrapper<>(calories);
			}
		});

		TableColumn<CustomRow, Double> proteinSumColumn = new TableColumn<>();
		proteinSumColumn.setMinWidth(100);

		proteinSumColumn.setCellValueFactory(new Callback<CellDataFeatures<CustomRow, Double>, ObservableValue<Double>>()
		{
			@Override
			public ObservableValue<Double> call(CellDataFeatures<CustomRow, Double> cellDataFeatures)
			{
				CustomRow customRow = cellDataFeatures.getValue();
				double protein = customRow.getProtein();

				return new ReadOnlyObjectWrapper<Double>(protein);
			}
		});

		TableColumn<CustomRow, Double> fatSumColumn = new TableColumn<>();
		fatSumColumn.setMinWidth(100);

		fatSumColumn.setCellValueFactory(new Callback<CellDataFeatures<CustomRow, Double>, ObservableValue<Double>>()
		{
			@Override
			public ObservableValue<Double> call(CellDataFeatures<CustomRow, Double> cellDataFeatures)
			{
				CustomRow customRow = cellDataFeatures.getValue();
				double fat = customRow.getFat();

				return new ReadOnlyObjectWrapper<Double>(fat);
			}
		});

		TableColumn<CustomRow, Double> carboSumColumn = new TableColumn<>();
		carboSumColumn.setMinWidth(100);

		carboSumColumn.setCellValueFactory(new Callback<CellDataFeatures<CustomRow, Double>, ObservableValue<Double>>()
		{
			@Override
			public ObservableValue<Double> call(CellDataFeatures<CustomRow, Double> cellDataFeatures)
			{
				CustomRow customRow = cellDataFeatures.getValue();
				double carbo = customRow.getCarbo();

				return new ReadOnlyObjectWrapper<Double>(carbo);
			}
		});

		tableSum.getColumns().addAll(Arrays.asList(nameSumColumn, caloriesSumColumn, proteinSumColumn, fatSumColumn, carboSumColumn));
		tableSum.setItems(sumColumnContent);

		gridPane.add(tableSum, 0, 3);
	}

	public Scene getAccordionDayContentScene()
	{
		return dailyMealContentScene;
	}
}
