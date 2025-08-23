package com.hackathon.automation_framework;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignInPage extends BaseTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By signInButton = By.xpath("//a[@id='Sign In']");
    private By usernameDropdown = By.xpath("//div[text()='Select Username']");
    private By passwordDropdown = By.xpath("//div[text()='Select Password']");
    private By loginButton = By.xpath("//button[text()='Log In']");
    private By dashboardUsername = By.xpath("//span[@class='username']");

    public SignInPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Click Sign In to navigate to sign-in page
    public void clickSignIn() {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
        logStep("Clicked on Sign In button");
    }

    // Verify username dropdown is displayed
    public boolean isUsernameDropdownDisplayed() {
        boolean displayed = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameDropdown)).isDisplayed();
        logStep("Username dropdown displayed: " + displayed);
        return displayed;
    }

    // Verify password dropdown is displayed
    public boolean isPasswordDropdownDisplayed() {
        boolean displayed = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordDropdown)).isDisplayed();
        logStep("Password dropdown displayed: " + displayed);
        return displayed;
    }

    // Enter username using Actions + sendKeys + ENTER
    public void enterUsername(String username) {
        wait.until(ExpectedConditions.elementToBeClickable(usernameDropdown)).click();
        Actions actions = new Actions(driver);
        actions.sendKeys(username).sendKeys(Keys.ENTER).build().perform();
        logStep("Entered username: " + username);
    }

    // Enter password using Actions + sendKeys + ENTER
    public void enterPassword(String password) {
        wait.until(ExpectedConditions.elementToBeClickable(passwordDropdown)).click();
        Actions actions = new Actions(driver);
        actions.sendKeys(password).sendKeys(Keys.ENTER).build().perform();
        logStep("Entered password: " + password);
    }

    // Click Log In button
    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        logStep("Clicked on Log In button");
    }

    // Get logged-in username from dashboard
    public String getLoggedInUsername() {
        String username = wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardUsername)).getText();
        logStep("Logged-in username on dashboard: " + username);
        return username;
    }
}
