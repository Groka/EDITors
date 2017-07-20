package com.editors.viberbot.service;

import java.util.Optional;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.editors.viberbot.database.entity.User;
import com.google.common.util.concurrent.Futures;
import com.viber.bot.Response;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;
import com.viber.bot.message.Message;
import com.viber.bot.message.TextMessage;
import com.viber.bot.profile.UserProfile;

@Service("viberBotService")
public class ViberBotServiceImpl implements ViberBotService {

	@Override
	public Future<Optional<Message>> conversationStarted(IncomingConversationStartedEvent event) {
		
		UserProfile viberUser = event.getUser();
		User user = new User(viberUser.getId(), viberUser.getName(), true);
			
		
		TextMessage message = new TextMessage("Vozda " + user.getName(), null, null, null);
		
		
		return Futures.immediateFuture(Optional.of(message));
	}

	@Override
	public void subscribe(IncomingSubscribedEvent event, Response response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(IncomingUnsubscribeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(IncomingMessageEvent event, Message message, Response response) {
		// TODO Auto-generated method stub
		
	}

}
