import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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
	
	
	public Server() throws IOException{
		
		
	}
	
	public static void main(String args[]) throws IOException, InterruptedException{
		ServerHandler handler = new ServerHandler();
		handler.start();

}

}

//Class for the ServerSocket thread and the Firebase data.
class ServerHandler extends Thread{
	ServerSocket welcomeSocket;
	public FBClass fb;
	public ServerHandler() throws IOException{
		welcomeSocket = new ServerSocket(3702);
	}
	
	public void run(){
		fb = new FBClass();
		try {
			fb.getData();
		} catch (InterruptedException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
			try {
				Socket connectionSocket = welcomeSocket.accept();
				System.out.println("Client Connected\n");
				ClientHandler handler = new ClientHandler(connectionSocket, this);
	            handler.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
//Thread for each client.
class ClientHandler extends Thread{
	ServerHandler parent;
	Socket connection;
	
	private ObjectOutputStream objectOutToClient;
	private BufferedReader inFromClient;
	public ClientHandler(Socket connectionSocket, ServerHandler serv) throws IOException{
		connection = connectionSocket;
		parent = serv;
		
		inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		objectOutToClient = new ObjectOutputStream(connection.getOutputStream());
	}
	
	public void run(){
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
			try {
				objectOutToClient.writeObject(parent.fb.sen.get(parent.fb.sen.size()-1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		try {
			objectOutToClient.flush();
			int size = parent.fb.sen.size();
			while(true){
				Thread.sleep(1000);
				int size2 = parent.fb.sen.size();
				if(size2 > size){
					size = size2;
					objectOutToClient.writeObject(parent.fb.sen.get(size - 1));
					objectOutToClient.flush();
				}/*
				while(!inFromClient.ready());
				Thread.sleep(500);
				
				if(inFromClient.readLine().equals("Update")){
					while(parent.fb.sen.size() <= 0);
					Thread.sleep(500);
					
					for(SensorData s : parent.fb.sen)
						objectOutToClient.writeObject(s);
					objectOutToClient.flush();
				}*/
			}
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class SensorData implements java.io.Serializable{
	public int humidity;
	public int temperature;
	
	public SensorData(){
		
	}
}

class FBClass{
	ArrayList<SensorData> sen = new ArrayList<SensorData>();
	public void getData() throws InterruptedException, IOException{
		FileInputStream serviceAccount =
				  new FileInputStream("C:\\Users\\shoup\\Documents\\CIS\\CIS457\\nodemcu-ac90a-firebase-adminsdk-d999q-4d2f43ad9c.json");//Change path!

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