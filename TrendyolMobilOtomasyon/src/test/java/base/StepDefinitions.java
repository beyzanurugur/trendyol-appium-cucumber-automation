package base;

import io.appium.java_client.AppiumBy;

import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.junit.Assert;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.JSONReader;
import utils.ReusableMethods;

import java.time.Duration;
import java.util.*;

public class StepDefinitions {

    private static AndroidDriver driver;
    private final WebDriverWait wait;
    private final ReusableMethods reusableMethods;
    private final Set<String> uniqueQuestionTitles;

    public StepDefinitions() {

        driver = BaseTest.driver;
        wait = BaseTest.wait;
        this.reusableMethods = new ReusableMethods(driver);
        this.uniqueQuestionTitles = new HashSet<>();

    }

    @Given("user clicks {string}")
    public void click(String value) {

        String elementLocator = JSONReader.getValuesFromJson(value);

        WebElement element = null;

        assert elementLocator != null;
        if (elementLocator.startsWith("//")) {  // XPath ile başlıyorsa
            element = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.xpath(elementLocator)));
        }

        else if (elementLocator.startsWith("new")) { // uiautomator ile başlıyorsa
            element = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.androidUIAutomator(elementLocator)));
        }

        else {
            element = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(elementLocator)));
        }

        element.click();
    }


    @And("user enters {string} on {string}")
    public void userEntersTextOnInput(String searchText, String elementKey) {
        try {

            String locator = JSONReader.getValuesFromJson(elementKey);
            assert locator != null;

            WebElement searchInput;
            if (locator.startsWith("//")) {
                searchInput = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.xpath(locator)));
            }
            else if (locator.startsWith("new")) {
                searchInput = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.androidUIAutomator(locator)));
            }
            else {
                searchInput = wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(locator)));
            }

            searchInput.sendKeys(searchText);
            System.out.println("Arama kutusuna '" + searchText + "' yazıldı.");

        } catch (Exception e) {
            throw new IllegalStateException("Arama kutusuna değer yazılırken bir hata oluştu: " + e.getMessage());
        }
    }


    @And("user selects element {int} in the {string}")
    public void selectItemFromList(int index, String menuKey) {
        try {
            String locator = JSONReader.getValuesFromJson(menuKey);
            if (locator == null || locator.isEmpty()) {
                throw new IllegalArgumentException("Locator for the menu key '" + menuKey + "' is null or empty.");
            }

            List<WebElement> item;
            WebElement selectedItem = null;
            if (locator.startsWith("//")) { //xpath ile bulma

                item = driver.findElements(AppiumBy.xpath(locator));
                wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.xpath(locator)));
                selectedItem = item.get(index-1);

            } else if (locator.startsWith("new")) { // UIAutomator ile bulma

                item = driver.findElements(AppiumBy.androidUIAutomator(locator));
                wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.androidUIAutomator(locator)));
                selectedItem = item.get(index-1);


            } else {
                throw new UnsupportedOperationException("Unsupported locator strategy for: " + locator);
            }

            assert selectedItem != null;
            selectedItem.click();
            System.out.println("Item with index " + index + " in menu " + menuKey + " has been selected.");

        } catch (NumberFormatException e) {
            System.err.println("The provided item index key '" + index + "' is not a valid integer.");
            throw e;
        } catch (Exception e) {
            System.err.println("Error while selecting item with key '" + index + "' in menu '" + menuKey + "': " + e.getMessage());
            throw e;
        }

    }


    @When("user slides the slider until the last slider")
    public void userSlidesSliderUntilLastSlider() {

        int startX = 900; // Slider başlangıç noktası (sağdan)
        int endX = 100;   // Slider bitiş noktası (soldan)
        int y = 400;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger"); //PointerInput: Cihaz üzerinde dokunma hareketlerini temsil eden bir sınıftır.
                                                                                        //PointerInput.Kind.TOUCH: Bu, hareketin bir "dokunmatik ekran" üzerinde gerçekleşeceğini belirtir.

        for (int i = 0; i < 2; i++) { // 5 slider olduğunu varsayıyoruz
            Sequence swipe = new Sequence(finger, 1);

            // Başlangıç noktasına dokun
            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, y));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

            // Kaydırma hareketi
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), endX, y));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            // Swipe hareketini driver'a gönder
            driver.perform(Arrays.asList(swipe));

            try {
                Thread.sleep(1000); // 1 saniye bekle
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @When("user goes back to the previous page")
    public void userGoesBackToPreviousPage() {
        try {
            // Android'de geri gitmek için
            driver.navigate().back();

            System.out.println("Kullanıcı bir önceki sayfaya gitti.");
        } catch (Exception e) {
            throw new IllegalStateException("Bir önceki sayfaya geri giderken hata oluştu: " + e.getMessage());
        }
    }


    @When("user scrolls down the page {string} percent")
    public void userScrollsDownThePage(String percent) throws InterruptedException {
        reusableMethods.userScrollsDownThePage(percent);
    }


    //Sayfada belirttiğim elemente kadar kaydırma yapması için yazdığım metod;
    @When("user scrolls until they see the {string}")
    public static void scrollToTheElement(String value) {
        String locator = JSONReader.getValuesFromJson(value);
        //System.out.println(locator);
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(" + locator + ")"
        ));

        System.out.println("Sayfa " +value+ " elementine kadar kaydırıldı");
    }


    @Then("verify user is on the {string}")
    public void verifyUserIsOn(String value) {
        String elementLocator = JSONReader.getValuesFromJson(value);

        WebElement element = null;

        assert elementLocator != null;
        if (elementLocator.startsWith("//")) {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.xpath(elementLocator)));
            System.out.println("user is on the " + value + " page");
        }

        else if (elementLocator.startsWith("new")) {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.androidUIAutomator(elementLocator)));
            System.out.println("user is on the " + value + " page");
        }
        else {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(elementLocator)));
            System.out.println("user is on the " + value + " page");
        }

        //assert element != null;
        if (!element.isDisplayed()) {
            throw new AssertionError("Expected page element is not displayed: " + value);
        }
    }


    @When("verify total question number is equal to {int}")
    public void verifyTotalQuestionNumberIsEqualTo(int totalNumberOfQuestions) throws InterruptedException {

        int previousCount = 0;

        //eğer soru sayısını dinamik olarak almak isterseniz aşağıdaki gibi yapabilirsiniz. Ancak bunun için senaryoda değişiklik yapmanız gerekir.
        //ilk olarak ürünün altındaki "Ürün Soru & Cevapları" alanına tıklatmadan önce soru sayısını aşağıdaki gibi dinamik olarak almalısınız(yani bunun için
        // ayrı bir test adımı ve metod gerekir). Daha sonra tıklama işlemini yaptırıp(ikinci test adımı) soru sayısnı bu metoddaki gibi saymmasını ve değerlerin
        // birbiriyle eşleşip eşleşmediğini(üçüncü test adımı) doğrulamalısınız.
        //String elementLocator = JSONReader.getValuesFromJson(expectedQuestionCount);
        //String count = driver.findElement(AppiumBy.androidUIAutomator(elementLocator)).getAttribute("text");
        //int totalNumberOfQuestions = Integer.parseInt(count.replaceAll("[^0-9]", ""));
        //System.out.println("count = " + totalNumberOfQuestions);

        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.TextView[@resource-id=\"trendyol.com:id/textViewQuestion\"]")));

        while (true) {
            // Listedeki mevcut soruları bul
            List<WebElement> questionItems = driver.findElements(AppiumBy.xpath("//android.widget.TextView[@resource-id=\"trendyol.com:id/textViewQuestion\"]"));

            // soru başlıklarını ekle
            for (WebElement questionItem : questionItems) {
                uniqueQuestionTitles.add(questionItem.getAttribute("text"));
                //System.out.println(questionItem.getAttribute("text"));
            }

            if (uniqueQuestionTitles.size() == previousCount) {// Eğer yeni kitap bulunmuyorsa (son sayfadaysanız), döngüden çık
                break;
            }

            // Önceki kitap sayısını güncelle
            System.out.println(previousCount);
            System.out.println(uniqueQuestionTitles.size());
            previousCount = uniqueQuestionTitles.size();

            reusableMethods.userScrollsDownThePage("45");
        }

        // Toplam benzersiz kitap sayısını yazdır
        System.out.println("Toplam benzersiz kitap sayısı: " + uniqueQuestionTitles.size());
        Assert.assertEquals(totalNumberOfQuestions,uniqueQuestionTitles.size());

    }

}
