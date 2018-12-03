/*************************************************
 * CIS 457: ESP8266 WiFi
 * Ali Scheffler
 * Dylan Shoup
 * 12/4/18
 * 
 * This program reads in values from serial, and 
 * updates Firebase database with the new data.
 */
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SoftwareSerial.h>
#include <string.h>

//Password for hotspot to connect to 
const char* ssid = "NETGEAR81";
const char* password = "ancientcomet701";
//const char* ssid = "ATT4SUsuI2";
//const char* password = "ct?uyuztadd3";

//Information to connect to Firebase
#define FIREBASE_HOST "nodemcu-ac90a.firebaseio.com"
#define FIREBASE_AUTH "MCrHpYT3hZPbFZwrCsKuneDdlcBHmPhGi4RbUVxf"

//For reading data from serial
String inString = "";

//Pass key
String token = "keyword";

void read_data();
bool wait_password();

void setup() {
      
      Serial.begin(115200);
    
      //Print out data about connecting 
      Serial.println();
      Serial.println();
      Serial.print("You're are connecting to ");
      Serial.println(ssid);

      //Connect to WiFi
      WiFi.begin(ssid, password);

      //Wait while not connected to a network
      while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
        Serial.println("");
        
      }
      Serial.println("Your ESP is connected!");  
      Serial.println("Your IP address is: ");
      Serial.println(WiFi.localIP()); 

      //Start Firebase
      Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

      bool secure = false;
      
      //Wait for password
      while(!secure)
      {
        secure = wait_password();
      }
}

void loop() {

      //Read in from serial
      read_data();
}

bool wait_password()
{
  while(Serial.available() > 0)
  {
    int data = Serial.read();
    
    if(data == '\r')
    {
      inString = inString; 
      if(inString.equals(token))
      {
        return true;
      }
      
    }
    inString += (char) data;
    
    delay(2000);
  }

  inString = "";
  
  return false;
}

void read_data()
{
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();
  
  int temperature = 0;
  int humidity = 0;
  int counter = 0;
  
  while(Serial.available() > 0)
  {
    Firebase.set("token", "setting");
    int data = Serial.read();  

    if(data == 'T')
    {
      counter = 0;
    }
    if(data == 'H')
    {
      counter = 1;
    }else{
      if(isDigit(data))
      {
        inString += (char) data;
      }
    }
    
    if(data == '\n')
    {
      if(counter == 0)
      {
        temperature = inString.toInt();
  
      }else{
        humidity = inString.toInt();
    
      }
      
      //clear the string for new input 
      inString = "";
    }
  
  }

  if(temperature > 0 && temperature < 100 && humidity > 0 & humidity < 100)
  {
    root["temperature"] = temperature;
    root["humidity"] = humidity;
    
    String name = Firebase.push("/sensor/values", root);

    if(Firebase.failed())
    {
        Serial.println("Firebase Pushing /sensor/dht failed:");
        Serial.println(Firebase.error());
    }
  }

}
