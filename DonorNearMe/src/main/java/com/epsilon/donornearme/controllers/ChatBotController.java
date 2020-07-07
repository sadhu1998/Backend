package com.epsilon.donornearme.controllers;

import com.epsilon.donornearme.operations.ChatBotOperator;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "Chatbot Control", description = "Talk to Bot")
public class ChatBotController extends BaseController {
    private static final Logger logger = LogManager.getLogger(ChatBotController.class);
    private final ChatBotOperator chatBotOperator = new ChatBotOperator();

    @RequestMapping(method = RequestMethod.POST, value = "/getbotreply")
    public String fetchMessage(@RequestBody String json) throws Exception {
        return chatBotOperator.sendMessageToBot(json);
    }
}
