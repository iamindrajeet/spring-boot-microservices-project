package com.example.employeeservice.service.impl;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.employeeservice.dto.ApiResponseDto;
import com.example.employeeservice.dto.DepartmentDto;
import com.example.employeeservice.dto.EmployeeDto;
import com.example.employeeservice.dto.OrganizationDto;
import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.exception.ResourceNotFoundException;
import com.example.employeeservice.repository.EmployeeRepository;
import com.example.employeeservice.service.ApiClient;
import com.example.employeeservice.service.EmployeeService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	private final EmployeeRepository employeeRepository;
	private final ModelMapper modelMapper;
	//private RestTemplate restTemplate;
    private WebClient webClient;
     private ApiClient apiClient;

	@Override
	public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
		Employee employee = modelMapper.map(employeeDto, Employee.class);
		Employee savedEmployee = employeeRepository.save(employee);
		EmployeeDto savedEmployeeDto = modelMapper.map(savedEmployee, EmployeeDto.class);
		return savedEmployeeDto;
	}

	@Override
	//@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
	@Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
	public ApiResponseDto getEmployeeById(Long employeeId) {
		
		LOGGER.info("inside getEmployeeById() method");
		
		Optional<Employee> employee = employeeRepository.findById(employeeId);
		if (employee.isEmpty())
			throw new ResourceNotFoundException("Employee", "employee-id", employeeId);
		EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

		// Calling department service using rest template
		//ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/" + employee.get().getDepartmentCode(), DepartmentDto.class);
		// DepartmentDto departmentDto = responseEntity.getBody();
		
		//calling department service using webclient
//		DepartmentDto departmentDto = webClient.get()
//		.uri("http://localhost:8080/api/departments/" + employee.get().getDepartmentCode())
//		.retrieve()
//		.bodyToMono(DepartmentDto.class)
//		.block();
		
		//calling using feign-client library
		DepartmentDto departmentDto = apiClient.getDepartment(employee.get().getDepartmentCode());
		
		//calling organization service using webclient
		OrganizationDto organizationDto = webClient.get()
		.uri("http://localhost:8083/api/organizations/" + employee.get().getOrganizationCode())
		.retrieve()
		.bodyToMono(OrganizationDto.class)
		.block();
		
		ApiResponseDto apiResponseDto = new ApiResponseDto();
		apiResponseDto.setEmployee(employeeDto);
		apiResponseDto.setDepartment(departmentDto);
		apiResponseDto.setOrganization(organizationDto);
		return apiResponseDto;
	}

//    //@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
//    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
//    @Override
//    public APIResponseDto getEmployeeById(Long employeeId) {
//
//        LOGGER.info("inside getEmployeeById() method");
//        Employee employee = employeeRepository.findById(employeeId).get();
//
////        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://DEPARTMENT-SERVICE/api/departments/" + employee.getDepartmentCode(),
////                DepartmentDto.class);
////
////        DepartmentDto departmentDto = responseEntity.getBody();
//
//        DepartmentDto departmentDto = webClient.get()
//                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
//                .retrieve()
//                .bodyToMono(DepartmentDto.class)
//                .block();
//
//      //  DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());
//
//        OrganizationDto organizationDto = webClient.get()
//                .uri("http://localhost:8083/api/organizations/" + employee.getOrganizationCode())
//                .retrieve()
//                .bodyToMono(OrganizationDto.class)
//                .block();
//
//        EmployeeDto employeeDto = EmployeeMapper.mapToEmployeeDto(employee);
//
//        APIResponseDto apiResponseDto = new APIResponseDto();
//        apiResponseDto.setEmployee(employeeDto);
//        apiResponseDto.setDepartment(departmentDto);
//        apiResponseDto.setOrganization(organizationDto);
//        return apiResponseDto;
//    }
//
    public ApiResponseDto getDefaultDepartment(Long employeeId, Exception exception) {

        LOGGER.info("inside getDefaultDepartment() method");

        Employee employee = employeeRepository.findById(employeeId).get();

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(1L);
        departmentDto.setDepartmentName("R&D Department");
        departmentDto.setDepartmentCode("RD001");
        departmentDto.setDepartmentDescription("Research and Development Department");

        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        ApiResponseDto apiResponseDto = new ApiResponseDto();
		apiResponseDto.setEmployee(employeeDto);
		apiResponseDto.setDepartment(departmentDto);
		return apiResponseDto;
    }
}