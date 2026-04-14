package com.zidio.nexushr.employee.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	private String department;

	private String designation;

	private Double salary;

	@Column(name = "joining_date")
	private LocalDate joiningDate;

	@Enumerated(EnumType.STRING)
	private EmployeeStatus status;

	@Column(name = "created_at", updatable = false)
	private LocalDate createdAt;
	
	private Role role;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}


	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDate.now();
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}