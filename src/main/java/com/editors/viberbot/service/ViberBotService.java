package com.editors.viberbot.service;

import com.viber.bot.Response;
import com.viber.bot.event.callback.OnMessageReceived;
import com.viber.bot.event.callback.OnSubscribe;
import com.viber.bot.event.callback.OnUnsubscribe;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.event.incoming.IncomingMessageEvent;
import com.viber.bot.event.incoming.IncomingSubscribedEvent;
import com.viber.bot.event.incoming.IncomingUnsubscribeEvent;
import com.viber.bot.message.Message;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by Tarik on 24.07.2017..
 */
public interface ViberBotService {
    public Future<Optional<Message>> onConversationStarted(final @Nonnull IncomingConversationStartedEvent event);
    public void onMessageReceived(IncomingMessageEvent event, Message message, Response response);
    public void onSubscribe(IncomingSubscribedEvent event);
    public void onUnsubscribe(IncomingUnsubscribeEvent event);
}
