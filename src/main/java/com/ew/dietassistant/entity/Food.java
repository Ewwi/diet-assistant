package com.ew.dietassistant.entity;

public class Food
{
	private int id;
	private String foodName;
	private int caloriesOfFood;
	private double fatOfFood;
	private double proteinOfFood;
	private double carboOfFood;
	private int grams;


	public Food(String foodName, int caloriesOfFood, double proteinOfFood, double fatOfFood, double carboOfFood, int grams)
	{
		this.foodName = foodName;
		this.caloriesOfFood = caloriesOfFood;
		this.fatOfFood = fatOfFood;
		this.proteinOfFood = proteinOfFood;
		this.carboOfFood = carboOfFood;
		this.grams = grams;
	}

	public Food(int idOfFood, String foodName, int grams, int caloriesOfFood, double fatOfFood, double proteinOfFood, double carboOfFood)
	{
		this(foodName, caloriesOfFood, proteinOfFood, fatOfFood, carboOfFood, grams);
		this.id = idOfFood;
	}


	public int getQuantity()
	{
		return grams;
	}

	public void setQuantity(int quantity)
	{
		this.grams = quantity;
	}

	public String getProductName()
	{
		return foodName;
	}

	public void setProductName(String productName)
	{
		this.foodName = productName;
	}

	public int getCaloriesOfProduct()
	{
		return caloriesOfFood;
	}

	public void setCaloriesOfProduct(int caloriesOfProduct)
	{
		this.caloriesOfFood = caloriesOfProduct;
	}

	public double getFatOfProduct()
	{
		return fatOfFood;
	}

	public void setFatOfProduct(double fatOfProduct)
	{
		this.fatOfFood = fatOfProduct;
	}

	public double getProteinOfProduct()
	{
		return proteinOfFood;
	}

	public void setProteinOfProduct(double proteinOfProduct)
	{
		this.proteinOfFood = proteinOfProduct;
	}

	public double getCarboOfProduct()
	{
		return carboOfFood;
	}

	public void setCarboOfProduct(double carboOfProduct)
	{
		this.carboOfFood = carboOfProduct;
	}

	public int getIdOfProduct()
	{
		return id;
	}

	public void setIdOfProduct(int idOfProduct)
	{
		this.id = idOfProduct;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + caloriesOfFood;
		long temp;
		temp = Double.doubleToLongBits(carboOfFood);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(fatOfFood);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((foodName == null) ? 0 : foodName.hashCode());
		result = prime * result + grams;
		result = prime * result + id;
		temp = Double.doubleToLongBits(proteinOfFood);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Food other = (Food) obj;
		if (caloriesOfFood != other.caloriesOfFood)
			return false;
		if (Double.doubleToLongBits(carboOfFood) != Double.doubleToLongBits(other.carboOfFood))
			return false;
		if (Double.doubleToLongBits(fatOfFood) != Double.doubleToLongBits(other.fatOfFood))
			return false;
		if (foodName == null)
		{
			if (other.foodName != null)
				return false;
		} else if (!foodName.equals(other.foodName))
			return false;
		if (grams != other.grams)
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(proteinOfFood) != Double.doubleToLongBits(other.proteinOfFood))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return foodName;
	}
}
