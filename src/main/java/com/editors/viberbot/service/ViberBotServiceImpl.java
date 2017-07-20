package com.editors.viberbot.service;

import com.google.common.util.concurrent.Futures;
import com.viber.bot.Response;
import com.viber.bot.event.callback.OnConversationStarted;
import com.viber.bot.event.callback.OnMessageReceived;
import com.viber.bot.event.callback.OnSubscribe;
import com.viber.bot.event.callback.OnUnsubscribe;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;
import com.viber.bot.message.Message;
import com.viber.bot.message.MessageKeyboard;
import com.viber.bot.message.TextMessage;
import com.viber.bot.message.TrackingData;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by User on 24.07.2017..
 */

@Service("viberBotService")
public class ViberBotServiceImpl implements ViberBotService {
	
	ViberBotServiceImpl2 viberBotServiceImpl2 = new ViberBotServiceImpl2();
	
    @Override
    public Future<Optional<Message>> onConversationStarted(IncomingConversationStartedEvent event) {
        // Button for reserving a room
        HashMap<String, Object> btnReserveARoom = new HashMap<>();
        btnReserveARoom.put("Columns", 6);
        btnReserveARoom.put("Rows", 1);
        btnReserveARoom.put("BgColor", "#2db9b9");
        btnReserveARoom.put("ActionType", "reply");
        btnReserveARoom.put("ActionBody", "Reserve a room");
        btnReserveARoom.put("Text", "Reserve a room");
        btnReserveARoom.put("TextVAlign", "middle");
        btnReserveARoom.put("TextHAlign", "center");
        btnReserveARoom.put("TextSize", "regular");
        // Button to show reservatios
        HashMap<String, Object> btnShowReservations = new HashMap<>();
        btnShowReservations.put("Columns", 6);
        btnShowReservations.put("Rows", 1);
        btnShowReservations.put("BgColor", "#2db9b9");
        btnShowReservations.put("ActionType", "reply");
        btnShowReservations.put("ActionBody", "Show reservations lomaaa");
        btnShowReservations.put("Text", "Show reservations");
        btnShowReservations.put("TextVAlign", "middle");
        btnShowReservations.put("TextHAlign", "center");
        btnShowReservations.put("TextSize", "regular");

        // buttons array
        ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
        // add buttons to the array
        buttons.add(btnReserveARoom);
        buttons.add(btnShowReservations);

        // Create a map for initialization of MessageKeyboard
        Map<String, Object> mapMessageKeyboard = new HashMap<>();
        mapMessageKeyboard.put("Type", "keyboard");
        mapMessageKeyboard.put("DefaultHight", true);
        mapMessageKeyboard.put("Buttons", buttons);

        // Create MessageKeyboard object

        MessageKeyboard messageKeyboard = new MessageKeyboard(mapMessageKeyboard);

        // Text to show when conversation starts
        String userName = event.getUser().getName();
        String text = "Greetings " + userName + "!";
        
        // Map for trackingdata
        
        Map<String, Object> mapTrackingData = new HashMap<>();
        mapTrackingData.put("menu", "main");
        
        
        // TrackingData object
        TrackingData trackingData = new TrackingData(mapTrackingData);

        TextMessage textMessage = new TextMessage(text, messageKeyboard, trackingData, null);
        
        return Futures.immediateFuture(Optional.of(textMessage));
    }
    
    private TextMessage showReservations(IncomingMessageEvent event, Message message){
    	// Map for trackingdata
        
        Map<String, Object> mapTrackingData = new HashMap<>();
        mapTrackingData.put("menu", "show_reservations");
        
        // TrackingData object
        TrackingData trackingData = new TrackingData(mapTrackingData);
        
    	TextMessage textMessage = new TextMessage("dobar radi vadi", null, trackingData, null);
    	
    	return textMessage;
    }
    
    @Override
    public void onMessageReceived(IncomingMessageEvent event, Message message, Response response) {
    	TrackingData trackingData = message.getTrackingData();
    	
    	// For testing purposes
    	System.out.println("Keys in the trackingdata:\n");
    	
    	for(String s : trackingData.keySet())
    		System.out.println(s);
    	
    	System.out.println(trackingData.get("menu").toString());
    	
    	switch(trackingData.get("menu").toString()){	
    	case "main":
    		System.out.println(message.toString());
    		//if(message.getMapRepresentation().get("ActionBody").equals("Show reservations"))
    			//response.send(showReservations(event, message));
    		break;
    	default:
    		System.out.println("U defaultu");
    		break;
    	}
    	
    }

    @Override
    public void onSubscribe(IncomingSubscribedEvent event) {
    	
    }

    @Override
    public void onUnsubscribe(IncomingUnsubscribeEvent event) {

    }
}

