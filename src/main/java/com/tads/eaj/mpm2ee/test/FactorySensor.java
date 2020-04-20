/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tads.eaj.mpm2ee.test;

import com.tads.eaj.mpm2ee.dao.NodeDAO;
import com.tads.eaj.mpm2ee.dao.PoliticaDAO;
import com.tads.eaj.mpm2ee.model.Node;
import com.tads.eaj.mpm2ee.model.Politica;
import com.tads.eaj.mpm2ee.model.Sensor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author berna
 */
public class FactorySensor {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

//        List<Sensor> s1 = new ArrayList<Sensor>();
//        s1.add(new Sensor("Temperatura água", "18"));
//        s1.add(new Sensor("pH", "39"));
//        s1.add(new Sensor("Energia", "41"));
//
//        List<Sensor> s2 = new ArrayList<Sensor>();
//        s2.add(new Sensor("Temperatura ambiente", "27"));
//        s2.add(new Sensor("Luminozidade", "27"));
//        s2.add(new Sensor("energia", "78"));
//
//        Node no1 = new Node("ESP1", "Interna", "", "", "60", "deepsleep", s1);
//        Node no2 = new Node("ESP2", "Externa", "", "", "60", "modemsleep", s2);
//
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime localTime = localDateTime.toLocalTime();
//        no1.setData(String.valueOf(localDate));
//        no1.setHora(String.valueOf(localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))));
//        
//        no2.setData(String.valueOf(localDate));
//        no2.setHora(String.valueOf(localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))));
//        
//        NodeDAO dao = new NodeDAO();
//        dao.salvar(no1);
//        dao.salvar(no2);
//
//        dao.gerarToken();
        Politica p = new Politica("deepsleep", 
                String.valueOf(localDate), 
                String.valueOf(localTime
                        .format(DateTimeFormatter
                        .ofLocalizedTime(FormatStyle.SHORT))), 
                "60");
        
        PoliticaDAO pdao = new PoliticaDAO();
        
        pdao.salvar(p);
        pdao.gerarToken();

    }

    /*
    Message message = new Message(username, body, time);
 
Map<String, Object> messageValues = message.toMap();
Map<String, Object> childUpdates = new HashMap<>();
 
String key = mDatabase.child("messages").push().getKey();
 
childUpdates.put("/messages/" + key, messageValues);
childUpdates.put("/user-messages/" + user.getUid() + "/" + key, messageValues);
     */
}
