package com.example.departmentservice.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.departmentservice.dto.DepartmentDto;
import com.example.departmentservice.entity.Department;
import com.example.departmentservice.exception.ResourceNotFoundException;
import com.example.departmentservice.repository.DepartmentRepository;
import com.example.departmentservice.service.DepartmentService;

import lombok.AllArgsConstructor;

//@AllArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public DepartmentDto saveDepartment(DepartmentDto departmentDto) {
		// convert department dto to department jpa entity
		Department department = modelMapper.map(departmentDto, Department.class);
		Department savedDepartment = departmentRepository.save(department);
		DepartmentDto savedDepartmentDto = modelMapper.map(savedDepartment, DepartmentDto.class);
		return savedDepartmentDto;
	}

	@Override
	public DepartmentDto getDepartmentByCode(String departmentCode) {
		Department department = departmentRepository.findByDepartmentCode(departmentCode);
		if(department == null) {
			throw new ResourceNotFoundException("Department", "department-code", departmentCode);
		}
		DepartmentDto departmentDto = modelMapper.map(department, DepartmentDto.class);
		return departmentDto;
	}

}
