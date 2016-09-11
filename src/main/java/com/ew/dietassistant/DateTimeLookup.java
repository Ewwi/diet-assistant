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
			monthName = "Styczeñ";
			break;
		case FEBRUARY:
			monthName = "Luty";
			break;
		case MARCH:
			monthName = "Marzec";
			break;
		case APRIL:
			monthName = "Kwiecieñ";
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
			monthName = "Sierpieñ";
			break;
		case SEPTEMBER:
			monthName = "Wrzesieñ";
			break;
		case OCTOBER:
			monthName = "PaŸdziernik";
			break;
		case NOVEMBER:
			monthName = "Listopad";
			break;
		case DECEMBER:
			monthName = "Grudzieñ";
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
			dayName = "Poniedzia³ek";
			break;
		case TUESDAY:
			dayName = "Wtorek";
			break;
		case WEDNESDAY:
			dayName = "Œroda";
			break;
		case THURSDAY:
			dayName = "Czwartek";
			break;
		case FRIDAY:
			dayName = "Pi¹tek";
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
