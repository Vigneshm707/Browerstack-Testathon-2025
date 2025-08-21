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

        homePage.open();

        // Wait until the page title contains expected text
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.titleContains(homePage.getExpectedTitle()));

        String actualTitle = homePage.getPageTitle();
        String expectedTitle = homePage.getExpectedTitle();

        // Manual check instead of Assert.assertEquals to prevent TestNG extra text
        if (!actualTitle.equals(expectedTitle)) {
            throw new AssertionError("Home Page Title Verification Failed. Expected: '" 
                    + expectedTitle + "' but found: '" + actualTitle + "'");
        }

        // Log pass
        testReport.get().log(Status.PASS, "Home Page Title Verified Successfully: " + actualTitle);
        System.out.println("Home page title verified successfully: " + actualTitle);
    }
}
