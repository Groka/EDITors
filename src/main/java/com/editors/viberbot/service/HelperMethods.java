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
    *   Returns TextMessage object with keyboard displaying
    *   buttons representing reservations
    *  
    *   show_reservations_step_1
    */
    protected void showReservations(Message message, Response response, boolean wasInvalid, String viberId){
    	// Map for trackingdata
        
        Map<String, Object> mapTrackingData = new HashMap<>();
        mapTrackingData.put("menu", "show_reservations_step_1");
        mapTrackingData.put("viberId", viberId);
        
        // TrackingData object
        TrackingData trackingData = new TrackingData(mapTrackingData);
        
        List<Reservation> reservations = reservationService.getByUser(viberId);
        
        // Creating array of reservations for keyboard
        ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
        
        // Add all reservations from DB to the array
        for(Reservation reservation : reservations){
        	String msg = "Date - " + reservation.getDate().toString() + ", ";
        	msg += "Time - " + reservation.getTime().toString() + ", ";
        	msg += "Room name - " + reservation.getRoom().getName();
        	HashMap<String, Object> btn = new HashMap<>();
            btn.put("Columns", 6);
            btn.put("Rows", 1);
            btn.put("BgColor", "#2db9b9");
            btn.put("ActionType", "reply");
            btn.put("ActionBody", "reservation_id=" + reservation.getId());
            btn.put("Text", msg);
            btn.put("TextVAlign", "middle");
            btn.put("TextHAlign", "center");
            btn.put("TextSize", "regular");
            
            buttons.add(btn);
        }
        
        // Add button to return to main menu
        buttons.add(btnReturnToMain());

        MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);
        
        // Response text
        String responseText = wasInvalid ? "Please choose reservation from the menu. " : "";
        responseText += "Click on the reservation to see details";
        
    	response.send(new TextMessage(responseText, messageKeyboard, trackingData, null));
    
    }
    
    /*
     * show_reservations_step_2
     * Shows "Cancel reservation" and "Return to main" options
     * Also shows reservation info
     */
    protected void showReservation(Message message, Response response, boolean wasInvalid){
    	// Get viberId
    	String viberId = message.getTrackingData().get("viberId").toString();
    	
    	Long reservationId = null;
    	// Get ActionBody
    	if(!wasInvalid){
    		String actionBody = message.getMapRepresentation().get("text").toString();
    	
    		String[] tmp = actionBody.split("=");
    		// Validation
        	if(!tmp[0].equals("reservation_id")) showReservations(message, response, true, viberId);
        	reservationId = Long.valueOf(tmp[1]);
    	}
    	else reservationId = Long.valueOf(message.getTrackingData().get("reservationId").toString());
    	
    	// Create map for trackingData
        Map<String, Object> mapTrackingData = new HashMap<>();
        
        mapTrackingData.put("menu", "show_reservations_step_2");
        mapTrackingData.put("reservationId", reservationId);
        mapTrackingData.put("viberId", viberId);
        
        // Create trackindata object
        TrackingData trackingData = new TrackingData(mapTrackingData);
    	
    	
    	
    	// Get reservation from DB
    	Reservation reservation = reservationService.getOne(reservationId);
    	if(reservation == null) showReservations(message, response, true, viberId);
    	
    	// Array for buttons
    	ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
    	
    	// Create "Cancel reservation" button
    	HashMap<String, Object> btnCancelReservation = new HashMap<>();
        btnCancelReservation.put("Columns", 6);
        btnCancelReservation.put("Rows", 1);
        btnCancelReservation.put("BgColor", "#ce1212");
        btnCancelReservation.put("ActionType", "reply");
        btnCancelReservation.put("ActionBody", "cancel_reservation");
        btnCancelReservation.put("Text", "Cancel this reservation");
        btnCancelReservation.put("TextVAlign", "middle");
        btnCancelReservation.put("TextHAlign", "center");
        btnCancelReservation.put("TextSize", "regular");
        
        buttons.add(btnCancelReservation);
        buttons.add(btnReturnToMain());
        
        // Create messageKeyboard
        MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);
        
        
        String responseText = wasInvalid ? "Wrong input. Please choose option from the menu. " : "";
        responseText += " Reservation info: ";
        responseText += "Date " + reservation.getDate().toString();
        responseText += " from " + reservation.getTime().toString();
        responseText += " to " + LocalTime.of(reservation.getTime().getHour() + 1, reservation.getTime().getMinute());
        responseText += " for room: " + reservation.getRoom().getName() + " " + reservation.getRoom().getNumber();
        
        response.send(new TextMessage(responseText, messageKeyboard, trackingData, null));
    }
    /*
     * cancel_reservation
     * Asks user to confirm reservation cancelling
     */
    protected void cancelReservationConfirm(Message message, Response response, boolean wasInvalid){
    	
    	//
    	
    	// Create map for trackingData
        Map<String, Object> mapTrackingData = new HashMap<>();
        
        mapTrackingData.put("menu", "confirm_cancel_reservation");
        mapTrackingData.put("reservationId", message.getTrackingData().get("reservationId").toString());
        mapTrackingData.put("viberId", message.getTrackingData().get("viberId").toString());
        
        // Create trackingdata object
        TrackingData trackingData = new TrackingData(mapTrackingData);
        
        // Get reservation from DB
    	Reservation reservation = reservationService.getOne(Long.valueOf(message.getTrackingData().get("reservationId").toString()));
        
        // Response text
        String responseText = wasInvalid ? "Bad input. " : "";
        responseText += " Please confirm that you want to cancel this reservation: ";
        responseText += "Date " + reservation.getDate().toString();
        responseText += " from " + reservation.getTime().toString();
        responseText += " to " + LocalTime.of(reservation.getTime().getHour() + 1, reservation.getTime().getMinute());
        responseText += " for room: " + reservation.getRoom().getName() + " " + reservation.getRoom().getNumber();
        
        // Array for buttons
    	ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
    	
    	// Create "Cancel reservation" button
    	HashMap<String, Object> btnCancelReservation = new HashMap<>();
        btnCancelReservation.put("Columns", 6);
        btnCancelReservation.put("Rows", 1);
        btnCancelReservation.put("BgColor", "#00ff00");
        btnCancelReservation.put("ActionType", "reply");
        btnCancelReservation.put("ActionBody", "confirm_cancel_reservation");
        btnCancelReservation.put("Text", "Confirm");
        btnCancelReservation.put("TextVAlign", "middle");
        btnCancelReservation.put("TextHAlign", "center");
        btnCancelReservation.put("TextSize", "regular");
        
        buttons.add(btnCancelReservation);
        buttons.add(btnReturnToMain());
        
        MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);
        
        response.send(new TextMessage(responseText, messageKeyboard, trackingData, null));
    }
    
    /*
     * confirm_cancel_reservation
     * Deletes reservation and goes back to the main menu
     */
    
    protected void cancelReservation(Message message, Response response){
    	
    	if(!message.getMapRepresentation().get("text").equals("confirm_cancel_reservation"))
    		cancelReservationConfirm(message, response, true);
    	
    	TrackingData trackingData = message.getTrackingData();
    	
    	// Get reservation from base
    	Reservation reservation = reservationService.getOne(Long.valueOf(trackingData.get("reservationId").toString()));
    	
    	// Cancel reservation
    	//reservationService.delete(reservation.getId());
    	
    	ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
    	
    	buttons.add(btnReturnToMain());
    	
    	MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);
    	
    	response.send(goToMain("Reservation cancelled."));
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
		
		// Add button to return to main menu
        buttons.add(btnReturnToMain());
		
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
    protected void askForDate(Message message, Response response, boolean checkRoom, boolean wasInvalid){
    	// Get room
    	Room room = null;
    	if(checkRoom){
	    	try{
	    		room = checkRoom(message);
	    	}catch(IllegalArgumentException e){
	    		System.out.println("Bad rooooooooom");
	    		e.printStackTrace();
	    		showRooms(message, response, true);
	    	}
    	}
    	else{
    		try {
				room = roomService.getOne(Long.valueOf(message.getTrackingData().get("roomId").toString()));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	String responseText = wasInvalid ? "Invalid date. " : "";
    	responseText += "Please enter a date in DD-MM-YYYY format";
    	
    	// Create map for TrackingData object
		Map<String, Object> mapTrackingData = new HashMap<>();
		mapTrackingData.put("menu", "make_a_reservation_step_2");
		mapTrackingData.put("roomId", room.getId());
		
		// Crate button list for keyboard
		ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
		
		// Add button to return to main menu
        buttons.add(btnReturnToMain());
        
        // Create messageKeyboard object
     	MessageKeyboard messageKeyboard = createMessageKeyboard(buttons);
		
		// Create TrackingData object
		TrackingData trackingData = new TrackingData(mapTrackingData);
		response.send(new TextMessage(responseText, messageKeyboard, trackingData, null));
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
    			System.out.println(message.getTrackingData().values().toString());
    			askForDate(message, response, false, true);
    		}
    	}
    	else date = LocalDate.parse(message.getTrackingData().get("date").toString());
	    	
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
    	
    	// Add button to return to main menu
        buttons.add(btnReturnToMain());
    	
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
        
        // Add button to return to main menu
        buttons.add(btnReturnToMain());
        
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
    
    /*
     * Button to return to main menu
     */
    private HashMap<String, Object> btnReturnToMain(){
    	// Create returnToMain button
        HashMap<String, Object> btnReturnToMain = new HashMap<>();
        btnReturnToMain.put("Columns", 6);
        btnReturnToMain.put("Rows", 1);
        btnReturnToMain.put("BgColor", "#ce1212");
        btnReturnToMain.put("ActionType", "reply");
        btnReturnToMain.put("ActionBody", "returnToMain");
        btnReturnToMain.put("Text", "Return to main");
        btnReturnToMain.put("TextVAlign", "middle");
        btnReturnToMain.put("TextHAlign", "center");
        btnReturnToMain.put("TextSize", "regular");
        return btnReturnToMain;
    }
}
