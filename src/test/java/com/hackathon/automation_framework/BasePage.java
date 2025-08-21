package com.hackathon.automation_framework;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    // 🔹 Click
    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    // 🔹 Type / Send Keys
    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    // 🔹 Clear Field
    protected void clear(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).clear();
    }

    // 🔹 Get Text
    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    // 🔹 Get Attribute
    protected String getAttribute(By locator, String attribute) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getAttribute(attribute);
    }

    // 🔹 Check if Element is Displayed
    protected boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // 🔹 Hover (Mouse Over)
    protected void hover(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        actions.moveToElement(element).perform();
    }

    // 🔹 Double Click
    protected void doubleClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        actions.doubleClick(element).perform();
    }

    // 🔹 Right Click (Context Click)
    protected void rightClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        actions.contextClick(element).perform();
    }

    // 🔹 Drag and Drop
    protected void dragAndDrop(By source, By target) {
        WebElement src = wait.until(ExpectedConditions.visibilityOfElementLocated(source));
        WebElement tgt = wait.until(ExpectedConditions.visibilityOfElementLocated(target));
        actions.dragAndDrop(src, tgt).perform();
    }

    // 🔹 Scroll to Element
    protected void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // 🔹 Scroll By Pixels
    protected void scrollBy(int x, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    // 🔹 Select from Dropdown (by visible text)
    protected void selectByVisibleText(By locator, String text) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Select(dropdown).selectByVisibleText(text);
    }

    // 🔹 Select from Dropdown (by value)
    protected void selectByValue(By locator, String value) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Select(dropdown).selectByValue(value);
    }

    // 🔹 Select from Dropdown (by index)
    protected void selectByIndex(By locator, int index) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Select(dropdown).selectByIndex(index);
    }

    // 🔹 Press Keyboard Keys
    protected void pressKey(By locator, Keys key) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.sendKeys(key);
    }

    // 🔹 Get Page Title
    public String getPageTitle() {
        return driver.getTitle();
    }
}
