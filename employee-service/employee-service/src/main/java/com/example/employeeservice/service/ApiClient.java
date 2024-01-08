package com.example.employeeservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.employeeservice.dto.DepartmentDto;

//OPEN FEIGN LIBRARY WILL DYNAMICALLY CREATE IMPLEMENATION FOR THIS INTERFACE WE DON'T HAVE TO PROVIDE IMPLEMENTATION EXPLICITLY
//@FeignClient(url = "http://localhost:8080", name = "DEPARTMENT-SERVICE", value = "DEPARTMENT-SERVICE")
@FeignClient(name = "DEPARTMENT-SERVICE")
public interface ApiClient {
	
    @GetMapping("api/departments/{department-code}")
    DepartmentDto getDepartment(@PathVariable("department-code") String departmentCode);
}
