package com.epsilon.donornearme.controllers;


import com.epsilon.donornearme.models.request.AddUserRequest;
import com.epsilon.donornearme.models.request.SendNotificationRequest;
import com.epsilon.donornearme.models.response.AddUserResponse;
import com.epsilon.donornearme.models.response.SendNotificationResponse;
import com.epsilon.donornearme.operations.NotificationOperator;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Notification Controller", description = "Notifications")
public class NotificationController {
    NotificationOperator notificationOperator = new NotificationOperator();

    @RequestMapping(method = RequestMethod.POST, value = "/sendnotification")
    public SendNotificationResponse addUser(@RequestBody SendNotificationRequest sendNotificationRequest) throws Exception {
        return notificationOperator.notifyUser(sendNotificationRequest);
    }

}
