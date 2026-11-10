Feature: Part 2 - Brand List Validation

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