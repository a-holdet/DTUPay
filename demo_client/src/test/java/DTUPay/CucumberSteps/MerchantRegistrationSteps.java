package DTUPay.CucumberSteps;

import CustomerMobileApp.CustomerAdapter;
import CustomerMobileApp.MerchantAdapter;
import DTUPay.Holders.*;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MerchantRegistrationSteps {
    //Adapters
    BankService bankService = new BankServiceService().getBankServicePort();
    MerchantAdapter merchantAdapter = new MerchantAdapter();

    //Holders
    private final MerchantHolder merchantHolder;
    private final OtherMerchantHolder otherMerchantHolder;
    private final ExceptionHolder exceptionHolder;

    public MerchantRegistrationSteps(MerchantHolder merchantHolder, OtherMerchantHolder otherMerchantHolder, ExceptionHolder exceptionHolder) {
        this.merchantHolder = merchantHolder;
        this.otherMerchantHolder = otherMerchantHolder;
        this.exceptionHolder = exceptionHolder;
    }

    @After
    public void after() {
        try {
            if (merchantHolder.getAccountId() != null)
                bankService.retireAccount(merchantHolder.getAccountId());
        } catch (BankServiceException_Exception e) {
        }
        try {
            if (otherMerchantHolder.getAccountId() != null)
                bankService.retireAccount(otherMerchantHolder.getAccountId());
        } catch (BankServiceException_Exception e) {
        }

        merchantHolder.reset();
        otherMerchantHolder.reset();
    }

    @And("the merchant has a bank account")
    public void theMerchantHasABankAccount() throws BankServiceException_Exception {
        merchantHolder.setMerchantBasics();
        User merchantBank = new User();
        merchantBank.setFirstName(merchantHolder.getFirstName());
        merchantBank.setLastName(merchantHolder.getLastName());
        merchantBank.setCprNumber(merchantHolder.getCpr());

        merchantHolder.setAccountId(bankService.createAccountWithBalance(merchantBank, new BigDecimal(2000)));
    }

    @Given("the merchant has no bank account")
    public void theMerchantHasNoBankAccount() {
        merchantHolder.setMerchantBasics();
        //Do not create account
    }

    @And("another merchant has a bank account")
    public void anotherMerchantHasABankAccount() throws BankServiceException_Exception {
        otherMerchantHolder.setMerchantBasics();
        User merchantBank = new User();
        merchantBank.setFirstName(otherMerchantHolder.getFirstName());
        merchantBank.setLastName(otherMerchantHolder.getLastName());
        merchantBank.setCprNumber(otherMerchantHolder.getCpr());

        otherMerchantHolder.setAccountId(bankService.createAccountWithBalance(merchantBank, new BigDecimal(2000)));
    }

    @And("the merchant is registering with DTUPay")
    public void theMerchantIsRegisteredWithDTUPay(){
        try {
            registerMerchantWithDTUPay(merchantHolder);
        } catch (IllegalArgumentException e) {
            exceptionHolder.setException(e);
            merchantHolder.setId(null);
        }
    }

    @And("the merchant is not registered with DTUPay")
    public void theMerchantIsNotRegisteredWithDTUPay() {
        //Do not register merchant with DTUPay
    }

    @And("the other merchant is registering with DTUPay")
    public void theOtherMerchantIsRegisteredWithDTUPay() throws IllegalArgumentException{
        registerMerchantWithDTUPay(otherMerchantHolder);
    }

    private void registerMerchantWithDTUPay(UserHolder merchantHolder) throws IllegalArgumentException{
        String createdMerchantId = merchantAdapter.registerMerchant(merchantHolder.getFirstName(), merchantHolder.getLastName(), merchantHolder.getCpr(), merchantHolder.getAccountId());
        merchantHolder.setId(createdMerchantId);
    }

    @Then("the merchant registration is successful")
    public void theMerchantRegistrationIsSuccessful() {
        assertNotNull(merchantHolder.getId());
    }

    @Then("the merchant registration is not successful")
    public void theMerchantRegistrationIsNotSuccessful() {
        assertNull(merchantHolder.getId());
    }
}
