package com.emailtest.cucmber;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import java.awt.*;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class StepDefinitions {

    private WebDriver driver;

    private final String DRIVER_PATH = "/Users/patricklai/dropbox/u4/ECSE 428/assignment b/driver";
    private final String OUTLOOK_URL = "https://outlook.live.com/owa/";
    private final String EMAIL_LOGIN = "test";
    private final String EMAIL_PASSWORD = "test";

    private final String EMAIL_TEST_SUBJECT = "ECSE 428 Assignment 2 test email";
    private final String NO_RECIPIENT_ERROR = "This message must have at least one recipient.";

    private final String NEW_MESSAGE_CLASS = "ms-Button ms-Button--action ms-Button--command _1Tegvg68M2d-4Z3rDrt2B3 root-50";

    private final int DEFAULT_DRIVER_TIMEOUT = 10;
    private final int PAGE_LOAD_TIMEOUT = 2;

    private void startDriver()  {
        if (driver == null) {
            System.out.println("Starting Chrome driver");
            System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
            driver = new ChromeDriver();
            System.out.print("Chrome driver started Done!\n");
        }
        else {
            System.out.println("Chrome driver is already running");
        }
    }

    private void waitForPageLoad() throws InterruptedException {
        TimeUnit.SECONDS.sleep(PAGE_LOAD_TIMEOUT);
    }

    private WebElement waitForElementByName(String name){
       return (new WebDriverWait(driver, DEFAULT_DRIVER_TIMEOUT)).until(ExpectedConditions.elementToBeClickable(By.name(name)));
    }

    private WebElement waitForElementById(String id){
        return (new WebDriverWait(driver, DEFAULT_DRIVER_TIMEOUT)).until(ExpectedConditions.elementToBeClickable(By.id(id)));
    }

    private void checkLogin(){
        // if we're logged in, we should see elements that can only be
        // seen on the inbox page
        if  (!(driver.getPageSource().contains("Focused") && driver.getPageSource().contains("Other"))){
            driver.close();
            throw new Error("Failed to log in to outlook, shutting down driver");
        }
    }

    @Given("^I am logged in to outlook$")
    public void givenLoggedIn() throws InterruptedException {
        startDriver();

        // navigate to the login page and fill in the username and password fields
        driver.get(OUTLOOK_URL);
        waitForElementByName("identifier").sendKeys(EMAIL_LOGIN);
        // tabbing to the next element from the username field should go to the password field
        waitForElementById("identifierNext").click();
        waitForElementByName("password").sendKeys(EMAIL_PASSWORD);
        // go to the login button
        waitForElementById("passwordNext").click();

        waitForPageLoad();
        checkLogin();
    }

    @When("^I create a new email$")
    public void createNewEmail(){
        driver.findElement(By.className(NEW_MESSAGE_CLASS)).click();
    }

    @And("^I add A as a recipient$")
    public void addRecipientA() throws InterruptedException{
        addRecipient("A");
    }

    @And("^I add B as a recipient$")
    public void addRecipientB() throws InterruptedException{
        addRecipient("B");
    }

    @And("^I add picture 1$")
    public void addPicture1() throws AWTException, InterruptedException{
        addPicture("picture1.jpg");
    }

    @And("^I add picture 2$")
    public void addPicture2() throws AWTException, InterruptedException{
        addPicture("picture2.jpg");
    }

    @And("^I add a subject$")
    public void addSubject(){
        driver.findElement(By.id("subjectLine0")).sendKeys(EMAIL_TEST_SUBJECT);
    }

    @And("^I click send$")
    public void sendEmail() {
        // in the case of outlook's send button, the classname is not always the same,
        // so we need to find the button by matching text
        driver.findElement(By.xpath("//div[text()='Send']")).click();
    }

    @Then("^The email should be sent$")
    public void checkIfEmailSent() throws InterruptedException{
        waitForPageLoad();

        // go to the sent messages
        driver.findElement(By.xpath("//div[text()='Sent Items']")).click();
        new WebDriverWait(driver, DEFAULT_DRIVER_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Empty folder']")));

        if (!driver.getPageSource().contains(EMAIL_TEST_SUBJECT)){
            driver.close();
            throw new Error("Email was not sent");
        }
    }

    @Then("^An error message is displayed saying there is no recipient$")
    public void noRecipientErrorMessage(){
        if(!driver.getPageSource().contains(NO_RECIPIENT_ERROR)){
            driver.close();
            throw new Error("Recipient error message was not displayed properly");
        }

        checkLogin();
        driver.close();
    }

    private void addRecipient(String recipient) throws InterruptedException{
        new WebDriverWait(driver, DEFAULT_DRIVER_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(By.name("to")));
        driver.findElement(By.name("to")).sendKeys(recipient);
    }

    private void addPicture(String path) throws InterruptedException, AWTException{
        waitForPageLoad();

        File imageFile = new File(path);
        String imagePath = imageFile.getAbsolutePath();
        driver.findElement(By.name("Filedata")).sendKeys(imagePath);
    }
}

