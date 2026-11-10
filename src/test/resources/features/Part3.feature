Feature: Part 3 - Product Validation

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