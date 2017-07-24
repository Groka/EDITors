package com.editors.viberbot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.editors.viberbot.database.entity.Reservation;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.message.Message;
import com.viber.bot.message.MessageKeyboard;
import com.viber.bot.message.TextMessage;
import com.viber.bot.message.TrackingData;

public class HelperMethods {
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired 
	private UserService userService;
	
	protected MessageKeyboard createMessageKeyboard(final ArrayList<HashMap<String, Object>> buttons){
    	// Create a map for initialization of MessageKeyboard
        Map<String, Object> mapMessageKeyboard = new HashMap<>();
        mapMessageKeyboard.put("Type", "keyboard");
        mapMessageKeyboard.put("DefaultHight", true);
        mapMessageKeyboard.put("Buttons", buttons);

        // Create MessageKeyboard object and return it

        return new MessageKeyboard(mapMessageKeyboard);
    }
    
    protected TextMessage goToMain(IncomingConversationStartedEvent cvrEvent, IncomingMessageEvent msgEvent, Message message){
    	// Button for reserving a room
        HashMap<String, Object> btnReserveARoom = new HashMap<>();
        btnReserveARoom.put("Columns", 6);
        btnReserveARoom.put("Rows", 1);
        btnReserveARoom.put("BgColor", "#2db9b9");
        btnReserveARoom.put("ActionType", "reply");
        btnReserveARoom.put("ActionBody", "make_a_reservation_step_1");
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

    /*
    *   Returns TextMessage object with keyboard displaying
    *   buttons representing reservations
    */
    protected TextMessage showReservations(IncomingMessageEvent event, Message message){
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
        for(Reservation reservation : reservations){;
        	String msgInfo = "Reservation info: ";
        	String msg = "Date - " + reservation.getDate().toString() + ", ";
        	msg += "Time - " + reservation.getTime().toString() + ", ";
        	msg += "Room name - " + reservation.getRoom().getName();
        	HashMap<String, Object> btn = new HashMap<>();
            btn.put("Columns", 6);
            btn.put("Rows", 1);
            btn.put("BgColor", "#2db9b9");
            btn.put("ActionType", "reply");
            btn.put("ActionBody", "reservation_id_" + reservation.getId());
            btn.put("Text", msgInfo + msg);
            btn.put("TextVAlign", "middle");
            btn.put("TextHAlign", "center");
            btn.put("TextSize", "regular");
            
            buttons.add(btn);
        }

        MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);
        
    	TextMessage textMessage = 
    			new TextMessage("Click on the reservation to see details", messageKeyboard, trackingData, null);
    	
    	return textMessage;
    }

    protected TextMessage makeAReservationStep1(IncomingMessageEvent event, Message message) {
        Map<String, Object> mapTrackingData = new HashMap<>();
        mapTrackingData.put("menu", "make_a_reservation_step_1");
        TrackingData trackingData = new TrackingData(mapTrackingData);
        return new TextMessage("Please enter the date", null, trackingData, null);
    }

    /*
    Step 2 of creating a reservation - Date parsing
     */

    protected Message getDate(IncomingMessageEvent event, Message message){
        String date;
        try{
            date = message.getMapRepresentation().get("text").toString();
            System.out.println("Date: " + date);
        }catch(Exception e){
            System.out.println("DATE PARSING BELAJ");
            e.printStackTrace();
        }

        return message;
    }
}
