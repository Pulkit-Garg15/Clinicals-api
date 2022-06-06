package com.pulkit.clinicals.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pulkit.clinicals.dto.ClinicalDataRequest;
import com.pulkit.clinicals.entities.ClinicalData;
import com.pulkit.clinicals.entities.Patient;
import com.pulkit.clinicals.repos.ClinicalDataRepository;
import com.pulkit.clinicals.repos.PatientRepository;
import com.pulkit.clinicals.util.BMICalculator;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ClinicalDataController {

	private ClinicalDataRepository clinicalDataRepos;
	private PatientRepository patientRepos;

	ClinicalDataController(ClinicalDataRepository clinicalRepos, PatientRepository patientRepos) {
		super();
		this.clinicalDataRepos = clinicalRepos;
		this.patientRepos = patientRepos;
	}

	@RequestMapping(value = "/clinicals", method = RequestMethod.POST)
	public ClinicalData saveClinicalData(@RequestBody ClinicalDataRequest request) {
		Patient patient = patientRepos.findById(request.getPatientId()).get();
		ClinicalData clinicalData = new ClinicalData();
		clinicalData.setComponentName(request.getComponentName());
		clinicalData.setComponentValue(request.getComponentValue());
		clinicalData.setPatient(patient);
		return clinicalDataRepos.save(clinicalData);
	}

	@RequestMapping(value = "/clinicals/{patientId}/{componentName}", method = RequestMethod.GET)
	public List<ClinicalData> getClinicalData(@PathVariable("patientId") int patientId,
			@PathVariable("componentName") String componentName) {

		if (componentName.equals("bmi")) {
			componentName = "hw";
		}

		List<ClinicalData> clinicalData = clinicalDataRepos
				.findByPatientIdAndComponentNameOrderByMeasuredDateTime(patientId, componentName);
		List<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);

		for (ClinicalData eachEntry : duplicateClinicalData) {
			BMICalculator.calculateBMI(clinicalData, eachEntry);
		}

		return clinicalData;

	}

}
