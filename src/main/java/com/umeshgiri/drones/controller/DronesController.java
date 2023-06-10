package com.umeshgiri.drones.controller;

import com.umeshgiri.drones.entity.Drone;
import com.umeshgiri.drones.entity.Payload;
import com.umeshgiri.drones.enums.DroneState;
import com.umeshgiri.drones.payload.CommonResponse;
import com.umeshgiri.drones.payload.CommonResponseStatus;
import com.umeshgiri.drones.service.DronesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/drone")
public class DronesController {

    @Autowired
    private DronesService dronesService;

    @PostMapping
    public CommonResponse save(@RequestBody @Valid Drone drone) {
        drone.setState(DroneState.IDLE);
        return CommonResponse.success(dronesService.save(drone));
    }

    @PostMapping("load/{drone_id}")
    public CommonResponse loadPayload(@Valid Payload payload, @PathVariable("drone_id") Long droneId) {
        return CommonResponse.builder().status(CommonResponseStatus.SUCCESS).message("Payload loaded to drone successfully").object(dronesService.processLoadRequest(droneId, payload)).build();
    }

    @GetMapping("payloads/{id}")
    public CommonResponse loadedPayloads(@PathVariable("id") Long droneId) {
        return CommonResponse.builder().status(CommonResponseStatus.SUCCESS).message("Success").objects(dronesService.getLoadedPayloadsPendingDelivery(droneId)).build();
    }

    @GetMapping("available")
    public CommonResponse availableDrones() {
        return CommonResponse.builder().status(CommonResponseStatus.SUCCESS).message("Success").objects(dronesService.getAvailableDrones()).build();
    }

    @GetMapping("battery/{id}")
    public CommonResponse battery(@PathVariable("id") Long droneId) {
        return CommonResponse.success(dronesService.getBattery(droneId));
    }
}
