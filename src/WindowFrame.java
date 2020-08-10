
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser. FileNameExtensionFilter ;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.UIManager;
import static javax.swing.JOptionPane.showMessageDialog;

import java.io.File;

public class WindowFrame extends JFrame
{
	String[] MONTHS =	{
								"January", "February", "March", "April", "May", "June",
								"July", "August", "September",  "October",  "November",  "December"
							};

	// GUI
	private JPanel _panel;
	private JPanel _button_panel;
	private JPanel _output_panel;

	private JButton _browse_button;
	private JFileChooser _file_chooser;
	private JButton _parse_button;

	private JComboBox<String> _month_combobox;
	private JComboBox<Integer> _year_combobox;

	// USER DATA
	private String _filepath;
	private int _month_index;
	private int _year;


	WindowFrame()
	{
		super("Readings Scrubber");

		// setup
		_panel = new JPanel(new GridLayout(2, 1));
		_button_panel = new JPanel(new GridLayout(1, 4));
		_output_panel = new JPanel();

		// BROWSE
		_browse_button = new JButton("Select File");
		_browse_button.setPreferredSize(new Dimension(150, 50));
		_button_panel.add(_browse_button);
		_browse_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {select_file();}
		});

		// MONTH
		_month_combobox = new JComboBox<String>(MONTHS);
		_month_combobox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JComboBox box = (JComboBox)e.getSource();
				String month = (String)box.getSelectedItem();
				for(int x = 0; x < 12; x++) if(MONTHS[x].equals(month)) _month_index = x;
			}
		});
		_button_panel.add(_month_combobox);

		// YEAR
		Integer[] years = {2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030};
		_year_combobox = new JComboBox<Integer>(years);
		_year_combobox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JComboBox box = (JComboBox)e.getSource();
				_year = (Integer)box.getSelectedItem();
			}
		});
		_button_panel.add(_year_combobox);


		// PARSE
		_parse_button = new JButton("Find Readings");
		_parse_button.setPreferredSize(new Dimension(150, 50));
		_button_panel.add(_parse_button);
		_parse_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {scrub_file();}
		});

		// add to GUI
		_panel.add(_button_panel);
		_panel.add(_output_panel);
		this.add(_panel);

		this.pack();
		this.setPreferredSize(new Dimension(700, 300));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	private void select_file()
	{
		JFileChooser file_chooser = new JFileChooser();
		file_chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileFilter filter = new FileNameExtensionFilter("Office Open XML Document", "docx");
		file_chooser.addChoosableFileFilter(filter);

		if(file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			_filepath = file_chooser.getSelectedFile().getAbsolutePath();
	}


	private void scrub_file()
	{
		try
		{
			Scrubber scrubber = new Scrubber(_filepath, _month_index, _year);
			String output = scrubber.to_string();
			showMessageDialog(null, output);
		}
		catch(Exception exception)
		{
			showMessageDialog(null, exception.toString());
		}
	}
}
