package com.editors.viberbot;


import java.util.*;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.editors.viberbot.service.ViberBotService;
import com.google.common.util.concurrent.Futures;
import com.viber.bot.ViberSignatureValidator;
import com.viber.bot.api.ViberBot;
import com.viber.bot.message.MessageKeyboard;
import com.viber.bot.message.TextMessage;
import com.viber.bot.profile.BotProfile;
import com.viber.bot.profile.UserProfile;

@SpringBootApplication

public class ViberbotApplication implements ApplicationListener<ApplicationReadyEvent>  {
	
	@Autowired
	private ViberBotService viberBotService;
	
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
				viberBotService.onMessageReceived(event, message, response);
	        });

	        //bot.onConversationStarted(event -> Futures.immediateFuture(Optional.of( // send 'Hi UserName' when conversation is started
	          //      new TextMessage("Hi"))));
		 	bot.onConversationStarted(event -> viberBotService.onConversationStarted(event));
	    }


	
	
	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(ViberbotApplication.class, args);

	}

}

