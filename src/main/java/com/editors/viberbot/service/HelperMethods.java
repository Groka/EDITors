package com.editors.viberbot.service;

import java.security.InvalidParameterException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.entity.Room;
import com.google.common.util.concurrent.Futures;
import com.viber.bot.Response;
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
        btnReserveARoom.put("ActionBody", "make_a_reservation");
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
    
    /*
     * Show rooms keyboard
     * if checkDate is true then check the date xD
     * 
     * make_a_reservation_step_2
     */
    
    protected void showRooms(Message message, Response response, boolean checkDate) {
    	LocalDate date = null;
    	if(checkDate){
    		try{
    			date = checkDate(message);
    		}catch(IllegalArgumentException e){
    			// if date is invalid ask for date again
    			askForDate(response, true);
    		}
    	}
    	// get all rooms from DB
		List<Room> rooms = roomService.findAll();
		
		// store rooms as buttons in array
		ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
		for(Room room : rooms){
			HashMap<String, Object> btn = new HashMap<>();
            btn.put("Columns", 2);
            btn.put("Rows", 1);
            btn.put("BgColor", "#2db9b9");
            btn.put("ActionType", "reply");
            btn.put("ActionBody", "room_id=" + room.getId());
            btn.put("Text", room.getName() + " " + room.getNumber());
            btn.put("TextVAlign", "middle");
            btn.put("TextHAlign", "center");
            btn.put("TextSize", "regular");
            
            buttons.add(btn);
		}
		
		// Create messageKeyboard object
		MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);
		
		// create map for trackingdata
		Map<String, Object> mapTrackingData = new HashMap<>();
		mapTrackingData.put("menu", "make_a_reservation_step_2");
		mapTrackingData.put("date", date.toString()); 
		
		// create TrackingData object
		TrackingData trackingData = new TrackingData(mapTrackingData);
		
		// respond
    	response.send(new TextMessage("Please choose a room from the menu", messageKeyboard, trackingData, null));
    }
    
    // Ask for the date
    protected void askForDate(Response response, boolean wasInvalid){
    	String responseText = wasInvalid ? "Invalid date. " : "";
    	responseText += "Please enter a date in DD-MM-YYYY format";
    	
    	// Create map for TrackingData object
		Map<String, Object> mapTrackingData = new HashMap<>();
		mapTrackingData.put("menu", "make_a_reservation_step_1"); // next is date input
		
		// Create TrackingData object
		TrackingData trackingData = new TrackingData(mapTrackingData);
		response.send(new TextMessage(responseText, null, trackingData, null));
    }
    
    
    /*
     * CHECK DATE
     */
    private LocalDate checkDate(Message message) throws IllegalArgumentException{
    	/*
    	 * Date needs to be in DD-MM-YYYY format
    	 */
    	String[] tmpArray = message.getMapRepresentation().get("text").toString().split("\\.|/|-");
    	LocalDate date;
		try{
			date = LocalDate.of(Integer.valueOf(tmpArray[2]), Integer.valueOf(tmpArray[1]), Integer.valueOf(tmpArray[0]));
		}catch(DateTimeException e){
			throw new IllegalArgumentException();
		}
		return date;
    }
    
    
    
    /*
     * Check if room is valid
     * 
     */
    private Room checkRoom(Message message){
    	Long roomId = null;
    	try{
    		// Get actionBody
    		String actionBody = message.getMapRepresentation().get("text").toString();
    		// Get room id from the message
    		roomId = Long.valueOf(actionBody.split("=")[1]);
    	}catch(Exception e){
    		System.out.println("Couldn't get roomId from ActionBody");
    		e.printStackTrace();
    	}
    	Room room = new Room();
    	try{
    		room = roomService.getOne(roomId);
    	}catch(NotFoundException e){
    		System.out.println("The room does not exist in DATABASE");
    		e.printStackTrace();
    	}
    	return room;
    }
    
    /*
     * 
     */

}
