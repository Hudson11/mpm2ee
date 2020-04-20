/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.pubsub;

/**
 *
 * @author HUDSON SILVA DE BORBA github.com/Hudson11
 */
import java.net.ConnectException;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A sample application that demonstrates how to use the Paho MQTT v3.1 Client blocking API.
 */
public class CloudMqttSubscriber implements MqttCallback {
    
    private final int qos = 1;
    private String topic = "mpm2ee/teste";
    private MqttClient client;
    
    public CloudMqttSubscriber(String uri) throws MqttException{
        String[] array = uri.split("@"); // Splita o endereço de host dos dados ed username e senha;
        String host = array[0];
        String[] credentials = array[1].split(":");
        String username = credentials[0];
        String password = credentials[1];
        
        String clientId = "MQTT-Java-mpm2ee";
        
        System.out.println("Host: " + host);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        
        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setUserName(username);
        conOpt.setPassword(password.toCharArray());
        
        this.client = new MqttClient(host, clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect(conOpt);
        
        this.client.subscribe(this.topic, this.qos);
    }
    
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost because: " + cause);
        System.exit(1);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(String.format("[%s] %s", topic, new String(message.getPayload())));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        
    }
    /*
        Publisher implementation
    */
    public void sendMessage(String payload) throws MqttException{
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(this.qos);
        this.client.publish(this.topic, message);
    }
    
    public static void main(String[] args) throws MqttException{
        CloudMqttSubscriber client = new CloudMqttSubscriber("tcp://m16.cloudmqtt.com:12792@mpcyxmak:WBmDfN0xgexI");
    }
    
}
