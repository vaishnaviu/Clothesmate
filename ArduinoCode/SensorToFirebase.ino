/* Read RFID Tag with RC522 RFID Reader
 * Send data to firebase
 */
#include <Arduino.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>

#include <math.h>
#include <string.h>
 
#include <SPI.h>
#include <MFRC522.h>
#include <ESPDateTime.h>

// Provide the token generation process info.
#include "addons/TokenHelper.h"
// Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert your network credentials
#define WIFI_SSID "BroncoFi"
#define WIFI_PASSWORD "84412704"

// Insert Firebase project API Key
#define API_KEY "AIzaSyBBM_cm6OkPxuTCzIiUZs1nSWsZ4BzMqi0"

// Insert RTDB URLefine the RTDB URL
#define DATABASE_URL "https://fir-test-2ab87-default-rtdb.firebaseio.com/"

constexpr uint8_t RST_PIN = D3;     // Configurable, see typical pin layout above
constexpr uint8_t SS_PIN = D4;     // Configurable, see typical pin layout above
constexpr uint8_t BUZZER_PIN = D2;

MFRC522 rfid(SS_PIN, RST_PIN); // Instance of the class
MFRC522::MIFARE_Key key;

// Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
bool signupOK = false;

// Getting current time
void setupDateTime() {
  DateTime.setServer("time.pool.aliyun.com");
  DateTime.setTimeZone("PST8PDT,M3.2.0,M11.1.0");
  DateTime.begin();
  if (!DateTime.isTimeValid()) {
    Serial.println("Failed to get time from server.");
  } else {
    Serial.printf("Date Now is %s\n", DateTime.toISOString().c_str());
    Serial.printf("Timestamp is %ld\n", DateTime.now());
  }
}

void beep(float frequency) {
  int loop = (int)(frequency / 2);
  float half_delay_micro = 1000000.0 / (frequency * 2);
  for (int i = 0; i < loop; i++) {
    digitalWrite(BUZZER_PIN, HIGH);
    delayMicroseconds((int)half_delay_micro);
    digitalWrite(BUZZER_PIN, LOW);
    delayMicroseconds((int)half_delay_micro);
  }
}

void setup(){
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");

  SPI.begin(); // Init SPI bus
  rfid.PCD_Init(); // Init MFRC522
  pinMode(BUZZER_PIN, OUTPUT);

  // Getting current time
  setupDateTime();

  while (WiFi.status() != WL_CONNECTED){
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  // Assign the api key (required)
  config.api_key = API_KEY;

  // Assign the RTDB URL (required)
  config.database_url = DATABASE_URL;

  // Sign up
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    signupOK = true;
  } else{
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  // Assign the callback function for the long running token generation task
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h
 
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
  if (Firebase.ready()) {
    Serial.println("Firebase ready.");
  }
}

void loop(){
  // Write an Int number on the database path test/int
  if (!rfid.PICC_IsNewCardPresent()) {
    return;
  }
  Serial.println("New card present.");
  uint32_t id;
  if (rfid.PICC_ReadCardSerial()) {
    id = *(uint32_t*)(rfid.uid.uidByte);
    Serial.println(id);
    rfid.PICC_HaltA();
    rfid.PCD_StopCrypto1();
  }

  // Frequency C3
  float base = 261.6256f;
  float sound_do  = base;
  float sound_do2 = base * 2;
 
  // Beep sound to indicate reading from sensor successful
  if (id == 328810224 || id == 348741200 || id == 275908609 || id == 1325766899) beep(sound_do);
 
  // Get current time
  String currentTime = DateTime.toString();
  Serial.println("Current Time: "+currentTime);
  String path = "ourtest/"+currentTime+"/id";
  String newUsePath = "ourtest/"+currentTime+"/newUse";
  String statusPath = "ourtest/"+currentTime+"/status";
 
  if (Firebase.RTDB.setInt(&fbdo, newUsePath, 1) && Firebase.RTDB.setInt(&fbdo, path, id) && Firebase.RTDB.setInt(&fbdo, statusPath, 1)){
    Serial.println("PASSED");
    Serial.println("PATH: " + fbdo.dataPath());
    Serial.println("TYPE: " + fbdo.dataType());

    // Beep sound to indicate writing to database successful
    if (id == 328810224 || id == 348741200 || id == 275908609 || id == 1325766899) beep(sound_do2);
  } else {
    Serial.println("FAILED");
    Serial.println("REASON: " + fbdo.errorReason());
  }
}
