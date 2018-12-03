/***********************************************************8
 * CIS 457: Temperature and Humidity Reader
 * Ali Scheffler
 * Dylan Shoup
 * 12/4/18
 * 
 * This program reads the temperature and humidity from the DHT 
 * sensor, then sends the values over serial communcation to 
 * the ESP8266
 */
 
#include <dht.h>
#include <SoftwareSerial.h>
#include <string.h>

dht DHT;

#define DHT11_PIN 7
#define RXpin 10
#define TXpin 11

//Password to send to ESP8266
String token = "keyword";

//Setup serial communication
SoftwareSerial espSerial(RXpin, TXpin);

void setup() {
  //Serial for outputting to monitor
  Serial.begin(9600);

  //Serial for sending data
  espSerial.begin(115200);

  //Send token 
  espSerial.println(token);
}

void loop() {
  // Read sensor
  int chk = DHT.read11(DHT11_PIN);

  //Clear serial 
  espSerial.flush();
  
  //Send data to ESP8266 via serial
  int temp = DHT.temperature;
  Serial.print("Temp: ");
  Serial.println(temp);
  espSerial.print("T");
  espSerial.println(temp);
 
  int hum = DHT.humidity;
  Serial.print("Hum: ");
  Serial.println(hum);
  espSerial.print("H");
  espSerial.println(hum);
  Serial.println();

  //Delay so firebase is getting updated every half a minute
  delay(30000);
}
