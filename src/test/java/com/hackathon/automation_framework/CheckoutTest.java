package com.hackathon.automation_framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

public class CheckoutTest extends BaseTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = getDriver();
        driver.get("https://testathon.live/");
        logStep("Navigated to testathon.live");

        // Login first
        SignInPage signInPage = new SignInPage(driver);
        signInPage.clickSignIn();
        signInPage.enterUsername("demouser");
        signInPage.enterPassword("testingisfun99");
        signInPage.clickLogin();
        logStep("Logged in successfully");
    }

    @Test
    public void completeCheckoutAndVerifyReceiptDownload() {
        // Step 1: Add first product to cart
        WebElement addToCartBtn = driver.findElement(By.xpath("(//div[@class='shelf-item__buy-btn'])[1]"));
        addToCartBtn.click();
        logStep("Clicked on Add to Cart for first product");

        // Step 2: Click Checkout
        WebElement checkoutBtn = driver.findElement(By.xpath("//div[@class='buy-btn']"));
        checkoutBtn.click();
        logStep("Clicked on Checkout button");

        // Step 3: Fill shipping details with random values
        driver.findElement(By.id("firstNameInput")).sendKeys("Vajjiravel" + System.currentTimeMillis());
        driver.findElement(By.id("lastNameInput")).sendKeys("Vignesh" + System.currentTimeMillis());
        driver.findElement(By.id("addressLine1Input")).sendKeys("No: 11/72, 2nd Street, Muthamizh Nagar, Chennai");
        driver.findElement(By.id("provinceInput")).sendKeys("Tamil Nadu");
        driver.findElement(By.id("postCodeInput")).sendKeys("600122");
        logStep("Filled in shipping details");

        // Step 4: Click Continue
        driver.findElement(By.id("checkout-shipping-continue")).click();
        logStep("Clicked Continue on shipping form");

        // Step 5: Download order receipt
        WebElement receiptLink = driver.findElement(By.xpath("//a[normalize-space(text())='Download order receipt']"));
        receiptLink.click();
        logStep("Clicked on Download order receipt");

        // Step 6: Verify file downloaded (basic check)
        String downloadPath = System.getProperty("user.home") + "/Downloads";
        File receiptFile = new File(downloadPath + "/order.txt"); // adjust file name if different

        boolean isDownloaded = false;
        for (int i = 0; i < 10; i++) { // wait max 10 sec
            if (receiptFile.exists()) {
                isDownloaded = true;
                break;
            }
            sleep(1000);
        }

        Assert.assertTrue(isDownloaded, "Order receipt was not downloaded!");
        logStep("Order receipt downloaded successfully.");
    }

    // Simple sleep utility
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
