import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Client {
	private int humidity;
	private int temperature;
	private boolean done = false;
	
	@SuppressWarnings("resource")
	public Client(){
		Frame gui = new Frame();
		gui.addWindowListener(new Disconnect(gui, this));
		
		try {
		Socket connection = new Socket("localhost",3702);
		
		DataOutputStream outToServer = new DataOutputStream(connection.getOutputStream());
		
		ObjectInputStream objectInFromServer = new ObjectInputStream(connection.getInputStream());
		ArrayList<SensorData> sensor = new ArrayList<SensorData>(); //Has all sensor data.
		while(!done){
			do{
				Thread.sleep(500);
				sensor.add((SensorData)objectInFromServer.readObject());		
		
				System.out.println(sensor.get(sensor.size()-1).humidity);
				int temp = sensor.get(sensor.size()-1).temperature;
				int hum = sensor.get(sensor.size()-1).humidity;
				
				gui.setTemperature(temp);
				gui.setHumidity(hum);
		
			}while(objectInFromServer.available() <= 0);
				
		}
		objectInFromServer.close();
		outToServer.close();
		connection.close();
		}catch(IOException e)
		{
			
		}catch(InterruptedException ie)
		{
			
		}catch(ClassNotFoundException ce)
		{
			
		}
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException{
		Client cli = new Client();
	}
	
	public void setDone(boolean status)
	{
		this.done = true;
	}
	

}
