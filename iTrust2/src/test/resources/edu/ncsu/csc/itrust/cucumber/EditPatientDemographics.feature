#Author jtwall3

Feature: Edit Patient Demographics
	As an HCP in iTrust2
	I want to be able to edit patient's demographics
	
Scenario Outline: Edit Patient Demographics
	Given All the Facilities exist
	Given The user really is logged in to iTrust2 as HCP
	Given HCP really has navigated to the Document Office Visit page
	When The HCP modifies the <demNumber> demographics to <newValue> in the office visit
	And The HCP completes the office visit
	Then Office visit really has been documented successfully
	
Examples:
	| demNumber | newValue |
	| 1 | karlfromwayback@gmail.com |
	| 2 | Karlfromwayback |
	| 3 | 03/04/1934 |