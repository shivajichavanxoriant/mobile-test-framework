Feature: Part 1 - Form Validation
 
   Scenario: Submit the form with valid user credentials
    Given User opens Part 1 screen
    When User enters a valid username
    And User enters a valid email
    And User enters a valid password
    And User taps the Submit button
    Then Success message should be displayed
 
    Scenario: Submit the form with invalid user credentials
    Given User opens Part 1 screen
    When User enters an invalid username
    And User enters an invalid email
    And User enters an invalid password
    And User taps the Submit button
    Then Validation error message should be displayed
 
   Scenario: Validate that list is in alphabetical order
      When I open Part 2
     And I scroll through the brand list
     Then the brand list should be in alphabetical order
 
   Scenario: Validate empty categories
    When I open Part 2
    And I scroll through the brand list
   Then the following categories should be empty
    | E |
    | G |
    | Q |
    | U |
    | W |
    | X |
    | Y |
    | Z |
 
  Scenario: Validate categories with 3 or more items
     When I open Part 2
    And I scroll through the brand list
     Then the following categories should have at least 3 items
     | A |
     | B |
 	 | C |
     | D |
     | F |
     | H |
     | L |
     | M |
     | R |
     | S |
 
  Scenario: Validate total sum
    When I open Part 3
    And I scroll through the product list
    Then the total should match the sum of all item prices
    
  Scenario: Validate specific brand sum
    When I open Part 3 for brand total validation
    And I scroll through the product list and calculate brand totals
    Then the displayed brand totals should match the calculated brand totals
    
    Scenario: Validate all unique FAKEIMAGE names
    When I open Part 3 for image validation
   And I scroll through the product list and save all item image names
    Then all item image names should match the allowed image names
    
  Scenario: TAT-71 Part 4 - Enable all Section One checkboxes
  When I open Part 4
  And I enable section 1 checkboxes
  And I click Submit
   Then I should see Success popup for section "1"
 
  Scenario: TAT-72 Part 4 - Enable all required section two checkboxes
   When I open Part 4
   And I enable section 2 checkboxes
   And I click Submit
   Then I should see Success popup for section "2"
 
  Scenario: TAT-73 Part 4 - Enable all section three checkboxes that have 'yes' under them
   When I open Part 4
   And I enable section 3 checkboxes
   And I click Submit
   Then I should see Success popup for section "3"
 
 