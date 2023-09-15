Feature:  CamundaCon2023 example conclude car insurance

Background:
  Given camunda ist ready
  Given the process is "ConcludeCarInsurance"

  Scenario: new car insurance
    Given the car is a "new car"
    And the car brand is a "mercedes"
    And the car model is "CLA 35"
    When the camunda process is running
    Then the process is completed with "car automatically insured"
    And a new contract "was" created for a "mercedes" "CLA 35"
    And the car "is" registered with the authorities
    And the costumer "is" informed about the new insurance policy
    And "no" job for a clerk was created

  Scenario: vintage car insurance
    Given the car is a "vintage car"
    And the car brand is a "Ford"
    And the car model is "1958 Fairlane"
    When the camunda process is running
    Then the process is completed with "manual processing necessary"
    And a new contract "was not" created for a "Ford" "1958 Fairlane"
    And the car "is not" registered with the authorities
    And the costumer "is not" informed about the new insurance policy
    And "a" job for a clerk was created