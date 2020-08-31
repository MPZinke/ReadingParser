
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


import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser. FileNameExtensionFilter ;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import static javax.swing.JOptionPane.showMessageDialog;

import java.io.File;


// main GUI class.
// takes user input for selecting file, determining days in month, calling Scrubber class.
public class WindowFrame extends JFrame
{
	String[] MONTHS =	{
								"January", "February", "March", "April", "May", "June",
								"July", "August", "September",  "October",  "November",  "December"
							};  // used as selection for determing length of month

	// GUI
	private JPanel _panel;
	private JPanel _button_panel;
	private JPanel _output_panel;

	private JButton _input_browse_button;
	private JButton _output_browse_button;
	private JButton _parse_button;

	private JComboBox<String> _month_combobox;
	private JComboBox<Integer> _year_combobox;

	// USER DATA
	private String _input_filepath = null;
	private String _output_folderpath = null;
	private int _month_index;
	private int _year;


	// creates window object & sets up functionality.
	WindowFrame()
	{
		super("Readings Scrubber");

		// WINDOW
		_panel = new JPanel(new GridLayout(2, 1));
		_button_panel = new JPanel(new GridLayout(1, 3));
		_output_panel = new JPanel(new GridLayout(1,1));

		// BROWSE INPUT FILES
		_input_browse_button = new JButton("Select Readings File");
		_input_browse_button.setPreferredSize(new Dimension(150, 50));
		_input_browse_button.setBackground(Properties.Purple);
		_input_browse_button.setForeground(Properties.Orange);
		_input_browse_button.setBorder(BorderFactory.createBevelBorder(0));
		_button_panel.add(_input_browse_button);
		_input_browse_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {select_input_file();}
		});

		// BROWSE INPUT FOLDER
		_output_browse_button = new JButton("Select Output Folder");
		_output_browse_button.setPreferredSize(new Dimension(150, 50));
		_output_browse_button.setBackground(Properties.Purple);
		_output_browse_button.setForeground(Properties.Orange);
		_output_browse_button.setBorder(BorderFactory.createBevelBorder(0));
		_button_panel.add(_output_browse_button);
		_output_browse_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {select_output_file();}
		});

		// MONTH
		_month_combobox = new JComboBox<String>(MONTHS);
		_month_combobox.setBackground(Properties.Purple);
		_month_combobox.setForeground(Properties.Orange);
		_month_combobox.setBorder(BorderFactory.createBevelBorder(0));
		_month_index = 0;  // HARDCODED
		_month_combobox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JComboBox<String> box = (JComboBox<String>)e.getSource();
				String month = (String)box.getSelectedItem();
				for(int x = 0; x < 12; x++) if(MONTHS[x].equals(month)) _month_index = x;
			}
		});
		_button_panel.add(_month_combobox);

		// YEAR
		Integer[] years = {2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030};  // HARDCODED
		_year_combobox = new JComboBox<Integer>(years);
		_year_combobox.setBackground(Properties.Purple);
		_year_combobox.setForeground(Properties.Orange);
		_year_combobox.setBorder(BorderFactory.createBevelBorder(0));
		_year = 2020;  // HARDCODED
		_year_combobox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JComboBox<Integer> box = (JComboBox<Integer>)e.getSource();
				_year = years[(Integer)box.getSelectedItem()];
			}
		});
		_button_panel.add(_year_combobox);

		// PARSE
		_parse_button = new JButton("Please Select File & Date First");
		_parse_button.setPreferredSize(new Dimension(150, 50));
		_parse_button.setBackground(Properties.Purple);
		_parse_button.setForeground(Properties.Orange);
		_parse_button.setBorder(BorderFactory.createBevelBorder(0));
		_parse_button.setEnabled(false);
		_output_panel.add(_parse_button);
		_parse_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {scrub_file();}
		});

		// SETUP
		_panel.add(_button_panel);
		_panel.add(_output_panel);
		this.add(_panel);

		this.pack();
		this.setPreferredSize(new Dimension(700, 300));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	// creates a JFileChooser to browse files on computer.
	// called by _input_browse_button ActionEvent. sets ::_input_filepath from selected file.
	private void select_input_file()
	{
		JFileChooser file_chooser = new JFileChooser();
		file_chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileFilter filter = new FileNameExtensionFilter("Office Open XML Document", "docx");
		file_chooser.addChoosableFileFilter(filter);

		if(file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			_input_filepath = file_chooser.getSelectedFile().getAbsolutePath();
			_parse_button.setText("Find Readings for "+_input_filepath);
		}

		if(_input_filepath != null && _output_folderpath != null) _parse_button.setEnabled(true);
	}


	private void select_output_file()
	{
		JFileChooser file_chooser = new JFileChooser();
		file_chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		FileFilter filter = new FileNameExtensionFilter("Office Open XML Document", "docx");
		file_chooser.addChoosableFileFilter(filter);

		if(file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			_output_folderpath = file_chooser.getSelectedFile().getAbsolutePath();
			_parse_button.setText("Find Readings for "+_output_folderpath);
		}

		if(_input_filepath != null && _output_folderpath != null) _parse_button.setEnabled(true);
	}


	// creates a Scrubber object to parse the file for relevent data.
	// writes the data to an output file.
	private void scrub_file()
	{
		try
		{
			Scrubber scrubber = new Scrubber(_input_filepath, _month_index, _year);
			scrubber.to_file(_output_folderpath);
			showMessageDialog(null, "Successfully created file in "+_output_folderpath);
		}
		catch(Exception exception)
		{
			showMessageDialog(null, exception.toString());
		}
	}
}
