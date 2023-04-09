#include "WiFi.h"
// Thư viện dùng để connect, publish/subscribe mqtt
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "DHT.h"

// setup wifi
//const char *ssid = "HangNguyen";    // tên của mạng wifi bạn muốn kết nối đến
//const char *password = "123456789"; // mật khẩu của mạng wifi
const char* ssid = "HUAWEI nova 3i";
const char* password =  "13456789";

// hardcode
char *flame_id = "6415cd65721d6866a1bdd44d";         // "FLAME_ID"
char *mq2_id = "6413483dedc53875fb617462";           // "MQ2_ID"
char *humi_and_temp_id = "6404abecf4d81e7a427ff726"; // "HUMI_AND_TEMP_ID"
char *light_1_id = "6379d0d290a72a5570becd5f";       // control light (led)

int flame_type = 1;
int mq2_type = 2;
int humi_and_temp_type = 3;

// broker MQTT
#define DHTTYPE DHT11
#define MQTT_SERVER "broker.hivemq.com"
#define MQTT_PORT 1883
#define MQTT_USER "fire_alarm_system1"
#define MQTT_PASSWORD "1234567"
#define MQTT_TOPIC_PUB_HAT "smart_home_humidity_and_temperature"
#define MQTT_TOPIC_PUB_FAM "smart_home_flame_and_mq2"
#define MQTT_TOPIC_SUB_CONTROL "smart_home_control_device"

#define LIGHT_PIN_1 27
#define FLAME_PIN_ANALOG 13
#define FLAME_PIN_DIGITAL 25
#define FLAME_PIN_WARNING 26
#define MQ2_PIN_ANALOG 4
#define MQ2_PIN_DIGITAL 15
#define MQ2_PIN_WARNING 18
#define DHTPIN 19
#define PHOTOSENSOR_PIN 34 // analog

int previous_status_flame = 1;
int previous_status_mq2 = 1;
int gas_analog_value = 4095;
int gas_digital_value = 1;
int flame_digital_value = 1;
int flame_analog_value = 4095;
int photosensor_analog_value = 0;
char *mode_light_1 = "MANUAL";
unsigned long interval = 60000;    // 60s
unsigned long interval_DHT = 5000; // 5s
unsigned long interval_warning = 10000;
unsigned long previousMillis;
// cấp phát bộ nhớ tại chỗ
StaticJsonDocument<200> mess_publish;
StaticJsonDocument<200> mess_subscribe;
// Để sử dụng thư viện PubSubClient ta cần khởi tạo một đối tượng tên là là client.
WiFiClient espClient;
PubSubClient client(espClient);

DHT dht(DHTPIN, DHTTYPE);

struct
{
  char *manualMode;
  char *autoMode;
} modeDevice;
struct
{
  char *onStatus;
  char *offStatus;
} statusDevice;
void setup_device()
{
  modeDevice.manualMode = "MANUAL";
  modeDevice.autoMode = "AUTO";
  statusDevice.onStatus = "ON";
  statusDevice.offStatus = "OFF";
}

void setup()
{
  Serial.begin(115200); // Khởi tạo kết nối Serial để truyền dữ liệu đến máy tính
  setup_wifi();         // gọi hàm setup wifi
  setup_device();
  pinMode(FLAME_PIN_ANALOG, INPUT); // thiết lập chân số 13 là chân nhận tín hiệu
  pinMode(MQ2_PIN_ANALOG, INPUT);
  pinMode(FLAME_PIN_DIGITAL, INPUT);
  pinMode(MQ2_PIN_DIGITAL, INPUT);
  // thiết lập chân số 26 là chân xuất tín hiệu
  pinMode(FLAME_PIN_WARNING, OUTPUT); // led or buzzer
  pinMode(MQ2_PIN_WARNING, OUTPUT);   // led or buzzer
  pinMode(LIGHT_PIN_1, OUTPUT);
  pinMode(PHOTOSENSOR_PIN, INPUT);

  // set up MQTT
  client.setServer(MQTT_SERVER, MQTT_PORT);
  client.setCallback(callback); // sử dụng cho nhận message MQTT

  digitalWrite(LIGHT_PIN_1, LOW);
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
  Serial.println(WiFi.localIP()); // gửi địa chỉ ip đến máy tính
  delay(1000);
}

// Hàm call back để nhận dữ liệu
void callback(char *topic, byte *payload, unsigned int length)
{
  Serial.println("------- New message from broker -----");
  Serial.print("topic: ");
  Serial.println(topic);
  Serial.print("message: ");
  Serial.println();
  char *messageTemp;
  messageTemp = (char *)malloc((length + 1) * sizeof(char));
  memset(messageTemp, 0, length + 1);
  for (int i = 0; i < length; i++)
  {
    messageTemp[i] = (char)payload[i];
  }
  Serial.println(messageTemp);

  // thực hiện chuyển từ string sang JSON
  deserializeJson(mess_subscribe, messageTemp);
  try
  {
    if (strcmp(mess_subscribe["deviceId"], "17") == 0)
    {
      Serial.println("door");
      //      if(strcmp(mess_subscribe["status"], "on") == 0) {
      //        Serial.println("on");
      //        myServo.write(150);
      //      } else if(strcmp(mess_subscribe["status"], "off") == 0) {
      //        Serial.println("off");
      //        myServo.write(60);
      //      }
    }
    else if (!strcmp(mess_subscribe["deviceId"], light_1_id))
    {
      if (!strcmp(mess_subscribe["mode"], modeDevice.manualMode))
      {
        mode_light_1 = modeDevice.manualMode;
        if (!strcmp(mess_subscribe["status"], statusDevice.onStatus))
        {
          Serial.println("ON ledPin1");
          digitalWrite(LIGHT_PIN_1, HIGH);
        }
        else if (!strcmp(mess_subscribe["status"], statusDevice.offStatus))
        {
          Serial.println("OFF ledPin1");
          digitalWrite(LIGHT_PIN_1, LOW);
        }
        Serial.println("ledPin1 is mode MANUAL");
      }
      else
      {
        mode_light_1 = modeDevice.autoMode;
        Serial.println("ledPin1 is mode AUTO");
      }
    }
    else if (strcmp(mess_subscribe["deviceId"], "3") == 0)
    {
      //      if(strcmp(mess_subscribe["status"], "on") == 0) {
      //      Serial.println("on ledPin3");
      //      digitalWrite(ledPin3, HIGH);
      //      } else if(strcmp(mess_subscribe["status"], "off") == 0) {
      //        Serial.println("off ledPin3");
      //        digitalWrite(ledPin3, LOW);
      //      }
    }
    else
    {
      Serial.println("khong ton tai thiet bi");
    }
  }
  catch (const std::exception &e)
  {
    Serial.println("Invalid task.");
  }
}

// Hàm reconnect thực hiện kết nối lại khi mất kết nối với MQTT Broker
void reconnect()
{
  while (!client.connected())
  {
    Serial.print("Attempting MQTT connection...");
    String clientId = "nodeWiFi32";
    clientId += String(random(0xffff), HEX);
    if (client.connect(clientId.c_str(), MQTT_USER, MQTT_PASSWORD))
    {
      Serial.println("connected");
      client.subscribe(MQTT_TOPIC_SUB_CONTROL);
    }
    else
    {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 2 seconds");
      delay(2000);
    }
  }
}

void Publish_Flame(int value)
{
  char buffer_flame[256];
  mess_publish["deviceId"] = flame_id;
  mess_publish["deviceType"] = flame_type;
  mess_publish["flameValue"] = value;
  // tạo đối tượng json
  serializeJson(mess_publish, buffer_flame);
  client.publish(MQTT_TOPIC_PUB_FAM, buffer_flame);
  delay(100);
}

void Publish_Mq2(int value)
{
  char buffer_mq2[256];
  mess_publish["deviceId"] = mq2_id;
  mess_publish["deviceType"] = mq2_type;
  mess_publish["MQ2Value"] = value;
  serializeJson(mess_publish, buffer_mq2);
  client.publish(MQTT_TOPIC_PUB_FAM, buffer_mq2);
  delay(100);
}

// Publish nhiệt độ, độ ẩm
void Publish_DHT()
{
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval_DHT)
  {
    float h = dht.readHumidity();
    float t = dht.readTemperature();
    // Check if any reads failed and exit early (to try again).
    if (isnan(h) || isnan(t))
    {
      delay(500);
      return;
    }
    else
    {
      char buffer_DHT[256];
      mess_publish["humidityAir"] = round(h);
      mess_publish["temperature"] = round(t);

      mess_publish["deviceId"] = humi_and_temp_id;
      mess_publish["deviceType"] = humi_and_temp_type;
      Serial.print("humidityAir: ");
      Serial.println(h);
      Serial.print("temperature: ");
      Serial.println(t);
      serializeJson(mess_publish, buffer_DHT);
      client.publish(MQTT_TOPIC_PUB_HAT, buffer_DHT);
      delay(100);
      previousMillis = currentMillis;
    }
  }
}

void loop()
{
  // lấy thời gian hiện tại (theo đơn vị milli giây)
  unsigned long currentMillis = millis();

  // liên tục kiểm tra kết nối MQTT
  if (!client.connected())
  {
    reconnect();
  }
  client.loop();

  // liên tục đọc giá trị cảm biến
  // gas_analog_value = analogRead(MQ2_PIN_ANALOG);
  // gas_digital_value = digitalRead(MQ2_PIN_DIGITAL);
  gas_digital_value = 1;
  flame_digital_value = digitalRead(FLAME_PIN_DIGITAL);
  // flame_analog_value = analogRead(FLAME_PIN_ANALOG);
  photosensor_analog_value = analogRead(PHOTOSENSOR_PIN);

  // Bật tắt đèn tự động
  if (!strcmp(mode_light_1, modeDevice.autoMode) && photosensor_analog_value > 2000)
  {
    digitalWrite(LIGHT_PIN_1, HIGH);
  }
  else if (!strcmp(mode_light_1, modeDevice.autoMode) && photosensor_analog_value < 2000)
  {
    digitalWrite(LIGHT_PIN_1, LOW);
  }

  Serial.print("gas and smoke: ");
  Serial.println(gas_digital_value);
  Serial.print("flame: ");
  Serial.println(flame_digital_value);
  Serial.println("");

  // kiểm tra xem đã đến lúc đèn LED nhấp nháy chưa
  // chênh lệch giữa thời gian hiện tại và lần trước bạn nhấp nháy
  // đèn LED lớn hơn khoảng thời gian bạn muốn
  // nhấp nháy đèn LED.
  if (flame_digital_value == LOW)
  {
    if (previous_status_flame == 1 || currentMillis - previousMillis >= interval)
    {
      digitalWrite(FLAME_PIN_WARNING, HIGH); // cập nhật led thực tế
      Publish_Flame(flame_digital_value);
      // lưu lại lần cuối cùng nhấp nháy đền led
      previousMillis = currentMillis;
      previous_status_flame = 0;
    }
    else
      previousMillis = currentMillis;
  }
  else
  {
    if (currentMillis - previousMillis >= interval_warning)
    {
      digitalWrite(FLAME_PIN_WARNING, LOW);
      previous_status_flame = 1;
      delay(50);
    }
  }

  if (gas_digital_value == 0)
  {
    if (previous_status_mq2 == 1 || currentMillis - previousMillis >= interval)
    {
      digitalWrite(MQ2_PIN_WARNING, HIGH);
      Publish_Mq2(gas_digital_value);
      previousMillis = currentMillis;
      previous_status_mq2 = 0;
    }
    else
      previousMillis = currentMillis;
  }
  else
  {
    if (currentMillis - previousMillis >= interval_warning)
    {
      digitalWrite(MQ2_PIN_WARNING, LOW);
      previous_status_mq2 = 1;
      delay(50);
    }
  }

  // sau 1 khoảng thời gian (interval) luôn publish
  if (currentMillis - previousMillis >= interval)
  {
    Publish_Flame(flame_digital_value);
    delay(1000);
    Publish_Mq2(gas_digital_value);
    previousMillis = currentMillis;
  }

  Publish_DHT();
  delay(2000);
}
