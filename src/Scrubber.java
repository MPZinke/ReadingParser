

import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.lang.System;
// package
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;


public class Scrubber
{
	int[] MONTH_LENGTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	String _filename;

	int _month;
	int _year;
	int _month_length;

	String _file_contents;
	Day[] _days;

	Scrubber(String filename, int month, int year) throws Exception
	{
		_filename = filename;
		_month = month;
		_year = year;

		// check for leap year
		if(_month == 2 && _year % 4 == 0) _month_length = 29;
		else _month_length = MONTH_LENGTH[_month];

		read_document();
		parse_days();
	}


	public void read_document() throws Exception
	{
		FileInputStream file = new FileInputStream(_filename);
		XWPFDocument xdoc = new XWPFDocument(file);
		XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
		_file_contents = extractor.getText();
	}


	public void parse_days() throws Exception
	{
		String lines[] = _file_contents.split("\n");

		_days = new Day[_month_length];
		for(int day = 1, line_number = 0; day <= _month_length; day++)  // iterate days of month
		{
			// iterate 
			while(line_number < lines.length && !line_is_start_of_day(day, lines[line_number])) line_number++;

			_days[day-1] = new Day(day, lines, line_number);
			_days[day-1].find_readings();
		}
	}


	// UTILITY
	public Boolean line_is_start_of_day(int day, String line)
	{
		String words[] = line.split(" ");
		return words[0].equals(Integer.toString(day)) && 1 < words.length && word_is_bracketed_day_of_week(words[1]);
	}


	public Boolean word_is_bracketed_day_of_week(String word)
	{
		// I know that the hardcoding is hideous, but the absence of branching saves cycles (you can still read it) :)
		return 	word.equals("[Sunday]") || word.equals("[Monday]") || word.equals("[Tuesday]")
					|| word.equals("[Wednesday]") || word.equals("[Thursday]") || word.equals("[Friday]")
					|| word.equals("[Saturday]");
	}


	public String to_string() throws Exception
	{
		String days_string = new String();
		for(int x = 0; x < _days.length; x++) days_string += _days[x].toString() + "\n";

		return days_string;
	}
}