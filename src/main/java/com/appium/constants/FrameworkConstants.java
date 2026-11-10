 
package com.appium.constants;
 
public final class FrameworkConstants {
 
    private FrameworkConstants() {
        // Prevent object creation.
    }
 
    public static final String APPIUM_SERVER_URL =
            "http://127.0.0.1:4723";
 
    public static final String DEVICE_NAME = "Android";
 
    public static final String PLATFORM_NAME = "Android";
 
    public static final String AUTOMATION_NAME = "UiAutomator2";
 
    public static final String CONFIG_FILE_PATH =
            "src/test/resources/config/config.properties";
 
    public static final int EXPLICIT_WAIT_SECONDS = 10;
}
 