#Author: Kat Mitchell (ksmitch3)

Feature: Prescription Black Box
    As an admin user
    I want to be able to add new prescriptions and diagnoses to the system
    So that HCP's can log these for their patients
    
Scenario Outline: Valid Prescription creation
	Given an admin has logged into the system
    When an admin attempts to create a new prescription with the NDC code <ndcCode> and name <name>
    Then the prescription is successfully created with the given information
    
Examples:
    | ndcCode     | name                     |
    |63323-459-09 | Heparin Sodium Injection |
    |0409-6509-01 | Vancomycin               |
    |0003-0857-22 | Happiness                |
    
Scenario Outline: Invalid Prescription creation
	Given an admin has logged into the system
    When an admin attempts to create a new prescription with the NDC code <ndcCode> and name <name>
    Then the prescription is not successfully created
    
Examples:
    | ndcCode     | name                     |
    |63323-459    | bad                      |
    |two oh one   | blue                     |
    |0003085722   | weed                     |
    
Scenario Outline: Valid Prescription edit
	Given an admin has logged into the system
	And the prescription with NDC code <ndcCode> and name <oldName> already exists
	When an admin attempts to edit to code's name to <newName>
	Then the prescription is successfully updated with the given information
	
Examples:
    | ndcCode     | oldName                  | newName                 |
    |63323-459-09 | Heparin Sodium Injection | Heparin Sodium Solution |
    |0409-6509-01 | Vancomycin               | Bug Spray               |
    |0003-0857-22 | Happiness                | Dose of Reality         |
    
Scenario Outline:   
	
