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
	
	public Client(){
		
	}
	
	@SuppressWarnings("resource")
	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException{
		Socket connection = new Socket("localhost",3702);
		
		DataOutputStream outToServer = new DataOutputStream(connection.getOutputStream());
		
		ObjectInputStream objectInFromServer = new ObjectInputStream(connection.getInputStream());
		ArrayList<SensorData> sensor = new ArrayList<SensorData>(); //Has all sensor data.
		while(true){
		do{
		Thread.sleep(500);
		sensor.add((SensorData)objectInFromServer.readObject());		
		
		System.out.println(sensor.get(sensor.size()-1).humidity);
		}while(objectInFromServer.available() <= 0);
		
		}
	}
}
