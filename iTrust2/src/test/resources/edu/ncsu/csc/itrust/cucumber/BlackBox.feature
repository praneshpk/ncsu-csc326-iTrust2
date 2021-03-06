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
	And the prescription with NDC code <ndcCode> and name <name> already exists
	When an admin attempts to edit to code's name to <newName>
	Then the prescription is successfully updated with the given information
	
Examples:
    | ndcCode     | name                     | newName                 |
    |63323-459-09 | Heparin Sodium Injection | Heparin Sodium Solution |
    |0409-6509-01 | Vancomycin               | Bug Spray               |
    |0003-0857-22 | Happiness                | Dose of Reality         |
    
Scenario Outline: Valid Prescription assignment
	Given an HCP has logged into the system and a patient exists
	And the prescription with NDC code <ndcCode> and name <name> already exists
	When the HCP assigns a dosage of <dosage>, <renewals> renewals, a start date of <startDate> and end date of <endDate>
	Then the prescription is successfully prescribed

Examples:
    | ndcCode      | name                     | dosage | renewals | startDate  | endDate    |
    | 63323-459-29 | Sleep                    | 30     | 0        | 12/12/2017 | 12/12/2018 |
    | 0409-6509-22 | Tea                      | 20     | 5        | 05/06/2018 | 10/6/2018  |
    | 0003-0857-20 | Spoonful of Sugar        | 5      | 6        | 11/16/1995 | 11/16/2017 |             

Scenario Outline: Invalid Prescription assignment
	Given an HCP has logged into the system and a patient exists
	And the prescription with NDC code <ndcCode> and name <name> already exists
	When the HCP assigns a dosage of <dosage>, <renewals> renewals, a start date of <startDate> and end date of <endDate>
	Then the prescription is NOT successfully prescribed

Examples:
    | ndcCode      | name                     | dosage | renewals | startDate  | endDate    |
    | 63323-459-29 | Sleep                    | -3     | 0        | 12/12/2017 | 12/12/2018 |
    | 0409-6509-22 | Tea                      | 20     | -5       | 05/06/2018 | 10/6/2018  |
    | 0003-0857-20 | Spoonful of Sugar        | -5     | -5        | 11/16/19  | 11/16/2017 |
    
#Scenario: Patient View Prescriptions
#	Given a patient has logged into the system and has a prescription assigned to them
#	Then they are able to view their prescriptions 
