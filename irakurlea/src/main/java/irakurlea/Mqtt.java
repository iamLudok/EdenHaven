package irakurlea;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Mqtt implements MqttCallback {

    public static final String BROKER = "tcp://localhost:1883";
    public static final String CLENT_ID = "TemperatureSimulator";
    public static final int QoS = 2;

    public static final String TOPIC_TEMPERATURE = "Sensor/Temperature";
    public static final String TOPIC_TEMPERATURE2 = "Sensor/Temperature2";
    public static final String TOPIC_TEMPERATURE3 = "Sensor/Temperature3";
    public static final String TOPIC_WATER = "Sensor/Water";
    public static final String TOPIC_WATER2 = "Sensor/Water2";
    public static final String TOPIC_WATER3 = "Sensor/Water3";
    public static final String TOPIC_GAS = "Sensor/Gas";
    public static final String TOPIC_GAS2 = "Sensor/Gas2";
    public static final String TOPIC_GAS3 = "Sensor/Gas3";
   
    private final MqttClient client;

    public volatile double valueTemperature;
    public volatile double valueTemperature2;
    public volatile double valueTemperature3;
    public volatile double valueWater;
    public volatile double valueWater2;
    public volatile double valueWater3;
    public volatile double valueGas;  
    public volatile double valueGas2; 
    public volatile double valueGas3; 
   

    public Mqtt(String broker, String clientId) throws MqttException 
    {
    	this.valueTemperature = 0.0;
        this.valueTemperature2 = 0.0;
        this.valueTemperature3 = 0.0;
        this.valueWater = 0.0;
        this.valueWater2 = 0.0;
        this.valueWater3 = 0.0;
        this.valueGas = 0.0;
        this.valueGas2 = 0.0;
        this.valueGas3 = 0.0;
     
        MemoryPersistence persistence = new MemoryPersistence();
             
        this.client = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        this.client.setCallback(this);
   
        System.out.println("[MQTT] Connecting to broker: " + broker);
        this.client.connect(connOpts);
        System.out.println("[MQTT] Connected");
        System.out.println("[MQTT] Subscribe " + TOPIC_TEMPERATURE);
        client.subscribe(TOPIC_TEMPERATURE, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_TEMPERATURE2);
        client.subscribe(TOPIC_TEMPERATURE2, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_TEMPERATURE3);
        client.subscribe(TOPIC_TEMPERATURE3, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_WATER);
        client.subscribe(TOPIC_WATER, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_WATER2);
        client.subscribe(TOPIC_WATER2, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_WATER3);
        client.subscribe(TOPIC_WATER3, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_GAS);
        client.subscribe(TOPIC_GAS, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_GAS2);
        client.subscribe(TOPIC_GAS2, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_GAS3);
        client.subscribe(TOPIC_GAS3, QoS);
        System.out.println("[MQTT] Ready");
        
    }

    public Mqtt() throws MqttException 
    {
        this(BROKER, CLENT_ID);
    }

    public void disconnect() throws MqttException 
    {
        this.client.disconnect();
    }

    void publish(String topic, String content) throws MqttException 
    {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(QoS);
        this.client.publish(topic, message);
    }

    public double getTemperature() 
    {
        return this.valueTemperature;
    }
    
    public double getTemperature2() 
    {
        return this.valueTemperature2;
    }
    
    public double getTemperature3() 
    {
        return this.valueTemperature3;
    }

    public double getWater() 
    {
        return this.valueWater;
    }
    
    public double getWater2() 
    {
        return this.valueWater2;
    }
    
    public double getWater3() 
    {
        return this.valueWater3;
    }
  
    public double getGas() 
    {
        return this.valueGas;
    }  
    
    public double getGas2() 
    {
        return this.valueGas2;
    } 
    
    public double getGas3() 
    {
        return this.valueGas3;
    } 

    @Override
    public void connectionLost(Throwable cause) 
    {
        System.err.println("Connection to MQTT broker lost!" + cause);
        System.exit(1);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {
    	
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws MqttException 
    {
        String content = new String(message.getPayload());

        switch(topic) 
        {
        case TOPIC_TEMPERATURE:
            this.valueTemperature = Double.parseDouble(content);
            System.out.println("Temperatura: "+this.valueTemperature);
            break;
        case TOPIC_TEMPERATURE2:
            this.valueTemperature2 = Double.parseDouble(content);
            System.out.println("Temperatura2: "+this.valueTemperature2);
            break;
        case TOPIC_TEMPERATURE3:
            this.valueTemperature3 = Double.parseDouble(content);
            System.out.println("Temperatura3: "+this.valueTemperature3);
            break;
        case TOPIC_WATER:
            this.valueWater = Double.parseDouble(content);
            System.out.println("Agua: "+this.valueWater);
            break;
        case TOPIC_WATER2:
            this.valueWater2 = Double.parseDouble(content);
            System.out.println("Agua2: "+this.valueWater2);
            break;
        case TOPIC_WATER3:
            this.valueWater3 = Double.parseDouble(content);
            System.out.println("Agua3: "+this.valueWater3);
            break;
        case TOPIC_GAS:
            this.valueGas = Double.parseDouble(content);
            System.out.println("Gas: "+this.valueGas);
            break;
        case TOPIC_GAS2:
            this.valueGas2 = Double.parseDouble(content);
            System.out.println("Gas2: "+this.valueGas2);
            break;
        case TOPIC_GAS3:
            this.valueGas3 = Double.parseDouble(content);
            System.out.println("Gas3: "+this.valueGas3);
            break;
        default:
            break;
        }
    }

}
