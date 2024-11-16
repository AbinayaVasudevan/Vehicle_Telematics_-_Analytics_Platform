package com.example.demo.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Notification;
import com.example.demo.Repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public Notification subscribeToEvent(Notification notification) {
        return repository.save(notification);
    }

    public List<Notification> getNotificationHistory(String vehicleId) {
        return repository.findByVehicleId(vehicleId);
    }

    public void unsubscribeFromEvent(String vehicleId, String eventType) {
        List<Notification> notifications = repository.findByVehicleId(vehicleId);
        notifications.stream()
                .filter(n -> n.getEventType().equals(eventType))
                .forEach(repository::delete);
    }

    public void saveNotification(String vehicleId, String eventType, String message) {
        Notification notification = new Notification();
        notification.setVehicleId(vehicleId);
        notification.setEventType(eventType);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        repository.save(notification);
    }
    @KafkaListener(topics = "vehicle_notifications_topic", groupId = "notification_group")
    public void listenToNotifications(String message) {
        System.out.println("Received notification message: " + message);
        
        // Trigger actual notification to the user (email, SMS, etc.)
        sendNotification(message);
    }

    private void sendNotification(String message) {
        // Logic to send notification (e.g., email or SMS) based on the message
        System.out.println("Notification sent to user: " + message);
    }
}
