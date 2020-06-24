package com.donornearme.UserRegistery.usercontroller;

import com.donornearme.UserRegistery.usermethods.UserUtility;
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
    private static final Logger logger = LogManager.getLogger(AreaController.class);
    private final UserUtility userUtility = new UserUtility();

    @RequestMapping(method = RequestMethod.POST, value = "/getbotreply")
    public String fetchMessage(@RequestBody String json) throws Exception {
        return userUtility.sendMessageToBot(json);
    }
}
