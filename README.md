# McGill ECSE 428: Software Engineering practice - Assignment 2
### By @patlai and @UAce
In this assignment, we are using Selenium Web Driver for Chrome to automate acceptance testing of an email service.
We have chosen to test outlook.com's web client by sending emails with different combinations between two recipients and two image attachments. We are using the Cucumber plugin for Java along with Gherkin step definitions to specifiy the tests. All these tools can be downloaded via the JetBrain's plugin window in IntelliJ Idea. The Gherkin feature can be found in `src/test/resources` and the step definitions in `src/test/java/com/emailtest/cucumber`

## Running the tests
1. Clone this repository
2. Download chrome driver and create a new folder in the repository root folder called `driver` and paste the driver executable there
3. Open the repository as a project in IntelliJ and wait for the maven dependencies to build
4. Ensure the cucumber plugin is installed and up to date.
5. Create a `password.txt` in the root folder with the correct password to access the test outlook account.
6. Right click on the Gherkin file in the sidebar of IntelliJ and click run