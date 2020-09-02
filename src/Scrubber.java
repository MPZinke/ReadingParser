
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


import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;

import java.lang.System;
// package
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;


// main functional class that scrapes information from word document.
// opens file, pulls information, divides info into Day objects (for each day).
// data is then written into a text file when ::to_file() is called.
public class Scrubber
{
	int[] MONTH_LENGTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	String _filename;

	int _month;
	int _year;
	int _month_length;

	String _file_contents;
	Day[] _days;


	// CONSTRUCTOR

	// takes the filename of the file to open & parse, month number (# of days in month), year(if leap year).
	// reads document, parses data into Day objects.
	Scrubber(String filename, int month, int year) throws Exception
	{
		_filename = filename;
		_month = month;
		_year = year;

		// check for leap year
		if(_month == 1 && _year % 4 == 0) _month_length = 29;
		else _month_length = MONTH_LENGTH[_month];

		read_document();
		parse_days();
	}


	// pull all data from document based on filename.
	// only accepts OOXML documents for now.
	public void read_document() throws Exception
	{
		FileInputStream file = new FileInputStream(_filename);
		XWPFDocument xdoc = new XWPFDocument(file);
		XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
		_file_contents = extractor.getText();
	}


	// go through each day of month & find the day in the file procedurally line by line.
	// create Day object when day found from context determined in ::line_is_start_of_day(..).
	// adds object to array.
	public void parse_days() throws Exception
	{
		String lines[] = _file_contents.split("\n");

		_days = new Day[_month_length];
		for(int day = 1, line_number = 0; day <= _month_length; day++)  // iterate days of month
		{
			// iterate 
			while(line_number < lines.length && !line_is_start_of_day(day, lines[line_number])) line_number++;

			_days[day-1] = new Day(day, _month, _year, lines, line_number);
		}
	}


	// UTILITY

	// checks whether the current line is start of the day. used to determine where day's info ends.
	// takes the next day's value (day of month), the line to be checked.
	// looks if the first word is the day's value passed & the next word is a day of week that has been bracketed.
	// returns the bool condition of this.
	public Boolean line_is_start_of_day(int day, String line)
	{
		String words[] = line.split(" ");
		return words[0].equals(Integer.toString(day)) && 1 < words.length && word_is_bracketed_day_of_week(words[1]);
	}


	// check if the word passed is a day of the week surrounded by brackets.
	// takes the word that is to be checked.
	// returns whether the word is any of the days of week.
	public Boolean word_is_bracketed_day_of_week(String word)
	{
		// I know that the hardcoding is hideous, but the absence of branching saves cycles (you can still read it) :)
		return 	word.equals("[Sunday]") || word.equals("[Monday]") || word.equals("[Tuesday]")
					|| word.equals("[Wednesday]") || word.equals("[Thursday]") || word.equals("[Friday]")
					|| word.equals("[Saturday]");
	}


	// returns converted parsed data into string format that Annie uses (more or less).
	@Override
	public String toString()
	{
		String days_string = new String();
		for(int x = 0; x < _days.length; x++) days_string += _days[x].toString()+"\n";

		return days_string;
	}


	// creates a text file containing the desired data determined in ::toString() -> Day::toString().
	public void to_file(String path) throws IOException
	{
		String filename =	path+"\\Readings_"+Integer.toString(_month+1)+"_"+Integer.toString(_year)+".txt";
		FileWriter writer = new FileWriter(filename);
		writer.write(this.toString());
		writer.close();
	}
}