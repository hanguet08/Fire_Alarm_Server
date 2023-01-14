#include "WiFi.h"
 // Thư viện dùng để connect, publish/subscribe mqtt
#include <PubSubClient.h>
#include <ArduinoJson.h>

// setup wifi
const char* ssid = "YOUR_USERNAME_WIFI"; // tên của mạng wifi bạn muốn kết nối đến
const char* password =  "YOUR_PASSWORD_WIFI" // mật khẩu của mạng wifi

// hardcode
String flame_id = "63241e55f11f2f2d50b55964";   // "FLAME_ID"
String mq2_id = "63240c72f11f2f2d50b558fb";	// "MQ2_ID"
String humi_and_temp_id = "6324120df11f2f2d50b55936";	// "HUMI_AND_TEMP_ID"

int flame_type = 1;
int mq2_type = 2;
int humi_and_temp_type = 3;

// broker MQTT
// khai báo một giá trị không đổi
#define MQTT_SERVER "broker.hivemq.com"
#define MQTT_PORT 1883
#define MQTT_USER "YOUR_MQTT_USERNAME"
#define MQTT_PASSWORD "YOUR_MQTT_PASSWORD"
#define MQTT_TOPIC_PUB_HAT "smart_home_humidity_and_temperature"
#define MQTT_TOPIC_PUB_FAM "smart_home_flame_and_mq2"

#define FLAME_PIN_ANALOG 13
#define FLAME_PIN_DIGITAL 25
#define FLAME_PIN_WARNING 26
#define MQ2_PIN_ANALOG 4
#define MQ2_PIN_DIGITAL 15
#define MQ2_PIN_WARNING 18

int previous_status_flame = 1;
int previous_status_mq2 = 1;
int gas_analog_value = 4095;
int gas_digital_value = 1;
int flame_digital_value = 1;
int flame_analog_value = 4095;
unsigned long interval = 60000;   // 60s 
unsigned long interval_warning = 10000;
unsigned long previousMillis;
// cấp phát bộ nhớ tại chỗ
StaticJsonDocument<200> mess_publish;
// Để sử dụng thư viện PubSubClient ta cần khởi tạo một đối tượng tên là là client.
WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
  Serial.begin(115200);  // Khởi tạo kết nối Serial để truyền dữ liệu đến máy tính
  // set up wifi
  setup_wifi(); // gọi hàm setup wifi
  pinMode(FLAME_PIN_ANALOG, INPUT); // thiết lập chân số 13 là chân nhận tín hiệu
  pinMode(MQ2_PIN_ANALOG, INPUT); 
  pinMode(FLAME_PIN_DIGITAL, INPUT); 
  pinMode(MQ2_PIN_DIGITAL, INPUT); 
  // thiết lập chân số 26 là chân xuất tín hiệu
  pinMode(FLAME_PIN_WARNING, OUTPUT);  // led or buzzer 
  pinMode(MQ2_PIN_WARNING, OUTPUT);   // led or buzzer
  
  //set up MQTT
  client.setServer(MQTT_SERVER, MQTT_PORT);
  // client.setCallback(callback);   // sử dụng cho nhận message MQTT

  delay(100);
}

// Hàm kết nối wifi
void setup_wifi()
{
    Serial.println();
    Serial.print("Connecting to ");
    Serial.println(ssid);
    WiFi.begin(ssid, password); // kết nối vào mạng wifi
    // chờ kết nối wifi được thiết lập
    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
        Serial.print(".");
    }
    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());  // gửi địa chỉ ip đến máy tính
    delay(1000);
}

// Hàm reconnect thực hiện kết nối lại khi mất kết nối với MQTT Broker
void reconnect()
{
    while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    String clientId = "nodeWiFi32";
    clientId += String(random(0xffff), HEX);
    if (client.connect(clientId.c_str(), MQTT_USER, MQTT_PASSWORD)) {
      Serial.println("connected");
      // client.subscribe(MQTT_TOPIC_SUB);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 2 seconds");
      delay(2000);
    }
  }
}

void Publish_Flame(int value) {
    char buffer_flame[256];
    mess_publish["deviceId"] = flame_id;
    mess_publish["deviceType"] = flame_type;
    mess_publish["flameValue"] = value;
    serializeJson(mess_publish, buffer_flame);
    client.publish(MQTT_TOPIC_PUB_FAM, buffer_flame);
    delay(100);
}

void Publish_Mq2(int value) {
    char buffer_mq2[256];
    mess_publish["deviceId"] = mq2_id;
    mess_publish["deviceType"] = mq2_type;
    mess_publish["MQ2Value"] = value;
    serializeJson(mess_publish, buffer_mq2);
    client.publish(MQTT_TOPIC_PUB_FAM, buffer_mq2);
    delay(100);
}

void loop() {
  // lấy thời gian hiện tại (theo đơn vị milli giây)
  unsigned long currentMillis = millis(); 
  
  // liên tục kiểm tra kết nối MQTT
  if (!client.connected())
    {
       reconnect();
    }
   client.loop();
   
   // liên tục đọc giá trị cảm biến
   delay(100);

   // gas_analog_value = analogRead(MQ2_PIN_ANALOG);
   gas_digital_value = digitalRead(MQ2_PIN_DIGITAL);
   flame_digital_value = digitalRead(FLAME_PIN_DIGITAL);
   // flame_analog_value = analogRead(FLAME_PIN_ANALOG);
   
   Serial.print("gas and smoke: ");
   Serial.println(gas_digital_value);
   Serial.print("flame: ");
   Serial.println(flame_digital_value);
   Serial.println("");
   
   if(flame_digital_value == LOW) {
      if(previous_status_flame == 1 || currentMillis - previousMillis >= interval){
        digitalWrite(FLAME_PIN_WARNING, HIGH); 
        Publish_Flame(flame_digital_value);
        previousMillis = currentMillis;
        previous_status_flame = 0;
      } else previousMillis = currentMillis;
   } else {
      if(currentMillis - previousMillis >= interval_warning) {
        digitalWrite(FLAME_PIN_WARNING, LOW); 
        previous_status_flame = 1;
        delay(50);
      }
   }

   if(gas_digital_value == 0) {
      if(previous_status_mq2 == 1 || currentMillis - previousMillis >= interval){
        digitalWrite(MQ2_PIN_WARNING, HIGH); 
        Publish_Mq2(gas_digital_value);
        previousMillis = currentMillis;
        previous_status_mq2 = 0;
      } else previousMillis = currentMillis;
   } else {
      if(currentMillis - previousMillis >= interval_warning) {
        digitalWrite(MQ2_PIN_WARNING, LOW); 
        previous_status_mq2 = 1;
        delay(50);
      }
   }

   // sau 1 khoảng thời gian (interval) luôn publish 
   if(currentMillis - previousMillis >= interval) {
      Publish_Flame(flame_digital_value);
      delay(1000);
      Publish_Mq2(gas_digital_value);
      previousMillis = currentMillis;
   }
   delay(1000);
}
