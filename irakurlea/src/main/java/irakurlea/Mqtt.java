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
    public static final String TOPIC_WATER = "Sensor/Water";
    public static final String TOPIC_GAS = "Sensor/Gas";
   
    private final MqttClient client;

    public volatile double valueTemperature;
    public volatile double valueWater;
    public volatile double valueGas;
   

    public Mqtt(String broker, String clientId) throws MqttException 
    {
        this.valueTemperature = 0.0;
        this.valueWater = 0.0;
        this.valueGas = 0.0;
     
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
        System.out.println("[MQTT] Subscribe " + TOPIC_WATER);
        client.subscribe(TOPIC_WATER, QoS);
        System.out.println("[MQTT] Subscribe " + TOPIC_GAS);
        client.subscribe(TOPIC_GAS, QoS);
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

    public double getWater() 
    {
        return this.valueWater;
    }
  
    public double getGas() 
    {
        return this.valueGas;
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
            case TOPIC_WATER:
                this.valueWater = Double.parseDouble(content);
                System.out.println("Agua: "+this.valueWater);
                break;
            case TOPIC_GAS:
                this.valueGas = Double.parseDouble(content);
                System.out.println("Gas: "+this.valueGas);
                break;
            default:
                break;
        }
    }

}
