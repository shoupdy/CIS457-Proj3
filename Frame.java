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
		
		JPanel display = new JPanel(new GridBagLayout());
		display.setBackground(Color.WHITE);
		
		Label temperature = new Label("Temperature");
		Label humidity = new Label("Humidity");
		displayTemp = new JTextField(5);
		displayHum = new JTextField(5);
		
		Font boldFont = new Font("Serif", Font.BOLD, 15);
		temperature.setFont(boldFont);
		humidity.setFont(boldFont);
		
		displayTemp.setEditable(false);
		displayHum.setEditable(false);
		displayTemp.setHorizontalAlignment(JTextField.CENTER);
		displayHum.setHorizontalAlignment(JTextField.CENTER);
		
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
	    
	    display.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""), BorderFactory.createEmptyBorder(5,5,5,5)));
	    
	    add(display, BorderLayout.CENTER);
	    
	    
	    setVisible(true);
		
	}
	
	public void setTemperature(int temp)
	{
		String temperature = Integer.toString(temp);
		displayTemp.setText(temperature);
	}
	
	public void setHumidity(int hum)
	{
		String humidity = Integer.toString(hum);
		displayHum.setText(humidity);
	}

}
