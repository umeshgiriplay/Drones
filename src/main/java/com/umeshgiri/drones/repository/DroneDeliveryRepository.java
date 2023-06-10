package com.umeshgiri.drones.repository;

import com.umeshgiri.drones.entity.DroneDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneDeliveryRepository extends JpaRepository<DroneDelivery, Long> {
}
