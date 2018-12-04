/*******************************************
* Server.java
* CIS 457-01
* Dylan Shoup
* Ali Scheffler
* 
* This class implements the server for IoT
* system. Reads temperature and humidty
* values from firebase and sends through
* connection to client.
*******************************************/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Server {
	
	public String sen;
	
	public static void main(String args[]) throws IOException, InterruptedException{
		ServerHandler handler = new ServerHandler();
		handler.start();
	}

}

/*Class for the ServerSocket thread and the Firebase data*/
class ServerHandler extends Thread{
	ServerSocket welcomeSocket;
	public FBClass fb;
	public ServerHandler() throws IOException{
		welcomeSocket = new ServerSocket(3702);
	}
	
	public void run(){
		fb = new FBClass();
		try {
			//Get data from firebase
			fb.getData();
		} catch (InterruptedException | IOException e1) {
			e1.printStackTrace();
		}
		while(true){
			try {
				//Create connection socket
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println("Client Connected\n");
				
				//Start client handler
				ClientHandler handler = new ClientHandler(connectionSocket, this);
	            handler.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
/*Thread for each client.*/
class ClientHandler extends Thread{
	ServerHandler parent;
	Socket connection;
	
	private ObjectOutputStream objectOutToClient;
	private BufferedReader inFromClient;
	
	public ClientHandler(Socket connectionSocket, ServerHandler serv) throws IOException{
		connection = connectionSocket;
		parent = serv;
		
		//Create streams
		inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		objectOutToClient = new ObjectOutputStream(connection.getOutputStream());
	}
	
	public void run(){
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
			
			try {
				//Write last object in Firebase to client
				objectOutToClient.writeObject(parent.fb.sen.get(parent.fb.sen.size()-1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		try {
			objectOutToClient.flush();
			int size = parent.fb.sen.size();
			
			//Update client every second
			while(!inFromClient.ready()){
				Thread.sleep(1000);
				int size2 = parent.fb.sen.size();
				if(size2 > size){
					size = size2;
					objectOutToClient.writeObject(parent.fb.sen.get(size - 1));
					objectOutToClient.flush();
				}
			}
			
			//Client disconnected, close socket and streams
			System.out.println("Client disconnected\n");
			inFromClient.close();
			objectOutToClient.close();
			connection.close();
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
class SensorData implements java.io.Serializable{
	public int humidity;
	public int temperature;
}

class FBClass{
	ArrayList<SensorData> sen = new ArrayList<SensorData>();
	public void getData() throws InterruptedException, IOException{
		FileInputStream serviceAccount =
				  new FileInputStream("C:\\Users\\schef\\OneDrive\\Documents\\GitHub\\CIS457-Proj3\\nodemcu-ac90a-firebase-adminsdk-d999q-4d2f43ad9c.json");//Change path!

				FirebaseOptions options = new FirebaseOptions.Builder()
				  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
				  .setDatabaseUrl("https://nodemcu-ac90a.firebaseio.com")
				  .build();

				FirebaseApp.initializeApp(options);
		
final FirebaseDatabase database = FirebaseDatabase.getInstance();
		
		DatabaseReference ref = database.getReference("sensor/values");
		ref.addValueEventListener(new ValueEventListener(){
			@Override
			  public void onDataChange(DataSnapshot dataSnapshot) {
				if(dataSnapshot != null){
					sen.clear();
					for(DataSnapshot datas : dataSnapshot.getChildren()){
						System.out.println(datas.getValue());
					SensorData sensorData = datas.getValue(SensorData.class);
					
					sen.add(sensorData);
					
					
					}
				}
			    
			  }

			  @Override
			  public void onCancelled(DatabaseError databaseError) {
			    System.out.println("The read failed: " + databaseError.getCode());
		}
});
		
		
	}
	
}