Browerstack-Testathon-2025

Repository for BrowserStack Testathon 2025 - Manual test cases, automation scripts, and reports.

Hey there! 
This repo contains everything I did for the BrowserStack Testathon 2025 - Manual test cases, automation scripts, and reports.

Manual Test Cases:

All manual test cases are documented here:  

Test Management URL:

https://test-management.browserstack.com/projects/2291126/folder/15081609/test-cases?public_token=5ca4fe287728b87269bb8250472518863b437da8f0a7d0793725fd88f01620a17cd8e18907558a16980604e8796b90b44e9e3d01b8c68fa7e5f66ae50377032d&public_token_id=6333

Note: The link points to the root folder. From there, you can expand the respective subfolders to view the individual test cases. I’ve kept it as a single URL for simplicity.

Automation Suite

Included an end-to-end automation framework using:  
1. Java + Selenium
2. TestNG for running tests
3. ExtentReports for detailed reports
4. BrowserStack for cross-browser testing  

All scripts are fully functional and reports are generated automatically.

--Running the Automation Suite

Before running the tests, set your BrowserStack credentials as environment variables.  

-- On Windows (Command Prompt)

set BROWSERSTACK_USERNAME=`<your-username>`
set BROWSERSTACK_ACCESS_KEY =`<your-access-key>`

-- On macOS / Linux (bash/zsh)

export BROWSERSTACK_USERNAME=`<your-username>`
export BROWSERSTACK_ACCESS_KEY=`<your-access-key>`

-- Test Management / Observability

Since the current automation suite does not have BrowserStack SDK integration, full Test Management and Observability dashboards are not available.

Instead, you can view the results of the automated test runs using this public link

https://automate.browserstack.com/projects/Hackathon+Project/builds/Hackathon_Build/29?tab=tests&testListView=flat&public_token=2963e5c31c6150afaa2d2e2907283298c18d6fbac2ede3ec350d17888a1d0a40

Maintained by Vignesh Manikandan – Software QA Engineer
