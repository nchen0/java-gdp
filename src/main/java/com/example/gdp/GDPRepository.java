package com.example.gdp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GDPRepository extends JpaRepository<GDP, Long> {
    GDP findByCountry(String country);
}
