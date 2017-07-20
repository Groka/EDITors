package com.editors.viberbot.service;

import java.util.Optional;
import java.util.concurrent.Future;

import com.viber.bot.Response;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;
import com.viber.bot.message.Message;

public interface ViberBotService {
	Future<Optional<Message>> conversationStarted(IncomingConversationStartedEvent event);
	void subscribe(IncomingSubscribedEvent event, Response response);
	void unsubscribe(IncomingUnsubscribeEvent event);
	void messageReceived(IncomingMessageEvent event, Message message, Response response);
}
