package com.ew.dietassistant.entity.person;

public class Person
{
	private String name;
	private Gender gender;
	private int age;
	private int height;
	private int weight;
	private int id;
	private Activity activity;

	public Person(String name, Gender gender, int age, int height, int weight, Activity activity)
	{
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.activity = activity;
	}

	public Person(int id, String name, Gender gender, int age, int height, int weight, Activity activity)
	{
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.id = id;
		this.height = height;
		this.weight = weight;
		this.activity = activity;
	}

	public Activity getActivity()
	{
		return activity;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int getAge()
	{
		return age;
	}

	public Gender getGender()
	{
		return gender;
	}

	public int getHeight()
	{
		return height;
	}

	public int getWeight()
	{
		return weight;
	}

	@Override
	public String toString()
	{
		return "Person [name=" + name + ", gender=" + gender + ", age=" + age + ", height=" + height + ", weight="
				+ weight + ", id=" + id + ", activity=" + activity + "]";
	}
}