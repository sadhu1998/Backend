package com.epsilon.donornearme.models.request;

public class SendNotificationRequest {
    String notification_body;
    String notification_title;
    String priority;
    String id;
    String receiver_maild;

    public String getNotification_body() {
        return notification_body;
    }

    public void setNotification_body(String notification_body) {
        this.notification_body = notification_body;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiver_maild() {
        return receiver_maild;
    }

    public void setReceiver_maild(String receiver_maild) {
        this.receiver_maild = receiver_maild;
    }
}
