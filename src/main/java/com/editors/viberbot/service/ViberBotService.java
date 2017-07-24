package com.editors.viberbot.service;

import com.viber.bot.event.callback.OnMessageReceived;
import com.viber.bot.event.callback.OnSubscribe;
import com.viber.bot.event.callback.OnUnsubscribe;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.message.Message;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by Tarik on 24.07.2017..
 */
public interface ViberBotService {
    public Future<Optional<Message>> onConversationStarted(final @Nonnull IncomingConversationStartedEvent listener);
    public void onMessageReceived(final @Nonnull OnMessageReceived listener);
    public void onSubscribe(final @Nonnull OnSubscribe listener);
    public void onUnsubscribe(final @Nonnull OnUnsubscribe listener);
}
