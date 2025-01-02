package base;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Locale;

public class BaseTest {

    public static AndroidDriver driver;
    public static WebDriverWait wait;

    @Before
    public static void setup() throws MalformedURLException {
        String appiumServerUrl= "http://127.0.0.1:4723/";

        Locale.setDefault(Locale.ENGLISH);

        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability("platformName", "Android");
        dc.setCapability("appium:automationName", "uiautomator2");
        dc.setCapability("appium:appPackage", "trendyol.com");
        dc.setCapability("appium:appActivity", "com.trendyol.common.splash.impl.ui.SplashActivity");
        dc.setCapability("noReset", "true");


        try {
            driver = new AndroidDriver(new URL(appiumServerUrl), dc);
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            System.out.println("Driver and application initialized successfully.");

            driver.activateApp("trendyol.com"); // Uygulamayı başlatması için
            System.out.println("Application started successfully.");
        } catch (MalformedURLException e) {
            System.err.println("Invalid Appium server URL: " + e.getMessage());
            throw e;
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(25));


        // aşağıdaki satırları her senaryodan önce baştaki Seçim sayfasını kapatması için yazdık ancak capabilities'e noReset özelliği eklediğimiz için
        //çerezleri koruyacak ve her seferinde aşağıdaki satırları koşturmaya gerek kalmayacak(yani seçim ekranını bir kere kapadıktan sonra aynı ekran
        //tekrar bizi karşılamayacak çünkü uygulama en son verileri koruyor)
        // WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id("trendyol.com:id/buttonDismiss"))); // Seçim sayfasındaki "Kapat" butonu
        // closeButton.click();

        // WebElement closeButton2 = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id("trendyol.com:id/imageButtonClose"))); // Seçim sayfasındaki "Kapat" butonu
        // closeButton2.click();
        // System.out.println("Seçim sayfası kapatıldı.");

    }


    @After
    public static void close(){
        if (driver != null) {
            try {
                driver.terminateApp("trendyol.com");  // Uygulamayı sonlandırması için
                System.out.println("Application terminated successfully.");

                driver.quit();
                System.out.println("Driver closed successfully.");
            } catch (WebDriverException e) {
                System.err.println("Error while closing the driver or application: " + e.getMessage());
            }
        } else {
            System.out.println("Driver was not initialized.");
        }
    }
}