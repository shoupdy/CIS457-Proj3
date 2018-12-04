/*******************************************
* Disconnect.java
*
* This class is used to call the host 
* disconnect function when the GUI is closed
* out of.
*******************************************/

import java.awt.event.*;

public class Disconnect extends WindowAdapter {
	private Frame gui;
	private Client host;
	
	public Disconnect(Frame gui, Client host)
	{
		this.gui = gui;
		this.host = host;
	}
	
	/*Call disconnect function on window closing event*/
	public void windowClosing(WindowEvent e){
		
		host.disconnect();
	}

	
}
