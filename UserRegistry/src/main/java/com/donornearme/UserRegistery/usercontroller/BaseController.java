package com.donornearme.UserRegistery.usercontroller;

import com.donornearme.UserRegistery.extensionmanager.DBConnectionManager;
import com.donornearme.UserRegistery.extensionmanager.SQLManager;
import com.donornearme.UserRegistery.extensionmanager.StringTemplateManager;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseController {

    protected ObjectMapper mapper = new ObjectMapper();
    protected StringTemplateManager STRenderer = new StringTemplateManager();
    protected DBConnectionManager dbConnection = new DBConnectionManager();
    protected SQLManager sqlManager = new SQLManager();

}
