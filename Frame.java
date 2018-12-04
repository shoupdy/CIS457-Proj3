/*******************************************
* Frame.java
*
* This class implements javax.swing to 
* display temperature and humidity 
* readings.
*******************************************/
import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame{
	private JTextField displayTemp;
	private JTextField displayHum;
	
	public Frame()
	{
		//Set up frame
		setTitle("Temp/Humidity Display");
		setBackground(Color.GRAY);
		setSize(250,250);
		setLayout(new BorderLayout());
		
		//Create display panel
		JPanel display = new JPanel(new GridBagLayout());
		display.setBackground(Color.WHITE);
		
		//Create label and text fields
		Label temperature = new Label("Temperature");
		Label humidity = new Label("Humidity");
		displayTemp = new JTextField(5);
		displayHum = new JTextField(5);
		
		//Change fonts
		Font boldFont = new Font("Serif", Font.BOLD, 15);
		temperature.setFont(boldFont);
		humidity.setFont(boldFont);
		
		//Set textfields to not be editable 
		displayTemp.setEditable(false);
		displayHum.setEditable(false);
		
		//Center text in textfield
		displayTemp.setHorizontalAlignment(JTextField.CENTER);
		displayHum.setHorizontalAlignment(JTextField.CENTER);
		
		//Add elements to panels
		GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.insets = new Insets(2,4,2,2);
	    c.gridx = 0;
	    c.gridy = 0;
	    
	    display.add(temperature, c);
	    
	    c.gridx = 1;
	    display.add(displayTemp, c);
	    
	    c.gridx = 0;
	    c.gridy = 1;
	    display.add(humidity, c);
	    
	    c.gridx = 1;
	    display.add(displayHum, c);
	    
	    //Set border around panel
	    display.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""), BorderFactory.createEmptyBorder(5,5,5,5)));
	    
	    //Add panel to frame
	    add(display, BorderLayout.CENTER);
	    
	    //Make frame visible
	    setVisible(true);
		
	}
	
	/*Set temperature textfield*/
	public void setTemperature(double temp)
	{
		//Format
		String temperature = String.format(" %.1f", temp);
		temperature = temperature + (char) 0xB0 + "F";
		
		//Display
		displayTemp.setText(temperature);
	}
	
	/*Set humidity textfield*/
	public void setHumidity(int hum)
	{
		//Format
		String humidity = Integer.toString(hum);
		humidity = humidity + "%";
		
		//Display
		displayHum.setText(humidity);
	}

}
