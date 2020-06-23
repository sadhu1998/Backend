package com.donornearme.UserRegistery.usercontroller;

import com.donornearme.UserRegistery.model.request.AuthenticationRequest;
import com.donornearme.UserRegistery.model.request.WelcomeRequest;
import com.donornearme.UserRegistery.model.response.AuthenticationResponse;
import com.donornearme.UserRegistery.model.response.WelcomeResponse;
import com.donornearme.UserRegistery.usermethods.UserUtility;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(value = "Additional", description = "Some Additional API's")
public class AddtionalController extends BaseController {
    protected static final Logger logger = LogManager.getLogger(AddtionalController.class);
    private UserUtility userUtility = new UserUtility();

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return userUtility.authenticate(authenticationRequest);
    }

    @GetMapping("/")
    public String welcome(WelcomeRequest welcomeRequest) {
        WelcomeResponse welcomeResponse = new WelcomeResponse();
        return "Hello World";
    }
}
