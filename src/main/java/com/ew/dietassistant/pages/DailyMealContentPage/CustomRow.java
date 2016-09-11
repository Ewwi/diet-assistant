package com.ew.dietassistant.pages.DailyMealContentPage;

class CustomRow
{
	private String rowHeading;
	private int calories;
	private double fat;
	private double protein;
	private double carbo;


	CustomRow(String rowHeading, int calories, double protein, double fat, double carbo)
	{
		this.rowHeading = rowHeading;
		this.calories = calories;
		this.fat = fat;
		this.protein = protein;
		this.carbo = carbo;
	}


	String getRowHeading()
	{
		return rowHeading;
	}

	int getCalories()
	{
		return calories;
	}

	double getFat()
	{
		return fat;
	}

	double getProtein()
	{
		return protein;
	}

	double getCarbo()
	{
		return carbo;
	}
}
