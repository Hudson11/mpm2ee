/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.pubsub;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
/**
 *
 * @author Lucas Bernardo
 */
public class SimpleCallback implements MqttCallback {
    public static boolean sms = false;
    @Override
    public void connectionLost(Throwable cause) { 
        //chamado quando o cliente perder a conexão com o broker
    }

    /**
     * Quando houver a detecção de uma nova mensagem no tópico de interesse do 
     * subscriber esse método é chamado e printa a mensagem recebida
     * @throws java.lang.Exception
     */ 
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");
        sms = true;
    }

    /**
     * Quando uma publicação de saída é concluída este método é chamado
     */
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
