package com.editors.viberbot.service;

import com.google.common.util.concurrent.Futures;
import com.viber.bot.event.callback.OnConversationStarted;
import com.viber.bot.event.callback.OnMessageReceived;
import com.viber.bot.event.callback.OnSubscribe;
import com.viber.bot.event.callback.OnUnsubscribe;
import com.viber.bot.event.incoming.IncomingConversationStartedEvent;
import com.viber.bot.message.Message;
import com.viber.bot.message.MessageKeyboard;
import com.viber.bot.message.TextMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by User on 24.07.2017..
 */

@Service("viberBotService")
public class ViberBotServiceImpl implements ViberBotService {
    @Override
    public Future<Optional<Message>> onConversationStarted(@Nonnull IncomingConversationStartedEvent listener) {
        // Button for reserving a room
        HashMap<String, Object> btnReserveARoom = new HashMap<>();
        btnReserveARoom.put("Columns", 6);
        btnReserveARoom.put("Rows", 1);
        btnReserveARoom.put("BgColor", "#2db9b9");
        btnReserveARoom.put("ActionType", "reply");
        btnReserveARoom.put("ActionBody", "Reserve a room");
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
        btnShowReservations.put("ActionBody", "Show Reservations");
        btnShowReservations.put("Text", "Show reservations");
        btnShowReservations.put("TextVAlign", "middle");
        btnShowReservations.put("TextHAlign", "center");
        btnShowReservations.put("TextSize", "regular");

        // buttons array
        ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
        // add buttons to the array
        buttons.add(btnReserveARoom);
        buttons.add(btnShowReservations);

        // Create a map for initialization of MessageKeyboard
        Map<String, Object> mapMessageKeyboard = new HashMap<>();
        mapMessageKeyboard.put("Type", "keyboard");
        mapMessageKeyboard.put("DefaultHight", true);

        // Create MessageKeyboard object

        MessageKeyboard messageKeyboard = new MessageKeyboard(mapMessageKeyboard);

        // Text to show when conversation starts
        String userName = listener.getUser().getName();
        String text = "Greetings " + userName + "!";

        TextMessage textMessage = new TextMessage(text, messageKeyboard, null, null);

        return Futures.immediateFuture(Optional.of(textMessage));
    }

    @Override
    public void onMessageReceived(@Nonnull OnMessageReceived listener) {

    }

    @Override
    public void onSubscribe(@Nonnull OnSubscribe listener) {

    }

    @Override
    public void onUnsubscribe(@Nonnull OnUnsubscribe listener) {

    }
}
