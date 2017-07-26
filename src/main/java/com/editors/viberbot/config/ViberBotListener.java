package com.editors.viberbot.config;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import com.editors.viberbot.service.ViberBotService;
import com.viber.bot.ViberSignatureValidator;
import com.viber.bot.api.ViberBot;

@Configuration
public class ViberBotListener implements ApplicationListener<ApplicationReadyEvent> {
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

		bot.onMessageReceived((event, message, response) ->
				viberBotService.onMessageReceived(event, message, response));

		bot.onConversationStarted(event -> viberBotService.onConversationStarted(event));
		
		//bot.onSubscribe((event, response) -> viberBotService.onSubscribe(event, response));
		
		bot.onUnsubscribe((event) -> viberBotService.onUnsubscribe(event));
	}


}
