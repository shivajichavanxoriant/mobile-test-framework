Feature: Part 4 - Checkbox Validation
 
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