package com.hackathon.automation_framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class ProductPageTest extends BaseTest {

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
    public void verifyOnlyOnePlusProducts() {
        // Click OnePlus category
        WebElement onePlusCategory = driver.findElement(By.xpath("//span[text()='OnePlus']"));
        onePlusCategory.click();
        logStep("Clicked on OnePlus category");

        // Wait for products to load
        sleep(2000);

        // Fetch all products inside the shelf
        List<WebElement> products = driver.findElements(
                By.xpath("//div[@class='shelf-container']//div[@class='shelf-item']")
        );

        logStep("Total products found: " + products.size());

        // Keep track of invalid products
        List<String> invalidProducts = new ArrayList<>();

        // Loop through each product and check its name / data-sku
        for (WebElement product : products) {
            String dataSku = product.getAttribute("data-sku");

            // Remove "-device-info.png"
            String productName = dataSku.replace("-device-info.png", "");


            if (!productName.toLowerCase().contains("oneplus")) {
                invalidProducts.add(productName);
            }
        }

        // Final assertion
        if (!invalidProducts.isEmpty()) {
            Assert.fail("OnePlus vendor page should only show OnePlus products. Found others: " + invalidProducts);
        } else {
            logStep("All products belong to OnePlus vendor only.");
        }
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
