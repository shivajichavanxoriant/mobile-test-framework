 
package com.appium.base;
 
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class AppiumServerManager {
 
    private static final Logger logger =
            LoggerFactory.getLogger(AppiumServerManager.class);
 
    private static Process appiumProcess;
 
    private static final String APPIUM_URL =
            "http://127.0.0.1:4723/status";
 
    public static synchronized void startServer() throws Exception {
 
        if (isServerRunning()) {
            logger.info("Appium server is already running.");
            return;
        }
 
        logger.info("Starting Appium server on port 4723.");
 
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "appium.cmd",
                "--address", "127.0.0.1",
                "--port", "4723"
        );
 
        builder.redirectErrorStream(true);
        builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
 
        appiumProcess = builder.start();
 
        long deadline = System.nanoTime()
                + TimeUnit.SECONDS.toNanos(30);
 
        while (System.nanoTime() < deadline) {
 
            if (isServerRunning()) {
                logger.info("Appium server started successfully.");
                return;
            }
 
            if (appiumProcess != null && !appiumProcess.isAlive()) {
                logger.error("Appium process exited unexpectedly.");
                throw new IllegalStateException(
                        "Appium process exited unexpectedly. "
                        + "Check Appium installation and port 4723."
                );
            }
 
            LockSupport.parkNanos(
                    TimeUnit.MILLISECONDS.toNanos(250)
            );
        }
 
        logger.error("Appium server startup timed out.");
 
        stopServer();
 
        throw new IllegalStateException(
                "Appium server did not start within 30 seconds."
        );
    }
 
    private static boolean isServerRunning() {
 
        HttpURLConnection connection = null;
 
        try {
            connection = (HttpURLConnection)
                    new URL(APPIUM_URL).openConnection();
 
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
 
            return connection.getResponseCode() == 200;
 
        } catch (IOException e) {
            return false;
 
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
 
    public static synchronized void stopServer() {
 
        if (appiumProcess != null && appiumProcess.isAlive()) {
 
            logger.info("Stopping Appium server.");
 
            appiumProcess.toHandle()
                    .descendants()
                    .forEach(ProcessHandle::destroyForcibly);
 
            appiumProcess.destroyForcibly();
 
            try {
                appiumProcess.waitFor(5, TimeUnit.SECONDS);
 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn(
                        "Interrupted while stopping Appium server.", e
                );
            }
 
            logger.info("Appium server stopped.");
        }
 
        appiumProcess = null;
    }
}
 