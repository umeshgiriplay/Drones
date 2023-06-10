package com.umeshgiri.drones.repository;

import com.umeshgiri.drones.entity.Payload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayloadRepository extends JpaRepository<Payload, Long> {
}
