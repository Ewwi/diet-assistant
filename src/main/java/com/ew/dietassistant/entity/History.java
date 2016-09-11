package com.ew.dietassistant.entity;

import java.time.LocalDate;

public class History
{
	private int id;
	private int personId;
	private LocalDate date;


	public History(int id, LocalDate date, int personId)
	{
		this(date, personId);
		this.id = id;
	}

	public History(LocalDate date, int personId)
	{
		this.personId = personId;
		this.date = date;
	}


	public int getPersonId()
	{
		return personId;
	}

	public void setPersonId(int personId)
	{
		this.personId = personId;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + personId;
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
		History other = (History) obj;
		if (date == null)
		{
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (personId != other.personId)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "History [id=" + id + ", personId=" + personId + ", date=" + date + "]";
	}
}
