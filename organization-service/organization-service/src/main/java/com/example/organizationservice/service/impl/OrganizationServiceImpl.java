package com.example.organizationservice.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.organizationservice.dto.OrganizationDto;
import com.example.organizationservice.entity.Organization;
import com.example.organizationservice.repository.OrganizationRepository;
import com.example.organizationservice.service.OrganizationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

	private OrganizationRepository organizationRepository;
	private ModelMapper modelMapper;
	
	@Override
	public OrganizationDto saveOrganization(OrganizationDto organizationDto) {
		Organization organization = modelMapper.map(organizationDto, Organization.class);
		Organization savedOrganization = organizationRepository.save(organization);
		return modelMapper.map(savedOrganization, OrganizationDto.class);
	}

	@Override
	public OrganizationDto getOrganizationByCode(String organizationCode) {
		Organization organization = organizationRepository.findByOrganizationCode(organizationCode);
		return modelMapper.map(organization, OrganizationDto.class);
	}

}
