package com.umeshgiri.drones.entity;

import com.umeshgiri.drones.enums.DroneState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DroneTest {


    @Test
    @DisplayName("Should return an existing pending delivery if it exists")
    void getOrCreatePendingDeliveryReturnsExistingPendingDelivery() {
        Drone drone = new Drone();
        DroneDelivery existingDelivery = new DroneDelivery();
        existingDelivery.setDrone(drone);
        existingDelivery.setDeliveryTime(null);
        drone.getDeliveries().add(existingDelivery);

        DroneDelivery result = drone.getOrCreatePendingDelivery();

        assertEquals(existingDelivery, result);
    }

    @Test
    @DisplayName("Should throw an exception when the drone battery capacity is below 25%")
    void throwExceptionIsCannotBeLoadedWhenBatteryCapacityIsBelow25() {
        Drone drone = new Drone();
        drone.setBatteryCapacity(20.0);
        drone.setState(DroneState.IDLE);

        assertThrows(IllegalArgumentException.class, () -> drone.throwExceptionIsCannotBeLoaded(10.0));
    }

    @Test
    @DisplayName("Should throw an exception when the drone state is not IDLE or LOADING")
    void throwExceptionIsCannotBeLoadedWhenStateIsNotIdleOrLoading() {
        Drone drone = new Drone();
        drone.setState(DroneState.DELIVERING);

        assertThrows(IllegalArgumentException.class, () -> drone.throwExceptionIsCannotBeLoaded(10.0));
    }

    @Test
    @DisplayName("Should throw an exception when the drone's loaded weight plus additional weight is greater than the weight limit")
    void throwExceptionIsCannotBeLoadedWhenLoadedWeightPlusAdditionalWeightIsGreaterThanWeightLimit() {// Create a new Drone object
        Drone drone = new Drone();
        drone.setWeightLimit(1000.0);
        drone.setBatteryCapacity(50.0);
        drone.setState(DroneState.IDLE);

        // Create a new DroneDelivery object
        DroneDelivery droneDelivery = new DroneDelivery();
        droneDelivery.setDrone(drone);

        // Create a new Payload object
        Payload payload = new Payload();
        payload.setWeight(500.0);

        // Add the payload to the drone delivery
        droneDelivery.getPayloads().add(payload);

        // Add the drone delivery to the drone
        drone.getDeliveries().add(droneDelivery);

        // Call the throwExceptionIsCannotBeLoaded method with an additional weight that exceeds the weight limit
        assertThrows(IllegalArgumentException.class, () -> drone.throwExceptionIsCannotBeLoaded(600.0));
    }
}