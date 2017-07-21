package com.editors.viberbot.service;

import java.util.Optional;
import java.util.concurrent.Future;

import com.viber.bot.event.callback.OnMessageReceived;
import com.viber.bot.event.callback.OnSubscribe;
import com.viber.bot.event.callback.OnUnsubscribe;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;
import com.viber.bot.message.Message;

public class ViberBotServiceImpl2 implements ViberBotService {

	@Override
	public Future<Optional<Message>> onConversationStarted(IncomingConversationStartedEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMessageReceived(IncomingMessageEvent event) {
		
	}

	@Override
	public void onSubscribe(IncomingSubscribedEvent event) {
	
		
		
	}

	@Override
	public void onUnsubscribe(IncomingUnsubscribeEvent event) {

		
		
	}

}
