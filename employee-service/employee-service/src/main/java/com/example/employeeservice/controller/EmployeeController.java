package com.example.employeeservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeeservice.dto.ApiResponseDto;
import com.example.employeeservice.dto.EmployeeDto;
import com.example.employeeservice.service.EmployeeService;

@RestController
@RequestMapping("api/employees/")
@AllArgsConstructor
public class EmployeeController {

	private EmployeeService employeeService;

	// Build Save Employee REST API
	@PostMapping
	public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
		EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto);
		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}

	// Build Get Employee REST API
	@GetMapping("{id}")
	public ResponseEntity<ApiResponseDto> getEmployee(@PathVariable("id") Long employeeId) {
		ApiResponseDto apiResponseDto = employeeService.getEmployeeById(employeeId);
		return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
	}
}