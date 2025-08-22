package com.hackathon.automation_framework;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final String pageUrl = "https://techbeamers.com/selenium-practice-test-page/";
    private final String expectedTitle = "Selenium Practice Test - TechBeamers";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(pageUrl);
    }

    public String getExpectedTitle() {
        return expectedTitle;
    }
}
