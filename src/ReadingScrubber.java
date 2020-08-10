
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

// GUI
import static javax.swing.JOptionPane.showMessageDialog;


// main object for running program.
// relies on apache-poi libraries: https://poi.apache.org/apidocs/4.1/
public class ReadingScrubber
{
	public static void main(String[] args)
	{
		try
		{
			new WindowFrame();
		}
		catch (Exception exception)
		{
			showMessageDialog(null, exception.toString());
		}
	}
}
