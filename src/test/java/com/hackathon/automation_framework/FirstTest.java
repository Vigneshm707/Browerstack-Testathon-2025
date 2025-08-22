package com.hackathon.automation_framework;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.time.Duration;

public class FirstTest extends BaseTest {

    @Test(description = "Verify Home Page Title is correct")
    public void verifyHomePageTitle() {
        HomePage homePage = new HomePage(getDriver());

        logStep("Opening Home Page");
        homePage.open();

        String expectedTitle = homePage.getExpectedTitle();
        String actualTitle = "";

        try {
            logStep("Waiting for Home Page title to be: " + expectedTitle);
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.titleContains(expectedTitle));
            actualTitle = homePage.getPageTitle();
            logStep("Retrieved actual page title: " + actualTitle);
        } catch (Exception e) {
            actualTitle = getDriver().getTitle();
            logStep("Exception while verifying title. Current title: " + actualTitle);
            throw new AssertionError(
                    "Home Page Title Verification Failed.\nExpected: '" + expectedTitle +
                            "'\nActual: '" + actualTitle + "'"
            );
        }

        if (!actualTitle.equals(expectedTitle)) {
            logStep("Title mismatch! Expected: '" + expectedTitle + "', Actual: '" + actualTitle + "'");
            throw new AssertionError(
                    "Home Page Title Verification Failed.\nExpected: '" + expectedTitle +
                            "'\nActual: '" + actualTitle + "'"
            );
        }

        logStep("Home Page Title Verified Successfull: " + actualTitle);
    }
}
