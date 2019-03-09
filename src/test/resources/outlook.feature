Feature: Outlook

  #Normal Flow:
  Scenario: Send a new email with image 1 attached to recipient A
    Given I am logged in to outlook
    When I press on New Message
    And I add recipient A
    And I add a subject
    And I attach image 1
    And I press Send
    Then the email is sent with an image attached to it

  #Alternate Flow:
  Scenario: Send a new email with image 2 attached to recipient B
    Given I am logged in to outlook
    When I press on New Message
    And I add recipient B
    And I add a subject
    And I attach image 2
    And I press Send
    Then the email is sent with an image attached to it

  #Alternate Flow:
  Scenario: Send a new email with image 1 attached to recipient A and B
    Given I am logged in to outlook
    When I press on New Message
    And I add recipient A
    And I add recipient B
    And I add a subject
    And I attach image 1
    And I press Send
    Then the email is sent with an image attached to it

  #Error Flow:
  Scenario: Send a new email with image 1 attached, without a recipient
    Given I am logged in to outlook
    When I press on New Message
    And I add a subject
    And I attach image 1
    And I press Send
    Then an error message saying This message must have at least one recipient appears

  #Error Flow:
  Scenario: Send a new email with image 2 attached to recipient A without a subject
    Given I am logged in to outlook
    When I press on New Message
    And I add recipient A
    And I attach image 2
    And I press Send
    Then a Missing Subject popup appears asking if you want to send this message without a subject