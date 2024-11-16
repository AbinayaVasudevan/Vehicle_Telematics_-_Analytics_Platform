package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.VehicleDataRepository;
import com.example.vehicle_shared.Entity.VehicleData;

@Service
public class VehicleDataService {
    
    private final VehicleDataRepository vehicleDataRepository;

    private final KafkaTemplate<String, VehicleData> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String kafkaTopic;

    public VehicleDataService(VehicleDataRepository vehicleDataRepository, KafkaTemplate<String, VehicleData> kafkaTemplate) {
        this.vehicleDataRepository = vehicleDataRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public VehicleData createVehicleData(VehicleData vehicleData) {
        return vehicleDataRepository.save(vehicleData);
    }

    public void sendDataToKafka(VehicleData vehicleData) {
        kafkaTemplate.send(kafkaTopic, vehicleData.getVehicleId(), vehicleData);
    }

    public List<VehicleData> getVehicleData(String vehicleId) {
        return vehicleDataRepository.findByVehicleId(vehicleId);
    }

    

    public void deleteVehicleData(Long id) {
        vehicleDataRepository.deleteById(id);
    }

    public void createBatchVehicleData(List<VehicleData> vehicleDataList) {
        vehicleDataRepository.saveAll(vehicleDataList);
    }

	public List<VehicleData> getAllVehicles() {
		return vehicleDataRepository.findAll();
	}
}
