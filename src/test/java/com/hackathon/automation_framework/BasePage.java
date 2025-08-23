package com.hackathon.automation_framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    // Click an element
    protected void click(By locator) {
        driver.findElement(locator).click();
    }

    // Enter text
    protected void type(By locator, String text) {
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(text);
    }

    // Get text from element
    protected String getText(By locator) {
        return driver.findElement(locator).getText();
    }

    // Check if element is displayed
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    
}
