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
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;

public class BaseTest {
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();

    // Use environment variables instead of hardcoding credentials
    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String HUB_URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    private static final String BUILD_NAME = "Hackathon_Build_1";
    private static String reportPath;

    public static WebDriver getDriver() {
        return driver.get();
    }

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
    }

    @Parameters({"browser", "os", "osVersion"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("Chrome") String browser,
                      @Optional("Windows") String os,
                      @Optional("11") String osVersion,
                      Method method) throws Exception {

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

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) throws IOException, InterruptedException {
        ExtentTest test = testReport.get();
        String methodName = result.getMethod().getMethodName();
        String failureMessage = "";

        if (result.getStatus() == ITestResult.FAILURE) {
            failureMessage = result.getThrowable() != null ? result.getThrowable().getMessage() :
                    "Test '" + methodName + "' failed";

            test.log(Status.FAIL, failureMessage);
            String screenshotPath = captureScreenshot(methodName);
            test.addScreenCaptureFromPath(screenshotPath);

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "Test passed successfully");
            failureMessage = "Test passed";
        } else {
            test.log(Status.SKIP, "Test skipped");
            failureMessage = "Test skipped";
        }

        if (getDriver() != null) {
            String sessionId = ((RemoteWebDriver) getDriver()).getSessionId().toString();
            System.out.println("BrowserStack Session ID: " + sessionId);

            try {
                URL url = new URL("https://api.browserstack.com/automate/sessions/" + sessionId + ".json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("PUT");
                String auth = USERNAME + ":" + ACCESS_KEY;
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
                conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
                conn.setRequestProperty("Content-Type", "application/json");

                String status = (result.getStatus() == ITestResult.SUCCESS) ? "passed" : "failed";
                String jsonBody = "{\"status\":\"" + status + "\",\"reason\":\"" + failureMessage + "\"}";
                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                System.out.println("BrowserStack REST API response: " + responseCode);
            } catch (Exception e) {
                System.out.println("Failed to update BrowserStack via REST API: " + e.getMessage());
            } finally {
                getDriver().quit();
                driver.remove();
            }
        }

        Thread.sleep(2000);
    }

    @AfterSuite
    public void tearDownExtentReport() {
        if (extent != null) extent.flush();

        try {
            File htmlFile = new File(reportPath);
            if (htmlFile.exists()) Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (Exception e) {
            System.out.println("Could not open report automatically: " + e.getMessage());
        }
    }

    public String captureScreenshot(String methodName) throws IOException {
        File screenshotsDir = new File("test-output/screenshots/");
        if (!screenshotsDir.exists()) screenshotsDir.mkdirs();

        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String path = "test-output/screenshots/" + methodName + "_" + System.currentTimeMillis() + ".png";
        File destFile = new File(path);
        Files.copy(srcFile.toPath(), destFile.toPath());
        return destFile.getAbsolutePath();
    }
}
