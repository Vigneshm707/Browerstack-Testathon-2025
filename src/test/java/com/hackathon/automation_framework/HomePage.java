package com.hackathon.automation_framework;

import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private String pageUrl = "https://techbeamers.com/selenium-practice-test-page/";
    private String expectedTitle = "Selenium Practice Test Page - TechBeamers";

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
