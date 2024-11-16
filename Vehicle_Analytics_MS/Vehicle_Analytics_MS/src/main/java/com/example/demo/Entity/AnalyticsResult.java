package com.example.demo.Entity;

import java.io.Serializable;

public class AnalyticsResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private String vehicleId;
	private String status;
    private double averageSpeed;
    private double totalDistance;
    private double fuelConsumption;
    private double averageFuelLevel;
    public AnalyticsResult() {
        // Default constructor
    }

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getAverageFuelLevel() {
		return averageFuelLevel;
	}

	public void setAverageFuelLevel(double averageFuelLevel) {
		this.averageFuelLevel = averageFuelLevel;
	}

	public AnalyticsResult(String status, double averageSpeed, double averageFuelLevel) {
        this.status = status;
        this.averageSpeed = averageSpeed;
        this.averageFuelLevel = averageFuelLevel;
    }
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public double getAverageSpeed() {
		return averageSpeed;
	}
	public void setAverageSpeed(double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}
	public double getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}
	public double getFuelConsumption() {
		return fuelConsumption;
	}
	public void setFuelConsumption(double fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}
    
}
