package com.editors.viberbot.service;

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
public class ViberBotServiceImpl extends HelperMethods implements ViberBotService {
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired 
	private UserService userService;
	
    @Override
    public Future<Optional<Message>> onConversationStarted(IncomingConversationStartedEvent event) {
    	/*
    	String viberId = event.getUser().getId();
    	User user = new User();
    	try {
			user = userService.getByViberId(viberId);
			System.out.println("POSTOJIIIIIIIIII");
		} catch (NotFoundException e) {
			System.out.println("NE POSTOJI U BAZI NE POSTOJI");
			String userName = event.getUser().getName();
			userService.addUser(new User(viberId, userName, true));
		}
		*/
    	onSubscribe(event);

    	// Greetings message
        String greeting = "Welcome to room reservation bot " + event.getUser().getName();
        return Futures.immediateFuture(Optional.of(goToMain(greeting)));
    }
        
    
    @Override
    public void onMessageReceived(IncomingMessageEvent event, Message message, Response response) {
    	TrackingData trackingData = message.getTrackingData();
    	
    	// For testing purposes
    	System.out.println("Keys in the trackingdata:\n");
    	
    	for(String s : trackingData.keySet())
    		System.out.println(s);
    	
    	System.out.println(trackingData.get("menu").toString());
    	
    	String dataToTrack = trackingData.get("menu").toString();
    	
    	switch(dataToTrack){	
    	// main menu
    	case "main":
    		if(message.getMapRepresentation().get("text").equals("show_reservations"))
    			response.send(showReservations(event, message));
    		else if(message.getMapRepresentation().get("text").equals("make_a_reservation")){
    			askForDate(response, false);
    		}
    			
    		break;
    	// Show reservations
    	case "show_reservations":
    		//if(message.getMapRepresentation().get("text").equals(""))
    		break;
    	// Date input; if valid respond with room menu to choose a room
    	case "make_a_reservation_step_1":
    		// Get rooms
    		showRooms(message, response, true, false);
    		break;
    	// Choose a room
    	// Show available appointments
    	case "make_a_reservation_step_2":
    		showFreePeriods(message, response, false);
    		break;
    	case "make_a_reservation_step_3":
    		confirmNewReservation(message, response);
    		break;
    	case "make_a_reservation_confirm":
    		confirmNewReservation(message, response);
    		break;
    	case "make_a_reservation_end":
    		addReservation(event, message, response);
    		break;
    	default:
    		System.out.println("U defaultu");
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

    }
}

