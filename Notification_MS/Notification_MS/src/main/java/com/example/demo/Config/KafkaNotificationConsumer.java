package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.Service.NotificationService;

@Service
public class KafkaNotificationConsumer {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "vehicle_data_topic", groupId = "notification-group")
    public void consumeVehicleData(String message) {
        // Parse the message to extract the event details
        String[] data = message.split(",");
        String vehicleId = data[0];
        String eventType = data[1];
        String eventDetails = data[2];

        // Trigger notification logic based on the event
        if (eventType.equals("OVER_SPEED")) {
            String notificationMessage = "Over-speeding detected for Vehicle ID: " + vehicleId;
            notificationService.saveNotification(vehicleId, eventType, notificationMessage);
        }
        // Handle other events similarly
    }
}
