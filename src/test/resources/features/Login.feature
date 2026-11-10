Feature: Mobile Application Test Cases
 
  # Part 1 - Form Validation
 
  Scenario Outline: Submit the form with different user credentials
    Given User opens Part 1 screen
    When User enters a "<usernameType>" username
    And User enters a "<emailType>" email
    And User enters a "<passwordType>" password
    And User taps the Submit button
    Then "<expectedResult>" message should be displayed
 
    Examples:
      | usernameType | emailType | passwordType | expectedResult   |
      | valid        | valid     | valid        | Success          |
      | invalid      | invalid   | invalid      | Validation error |
 
 