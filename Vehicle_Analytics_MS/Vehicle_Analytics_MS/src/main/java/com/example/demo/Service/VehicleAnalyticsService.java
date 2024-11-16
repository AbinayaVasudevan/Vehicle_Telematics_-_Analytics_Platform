package com.example.demo.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.AnalyticsResult;
import com.example.vehicle_shared.Entity.VehicleData;
@Service
public class VehicleAnalyticsService {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final RedisTemplate<String, VehicleData> redisTemplate;

    // Cache expiration in seconds
    private static final long CACHE_EXPIRATION_SECONDS = 600;

    public VehicleAnalyticsService(KafkaTemplate<String, Object> kafkaTemplate, 
                                   RedisTemplate<String, VehicleData> redisTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
    }
	@KafkaListener(topics = "${kafka.topic.name}", groupId = "vehicle_analytics_group")
	public void listenVehicleData(VehicleData vehicleData) {
		System.out.println("Received VehicleData: " + vehicleData);
	    consumeVehicleData(vehicleData);  // Store vehicle data for analytics
	}
	public void consumeVehicleData(VehicleData vehicleData) {
        // Save data to Redis cache
		String key = "vehicle:" + vehicleData.getVehicleId();
	    redisTemplate.opsForValue().set(key, vehicleData); // Cache vehicle data in Redis
	    System.out.println("VehicleData cached in Redis for vehicle ID: " + vehicleData.getVehicleId());
	    
	    AnalyticsResult analyticsResult = calculateAnalytics(vehicleData);
	    if ("Over Speeding".equals(analyticsResult.getStatus())) {
	        sendOverSpeedingNotification(vehicleData);
	    }
    }

    private void cacheVehicleData(String vehicleId, VehicleData vehicleData) {
        // Cache vehicle data in Redis with expiration time
        redisTemplate.opsForValue().set(vehicleId, vehicleData, CACHE_EXPIRATION_SECONDS);
        System.out.println("Cached VehicleData for ID: " + vehicleId);
    }

    private VehicleData getVehicleDataFromCache(String vehicleId) {
        return redisTemplate.opsForValue().get(vehicleId); // Retrieve from Redis cache
    }
	private void sendOverSpeedingNotification(VehicleData vehicleData) {
        String message = String.format("Vehicle ID: %s is over-speeding at %.2f km/h", 
                                        vehicleData.getVehicleId(), vehicleData.getSpeed());
        kafkaTemplate.send("vehicle_notifications_topic", message); // Kafka topic for notifications
        System.out.println("Over-speeding notification sent: " + message);
    }
	private Map<String, VehicleData> vehicleDataMap = new HashMap<>(); // Cache of incoming data
	
	@Cacheable(value = "vehicleData", key = "#vehicleId")
    public AnalyticsResult getVehicleSummary(String vehicleId) {
    	VehicleData data = redisTemplate.opsForValue().get("vehicle:" + vehicleId);
        return calculateAnalytics(data);
    }
    private AnalyticsResult calculateAnalytics(VehicleData data) {
        if (data == null) {
            return new AnalyticsResult("No data available", 0.0, 0.0); // Handle case with no data
        }

        // Example calculations
        double averageSpeed = data.getSpeed(); // Assuming analyzing a single data point
        double totalDistance = averageSpeed * 1; // For example, if you want to assume 1 hour of travel
        double averageFuelLevel = data.getFuelLevel(); // Similarly, can be adjusted

        // Example status logic
        String status = (averageSpeed > 80) ? "Over Speeding" : "Normal Speed";

        // Create an AnalyticsResult object with calculated data
        AnalyticsResult result = new AnalyticsResult();
        result.setVehicleId(data.getVehicleId());
        result.setAverageSpeed(averageSpeed);
        result.setTotalDistance(totalDistance);
        result.setFuelConsumption((totalDistance / averageFuelLevel) * 100); // Example calculation
        result.setAverageFuelLevel(averageFuelLevel);
        result.setStatus(status);
        
        return result;
    }
    public String getFleetSummary() {
        if (vehicleDataMap.isEmpty()) {
            return "No vehicle data available.";
        }

        int totalVehicles = vehicleDataMap.size();
        double totalDistance = vehicleDataMap.values().stream()
                .mapToDouble(v -> v.getSpeed()) // Assuming speed as distance for simplification
                .sum();
        double averageSpeed = totalDistance / totalVehicles;

        return String.format("Fleet Summary: Total Vehicles: %d, Total Distance: %.2f, Average Speed: %.2f", totalVehicles, totalDistance, averageSpeed);
    }
    public String getTopSpeedVehicles(int limit) {
        List<VehicleData> topSpeedVehicles = vehicleDataMap.values().stream()
                .sorted(Comparator.comparingDouble(VehicleData::getSpeed).reversed())
                .limit(limit)
                .collect(Collectors.toList());

        StringBuilder response = new StringBuilder("Top Speed Vehicles:\n");
        for (VehicleData vehicle : topSpeedVehicles) {
            response.append(String.format("Vehicle ID: %s, Speed: %.2f\n", vehicle.getVehicleId(), vehicle.getSpeed()));
        }

        return response.toString();
    }
    public String getFuelConsumption(String vehicleId) {
        VehicleData data = vehicleDataMap.get(vehicleId);
        if (data == null) {
            return "No data available for vehicle: " + vehicleId;
        }

        double totalDistance = data.getSpeed(); // Assuming speed as distance
        double fuelConsumption = (totalDistance / data.getFuelLevel()) * 100; // Simplified calculation

        return String.format("Fuel consumption for vehicle %s: %.2f liters per 100 km", vehicleId, fuelConsumption);
    }


}
