package com.editors.viberbot.config;

import java.security.InvalidParameterException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.database.entity.User;
import com.editors.viberbot.service.ReservationService;
import com.editors.viberbot.service.RoomService;
import com.editors.viberbot.service.UserService;
import com.google.common.util.concurrent.Futures;
import com.viber.bot.Response;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.message.Message;
import com.viber.bot.message.MessageKeyboard;
import com.viber.bot.message.TextMessage;
import com.viber.bot.message.TrackingData;

public abstract class HelperMethods {
	@Autowired
	protected RoomService roomService;
	
	@Autowired
	protected ReservationService reservationService;
	
	@Autowired 
	protected UserService userService;
	
	protected final MessageKeyboard createMessageKeyboard(final ArrayList<HashMap<String, Object>> buttons){
    	// Create a map for initialization of MessageKeyboard
        Map<String, Object> mapMessageKeyboard = new HashMap<>();
        mapMessageKeyboard.put("Type", "keyboard");
        mapMessageKeyboard.put("DefaultHight", true);
        mapMessageKeyboard.put("Buttons", buttons);

        // Create MessageKeyboard object and return it

        return new MessageKeyboard(mapMessageKeyboard);
    }

    /*
        Return MessageKeyboard object for main menu
        with custom message
     */
    
    protected final TextMessage goToMain(String message){
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
        btnShowReservations.put("Text", "Show my reservations");
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
        
        // Map for trackingdata
        
        Map<String, Object> mapTrackingData = new HashMap<>();
        mapTrackingData.put("menu", "main");
        
        
        // TrackingData object
        TrackingData trackingData = new TrackingData(mapTrackingData);

        return new TextMessage(message, messageKeyboard, trackingData, null);
    }

    
    
    
    
    
    /*
     * CHECK DATE
     */
    protected final LocalDate checkDate(Message message) throws IllegalArgumentException{
    	/*
    	 * Date needs to be in DD-MM-YYYY format
    	 */
    	String[] tmpArray = message.getMapRepresentation().get("text").toString().split("\\.|/|-");
    	LocalDate date;
		try{
			if(tmpArray.length != 3) throw new DateTimeException("Some values are missing in date");
			date = LocalDate.of(Integer.valueOf(tmpArray[2]), Integer.valueOf(tmpArray[1]), Integer.valueOf(tmpArray[0]));
			
			LocalDate today = LocalDate.now();
						
			if(date.isBefore(today)) throw new DateTimeException("Date must be in future");		
		}catch(DateTimeException e){
			throw new IllegalArgumentException();
		}
		return date;
    }
        
    
    /*
     * Check if room is valid
     * 
     */
    protected final Room checkRoom(Message message){
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
     * Check if inputed time is ok
     */
    private LocalTime checkTime(Message message) throws IllegalArgumentException {
    	if(!message.getMapRepresentation().containsKey("text"))
    		throw new IllegalArgumentException();
    	String timeStr = message.getMapRepresentation().get("text").toString();
    	LocalTime time = LocalTime.parse(timeStr.split("=")[1]); // PROVJERITI
    	return time;
    }
    
    
    /*
     * Button to return to main menu
     */
    protected final HashMap<String, Object> btnReturnToMain(){
    	// Create returnToMain button
        HashMap<String, Object> btnReturnToMain = new HashMap<>();
        btnReturnToMain.put("Columns", 6);
        btnReturnToMain.put("Rows", 1);
        btnReturnToMain.put("BgColor", "#f96164");
        btnReturnToMain.put("ActionType", "reply");
        btnReturnToMain.put("ActionBody", "returnToMain");
        btnReturnToMain.put("Text", "Return to main");
        btnReturnToMain.put("TextVAlign", "middle");
        btnReturnToMain.put("TextHAlign", "center");
        btnReturnToMain.put("TextSize", "regular");
        return btnReturnToMain;
    }
}
