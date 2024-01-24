package irakurlea;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.*;

public class Mqtt implements MqttCallback 
{
	/*MAKINA VIRTUALAREN BROKER: 172.20.10.4 / ORDENAGAILUKO BROKER: localhost*/
    public static final String BROKER = "tcp://172.20.10.4:1883"; 
    public static final String CLENT_ID = "TemperatureSimulator";
    public static final int QoS = 1;
    
    //TOPIKOAK
    public static final String TOPIC_ADC1 = "ADC1";
    //public static final String TOPIC_TEMPERATURE2 = "Sensor/Temperature2";
    //public static final String TOPIC_TEMPERATURE3 = "Sensor/Temperature3";
    public static final String TOPIC_ADC2 = "ADC2";
    //public static final String TOPIC_WATER2 = "Sensor/Water2";
    //public static final String TOPIC_WATER3 = "Sensor/Water3";
    public static final String TOPIC_ADC3 = "ADC3";
    //public static final String TOPIC_GAS2 = "Sensor/Gas2";
    //public static final String TOPIC_GAS3 = "Sensor/Gas3";
   
    //BEZEROA
    private final MqttClient client;

    //STRING
    //public volatile String valueTemperature;
    //public volatile String valueTemperature2;
    //public volatile String valueTemperature3;
    //public volatile String valueGas;  
    //public volatile String valueGas2; 
    //public volatile String valueGas3; 
    
    public volatile String adc1;  
    public volatile String adc2; 
    public volatile String adc3; 
    
    //INT
    //public volatile int valueWater;
    //public volatile int valueWater2;
    //public volatile int valueWater3;
   
    //KONSTRUKTOREA
    public Mqtt(String broker, String clientId) throws MqttException 
    {
    	//ALDAGAIAK HASIERATU
    	//this.valueTemperature = "NORMALA";
        //this.valueTemperature2 = "NORMALA";
        //this.valueTemperature3 = "NORMALA";
        //this.valueWater = 0;
        //this.valueWater2 = 0;
        //this.valueWater3 = 0;
        //this.valueGas = "TRANKIL";
        //this.valueGas2 = "TRANKIL";
        //this.valueGas3 = "TRANKIL";
        
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
        //System.out.println("[MQTT] Subscribe " + TOPIC_TEMPERATURE2);
        //client.subscribe(TOPIC_TEMPERATURE2, QoS);
        //System.out.println("[MQTT] Subscribe " + TOPIC_TEMPERATURE3);
        //client.subscribe(TOPIC_TEMPERATURE3, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_ADC2);
        client.subscribe(TOPIC_ADC2, QoS);
        //System.out.println("[MQTT] Subscribe " + TOPIC_WATER2);
        //client.subscribe(TOPIC_WATER2, QoS);
        //System.out.println("[MQTT] Subscribe " + TOPIC_WATER3);
        //client.subscribe(TOPIC_WATER3, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_ADC3);
        client.subscribe(TOPIC_ADC3, QoS);
        //System.out.println("[MQTT] Subscribe " + TOPIC_GAS2);
        //client.subscribe(TOPIC_GAS2, QoS);
        //System.out.println("[MQTT] Subscribe " + TOPIC_GAS3);
        //client.subscribe(TOPIC_GAS3, QoS);
        System.out.println("[MQTT] Ready");       
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

    //GET-AK
    /*public String getTemperature() 
    {
        return this.valueTemperature;
    }*/
    
    /*public String getTemperature2() 
    {
        return this.valueTemperature2;
    }*/
    
    /*public String getTemperature3() 
    {
        return this.valueTemperature3;
    }*/

    /*public int getWater() 
    {
        return this.valueWater;
    }*/
    
    /*public int getWater2() 
    {
        return this.valueWater2;
    }*/
    
    /*public int getWater3() 
    {
        return this.valueWater3;
    }*/
  
    /*public String getGas() 
    {
        return this.valueGas;
    } */ 
    
    /*public String getGas2() 
    {
        return this.valueGas2;
    }*/ 
    
    /*public String getGas3() 
    {
        return this.valueGas3;
    }*/
    
    public String getAdc1() 
    {
        return this.adc1;
    }
    
    public String getAdc2() 
    {
        return this.adc2;
    }
    
    public String getAdc3() 
    {
        return this.adc3;
    }

    //KONEXIOA GALTZEN BALDIN BADA
    @Override
    public void connectionLost(Throwable cause) 
    {
        System.err.println("Connection to MQTT broker lost!" + cause);
        System.exit(1);
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
        String content = new String(message.getPayload());

        switch(topic) 
        {
        case TOPIC_ADC1:
            this.adc1 = content;
            System.out.println("ADC1: "+this.adc1);
            break;
        /*case TOPIC_TEMPERATURE2:
            this.valueTemperature2 = content;
            System.out.println("Temperatura2: "+this.valueTemperature2);
            break;*/
        /*case TOPIC_TEMPERATURE3:
            this.valueTemperature3 = content;
            System.out.println("Temperatura3: "+this.valueTemperature3);
            break;*/
        case TOPIC_ADC2:
            this.adc2 = content;
            System.out.println("ADC2: "+this.adc2);
            break;
        /*case TOPIC_WATER2:
            this.valueWater2 = Integer.parseInt(content);
            System.out.println("Agua2: "+this.valueWater2);
            break;*/
        /*case TOPIC_WATER3:
            this.valueWater3 = Integer.parseInt(content);
            System.out.println("Agua3: "+this.valueWater3);
            break;*/
        case TOPIC_ADC3:
            this.adc3 = content;
            System.out.println("ADC3: "+this.adc3);
            break;
        /*case TOPIC_GAS2:
            this.valueGas2 = content;
            System.out.println("Gas2: "+this.valueGas2);
            break;*/
        /*case TOPIC_GAS3:
            this.valueGas3 = content;
            System.out.println("Gas3: "+this.valueGas3);
            break;*/
        default:
            break;
        }
    }
}
