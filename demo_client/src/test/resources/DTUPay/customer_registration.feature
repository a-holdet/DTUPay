Feature: Customer Registration
    Scenario: Successful Registration
       Given the customer has a bank account
        When the customer is registering with DTUPay
        Then the customer registration is successful
    Scenario: No Bank Account
        Given the customer has no bank account
        When the customer is registering with DTUPay
        Then the customer registration is not successful
        And the error message is "Customer must have a bank account to be created in DTUPay"
  #Scenario: Duplicate Customer //We do not need this - had a session with Ragnar. //TODO if we need something to do later we could create functionality for deleting customer/merchant
    #Given the customer has a bank account
   # When the customer is registering with DTUPay
  #  And the customer is registering with DTUPay
 #   Then the customer registration is not successful
#    And the error message is "Customer must have an account id to be created in DTUPay"