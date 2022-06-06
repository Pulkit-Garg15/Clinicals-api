package com.pulkit.clinicals.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulkit.clinicals.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient , Integer> {

}
