package com.example.organizationservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.organizationservice.dto.OrganizationDto;
import com.example.organizationservice.service.OrganizationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/organizations")
@AllArgsConstructor
public class OrganizationController {

	private OrganizationService organizationService;

	// Build Save Organization REST API
	@PostMapping
	public ResponseEntity<OrganizationDto> saveOrganization(@RequestBody OrganizationDto organizationDto) {
		OrganizationDto savedOrganization = organizationService.saveOrganization(organizationDto);
		return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
	}

	// Build Get Organization REST API
	@GetMapping("{code}")
	public ResponseEntity<OrganizationDto> getEmployee(@PathVariable("code") String organizationCode) {
		OrganizationDto apiResponseDto = organizationService.getOrganizationByCode(organizationCode);
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}
}
