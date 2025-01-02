package utils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

public class ReusableMethods {

    private AppiumDriver driver;

    // Constructor ile driver'ı başlattım
    public ReusableMethods(AppiumDriver driver) {
        this.driver = driver;
    }


    public void userScrollsDownThePage(String percent) throws InterruptedException {

        int scrollPercentage = Integer.parseInt(percent);

        Dimension size = driver.manage().window().getSize();
        int endX = size.getWidth() / 8;
        int endY = (int) (size.getHeight() * 0.1);
        int startX = endX;
        int startY = (int) (size.getHeight() * (scrollPercentage / 100.0)); //mesela feature dosyasından 80 değeri geldi, sayfanın aşağıya en yakın kısmından
                                                                        // tutup endY'ye(sayfanın en üst kısımlarına) kadar çıkartacak


        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        for (int i = 0; i < 1; i++) {
            Sequence sequence = new Sequence(finger1, 1)
                    .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY)) //parmağın sayfanın neresine dokunacağını belirlemek için
                    .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg())) //ekrana dokunması için
                    .addAction(new Pause(finger1, Duration.ofMillis(100))) //Parmağın dokunduğu noktada kısa bir süre beklemesi için
                    .addAction(finger1.createPointerMove(Duration.ofMillis(300), PointerInput.Origin.viewport(), endX, endY)) //parmağı verdiğim koordinatlara kadar kaydırması için
                    .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg())); //parmağı ekrandan kaldırması için

            driver.perform(Collections.singletonList(sequence));
        }

        Thread.sleep(3000);  // Kaydırma sonrası bekleme
    }

}
