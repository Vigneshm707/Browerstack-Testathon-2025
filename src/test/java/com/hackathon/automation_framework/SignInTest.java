package com.hackathon.automation_framework;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SignInTest extends BaseTest {

    @Test
    public void validLoginTest() {
        WebDriver driver = getDriver();
        driver.get("https://testathon.live/");
        logStep("Navigated to testathon.live");

        SignInPage signInPage = new SignInPage(driver);

        // Step 1: Click Sign In
        signInPage.clickSignIn();

        // Step 2: Verify dropdowns are displayed
        Assert.assertTrue(signInPage.isUsernameDropdownDisplayed(), "Username dropdown is not displayed");
        Assert.assertTrue(signInPage.isPasswordDropdownDisplayed(), "Password dropdown is not displayed");

        // Step 3: Enter username and password
        String username = "demouser";
        String password = "testingisfun99";
        signInPage.enterUsername(username);
        signInPage.enterPassword(password);

        // Step 4: Click login
        signInPage.clickLogin();

        // Step 5: Verify logged-in username matches selection
        String dashboardUsername = signInPage.getLoggedInUsername();
        Assert.assertEquals(dashboardUsername, username, "Logged-in username does not match entered username");
        logStep("Valid login verified successfully");
    }
}
