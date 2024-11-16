package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Service.VehicleDataService;
import com.example.vehicle_shared.Entity.VehicleData;

@RestController
@RequestMapping("/vehicle")
public class VehicleDataController {
    
    private final VehicleDataService vehicleDataService;

    public VehicleDataController(VehicleDataService vehicleDataService) {
        this.vehicleDataService = vehicleDataService;
    }

    @PostMapping("/data")
    public ResponseEntity<VehicleData> createVehicleData(@RequestBody VehicleData vehicleData) {
        VehicleData createdData = vehicleDataService.createVehicleData(vehicleData);
        vehicleDataService.sendDataToKafka(createdData); // Send data to Kafka
        return new ResponseEntity<>(createdData, HttpStatus.CREATED);
    }

    @GetMapping("/{vehicleId}/data")
    public ResponseEntity<List<VehicleData>> getVehicleData(@PathVariable String vehicleId) {
        List<VehicleData> vehicleDataList = vehicleDataService.getVehicleData(vehicleId);
        if (!vehicleDataList.isEmpty()) {
            return new ResponseEntity<>(vehicleDataList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleData>> getAllVehicles() {
        List<VehicleData> allVehicles = vehicleDataService.getAllVehicles();
        if (!allVehicles.isEmpty()) {
            return new ResponseEntity<>(allVehicles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @DeleteMapping("/data/{id}")
    public ResponseEntity<Void> deleteVehicleData(@PathVariable Long id) {
        vehicleDataService.deleteVehicleData(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/batch/data")
    public ResponseEntity<Void> createBatchVehicleData(@RequestBody List<VehicleData> vehicleDataList) {
        vehicleDataService.createBatchVehicleData(vehicleDataList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
