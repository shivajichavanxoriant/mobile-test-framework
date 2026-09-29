# Mobile Test Automation Framework
 
## 1. Execution Requirements
 
The following software and tools are required to execute this project:
 
- Java JDK 21
- Eclipse IDE
- Apache Maven
- Node.js and npm
- Appium Server
- Appium UiAutomator2 Driver
- Android Studio
- Android SDK and Platform Tools (ADB)
- Android physical device or emulator
- USB debugging enabled for physical devices
- Git for version control
 
## 2. Setup Instructions
 
Follow these steps to configure the Mobile Test Automation Framework.
 
### Step 1: Install Required Software
 
- Install Java JDK 21.
- Install Eclipse IDE.
- Install Apache Maven.
- Install Node.js and npm.
- Install Android Studio and Android SDK.
- Install Appium Server and the UiAutomator2 driver.
 
### Step 2: Clone the GitHub Repository
 
Clone the project repository to your local system using Git.
 
### Step 3: Import the Project into Eclipse
 
1. Open Eclipse IDE.
2. Select **File → Import**.
3. Select **Maven → Existing Maven Projects**.
4. Browse to the project directory.
5. Select the `pom.xml` file.
6. Click **Finish**.
 
### Step 4: Configure Maven Dependencies
 
1. Right-click the project.
2. Select **Maven → Update Project**.
3. Select the project and click **OK**.
4. Wait for Maven to download the required dependencies.
 
### Step 5: Configure the Android Device
 
1. Enable Developer Options and USB Debugging on the Android device.
2. Connect the device to the computer using a USB cable.
3. Run the following command to verify the connection:
 
```bash
adb devices
```
 
4. Ensure the connected device is listed.
 
## 3. Execution Instructions
 
Follow these steps to execute the mobile automation tests.
 
### Step 1: Start Appium Server
 
Open Command Prompt and execute:
 
```bash
appium
```
 
Ensure the Appium server starts successfully at:
 
`http://127.0.0.1:4723`
 
### Step 2: Verify Android Device Connection
 
Open another Command Prompt and execute:
 
```bash
adb devices
```
 
Verify that the connected Android device is listed and its status is `device`.
 
### Step 3: Open the Project in Eclipse
 
1. Open Eclipse IDE.
2. Open the `mobile-test-framework` project.
3. Ensure Maven dependencies are resolved.
4. Verify that the Appium server is running.
 
### Step 4: Execute the Test Scenarios
 
1. Navigate to `src/test/java`.
2. Open the configured Cucumber/TestNG runner class in `com.appium.runners`.
3. Right-click the runner class.
4. Select **Run As → TestNG Test**.
 
### Step 5: Review Execution Results
 
- Check the execution status in the Eclipse Console.
- Review the test results and any failure messages.
- Check the generated test reports, if configured.
 
## 4. Test Reports
 
Test execution results can be reviewed in the Eclipse Console.
 
The project uses Cucumber and TestNG for test execution and reporting.
 
After execution, review the console output for:
 
- Passed test scenarios
- Failed test scenarios
- Skipped test scenarios
- Failure details and error messages
 
If configured, generated test reports can also be reviewed after execution.
 