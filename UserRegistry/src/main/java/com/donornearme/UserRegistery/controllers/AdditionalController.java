package com.donornearme.UserRegistery.controllers;

import com.donornearme.UserRegistery.models.request.AuthenticationRequest;
import com.donornearme.UserRegistery.models.request.WelcomeRequest;
import com.donornearme.UserRegistery.models.response.AuthenticationResponse;
import com.donornearme.UserRegistery.models.response.WelcomeResponse;
import com.donornearme.UserRegistery.operations.AdditionalOperator;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Additional", description = "Some Additional API's")
public class AdditionalController extends BaseController {
    protected static final Logger logger = LogManager.getLogger(AdditionalController.class);
    private final AdditionalOperator additionalOperator = new AdditionalOperator();

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return additionalOperator.authenticate(authenticationRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public WelcomeResponse welcome(WelcomeRequest welcomeRequest) {
        WelcomeResponse welcomeResponse = new WelcomeResponse();
        welcomeResponse.setStatus("Entered");
        return welcomeResponse;
    }
}
