Feature:  CamundaCon2023 example conclude car insurance

Background:
  Given Camunda is ready

  Scenario: new car insurance
    Given the car is a "new car"
    And the car brand is a "mercedes"
    And the car model is "CLA 35"
    When the process is started
    Then the process is completed
    And a new contract "was" created for the given brand and model
    And the costumer "was" informed about the new insurance policy
    And "no" job for a clerk was created

  Scenario: vintage car insurance
    Given the car is a "vintage car"
    And the car brand is a "Ford"
    And the car model is "1958 Fairlane"
    When the process is started
    Then the process is completed
    And a new contract "was not" created for the given brand and model
    And the costumer "was not" informed about the new insurance policy
    And "a" job for a clerk was created

  Scenario: new car insurance - error during automatic processing
    Given the car is a "new car"
    And the car brand is a "mercedes"
    And the car model is "CLA 35"
    And the backend for automatic insurance contract creation is not reachable
    When the process is started
    Then the process is completed
    And a new contract "could not have been" created for the given brand and model
    And the costumer "was not" informed about the new insurance policy
    And "a" job for a clerk was created