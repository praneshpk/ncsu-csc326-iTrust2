#Author: Kat Mitchell (ksmitch3)

Feature: Password Black Box
		As an user
		I want to be able to change my password
		So that I can keep my account secure
		
Scenario Outline: Admin changes password
		Given an admin exists in the system
		When the user changes their password to <password>
		Then their password will be changed
		
Examples:
		| password   |
		| 654321     |
		| mydogsname |
		| strawberry |

Scenario Outline: Patient changes password
		Given a patient exists in the system
		When the user changes their password to <password>
		Then their password will be changed
		
Examples:
		| password   |
		| bologna    |
		| csc326hell |
		| banana     |
		
Scenario Outline: HCP changes password
		Given a HCP exists in the system
		When the user changes their password to <password>
		Then their password will be changed
		
Examples:
		| password   |
		| cellphone  |
		| helloworld |
		| grapes     |
		
		
