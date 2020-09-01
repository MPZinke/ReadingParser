
/***********************************************************************************************************
*
*	created by: MPZinke for St. Peter's Orthodox Church [Fort Worth, TX]
*	on 2020.08.06
*
*	DESCRIPTION:	Scrapes Bible readings from month Orthodox reading announcment. 
*						The purpose is to assist Annie Fischer in the operation of her job & 
*						save time in the monthly tediousness of this task.  May His grace be 
*						with us all.
*	BUGS:		-Does not check that inputs have been applied.
*	FUTURE:	-Better GUI (not for me though, I don't like UIs).
*				-User specified scrapping formats.
*	LICENSE:	Anyone is free to use and modify this, so long as it is within the confines of the
*				United States law & not used maliciously.  I do not assume liability for any 
*				outcomes of usage, nor do I maintain responsibility for usage, upkeep or bug
*				fixing.
*
***********************************************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;


// parser for individual day data.
// reads & organizes the information for the desired day of month.
// including saints/day info & readings for the day.
class Day
{
	int _day;  // day of month number
	int _month;  // month of year
	int _year;  // year of list
	ArrayList<String> _lines;  // lines of file for this day
	int _section_line = 0;  // last saught line number while finding section readings
	HashMap<String, HashMap<String, String>> _readings;  // organized readings for day
	String _saints;  // string of saints & other info for day

	// CONSTRUCTION

	// section out lines belonging to this day.
	// takes the day of the month, all lines of the file, the starting line number in all lines.
	// throws exception of if a reading is not found.
	Day(int day, int month, int year, String[] lines, int starting_line_number) throws Exception
	{
		_day = day;
		_month = month;
		_year = year;

		find_lines(starting_line_number, lines);
		find_readings();
		find_saints();
	}


	// add lines of document to ::_lines until next day or EOF.
	// takes the starting line number, lines to iterate through (of whole document).
	// increments the starting line number as it iterates through the document.
	private void find_lines(int line_number, String[] lines)
	{
		_lines = new ArrayList<String>();

		while(line_number < lines.length && !line_is_start_of_day(_day+1, lines[line_number]))
			_lines.add(lines[line_number++]);
	}


	// finds the four readings (M. Old, M. New, V. Old, V. New) throughout the day.
	// procedurally goes through document with ::testament_reading(.) & stores value in ::_readings.
	private void find_readings() throws Exception
	{
		_readings = new HashMap<String, HashMap<String, String>>();
		_readings.put("Matins", new HashMap<String, String>());
		_readings.get("Matins").put("Old Testament", testament_reading("Old Testament Lesson:"));
		_readings.get("Matins").put("New Testament", testament_reading("New Testament Lesson:"));

		_readings.put("Vespers", new HashMap<String, String>());
		_readings.get("Vespers").put("Old Testament", testament_reading("Old Testament Lesson:"));
		_readings.get("Vespers").put("New Testament", testament_reading("New Testament Lesson:"));
	}


	// retrieves information about day.
	// cleans up lines to remove unnecessary information through string parsing. concats information together.
	private void find_saints()
	{
		// get & clean up first line
		_saints = _lines.get(0);
		_saints = _saints.substring(_saints.indexOf("]")+4);
		_saints = _saints.substring(0, _saints.indexOf("[")).trim();

		int size = _lines.size();  // save dem cycles
		int line_number = 1;
		while(line_number < size && _lines.get(line_number).indexOf("Matins:") < 0)
		{
			_saints += "; " + _lines.get(line_number++).trim();
			if(0 <= _saints.indexOf("[")) _saints = _saints.substring(0, _saints.indexOf("["));
		}
	}


	// finds the reading based on the first occurance of the saught phrase.
	// takes a string of the phrase that indicates what is saught.
	// updates ::_section_line to keep track of prodecural searching.
	// returns everything following the saught phrase.
	private String testament_reading(String seek_phrase) throws Exception
	{
		int line_count = _lines.size();  // save a few method calls

		// search through lines until line with pharse found
		while(_section_line < line_count && _lines.get(_section_line).indexOf(seek_phrase) < 0) _section_line++;
		// throw error if line not found
		if(_section_line == line_count) throw new Exception("Unable to find all readings for "+Integer.toString(_day));

		return _lines.get(_section_line).trim().substring(seek_phrase.length()+1);
	}


	// UTILITY

	// checks whether the current line is start of the day. used to determine where day's info ends.
	// takes the next day's value (day of month), the line to be checked.
	// looks if the first word is the day's value passed & the next word is a day of week that has been bracketed.
	// returns the bool condition of this.
	public Boolean line_is_start_of_day(int day, String line)
	{
		String words[] = line.split(" ");
		return	words[0].equals(Integer.toString(day)) && 1 < words.length 
					&& word_is_bracketed_day_of_week(words[1]);
	}


	// check if the word passed is a day of the week surrounded by brackets.
	// takes the word that is to be checked.
	// returns whether the word is any of the days of week.
	public Boolean word_is_bracketed_day_of_week(String word)
	{
		// I know that the hardcoding is hideous, 
		// but the absence of array iteration saves cycles (you can still read it).
		// it would be really nice if Java had boolean multiplication (fewer branches == WAY less time)  :)
		return 	word.equals("[Sunday]") || word.equals("[Monday]") || word.equals("[Tuesday]")
					|| word.equals("[Wednesday]") || word.equals("[Thursday]") || word.equals("[Friday]")
					|| word.equals("[Saturday]");
	}


	// gets the string representation of the day of week from the first line of day.
	// parses the first thing in brackets (should be day of week).
	// returns parsed string (day of week).
	private String day_of_week()
	{
		String line = _lines.get(0);
		return line.substring(line.indexOf("[")+1, line.indexOf("]"));
	}


	// returns part of converted parsed data into string format that Annie uses (more or less).
	@Override
	public String toString()
	{
		return Integer.toString(_month+1) + "/" + Integer.toString(_day) + "/" + Integer.toString(_year) + "--" 
				+ _saints
				+ "\n" + _readings.get("Matins").get("Old Testament")
				+"/"+_readings.get("Matins").get("New Testament")
				+ "\n" + _readings.get("Vespers").get("Old Testament")
				+"/"+_readings.get("Vespers").get("New Testament")
				+ "\n";
	}
}