package com.editors.viberbot;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;

import com.editors.viberbot.config.ViberBotListener;
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

public class ViberbotApplication {
	
	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(ViberbotApplication.class, args);
		
	}

}

