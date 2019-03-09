package com.emailtest.cucumber;

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
import java.io.*;
import java.util.concurrent.TimeUnit;

public class StepDefinitions {

    private WebDriver driver;

    private final String DRIVER_PATH = "/Users/patricklai/dropbox/u4/ECSE 428/assignment b/driver/chromedriver";
    private final String OUTLOOK_URL = "https://outlook.live.com/owa/";
    private final String EMAIL_LOGIN = "tester113355@outlook.com";
    private String emailPassword = "";

    private final String EMAIL_TEST_SUBJECT = "ECSE 428 Assignment 2 test email";
    private final String NO_RECIPIENT_ERROR = "This message must have at least one recipient.";
    private final String NO_SUBJECT_WARNING = "Missing subject";

    private final String NEW_MESSAGE_CLASS = "ms-Button ms-Button--action ms-Button--command _1Tegvg68M2d-4Z3rDrt2B3 root-50";

    private final int DEFAULT_DRIVER_TIMEOUT = 10;
    private final int PAGE_LOAD_TIMEOUT = 2;

    private void startDriver() throws IOException {
        if (driver == null) {
            System.out.println("Starting Chrome driver");
            System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
            driver = new ChromeDriver();
            System.out.print("Chrome driver started Done!\n");
        }
        else {
            System.out.println("Chrome driver is already running");
        }

        loadPassword();
    }

    private void loadPassword() throws IOException{
        File file = new File("password.txt"); 

        if (file.exists()){
            BufferedReader br = new BufferedReader(new FileReader(file));
            emailPassword = br.readLine();
            System.out.println(emailPassword);
        }
    }

    private void waitForPageLoad() throws InterruptedException {
        System.out.println("waiting for page to load");
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
    public void givenLoggedIn() throws InterruptedException, IOException {
        startDriver();

        // navigate to the login page and fill in the username and password fields
        driver.get(OUTLOOK_URL);
        driver.findElement(By.className("officeHeaderMenu")).click();
        waitForPageLoad();

        waitForElementById("i0116").sendKeys(EMAIL_LOGIN);
        waitForElementById("idSIButton9").click();
        waitForElementById("i0118").sendKeys(emailPassword);
        waitForElementById("idSIButton9").click();

        waitForPageLoad();
        checkLogin();
    }

    @When("^I press on New Message$")
    public void createNewEmail(){
        driver.findElement(By.id("id__5")).click();
    }

    @And("^I add recipient A$")
    public void addRecipientA() throws InterruptedException{
        addRecipient("patricklai10123@gmail.com");
    }

    @And("^I add recipient B$")
    public void addRecipientB() throws InterruptedException{
        addRecipient("B");
    }

    @And("^I attach image 1$")
    public void addPicture1() throws AWTException, InterruptedException{
        addPicture("picture1.jpg");
    }

    @And("^I attach image 2$")
    public void addPicture2() throws AWTException, InterruptedException{
        addPicture("picture2.jpg");
    }

    @And("^I add a subject$")
    public void addSubject(){
        driver.findElement(By.id("subjectLine0")).sendKeys(EMAIL_TEST_SUBJECT);
    }

    @And("^I press Send$")
    public void sendEmail() {
        // in the case of outlook's send button, the classname is not always the same,
        // so we need to find the button by matching text
        driver.findElement(By.xpath("//div[text()='Send']")).click();
    }

    @Then("^the email is sent with an image attached to it$")
    public void checkIfEmailSent() throws InterruptedException{
        waitForPageLoad();

        // go to the sent messages
        driver.findElement(By.xpath("//div[@title='Sent Items']")).click();
        new WebDriverWait(driver, DEFAULT_DRIVER_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[text()='Empty folder']")));

        if (!driver.getPageSource().contains(EMAIL_TEST_SUBJECT)){
            driver.close();
            throw new Error("Email was not sent");
        }
    }

    @Then("^a Missing Subject popup appears asking if you want to send this message without a subject$")
    public void noSubjectWarningMessage(){
        if(!driver.getPageSource().contains(NO_SUBJECT_WARNING)){
            driver.close();
            throw new Error("No subject warning message was not displayed properly");
        }

        driver.findElement(By.xpath("//div[text()='Don't send']")).click();
        checkLogin();
        driver.close();
    }

    @Then("^an error message saying This message must have at least one recipient appears$")
    public void noRecipientErrorMessage(){
        if(!driver.getPageSource().contains(NO_RECIPIENT_ERROR)){
            driver.close();
            throw new Error("Recipient error message was not displayed properly");
        }

        checkLogin();
        driver.close();
    }

    private void addRecipient(String recipient) throws InterruptedException{
        new WebDriverWait(driver, DEFAULT_DRIVER_TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@class='ms-BasePicker-input pickerInput_269bfa71']")))
                .sendKeys(recipient);
    }

    private void addPicture(String path) throws InterruptedException, AWTException{
        waitForPageLoad();

        File imageFile = new File(path);
        String imagePath = imageFile.getAbsolutePath();

        // send the image to the attachment input field
        driver.findElement(By.xpath("//input[@accept='image/*']")).sendKeys("/Users/patricklai/dropbox/u4/ECSE 428/assignment b/patrick-yueh-a2/image1.jpg");

        // wait for the image to laod
        waitForPageLoad();
        //new WebDriverWait(driver, DEFAULT_DRIVER_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@dir='ltr']")));
    }
}

