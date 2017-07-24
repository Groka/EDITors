package com.editors.viberbot;


import java.util.*;

import javax.inject.Inject;

import com.editors.viberbot.service.ViberBotService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viber.bot.message.MessageKeyboard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.Futures;
import com.viber.bot.ViberSignatureValidator;
import com.viber.bot.api.ViberBot;
import com.viber.bot.message.TextMessage;
import com.viber.bot.profile.BotProfile;
import com.viber.bot.profile.UserProfile;

@SpringBootApplication

public class ViberbotApplication implements ApplicationListener<ApplicationReadyEvent>  {

	ViberBotService viberBotService;
	
	 @Inject
	 private ViberBot bot;

	 @Inject
	 private ViberSignatureValidator signatureValidator;

	 @Value("${application.viber-bot.webhook-url}")
	 private String webhookUrl;

	 @Override
	    public void onApplicationEvent(ApplicationReadyEvent appReadyEvent) {
	        try {
	            bot.setWebhook(webhookUrl).get();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       

	        bot.onMessageReceived((event, message, response) ->{
				String text = "Testing keyboard";

				Map<String, Object> messageKeyboard = new HashMap<>();
				messageKeyboard.put("Type", "keyboard");
				messageKeyboard.put("DefaultHight", true);

				ArrayList<HashMap<String, Object>> buttons = new ArrayList<>();
				HashMap<String, Object> button1 = new HashMap<>();
				button1.put("ActionType", "reply");
				button1.put("ActionBody", "reply to PA");
				button1.put("Text", "Test");

				buttons.add(button1);

				messageKeyboard.put("Buttons", buttons);

				MessageKeyboard messageKeyboard2 = new MessageKeyboard(messageKeyboard);

				TextMessage textMessage = new TextMessage(text, messageKeyboard2, null, null);
	        	System.out.println("Nesto se desavaaaaaaaaaaaaa");
	        	response.send(textMessage);
	        });

	        //bot.onConversationStarted(event -> Futures.immediateFuture(Optional.of( // send 'Hi UserName' when conversation is started
	          //      new TextMessage("Hi"))));
		 	bot.onConversationStarted(event -> viberBotService.onConversationStarted(event));
	    }


	
	
	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(ViberbotApplication.class, args);

	}

}

