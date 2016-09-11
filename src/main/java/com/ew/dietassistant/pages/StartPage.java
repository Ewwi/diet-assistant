package com.ew.dietassistant.pages;

import com.ew.dietassistant.Main;
import com.ew.dietassistant.database.DatabasePerson;
import com.ew.dietassistant.database.DatabaseProvider;
import com.ew.dietassistant.entity.person.Activity;
import com.ew.dietassistant.entity.person.Gender;
import com.ew.dietassistant.entity.person.Person;
import com.ew.dietassistant.pages.welcome.WelcomePage;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartPage
{
	private String cssPath;
	private Stage primaryStage;
	private TextField ageField, weightField, heightField, nameField;
	private GridPane gridPane;
	private Scene sceneStart;
	private RadioButton f, m, low, medium, high;
	private Button nextButton;
	private WelcomePage welcomePage;
	private TableView<Person> table;

	private DatabasePerson database = DatabaseProvider.getDatabasePerson();


	public StartPage(String cssPath, Stage primaryStage)
	{
		this.cssPath = cssPath;
		this.primaryStage = primaryStage;
	}


	public Scene getSceneStart()
	{
		return sceneStart;
	}

	public void setWelcomePage(WelcomePage welcomePage)
	{
		this.welcomePage = welcomePage;
	}

	public void initializePage()
	{
		gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(10);
		gridPane.setHgap(10);

		gridPane.setGridLinesVisible(Main.GRID_LINES_VISIBLE);

		ColumnConstraints column = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(column);

		ColumnConstraints column2 = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(column2);

		ColumnConstraints column3 = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(column3);

		ColumnConstraints column4 = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(column4);

		ColumnConstraints column5 = new ColumnConstraints(130);
		gridPane.getColumnConstraints().add(column5);

		nameField = new TextField();
		gridPane.add(nameField, 2, 4, 2, 1);

		Clock clock = new Clock();
		clock.createClock();

		gridPane.add(clock.getClockLabel(), 0, 1);

		initializeTableView();

		initializeText();

		initializeHBoxAgeWeightHeight();

		initializeHBoxActivityGender();

		initializeButtons();

		sceneStart = new Scene(gridPane, 750, 700);
		sceneStart.getStylesheets().add(cssPath);

		primaryStage.setScene(sceneStart);
		primaryStage.setTitle("Organizer");
	}

	private void initializeTableView()
	{
		table = new TableView<Person>();
		table.setMaxWidth(400);
		table.setMaxHeight(200);

		gridPane.add(table, 1, 11, 5, 1);

		TableColumn<Person, String> name = new TableColumn<>("u¿ytkownik");
		name.setMinWidth(398);

		name.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));

		name.setCellFactory(TextFieldTableCell.forTableColumn());

		refreshTable();

		table.getColumns().add(name);

		table.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event)
			{
				nextButton.setDisable(false);
			}
		});
	}


	public void refreshTable()
	{
		table.setItems(FXCollections.observableArrayList(database.selectAllPersons()));
	}

	private void initializeText()
	{
		Text formTitleText = new Text("Organizer diety");
		formTitleText.setId("formTitle");
		gridPane.add(formTitleText, 0, 1, 5, 1);
		GridPane.setHalignment(formTitleText, HPos.CENTER);

		Text infoSectionText = new Text("Witaj w organizerze diety! Wype³nij poni¿sze dane, aby stworzyæ profil.");
		gridPane.add(infoSectionText, 1, 3);
		infoSectionText.setWrappingWidth(500);
	}

	private void initializeHBoxAgeWeightHeight()
	{
		Label nameLabel = new Label("wpisz swoje imiê:");
		gridPane.add(nameLabel, 1, 4);

		Label ageLabel = new Label("wpisz swój wiek:");
		ageField = new TextField();
		ageField.setMaxWidth(40);

		Label weightLabel = new Label("wpisz swoj¹ wagê:");
		weightField = new TextField();
		weightField.setMaxWidth(40);

		Label heightLabel = new Label("wpisz swój wzrost:");
		heightField = new TextField();
		heightField.setMaxWidth(40);

		HBox ageWeightHeightBox = new HBox(10, ageLabel, ageField, weightLabel, weightField, heightLabel, heightField);
		ageWeightHeightBox.setPadding(new Insets(1));
		ageWeightHeightBox.setAlignment(Pos.CENTER_LEFT);
		gridPane.add(ageWeightHeightBox, 1, 6, 5, 1);
	}

	private void initializeHBoxActivityGender()
	{
		Label activityLabel = new Label("zaznacz swoj¹ aktywnoœæ:");
		low = new RadioButton("ma³y");
		medium = new RadioButton("œredni");
		high = new RadioButton("du¿y");

		ToggleGroup tGroup2 = new ToggleGroup();
		low.setToggleGroup(tGroup2);
		medium.setToggleGroup(tGroup2);
		high.setToggleGroup(tGroup2);

		low.setSelected(true);

		HBox hbradio2 = new HBox(16, activityLabel, low, medium, high);
		hbradio2.setPadding(new Insets(1));
		gridPane.add(hbradio2, 1, 7, 3, 1);

		Label genderLabel = new Label("wybierz swoj¹ p³eæ:");
		f = new RadioButton("kobieta");
		m = new RadioButton("mê¿czyzna");

		ToggleGroup tGroup = new ToggleGroup();
		f.setToggleGroup(tGroup);
		m.setToggleGroup(tGroup);

		f.setSelected(true);

		HBox hbradio = new HBox(15, genderLabel, f, m);
		hbradio.setPadding(new Insets(1));
		gridPane.add(hbradio, 1, 5, 3, 1);
	}

	private void initializeButtons()
	{
		Button savePersonButton = new Button("zapisz mnie");

		Text notification = new Text("");
		notification.setId("notification");
		gridPane.add(notification, 1, 10, 5, 1);
		GridPane.setHalignment(notification, HPos.CENTER);

		savePersonButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if (isInputDataValid(notification))
				{
					createPersonProfile();
				}
			}

			private boolean isInputDataValid(Text notification)
			{
				if(nameField.getText().isEmpty())
				{
					notification.setText("wpisz swoje imiê");
					return false;
				}
				else if(isEmptyOrFailsNumberOnlyRegex(ageField))
				{
					notification.setText("wpisz swój wiek");
					return false;
				}
				else if(isEmptyOrFailsNumberOnlyRegex(weightField))
				{
					notification.setText("wpisz swoj¹ wagê");
					return false;
				}
				else if(isEmptyOrFailsNumberOnlyRegex(heightField))
				{
					notification.setText("wpisz swój wzrost");
					return false;
				}
				else
				{
					notification.setText("");
					return true;
				}
			}

			private boolean isEmptyOrFailsNumberOnlyRegex(TextField textfield)
			{
				String regex = "^\\d+$";

				return textfield.getText().isEmpty() || textfield.getText().matches(regex) == false;
			}

			private void createPersonProfile()
			{
				String name = nameField.getText();
				int age = Integer.valueOf(ageField.getText());
				int weight = Integer.valueOf(weightField.getText());
				int height = Integer.valueOf(heightField.getText());

				Gender gender = null;
				if(f.isSelected())
				{
					gender = Gender.FEMALE;
				}
				else
				{
					gender = Gender.MALE;
				}

				Activity activity = null;
				if(low.isSelected())
				{
					activity = Activity.LOW;
				}
				else if(medium.isSelected())
				{
					activity = Activity.MEDIUM;
				}
				else
				{
					activity = Activity.HIGH;
				}

				Person person = new Person(name, gender, age, height, weight, activity);

				database.insertPerson(person);
				refreshTable();

				nameField.clear();
				ageField.clear();
				weightField.clear();
				heightField.clear();
			}
		});

		gridPane.add(savePersonButton, 1, 9);
		GridPane.setHalignment(savePersonButton, HPos.CENTER);

		nextButton = new Button("dalej");
		nextButton.setDisable(true);
		nextButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				openWelcomePage();
			}

			public void openWelcomePage()
			{
				Person selectedPerson = table.getSelectionModel().getSelectedItem();
				int selectedPersonId = selectedPerson.getId();

				welcomePage.setSelectedPersonId(selectedPersonId);
				welcomePage.initializePage();

				primaryStage.setScene(welcomePage.getWelcomeScene());
			}
		});

		Button clearButton = new Button("clear");
		clearButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				Person selectedItem = table.getSelectionModel().getSelectedItem();
				database.removeByPersonId(selectedItem.getId());
				refreshTable();
				nextButton.setDisable(true);
			}
		});

		HBox hbButton = new HBox();
		hbButton.getChildren().addAll(clearButton, nextButton);
		hbButton.setSpacing(4);
		gridPane.add(hbButton, 0, 12, 5, 1);
		hbButton.setAlignment(Pos.CENTER);

		Button closeApplicationButton = new Button("zamknij");
		closeApplicationButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				primaryStage.close();
			}
		});
		gridPane.add(closeApplicationButton, 4, 17);
		GridPane.setHalignment(closeApplicationButton, HPos.CENTER);
	}
}