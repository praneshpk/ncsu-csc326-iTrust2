#Author jtwall3

Feature: Diagnosis
	As an iTrust2 User
	I want to be able to interact with diagnosis
	So that they can be recorded for patients

	
Scenario Outline: Give Diagnosis
	Given Facilities exist
	Given The diagnosis exists with the name <name> and the code <code>
	Given The user is logged in to iTrust2 as HCP
	Given HCP navigated to the Document Office Visit page
	Given HCP filled in information on the office visit
	When The HCP selects the diagnosis with name <name> and code <code>
	Then Office visit is documented successfully
	
Examples:
	| name | code |
	| Hit face with phone while laying on bed | F43 |
	| Didn't brush teeth for 17 years | T65 |
	
Scenario: Don't Give Diagnosis
	Given Facilities exist
	Given The user is logged in to iTrust2 as HCP
	Given HCP navigated to the Document Office Visit page
	Given HCP filled in information on the office visit
	When The HCP submits the office visit
	Then Office visit is documented successfully

Scenario Outline: View Diagnosis
	Given Facilities exist
	Given The diagnosis exists with the name <name> and the code <code>
	Given An office visit has been created with a diagnosis of <name>
	Given The user is logged in to iTrust2 as patient
	When The patient navigated to the View Office Visits page
	Then The patient can successfully see their diagnosis of <name>
	
Examples:
	| name | code |
	| Microwaved Cup of Noodles when told not to on the package | F01 |
	| Left handed person used an appliance intended for a right handed person | R43 |