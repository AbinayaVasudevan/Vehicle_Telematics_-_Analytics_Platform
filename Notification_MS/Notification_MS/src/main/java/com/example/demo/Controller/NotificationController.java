package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Notification;
import com.example.demo.Service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody Notification notification) {
        notificationService.subscribeToEvent(notification);
        return ResponseEntity.ok("Subscribed to notifications for vehicle event: " + notification.getEventType());
    }

    @GetMapping("/history/{vehicleId}")
    public ResponseEntity<List<Notification>> getNotificationHistory(@PathVariable String vehicleId) {
        List<Notification> history = notificationService.getNotificationHistory(vehicleId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody Notification notification) {
        notificationService.unsubscribeFromEvent(notification.getVehicleId(), notification.getEventType());
        return ResponseEntity.ok("Unsubscribed from notifications for event: " + notification.getEventType());
    }
}
