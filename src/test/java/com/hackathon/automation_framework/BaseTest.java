package com.hackathon.automation_framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.HashMap;

public class BaseTest {

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();

    // Environment variable fallback
    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME") != null ?
            System.getenv("BROWSERSTACK_USERNAME") : "YOUR_USERNAME";
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY") != null ?
            System.getenv("BROWSERSTACK_ACCESS_KEY") : "YOUR_ACCESS_KEY";
    private static final String HUB_URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    private static final String BUILD_NAME = "Hackathon_Build_1";
    private static String reportPath;

    public static WebDriver getDriver() {
        return driver.get();
    }

    // ---------------------- Extent Report Setup ----------------------
    @BeforeSuite
    public void setUpExtentReport() {
        String reportName = "ExtentReport_" + System.currentTimeMillis() + ".html";
        reportPath = "test-output/" + reportName;

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Hackathon Automation Report");
        spark.config().setReportName("Hackathon Test Execution");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        System.out.println("Extent Report initialized at: " + reportPath);
    }

    // ---------------------- Test Setup ----------------------
    @Parameters({"browser", "os", "osVersion"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser,
                      @Optional("Windows") String os,
                      @Optional("11") String osVersion,
                      Method method) throws Exception {

        System.out.println("Starting test: " + method.getName() + " on browser: " + browser);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", browser);
        caps.setCapability("browserVersion", "latest");

        HashMap<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", os);
        bstackOptions.put("osVersion", osVersion);
        bstackOptions.put("projectName", "Hackathon Project");
        bstackOptions.put("buildName", BUILD_NAME);
        bstackOptions.put("sessionName", method.getName() + " (" + browser + ")");

        caps.setCapability("bstack:options", bstackOptions);

        WebDriver webDriver = new RemoteWebDriver(new URL(HUB_URL), caps);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.set(webDriver);

        ExtentTest test = extent.createTest(method.getName() + " (" + browser + ")");
        testReport.set(test);
    }

    // ---------------------- Step Logging ----------------------
    public void logStep(String message) {
        System.out.println("STEP: " + message);
        ExtentTest test = testReport.get();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }

    // ---------------------- Test Teardown ----------------------
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        ExtentTest test = testReport.get();
        String methodName = result.getMethod().getMethodName();
        String status = "passed";

        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                status = "failed";
                String failureMessage = result.getThrowable() != null ?
                        result.getThrowable().getMessage() : "Test failed";

                System.out.println("TEST FAILED: " + methodName);
                System.out.println("Reason: " + failureMessage);

                if (test != null) test.log(Status.FAIL, failureMessage);

                // Capture screenshot
                if (getDriver() instanceof TakesScreenshot) {
                    File screenshotsDir = new File("test-output/screenshots/");
                    if (!screenshotsDir.exists()) screenshotsDir.mkdirs();

                    File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
                    File destFile = new File(screenshotsDir, methodName + "_" + System.currentTimeMillis() + ".png");
                    Files.copy(srcFile.toPath(), destFile.toPath());

                    if (test != null) test.addScreenCaptureFromPath("screenshots/" + destFile.getName());
                    System.out.println("Screenshot saved: " + destFile.getAbsolutePath());
                }

            } else if (result.getStatus() == ITestResult.SKIP) {
                status = "skipped";
                System.out.println("TEST SKIPPED: " + methodName);
                if (test != null) test.log(Status.SKIP, "Test skipped");
            } else {
                System.out.println("TEST PASSED: " + methodName);
                if (test != null) test.log(Status.PASS, "Test passed successfully");
            }

            // Set BrowserStack session status
            if (getDriver() instanceof RemoteWebDriver) {
                ((RemoteWebDriver) getDriver()).executeScript(
                        "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\""
                                + status + "\", \"reason\": \"" + methodName + "\"}}"
                );
            }

        } catch (Exception e) {
            System.out.println("Error in tearDown: " + e.getMessage());
        } finally {
            if (getDriver() != null) getDriver().quit();
            driver.remove();
        }
    }

    // ---------------------- Extent Report Teardown ----------------------
    @AfterSuite
    public void tearDownExtentReport() {
        if (extent != null) extent.flush();
        try {
            File htmlFile = new File(reportPath);
            if (htmlFile.exists()) Desktop.getDesktop().browse(htmlFile.toURI());
            System.out.println("Extent Report opened automatically.");
        } catch (Exception e) {
            System.out.println("Could not open report automatically: " + e.getMessage());
        }
    }
}
