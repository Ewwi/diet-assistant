package com.ew.dietassistant.entity;

import java.time.LocalDate;

public class DailyMeal
{
	private int id;
	private int idOfPerson;
	private LocalDate dateOfDailyMeal;
	private Food food;
	private int grams;


	public DailyMeal(int idOfPerson, LocalDate dateOfDailyMeal, Food food, int gramsOfProduct)
	{
		this.dateOfDailyMeal = dateOfDailyMeal;
		this.idOfPerson = idOfPerson;
		this.food = food;
		this.grams = gramsOfProduct;
	}

	public DailyMeal(int id, int idOfPerson, LocalDate dateOfDailyMeal, Food food, int gramsOfProduct)
	{
		this(idOfPerson, dateOfDailyMeal, food, gramsOfProduct);
		this.id = id;
	}


	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getIdOfPerson()
	{
		return idOfPerson;
	}

	public void setIdOfPerson(int idOfPerson)
	{
		this.idOfPerson = idOfPerson;
	}

	public LocalDate getDateOfDailyMeal()
	{
		return dateOfDailyMeal;
	}

	public void setDateOfDailyMeal(LocalDate dateOfDailyMeal)
	{
		this.dateOfDailyMeal = dateOfDailyMeal;
	}

	public Food getFood()
	{
		return food;
	}

	public void setFood(Food food)
	{
		this.food = food;
	}

	public int getGrams()
	{
		return grams;
	}

	public void setGrams(int grams)
	{
		this.grams = grams;
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateOfDailyMeal == null) ? 0 : dateOfDailyMeal.hashCode());
		result = prime * result + ((food == null) ? 0 : food.hashCode());
		result = prime * result + grams;
		result = prime * result + id;
		result = prime * result + idOfPerson;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DailyMeal other = (DailyMeal) obj;
		if (dateOfDailyMeal == null)
		{
			if (other.dateOfDailyMeal != null)
				return false;
		} else if (!dateOfDailyMeal.equals(other.dateOfDailyMeal))
			return false;
		if (food == null)
		{
			if (other.food != null)
				return false;
		} else if (!food.equals(other.food))
			return false;
		if (grams != other.grams)
			return false;
		if (id != other.id)
			return false;
		if (idOfPerson != other.idOfPerson)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "DailyMeal [id=" + id + ", idOfPerson=" + idOfPerson + ", dateOfDailyMeal=" + dateOfDailyMeal + ", food="
				+ food + ", grams=" + grams + "]";
	}
}
