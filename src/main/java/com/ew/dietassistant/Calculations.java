package com.ew.dietassistant;

import static com.ew.dietassistant.entity.person.Activity.HIGH;
import static com.ew.dietassistant.entity.person.Activity.LOW;
import static com.ew.dietassistant.entity.person.Activity.MEDIUM;
import static com.ew.dietassistant.entity.person.Gender.FEMALE;
import static com.ew.dietassistant.entity.person.Gender.MALE;

import java.util.List;

import com.ew.dietassistant.entity.DailyMeal;
import com.ew.dietassistant.entity.person.Person;

/**
 * Result of all calculations are rounded to two decimal places.
 */
public class Calculations
{
	private double round(double given)
	{
		double result = 0;
		result = given*100;
		result = Math.round(result);
		result = result /100;
		return result;
	}

	public double calculateDailyCaloriesIntake(Person person)
	{
		double calories = 0;

		if(person.getGender() == FEMALE)
		{
			calories = 665 + (9.6 * person.getWeight()) + (1.85 * person.getHeight()) - (4.67 * person.getAge());
		}
		else if(person.getGender() == MALE)
		{
			calories = 66.5 + 13.75 * person.getWeight() + 5 * (person.getHeight()) - 6.75 * person.getAge();
		}

		if(person.getActivity() == LOW)
		{
			calories = calories * 1.2;
		}
		if(person.getActivity() == MEDIUM)
		{
			calories = calories * 1.4;
		}
		else if (person.getActivity()== HIGH)
		{
			calories = calories * 1.6;
		}

		calories = round(calories);

		return calories;
	}

	public double calculateDailyFatIntake(Person person)
	{
		double calories = calculateDailyCaloriesIntake(person);

		double fat = calories * 0.3;
		fat = fat / 9;

		fat = round(fat);

		return fat;
	}

	public double calculateDailyProteinIntake(Person person)
	{
		double protein = person.getWeight() * 1.2;

		protein = round(protein);

		return protein;
	}

	public double calculateDailyCarbohydratesIntake(Person person)
	{
		double calories = calculateDailyCaloriesIntake(person);

		double carbo = calories * 0.56;
		carbo = carbo /4;

		carbo = round(carbo);

		return carbo;
	}

	public double calculateBMI(Person person)
	{
		double bmi = 0;
		double a = person.getWeight();
		double b = person.getHeight()*0.01;
		double baseBMI = a / (b*2);

		if(person.getGender() == (FEMALE))
		{
			bmi = 0.4 * baseBMI + 0.003 * person.getAge() + 11;
		}
		else if(person.getGender() == (MALE))
		{
			bmi = 0.5 * baseBMI + 11.5;
		}

		bmi = round(bmi);

		return bmi;
	}

	public int calculateDailyProteinPercentIntake(List<DailyMeal> someDailyMeals, Person selectPerson)
	{
		double proteinForThisDay = calculateProteinSumForThisDay(someDailyMeals);
		double proteinForThisPerson = calculateDailyProteinIntake(selectPerson);
		double proteinPercent = proteinForThisDay / proteinForThisPerson * 100;
		proteinPercent = Math.round(round(proteinPercent));
		return (int)proteinPercent;
	}

	public int calculateDailyFatPercentIntake(List<DailyMeal> someDailyMeals, Person selectPerson)
	{
		double fatForThisDay = calculateFatSumForThisDay(someDailyMeals);
		double fatForThisPerson = calculateDailyFatIntake(selectPerson);
		double fatPercent = fatForThisDay / fatForThisPerson * 100;
		fatPercent = Math.round(round(fatPercent));
		return (int)fatPercent;
	}

	public int calculateDailyCarboPercentIntake(List<DailyMeal> someDailyMeals, Person selectPerson)
	{
		double carboForThisDay = calculateCarboSumForThisDay(someDailyMeals);
		double carboForThisPerson = calculateDailyCarbohydratesIntake(selectPerson);
		double carboPercent = carboForThisDay / carboForThisPerson * 100;
		carboPercent = Math.round(round(carboPercent));
		return (int) carboPercent;
	}

	public int calculateDailyCaloriesPercentIntake(List<DailyMeal> someDailyMeals, Person selectPerson)
	{
		int caloriesForThisDay = calculateCaloriesForThisDay(someDailyMeals);
		int caloriesForThisPerson = (int)calculateDailyCaloriesIntake(selectPerson);
		double result = (double)caloriesForThisDay / caloriesForThisPerson * 100;
		return (int) result;
	}

	public double calculateCarboSumForThisDay(List<DailyMeal> someDailyMeals)
	{
		double carboForThisDay = 0;
		for (DailyMeal dailyMeal : someDailyMeals)
		{
			int grams = dailyMeal.getGrams();
			double a = dailyMeal.getFood().getCarboOfProduct() * (grams/(double)100);
			a = Math.round(a);
			carboForThisDay = carboForThisDay + a;
		}
		return carboForThisDay;
	}

	public double calculateFatSumForThisDay(List<DailyMeal> someDailyMeals)
	{
		double fatForThisDay = 0;
		for (DailyMeal dailyMeal : someDailyMeals)
		{
			int grams = dailyMeal.getGrams();
			double count = dailyMeal.getFood().getFatOfProduct()* (grams/(double)100);
			count = Math.round(count);
			fatForThisDay = fatForThisDay + count;
		}
		return fatForThisDay;
	}

	public double calculateProteinSumForThisDay(List<DailyMeal> someDailyMeals)
	{
		double proteinForThisDay = 0;
		for (DailyMeal dailyMeal : someDailyMeals)
		{
			int grams = dailyMeal.getGrams();
			double a = dailyMeal.getFood().getProteinOfProduct()* (grams/(double)100);
			a = Math.round(a);
			proteinForThisDay = proteinForThisDay + a;
		}
		return proteinForThisDay;
	}

	public int calculateCaloriesForThisDay(List<DailyMeal> someDailyMeals)
	{
		int caloriesForThisDay = 0;
		for (DailyMeal dailyMeal : someDailyMeals)
		{
			int grams = dailyMeal.getGrams();
			double count = dailyMeal.getFood().getCaloriesOfProduct()* (grams/(double)100);
			caloriesForThisDay = caloriesForThisDay + (int)count;
		}
		return caloriesForThisDay;
	}
}
