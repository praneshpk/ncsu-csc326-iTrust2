#Author jshore and mrgray4
Feature: Document office visit with basic health metrics
	As an iTrust2 HCP
	I want to document an office visit with basic health metrics
	So that a record exits of a Patient visiting the doctor

Scenario Outline: Document an Office Visit with Basic Health Metrics for Infant
Given The required facilities exist
And A patient exists with name: <first> <last> and date of birth: <birthday>
When I log in to iTrust2 as a HCP
When I navigate to the Document Office Visit page
When I fill in information on the office visit for an infant with date: <date>, weight: <weight>, length: <length>, head circumference: <head>, household smoking status: <smoking>, and note: <note>
Then The office visit is documented successfully

Examples:
	| first    | last    | birthday   | date       | weight | length | head | smoking | note                                                                                      |
	| Brynn    | McClain | 05/01/2017 | 10/01/2017 | 16.5   | 22.3   | 16.1 | 1       | Brynn can start eating rice cereal mixed with breast milk or formula once a day.          |
	

#12yrs and over valid
Scenario Outline: Document an Office Visit with Basic Health Metrics for people 12 and over
Given The required facilities exist
And A patient exists with name: <first> <last> and date of birth: <birthday>
When I log in to iTrust2 as a HCP
When I navigate to the Document Office Visit page
When I fill in information on the office visit for people 12 and over with date: <date>, weight: <weight>, height: <height>, systolic blood pressure: <sys>, diastolic blood pressure: <dia>, household smoking status: <houseSmoking>, patient smoking status: <patientSmoking>, HDL cholesterol: <HDL>, LDL cholesterol: <LDL>, triglycerides: <triglycerides>, and note: <note>
Then The office visit is documented successfully

Examples:
	| first    | last    | birthday   | date       | weight | height | sys | dia | houseSmoking | patientSmoking | HDL | LDL | triglycerides | note                                                                            |
	| Daria    | Griffin | 10/25/1997 | 10/25/2017 | 124.3  | 62.3   | 110 | 175 | 1            | 3              | 65  | 102 | 147           | Patient is healthy                                                              |
	

#3yrs to 12yrs valid
Scenario Outline: Document an Office Visit with Basic Health Metrics for Child
Given The required facilities exist
And A patient exists with name: <first> <last> and date of birth: <birthday>
When I log in to iTrust2 as a HCP
When I navigate to the Document Office Visit page
When I fill in information on the office visit for patients of age 3 to 12 with date: <date>, weight: <weight>, height: <height>, systolic blood pressure: <sys>, diastolic blood pressure: <dia>, household smoking status: <houseSmoking>, and note: <note>
Then The office visit is documented successfully

Examples:
	| first    | last    | birthday   | date       | weight | height | sys | dia | houseSmoking | note                                                                            |
	| Fulton   | Gray    | 10/10/2012 | 10/13/2017 | 37.9   | 42.9   | 95  | 65  | 2            | Fulton has all required immunizations to start kindergarten next year.          |
	| Timothy  | Dorsett | 02/29/2012 | 11/16/2017 | 46.9   | 47.7   | 110 | 75  | 1            | Timothy is healthy.                                                             |

