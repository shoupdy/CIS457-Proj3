/*******************************************
* Client.java
* CIS 457-01
* Dylan Shoup
* Ali Scheffler
* 
* This class implements the client side for
* the IoT system. Gets temperature and 
* humidity values from server and displays.
*******************************************/

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
	private DataOutputStream outToServer;
	private ObjectInputStream objectInFromServer;
	private Socket connection;
	
	@SuppressWarnings("resource")
	public Client(){
		Frame gui = new Frame();
		gui.addWindowListener(new Disconnect(gui, this));
		
		try {
			connection = new Socket("localhost",3702);
		
			outToServer = new DataOutputStream(connection.getOutputStream());
		
			objectInFromServer = new ObjectInputStream(connection.getInputStream());
			ArrayList<SensorData> sensor = new ArrayList<SensorData>(); //Has all sensor data.
			while(true){
				do{
					Thread.sleep(500);
					
					sensor.add((SensorData)objectInFromServer.readObject());		
			
					System.out.println(sensor.get(sensor.size()-1).humidity);
					
					//Get readings
					int temp = sensor.get(sensor.size()-1).temperature;
					int hum = sensor.get(sensor.size()-1).humidity;
					
					//Convert to fahrenheit
					double tempF = (temp * (9.0/5.0) + 32.0);
					
					//Set display
					gui.setTemperature(tempF);
					gui.setHumidity(hum);
			
			}while(objectInFromServer.available() <= 0);
				
		}
		
		}catch(IOException e)
		{
			System.out.println("IO ERROR");
		}catch(InterruptedException ie)
		{
			System.out.println("ERROR");
		}catch(ClassNotFoundException ce)
		{
			System.out.println("Class Not Found ERROR");
		}
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException{
		Client cli = new Client();
	}
	
	/*Disconnect from server*/
	public void disconnect(){
		try{
			
			outToServer.flush();
			outToServer.writeBytes("Close\n");

			//Close control socket
			objectInFromServer.close();
			outToServer.close();
			connection.close();

			//Exit host
			System.exit(0);
		}catch(IOException e)
		{
			System.out.println("ERROR");
		}
	}

	

}