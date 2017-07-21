package com.editors.viberbot.service;

import com.editors.viberbot.config.Flow;
import com.editors.viberbot.config.HelperMethods;
import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.entity.User;
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
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
public class ViberBotServiceImpl extends Flow implements ViberBotService {
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired 
	private UserService userService;

	
    @Override
    public Future<Optional<Message>> onConversationStarted(IncomingConversationStartedEvent event) {
    	
    	onSubscribe(event);

    	// Greetings message
        String greeting = "Welcome to room reservation bot " + event.getUser().getName();
        return Futures.immediateFuture(Optional.of(goToMain(greeting)));
    }
        
    
    @Override
    public void onMessageReceived(IncomingMessageEvent event, Message message, Response response) {
    	TrackingData trackingData = message.getTrackingData();
    	
    	String dataToTrack = trackingData.get("menu").toString();
    	
    	switch(dataToTrack){	
    	// main menu
    	case "main":
    		if(message.getMapRepresentation().get("text").equals("show_reservations")){
    			String viberId = event.getSender().getId();
    			showReservations(message, response, false, viberId);
    		}
    		else if(message.getMapRepresentation().get("text").equals("make_a_reservation")){
    			showRooms(message, response, false);
    		}
    		else{
    			String error = "Please choose an option from the menu.";
    			response.send(goToMain(error));
    		}
    		break;
    		
    	// Show reservations
    	case "show_reservations_step_1":
    		// Displays info about single reservation
    		if(message.getMapRepresentation().get("text").toString().equals("returnToMain")){
    			// Greetings message
    	        String greeting = "Welcome to room reservation bot " + event.getSender().getName();
    			response.send(goToMain(greeting));
    		}
    		else showReservation(message, response, false);
    		break;
    	case "show_reservations_step_2":
    		if(message.getMapRepresentation().get("text").toString().equals("returnToMain")){
    			// Greetings message
    	        String greeting = "Welcome to room reservation bot " + event.getSender().getName();
    			response.send(goToMain(greeting));
    		}
    		else cancelReservationConfirm(message, response, false);
    		break;
    	case "confirm_cancel_reservation":
    		if(message.getMapRepresentation().get("text").toString().equals("returnToMain")){
    			// Greetings message
    	        String greeting = "Welcome to room reservation bot " + event.getSender().getName();
    			response.send(goToMain(greeting));
    		}
    		else cancelReservation(message, response);
    		break;
    		
    		
    		
    		
    	// Date input; if valid respond with room menu to choose a room
    	case "make_a_reservation_step_1":
    		if(message.getMapRepresentation().get("text").toString().equals("returnToMain")){
    			// Greetings message
    	        String greeting = "Welcome to room reservation bot " + event.getSender().getName();
    			response.send(goToMain(greeting));
    		}
    		else askForDate(message, response, true, false);
    		break;
    	// Choose a room
    	// Show available appointments
    	case "make_a_reservation_step_2":
    		if(message.getMapRepresentation().get("text").toString().equals("returnToMain")){
    			// Greetings message
    	        String greeting = "Welcome to room reservation bot " + event.getSender().getName();
    			response.send(goToMain(greeting));
    		}
    		else showFreePeriods(message, response, true, false);
    		break;
    	case "make_a_reservation_step_3":
    		if(message.getMapRepresentation().get("text").toString().equals("returnToMain")){
    			// Greetings message
    	        String greeting = "Welcome to room reservation bot " + event.getSender().getName();
    			response.send(goToMain(greeting));
    		}
    		else confirmNewReservation(message, response, true, false);
    		break;
    	case "make_a_reservation_end":
    		if(message.getMapRepresentation().get("text").toString().equals("returnToMain")){
    			// Greetings message
    	        String greeting = "Welcome to room reservation bot " + event.getSender().getName();
    			response.send(goToMain(greeting));
    		}
    		else addReservation(event, message, response);
    		break;
    	default:
    		System.out.println("BAD STUFF: DEFAULT IN VIBERBOTSERVICEIMPL");
    		break;
    	}
    	
    }

	@Override
    public void onSubscribe(IncomingConversationStartedEvent event) {
    	String viberId = event.getUser().getId();
    	try {
			userService.subscribe(viberId);
		} catch (NotFoundException e) {
			String userName = event.getUser().getName();
			User user = new User(viberId, userName, true);
			userService.addUser(user);
		}
    }

    @Override
    public void onUnsubscribe(IncomingUnsubscribeEvent event) {
        String viberId = event.getUserId();
        try {
            userService.unsubscribe(viberId);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
}

