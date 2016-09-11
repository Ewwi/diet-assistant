package com.ew.dietassistant.pages;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Clock
{
	private Label clockLabel;

	public void createClock()
	{
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy     HH:mm:ss");
		clockLabel = new Label();
		Timeline timeLine = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				LocalDateTime now = LocalDateTime.now();
				String formattedDateTime = now.format(dateFormatter);
				clockLabel.setText(formattedDateTime);
			}
		}));
		timeLine.setCycleCount(Animation.INDEFINITE);
		timeLine.play();
	}

	public Label getClockLabel()
	{
		return clockLabel;
	}
}
