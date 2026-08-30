package com.appium.hooks;
 
import com.appium.base.BaseTest;
 
import io.cucumber.java.After;
import io.cucumber.java.Before;
 
public class Hooks {
 
    BaseTest baseTest = new BaseTest();
 
    @Before
    public void setUp() throws Exception {
        baseTest.setUp();
    }
 
    @After
    public void tearDown() {
        baseTest.tearDown();
    }
}