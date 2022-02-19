package com.fullstack;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TestController {

	@Autowired		
	private TempProcessor processor;	
	
	private List<Employee> employees = createList();

	@RequestMapping(value = "/employees", method = RequestMethod.GET, produces = "application/json")
	public List<Employee> firstPage() {
		return employees;
	}

	@RequestMapping(value = "/cityData", method = RequestMethod.GET, produces = "application/json")
	public EnergyData energyData(@RequestParam("longitude") double longitude,
            @RequestParam("latitude") double latitude, @RequestParam("date") String date) throws Exception {
		
		System.out.println("Request Params are : lng " + longitude + " lat " + latitude + " Date " + date);
		EnergyData engData = new EnergyData();
//		Processor processor = new Processor();
//		TempProcessor processor = new TempProcessor();
		processor.process(longitude, latitude, date, engData);
		return engData;
	}

	
	private static List<Employee> createList() {
		List<Employee> tempEmployees = new ArrayList<Employee>();
		Employee emp1 = new Employee();
		emp1.setName("emp1");
		emp1.setDesignation("manager");
		emp1.setEmpId("1");
		emp1.setSalary(3000);

		Employee emp2 = new Employee();
		emp2.setName("emp2");
		emp2.setDesignation("developer");
		emp2.setEmpId("2");
		emp2.setSalary(3000);
		tempEmployees.add(emp1);
		tempEmployees.add(emp2);
		return tempEmployees;
	}

}
