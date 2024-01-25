package irakurlea;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.*;

public class Mqtt implements MqttCallback 
{
	/*MAKINA VIRTUALAREN BROKER: 172.20.10.4 / ORDENAGAILUKO BROKER: localhost*/
    public static final String BROKER = "tcp://172.20.10.4:1883"; 
    public static final String CLENT_ID = "PBL";
    public static final int QoS = 1;
    
    //TOPIKOAK
    public static final String TOPIC_ADC1 = "ADC1";
    public static final String TOPIC_ADC2 = "ADC2";
    public static final String TOPIC_ADC3 = "ADC3";
   
    //BEZEROA
    private final MqttClient client;

    //STRING    
    public volatile String adc1;  
    public volatile String adc2; 
    public volatile String adc3; 
    
    //KONSTRUKTOREA
    public Mqtt(String broker, String clientId) throws MqttException 
    {
    	//ALDAGAIAK HASIERATU       
        this.adc1 = "0";
        this.adc2 = "0";
        this.adc3 = "0";
     
        MemoryPersistence persistence = new MemoryPersistence();
             
        this.client = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        this.client.setCallback(this);
   
        //MQTT HASIERATU
        System.out.println("[MQTT] Connecting to broker: " + broker);
        this.client.connect(connOpts);
        System.out.println("[MQTT] Connected");
        System.out.println("[MQTT] Subscribe " + TOPIC_ADC1);
        client.subscribe(TOPIC_ADC1, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_ADC2);
        client.subscribe(TOPIC_ADC2, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_ADC3);
        client.subscribe(TOPIC_ADC3, QoS);
        System.out.println("[MQTT] Ready"); //Mqtt prest dago datuak jasotzeko      
    }

    //KONSTRUKTOREA
    public Mqtt() throws MqttException 
    {
        this(BROKER, CLENT_ID);
    }

    //DESKONEKTATZEN BALDIN BADA
    public void disconnect() throws MqttException 
    {
        this.client.disconnect();
    }

    //PUBLIKATU
    void publish(String topic, String content) throws MqttException 
    {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(QoS);
        this.client.publish(topic, message);
    }

    //GET ADC1
    public String getAdc1() 
    {
        return this.adc1;
    }
    
    //GET ADC2
    public String getAdc2() 
    {
        return this.adc2;
    }
    
    //GET ADC3
    public String getAdc3() 
    {
        return this.adc3;
    }

    //KONEXIOA GALTZEN BALDIN BADA
    @Override
    public void connectionLost(Throwable cause) 
    {
        System.err.println("Connection to MQTT broker lost!" + cause); //Konexioa galdu egin da
        System.exit(1); //Programa itxi
    }

    //HUTSIK
    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {
    	
    }

    //MEZUA HELTZEAN CONSOLAN IDAZTEN DU
    @Override
    public void messageArrived(String topic, MqttMessage message) throws MqttException 
    {
        String content = new String(message.getPayload()); //Content string-ean gorde

        switch(topic) 
        {
	        case TOPIC_ADC1:
	            this.adc1 = content; //Adc1-ean gorde content
	            System.out.println("ADC1: "+this.adc1); //Adc1 balioa idatzi
	            break;
	        case TOPIC_ADC2:
	            this.adc2 = content; //Ad2-an gorde content
	            System.out.println("ADC2: "+this.adc2); //Adc2 balioa idatzi
	            break;
	        case TOPIC_ADC3:
	            this.adc3 = content; //Adc3-an gorde content
	            System.out.println("ADC3: "+this.adc3); //Adc3 balioa idatzi
	            break;
	        default:
	            break;
        }
    }
}
