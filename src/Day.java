

import java.util.ArrayList;
import java.util.HashMap;


class Day
{
	int _day;
	ArrayList<String> _lines;
	int _section_line;
	HashMap<String, HashMap<String, String>> _readings;

	// section out lines belonging to this day
	Day(int day, String[] lines, int line_number)
	{
		_day = day;
		_lines = new ArrayList<String>();

		while(line_number < lines.length && !line_is_start_of_day(day+1, lines[line_number]))
			_lines.add(lines[line_number++]);

	}


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


	// UTILITY
	public void print_lines()
	{
		for(int x = 0; x < _lines.size(); x++) System.out.println(_lines.get(x));
	}


	public void print_readings()
	{
		System.out.println(Integer.toString(_day));
		System.out.println("\tMatins: "+_readings.get("Matins").get("Old Testament")+"/"+_readings.get("Matins").get("New Testament"));
		System.out.println("\tVespers: "+_readings.get("Vespers").get("Old Testament")+"/"+_readings.get("Vespers").get("New Testament"));
	}


	@Override
	public String toString()
	{
		String day_string = Integer.toString(_day);
		day_string += "\n\tMatins: "+_readings.get("Matins").get("Old Testament")+"/"+_readings.get("Matins").get("New Testament");
		day_string += "\n\tVespers: "+_readings.get("Vespers").get("Old Testament")+"/"+_readings.get("Vespers").get("New Testament");

		return day_string;
	}


	// FIND READINGS
	public void find_readings() throws Exception
	{
		int _section_line = 0;

		_readings = new HashMap<String, HashMap<String, String>>();
		_readings.put("Matins", new HashMap<String, String>());
		_readings.get("Matins").put("Old Testament", testament_reading("Old Testament Lesson:"));
		_readings.get("Matins").put("New Testament", testament_reading("New Testament Lesson:"));

		_readings.put("Vespers", new HashMap<String, String>());
		_readings.get("Vespers").put("Old Testament", testament_reading("Old Testament Lesson:"));
		_readings.get("Vespers").put("New Testament", testament_reading("New Testament Lesson:"));
	}


	public String testament_reading(String seek_phrase) throws Exception
	{
		int line_count = _lines.size();

		while(_section_line < line_count && _lines.get(_section_line).indexOf(seek_phrase) < 0) _section_line++;
		if(_section_line == line_count) throw new Exception("Unable to find all readings for "+Integer.toString(_day));

		return _lines.get(_section_line).trim().substring(22);
	}
}