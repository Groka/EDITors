package com.editors.viberbot.service;

import java.security.InvalidParameterException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    /*
        Return MessageKeyboard object for main menu
        with custom message
     */
    
    protected TextMessage goToMain(String message){
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
        
        // Map for trackingdata
        
        Map<String, Object> mapTrackingData = new HashMap<>();
        mapTrackingData.put("menu", "main");
        
        
        // TrackingData object
        TrackingData trackingData = new TrackingData(mapTrackingData);

        return new TextMessage(message, messageKeyboard, trackingData, null);
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
        for(Reservation reservation : reservations){
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
    
    protected void showRooms(Message message, Response response, boolean wasInvalid) {
    	
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
		mapTrackingData.put("menu", "make_a_reservation_step_1");
		
		// create TrackingData object
		TrackingData trackingData = new TrackingData(mapTrackingData);
		
		// respond
		String responseText = wasInvalid ? "Invalid input. " : "";
		responseText += "Please choose a room from the menu";
    	response.send(new TextMessage(responseText, messageKeyboard, trackingData, null));
    }
    
    // Ask for the date
    protected void askForDate(Message message, Response response, boolean wasInvalid){
    	// Get room
    	Room room = null;
    	try{
    		room = checkRoom(message);
    	}catch(IllegalArgumentException e){
    		System.out.println("Bad rooooooooom");
    		e.printStackTrace();
    		showRooms(message, response, true);
    	}
    	
    	String responseText = wasInvalid ? "Invalid date. " : "";
    	responseText += "Please enter a date in DD-MM-YYYY format";
    	
    	// Create map for TrackingData object
		Map<String, Object> mapTrackingData = new HashMap<>();
		mapTrackingData.put("menu", "make_a_reservation_step_2");
		mapTrackingData.put("roomId", room.getId());
		
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
			if(tmpArray.length != 3) throw new DateTimeException("Some values are missing in date");
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
     * make_a_reservation_step_3
     */
    
    protected void showFreePeriods(Message message, Response response, boolean checkDate, boolean wasInvalid){
    	LocalDate date = null;
    	if(checkDate){
    		try{
    			date = checkDate(message);
    		}catch(IllegalArgumentException e){
    			// if date is invalid ask for date again
    			askForDate(message, response, true);
    		}
    	}
	    	
    	// Get room from trackingData
    	Long roomId = Long.valueOf(message.getTrackingData().get("roomId").toString());
    	Room room = null;
		try {
			room = roomService.getOne(roomId);
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Get free periods on the given date and room
    	List<LocalTime> periods = new ArrayList<>();
    	try {
			periods = reservationService.getFreeRoomCapacitiesOnDate(room.getId(), date);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Nadam se da se nikada neces prikazat");
			e.printStackTrace();
		}
    	
    	// Create array for buttons
    	// Available periods will be shown in buttons
    	ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
    	
    	// Add buttons
    	for(LocalTime localTime : periods){
    		HashMap<String, Object> btn = new HashMap<>();
            btn.put("Columns", 2);
            btn.put("Rows", 1);
            btn.put("BgColor", "#2db9b9");
            btn.put("ActionType", "reply");
            btn.put("ActionBody", "time=" + localTime.toString());
            btn.put("Text", localTime.toString());
            btn.put("TextVAlign", "middle");
            btn.put("TextHAlign", "center");
            btn.put("TextSize", "regular");
            
            buttons.add(btn);
    	}
    	
    	// Create MessageKeyboard object
    	MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);
    	
    	// Create map for TrackingData object
    	Map<String, Object> mapTrackingData = new HashMap<>();
    	
    	mapTrackingData.put("menu", "make_a_reservation_step_3");
    	mapTrackingData.put("date", date.toString());
    	mapTrackingData.put("roomId", room.getId());
    	
    	// Create TrackingData object
    	TrackingData trackingData = new TrackingData(mapTrackingData);
    	
    	//Response text
    	String responseText = wasInvalid ? "Invalid time. " : "";
    	responseText += "Please choose a time from the menu";
    	
    	response.send(new TextMessage(responseText, messageKeyboard, trackingData, null));
    	
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
     * 
     * make_a_reservation_confirm
     */
    protected void confirmNewReservation(Message message, Response response){
    	Map<String, Object> mapTrackingData = new HashMap<>();
    	
    	// Create trackingData map
    	mapTrackingData.put("menu", "make_a_reservation_end");
    	mapTrackingData.put("date", message.getTrackingData().get("date").toString());
    	mapTrackingData.put("roomId", message.getTrackingData().get("roomId").toString());
    	
    	
    	// Get time
    	String[] timeStr = message.getMapRepresentation().get("text").toString().split("=");
    	// First check if time is good
    	if(timeStr.length == 0) 
    		showFreePeriods(message, response, false, true);
    	LocalTime time = LocalTime.parse(timeStr[1]);
    	
    	// Add time to trackingData
    	mapTrackingData.put("time", time.toString());
    	
    	// Create TrackingData
    	
    	TrackingData trackingData = new TrackingData(mapTrackingData);
    	
    	// Create array for buttons
    	
    	ArrayList<HashMap<String, Object>> buttons = new ArrayList<> ();
    	
    	// Create confirm button
    	HashMap<String, Object> btnConfirm = new HashMap<>();
        btnConfirm.put("Columns", 6);
        btnConfirm.put("Rows", 1);
        btnConfirm.put("BgColor", "#2db9b9");
        btnConfirm.put("ActionType", "reply");
        btnConfirm.put("ActionBody", "make_a_reservation_end");
        btnConfirm.put("Text", "Confirm");
        btnConfirm.put("TextVAlign", "middle");
        btnConfirm.put("TextHAlign", "center");
        btnConfirm.put("TextSize", "regular");
        
        buttons.add(btnConfirm);
        
        // Create MessageKeyboard object
        MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);

        // Get room by id
        Room room = null;
        try {
            room = roomService.getOne(Long.valueOf(message.getTrackingData().get("roomId").toString()));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        // Response
    	String responseText = "Reservation info: Date " + message.getTrackingData().get("date").toString();

        // Calculated reservation ending time
        LocalTime endTime = LocalTime.of(time.getHour() + 1, time.getMinute());
        responseText += " from " + time.toString() + " to " + endTime.toString();
        responseText += " for room: " + room.getName() + " " + room.getNumber();
        responseText += ". Please confirm.";
    	response.send(new TextMessage(responseText, messageKeyboard, trackingData, null));
    }
    
    /*
     * END OF MAKE_A_RESERVATION
     */
    
    protected void addReservation(IncomingMessageEvent event, Message message, Response response){
    	// Get trackingData
    	TrackingData trackingData = message.getTrackingData();
    	
    	// Get roomId
    	Long roomId = Long.valueOf(trackingData.get("roomId").toString());
    	// Get room
    	Room room = new Room();
		try {
			room = roomService.getOne(roomId);
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Get date
    	LocalDate date = LocalDate.parse(trackingData.get("date").toString());
    	
    	// Get time  	
    	LocalTime time = LocalTime.parse(trackingData.get("time").toString());
    	
    	// Get users viberId
    	String viberId = event.getSender().getId();
    	
    	// Get user by viberId
    	User user = new User();
		try {
			user = userService.getByViberId(viberId);
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
    	
    	
    	// Create a new Reservation object
    	Reservation reservation = new Reservation(user, room, date, time);
    	reservationService.reserve(reservation);
    	
    	// Respond with main menu and confirmation
        String responseText = "Created reservation on " + date.toString();
        // Calculated reservation ending time
        LocalTime endTime = LocalTime.of(time.getHour() + 1, time.getMinute());
        responseText += " from " + time.toString() + " to " + endTime.toString();
        responseText += " for room: " + room.getName() + " " + room.getNumber();

        response.send(goToMain(responseText));
    }
}
