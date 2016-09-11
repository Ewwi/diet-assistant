package com.ew.dietassistant.pages.welcome;

import java.time.DateTimeException;
import java.time.LocalDate;

import com.ew.dietassistant.DateTimeLookup;
import com.ew.dietassistant.Main;
import com.ew.dietassistant.database.DatabaseHistory;
import com.ew.dietassistant.database.DatabaseProvider;
import com.ew.dietassistant.entity.History;
import com.ew.dietassistant.pages.StartPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

class PopupCreateNewDay
{
	private Scene scenePopup;
	private String cssPath;
	private Stage popUpStage;
	private int personId;
	private int year;
	private int month;
	private int day;
	private Stage primaryStage;
	private String dialogContent;


	PopupCreateNewDay(String cssPath, int personId, Stage primaryStage)
	{
		this.cssPath = cssPath;
		this.personId = personId;
		this.primaryStage = primaryStage;
	}


	Stage getPopUpStage()
	{
		return popUpStage;
	}

	void initializePopup()
	{
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(25, 25, 25, 25));
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		gridPane.setGridLinesVisible(Main.GRID_LINES_VISIBLE);

		ColumnConstraints columnContraint1 = new ColumnConstraints(100);
		gridPane.getColumnConstraints().add(columnContraint1);

		ColumnConstraints columnContraint2 = new ColumnConstraints(50);
		gridPane.getColumnConstraints().add(columnContraint2);

		ColumnConstraints columnContraint3 = new ColumnConstraints(50);
		gridPane.getColumnConstraints().add(columnContraint3);

		ColumnConstraints columnContraint4 = new ColumnConstraints(100);
		gridPane.getColumnConstraints().add(columnContraint4);

		RadioButton currentDayRadioButton = new RadioButton();
		currentDayRadioButton.setSelected(true);
		Label radioButtonLabel = new Label("stwórz dzieñ z dzisiejsz¹ dat¹");
		gridPane.add(radioButtonLabel, 0, 0);

		HBox radioHB = new HBox(10, radioButtonLabel, currentDayRadioButton);
		gridPane.add(radioHB, 0, 0, 4, 1);
		radioHB.setAlignment(Pos.CENTER);

		Label yearLabel = new Label("rok: ");
		gridPane.add(yearLabel, 1, 1);
		Label monthLabel = new Label("miesi¹c: ");
		gridPane.add(monthLabel, 1, 2);
		Label dayLabel = new Label("dzieñ: ");
		gridPane.add(dayLabel, 1, 3);

		TextField yearTextField = new TextField();
		gridPane.add(yearTextField, 2, 1);
		yearTextField.setMaxWidth(50);

		TextField monthTextField = new TextField();
		gridPane.add(monthTextField, 2, 2);
		monthTextField.setMaxWidth(50);

		TextField dayTextField = new TextField();
		gridPane.add(dayTextField, 2, 3);
		dayTextField.setMaxWidth(50);

		currentDayRadioButtonActions(currentDayRadioButton, yearTextField, monthTextField, dayTextField);

		Button newDateButton = createNewDayButton(yearTextField, monthTextField, dayTextField);

		gridPane.add(newDateButton, 0, 4, 4, 1);
		GridPane.setHalignment(newDateButton, HPos.CENTER);

		Text notification = new Text();
		gridPane.add(notification, 0, 5, 4, 1);
		notification.setId("notification");

		scenePopup = new Scene(gridPane, 400, 250);
		scenePopup.getStylesheets().add(cssPath);

		popUpStage = new Stage();
		popUpStage.setScene(scenePopup);
		popUpStage.initModality(Modality.APPLICATION_MODAL);
		popUpStage.setTitle("Stwórz nowy dzieñ");
	}

	private Button createNewDayButton(TextField yearTextField, TextField monthTextField, TextField dayTextField)
	{
		Button newDateButton = new Button("stwórz dzieñ");

		newDateButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				try
				{
					year = Integer.parseInt(yearTextField.getText());
					month = Integer.parseInt(monthTextField.getText());
					day = Integer.parseInt(dayTextField.getText());
					History dateInDailyMeals = new History(LocalDate.of(year, month, day), personId);

					DatabaseHistory databaseHistory = DatabaseProvider.getDatabaseHistory();
					databaseHistory.insertHistory(dateInDailyMeals);
					popUpStage.close();
					StartPage startPage = new StartPage(cssPath, primaryStage);
					WelcomePage welcomePage = new WelcomePage(cssPath, primaryStage, startPage.getSceneStart(), personId);
					welcomePage.initializePage();
				}
				catch(DateTimeException | NumberFormatException e)
				{
					dialogContent = "wpisz poprawne dane.";
					initializeNotificationPopUp();
				}
				catch(RuntimeException e)
				{
					dialogContent = "ta data ju¿ istnieje.";
					initializeNotificationPopUp();
				}
			}
		});

		return newDateButton;
	}

	private void currentDayRadioButtonActions(RadioButton currentDayRadioButton, TextField yearTextField,
			TextField monthTextField, TextField dayTextField)
	{
		if(currentDayRadioButton.isSelected())
		{
			year = LocalDate.now().getYear();
			month = LocalDate.now().getMonthValue();
			day = LocalDate.now().getDayOfMonth();

			yearTextField.setText("" + year);
			monthTextField.setText(DateTimeLookup.addLeadingZeroIfHasOnlyOneDigit(month));
			dayTextField.setText(DateTimeLookup.addLeadingZeroIfHasOnlyOneDigit(day));
		}
		currentDayRadioButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				createDayHeader(currentDayRadioButton, yearTextField, monthTextField, dayTextField);
			}

			private void createDayHeader(RadioButton currentDayRadioButton, TextField yearTextField, TextField monthTextField,
					TextField dayTextField)
			{
				if(currentDayRadioButton.isSelected() == false)
				{
					yearTextField.clear();
					monthTextField.clear();
					dayTextField.clear();
				}
				else
				{
					year = LocalDate.now().getYear();
					month = LocalDate.now().getMonthValue();
					day = LocalDate.now().getDayOfMonth();

					yearTextField.setText("" + year);
					monthTextField.setText(DateTimeLookup.addLeadingZeroIfHasOnlyOneDigit(month));
					dayTextField.setText(DateTimeLookup.addLeadingZeroIfHasOnlyOneDigit(day));
				}
			}
		});
	}

	private void initializeNotificationPopUp()
	{
		ButtonType loginButtonType = new ButtonType("Zamknij", ButtonData.OK_DONE);
		Dialog<String> dialog = new Dialog<>();
		dialog.getDialogPane().setContentText(dialogContent);
		dialog.getDialogPane().getButtonTypes().add(loginButtonType);
		dialog.show();
	}
}
