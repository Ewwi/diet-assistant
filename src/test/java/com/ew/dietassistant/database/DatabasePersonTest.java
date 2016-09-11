package com.ew.dietassistant.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ew.dietassistant.entity.person.Activity;
import com.ew.dietassistant.entity.person.Gender;
import com.ew.dietassistant.entity.person.Person;

public class DatabasePersonTest
{
	@Before
	public void beforeEachMethod()
	{

		DatabasePerson database = new DatabasePerson("testDatabase.db");
		database.dropTable();
	}


	@Test
	public void testInsert()
	{
		// given
		DatabasePerson database = new DatabasePerson("testDatabase.db");
		Person expectedPerson = new Person(0, "Ewelinka", Gender.FEMALE, 23, 180, 67, Activity.LOW);

		// when
		database.insertPerson(expectedPerson);
		Person actualPerson = database.selectAllPersons().get(0);

		// then
		assertPersonEqual(expectedPerson, actualPerson);
	}

	public void assertPersonEqual(Person testPerson, Person personFromDatabase)
	{
		assertEquals(testPerson.getName(), personFromDatabase.getName());
		assertEquals(testPerson.getAge(), personFromDatabase.getAge());
		assertEquals(testPerson.getGender(), personFromDatabase.getGender());
		assertEquals(testPerson.getHeight(), personFromDatabase.getHeight());
		assertEquals(testPerson.getWeight(), personFromDatabase.getWeight());
	}

	@Test
	public void testDatabaseIsEmpty()
	{
		// given
		DatabasePerson database = new DatabasePerson("testDatabase.db");

		// when
		List<Person> peopleInDatabase = database.selectAllPersons();

		// then
		assertTrue(peopleInDatabase.isEmpty());
	}
}
