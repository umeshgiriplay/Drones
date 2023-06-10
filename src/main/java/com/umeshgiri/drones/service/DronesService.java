package com.umeshgiri.drones.service;

import com.umeshgiri.drones.entity.Drone;
import com.umeshgiri.drones.entity.DroneBatteryAudit;
import com.umeshgiri.drones.entity.DroneDelivery;
import com.umeshgiri.drones.entity.Payload;
import com.umeshgiri.drones.enums.DocumentType;
import com.umeshgiri.drones.enums.DroneState;
import com.umeshgiri.drones.repository.DroneBatteryAuditRepository;
import com.umeshgiri.drones.repository.DroneDeliveryRepository;
import com.umeshgiri.drones.repository.DroneRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DronesService {

    private final DroneRepository droneRepository;
    private final DroneBatteryAuditRepository droneBatteryAuditRepository;
    private final DroneDeliveryRepository droneDeliveryRepository;
    private final DocumentService documentService;

    public DronesService(DroneRepository droneRepository, DroneBatteryAuditRepository droneBatteryAuditRepository, DroneDeliveryRepository droneDeliveryRepository, DocumentService documentService) {
        this.droneRepository = droneRepository;
        this.droneBatteryAuditRepository = droneBatteryAuditRepository;
        this.droneDeliveryRepository = droneDeliveryRepository;
        this.documentService = documentService;
    }

    public Drone save(Drone drone) {
        return droneRepository.save(drone);
    }

    public DroneDelivery processLoadRequest(Long droneId, Payload payload) {
        final Drone drone = getDrone(droneId);

        if (payload.getUploadImage() != null && !payload.getUploadImage().isEmpty()) {
            payload.setImage(documentService.save(payload.getUploadImage(), DocumentType.MEDICATION));
            payload.setUploadImage(null);
        }

        return loadPayload(drone, payload);
    }

    private Drone getDrone(Long droneId) {
        return droneRepository.findById(droneId).orElseThrow(() -> new IllegalArgumentException("Drone with ID:" + droneId + " not found"));
    }

    private DroneDelivery loadPayload(Drone drone, Payload payload) {
        drone.throwExceptionIsCannotBeLoaded(payload.getWeight());
        drone.setState(DroneState.LOADING);
        drone = droneRepository.save(drone);
        final DroneDelivery droneDelivery = drone.getOrCreatePendingDelivery();
        payload.setDelivery(droneDelivery);
        droneDelivery.getPayloads().add(payload);
        return droneDeliveryRepository.save(droneDelivery);
    }

    public List<Payload> getLoadedPayloadsPendingDelivery(Long droneId) {
        Drone drone = getDrone(droneId);
        return drone.getOrCreatePendingDelivery().getPayloads().stream().toList();
    }

    public List<Drone> getAvailableDrones() {
        return droneRepository.findAll().stream().filter(Drone::canBeLoaded).toList();
    }

    public Double getBattery(Long droneId) {
        return getDrone(droneId).getBatteryCapacity();
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void auditDroneBattery() {
        droneRepository.findAll().forEach(drone -> {
            DroneBatteryAudit droneBatteryAudit = new DroneBatteryAudit();
            droneBatteryAudit.setDrone(drone);
            droneBatteryAudit.setBatteryCapacity(drone.getBatteryCapacity());
            droneBatteryAuditRepository.save(droneBatteryAudit);
        });
    }
}
