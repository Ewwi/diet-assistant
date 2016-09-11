package com.ew.dietassistant;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class DateTimeLookup
{
	public String getMonthName(LocalDate dateTime)
	{
		Month currentMonth = dateTime.getMonth();
		String monthName = "";

		switch(currentMonth)
		{
		case JANUARY:
			monthName = "Stycze�";
			break;
		case FEBRUARY:
			monthName = "Luty";
			break;
		case MARCH:
			monthName = "Marzec";
			break;
		case APRIL:
			monthName = "Kwiecie�";
			break;
		case MAY:
			monthName = "Maj";
			break;
		case JUNE:
			monthName = "Czerwiec";
			break;
		case JULY:
			monthName = "Lipiec";
			break;
		case AUGUST:
			monthName = "Sierpie�";
			break;
		case SEPTEMBER:
			monthName = "Wrzesie�";
			break;
		case OCTOBER:
			monthName = "Pa�dziernik";
			break;
		case NOVEMBER:
			monthName = "Listopad";
			break;
		case DECEMBER:
			monthName = "Grudzie�";
			break;
		}
		return monthName;
	}

	public String getNameOfDayOfTheWeek(LocalDate dateTime)
	{
		DayOfWeek currentDayOfWeek = dateTime.getDayOfWeek();
		String dayName = "";

		switch(currentDayOfWeek)
		{
		case MONDAY:
			dayName = "Poniedzia�ek";
			break;
		case TUESDAY:
			dayName = "Wtorek";
			break;
		case WEDNESDAY:
			dayName = "�roda";
			break;
		case THURSDAY:
			dayName = "Czwartek";
			break;
		case FRIDAY:
			dayName = "Pi�tek";
			break;
		case SATURDAY:
			dayName = "Sobota";
			break;
		case SUNDAY:
			dayName = "Niedziela";
			break;
		}
		return dayName;
	}

	public static String addLeadingZeroIfHasOnlyOneDigit(int number)
	{
		if(number >= 10)
		{
			return String.valueOf(number);
		}
		else
		{
			return "0" + number;
		}
	}
}
