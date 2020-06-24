package com.donornearme.UserRegistery.usercontroller;

import com.donornearme.UserRegistery.model.request.AuthenticationRequest;
import com.donornearme.UserRegistery.model.request.WelcomeRequest;
import com.donornearme.UserRegistery.model.response.AuthenticationResponse;
import com.donornearme.UserRegistery.model.response.WelcomeResponse;
import com.donornearme.UserRegistery.usermethods.UserUtility;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Additional", description = "Some Additional API's")
public class AddtionalController extends BaseController {
    protected static final Logger logger = LogManager.getLogger(AddtionalController.class);
    private UserUtility userUtility = new UserUtility();

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return userUtility.authenticate(authenticationRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public WelcomeResponse welcome(WelcomeRequest welcomeRequest) {
        WelcomeResponse welcomeResponse = new WelcomeResponse();
        welcomeResponse.setStatus("Entered");
        return welcomeResponse;
    }
}
