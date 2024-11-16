package com.example.demo.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.AnalyticsResult;
import com.example.demo.Service.VehicleAnalyticsService;

@RestController
@RequestMapping("/analytics")
public class VehicleAnalyticsController {
	private final VehicleAnalyticsService vehicleAnalyticsService;

    public VehicleAnalyticsController(VehicleAnalyticsService vehicleAnalyticsService) {
        this.vehicleAnalyticsService = vehicleAnalyticsService;
    }

    @GetMapping("/summary/{vehicleId}")
    public ResponseEntity<AnalyticsResult> getVehicleSummary(@PathVariable String vehicleId) {
        return new ResponseEntity<>(vehicleAnalyticsService.getVehicleSummary(vehicleId), HttpStatus.OK);
    }
    @GetMapping("/fleet/summary")
    public ResponseEntity<String> getFleetSummary() {
        return new ResponseEntity<>(vehicleAnalyticsService.getFleetSummary(), HttpStatus.OK);
    }

    @GetMapping("/top-speed")
    public ResponseEntity<String> getTopSpeedVehicles(@RequestParam int limit) {
        return new ResponseEntity<>(vehicleAnalyticsService.getTopSpeedVehicles(limit), HttpStatus.OK);
    }

    @GetMapping("/fuel-consumption/{vehicleId}")
    public ResponseEntity<String> getFuelConsumption(@PathVariable String vehicleId) {
        return new ResponseEntity<>(vehicleAnalyticsService.getFuelConsumption(vehicleId), HttpStatus.OK);
    }
}
