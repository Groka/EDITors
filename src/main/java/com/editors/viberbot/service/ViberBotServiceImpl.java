package com.editors.viberbot.service;

import com.editors.viberbot.database.entity.Reservation;
import com.google.common.util.concurrent.Futures;
import com.viber.bot.Response;
import com.viber.bot.event.callback.OnConversationStarted;
import com.viber.bot.event.callback.OnMessageReceived;
import com.viber.bot.event.callback.OnSubscribe;
import com.viber.bot.event.callback.OnUnsubscribe;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingErrorEvent;
import com.viber.bot.event.incoming.IncomingEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;
import com.viber.bot.message.Message;
import com.viber.bot.message.MessageKeyboard;
import com.viber.bot.message.TextMessage;
import com.viber.bot.message.TrackingData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by User on 24.07.2017..
 */

@Service("viberBotService")
public class ViberBotServiceImpl implements ViberBotService {
	
	private ViberBotServiceImpl2 viberBotServiceImpl2 = new ViberBotServiceImpl2();
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private ReservationService reservationService;
	
    @Override
    public Future<Optional<Message>> onConversationStarted(IncomingConversationStartedEvent event) {
        return Futures.immediateFuture(Optional.of(goToMain(event, null, null)));
    }
    
    private MessageKeyboard createMessageKeyboard(final ArrayList<HashMap<String, Object>> buttons){
    	// Create a map for initialization of MessageKeyboard
        Map<String, Object> mapMessageKeyboard = new HashMap<>();
        mapMessageKeyboard.put("Type", "keyboard");
        mapMessageKeyboard.put("DefaultHight", true);
        mapMessageKeyboard.put("Buttons", buttons);

        // Create MessageKeyboard object and return it

        return new MessageKeyboard(mapMessageKeyboard);
    }
    
    private TextMessage goToMain(IncomingConversationStartedEvent cvrEvent, IncomingMessageEvent msgEvent, Message message){
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
        btnShowReservations.put("ActionBody", "show_reservations");
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

        MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);

        // Text to show when conversation starts
        String userName = cvrEvent.getUser().getName();
        String text = "Greetings " + userName + "!";
        
        // Map for trackingdata
        
        Map<String, Object> mapTrackingData = new HashMap<>();
        mapTrackingData.put("menu", "main");
        
        
        // TrackingData object
        TrackingData trackingData = new TrackingData(mapTrackingData);

        return new TextMessage(text, messageKeyboard, trackingData, null);
    }
    
    private TextMessage showReservations(IncomingMessageEvent event, Message message){
    	// Map for trackingdata
        
        Map<String, Object> mapTrackingData = new HashMap<>();
        mapTrackingData.put("menu", "show_reservations");
        
        // TrackingData object
        TrackingData trackingData = new TrackingData(mapTrackingData);
        
        // Get user reservations
        String viberId = event.getSender().getId();
        List<Reservation> reservations = reservationService.getByUser(viberId);
        
        // Creating array of reservations for keyboard
        ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
        
        // Add all reservations from DB to the array
        for(Reservation reservation : reservations){
        	String msgDelete = "delete_reservation_";
        	String msgInfo = "Reservation info: ";
        	String msg = "Date - " + reservation.getDate().toString() + ", ";
        	msg += "Time - " + reservation.getTime().toString() + ", ";
        	msg += "Room name - " + reservation.getRoom().getName();
        	HashMap<String, Object> btn = new HashMap<>();
            btn.put("Columns", 6);
            btn.put("Rows", 1);
            btn.put("BgColor", "#2db9b9");
            btn.put("ActionType", "reply");
            btn.put("ActionBody", msgDelete + reservation.getId());
            btn.put("Text", msgInfo + msg);
            btn.put("TextVAlign", "middle");
            btn.put("TextHAlign", "center");
            btn.put("TextSize", "regular");
            
            buttons.add(btn);
        }

        MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);
        
    	TextMessage textMessage = 
    			new TextMessage("Click on the reservation if you want to delete it", messageKeyboard, trackingData, null);
    	
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
    		if(message.getMapRepresentation().get("text").equals("show_reservations"))
    			response.send(showReservations(event, message));
    		break;
    	case "show_reservations":
    		if(message.getMapRepresentation().get("text").equals(""))
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

