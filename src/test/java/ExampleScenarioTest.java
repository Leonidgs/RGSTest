import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExampleScenarioTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10, 1000);

        String baseUrl = "https://www.sberbank.ru/ru/person";
        driver.get(baseUrl);
    }

    @Test
    public void exampleScenario() {

        // выбрать пункт меню - "Страхование"
        String insuranceButtonXPath = "//a[@aria-label='Страхование']";
        List<WebElement> insuranceButtonList = driver.findElements(By.xpath(insuranceButtonXPath));
        if (!insuranceButtonList.isEmpty()){
            insuranceButtonList.get(0).click();
        }

        // выбрать пункт подменю - "Перейти в каталог"
        String sberInsuranceButtonXPath = "//a[text()='Перейти в каталог' and contains(@class, 'link_second')]";
        WebElement travellersInsuranceButton = driver.findElement(By.xpath(sberInsuranceButtonXPath));
        travellersInsuranceButton.click();

        // проверка открытия страницы "Страхование"
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Оформить страховку в СберБанке — СберБанк", driver.getTitle());

        // перейти к опции "Страхование путешественников"
        String travellersInsuranceHeaderXPath = "//h3[text()='Страхование для путешественников']";
        WebElement travellersInsuranceHeader = driver.findElement(By.xpath(travellersInsuranceHeaderXPath));
        scrollToElementJs(travellersInsuranceHeader);

        // нажать кнопку "Оформить онлайн"
        String checkoutOnlineXPath = "../../following-sibling::div//b[text()='Оформить онлайн']/..";
        WebElement checkoutOnlineButton = travellersInsuranceHeader.findElement(By.xpath(checkoutOnlineXPath));
        waitUtilElementToBeClickable(checkoutOnlineButton);
        checkoutOnlineButton.click();

        // проверка открытия страницы "Страхование путешественников"
        String pageTitleXPath = "//h2";
        waitUtilElementToBeVisible(By.xpath(pageTitleXPath));
        WebElement pageTitle = driver.findElement(By.xpath(pageTitleXPath));
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Страхование путешественников", pageTitle.getText());

        // выбрать тариф страхования "Минимальный"
        String insuranceCoverageAmountXPath = "//h3[text()='Минимальная']";
        WebElement insuranceCoverageAmount = driver.findElement(By.xpath(insuranceCoverageAmountXPath));
        scrollToElementJs(insuranceCoverageAmount);
        waitUtilElementToBeClickable(insuranceCoverageAmount);
        insuranceCoverageAmount.click();

        // кликнуть по кнопке "Оформить"
        String checkoutButtonXPath = "//button[text()='Оформить']";
        WebElement checkoutButton = driver.findElement(By.xpath(checkoutButtonXPath));
        scrollToElementJs(checkoutButton);
        waitUtilElementToBeClickable(checkoutButton);
        checkoutButton.click();

        // заполнить поля данными
        String fieldXPath = "//input[@id='%s']";
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "surname_vzr_ins_0"))), "Застрахованный");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "name_vzr_ins_0"))), "Степан");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "birthDate_vzr_ins_0"))), "01.01.1990");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "person_lastName"))), "Страхователь");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "person_firstName"))), "Иван");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "person_middleName"))), "Иванович");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "person_birthDate"))), "02.02.1992");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "passportSeries"))), "4444");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "passportNumber"))), "777666");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "documentDate"))), "09.09.2009");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "documentIssue"))), "Паспортный стол");

        // кликнуть по кнопке "Продолжить"
        String continueButtonXPath = "//button[contains(text(),'Продолжить')]";
        WebElement continueButton = driver.findElement(By.xpath(continueButtonXPath));
        scrollToElementJs(continueButton);
        waitUtilElementToBeClickable(continueButton);
        continueButton.click();

        // проверить сообщение об ошибке
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(fieldXPath, "phone"))), "Поле не заполнено.");
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(fieldXPath, "email"))), "Поле не заполнено.");
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(fieldXPath, "repeatEmail"))), "Поле не заполнено.");

        // проверить сообщение об ошибке
        String errorAlertXPath = "//div[@class='alert-form alert-form-error']";
        WebElement errorAlert = driver.findElement(By.xpath(errorAlertXPath));
        scrollToElementJs(errorAlert);
        waitUtilElementToBeVisible(errorAlert);
        Assert.assertEquals("Проверка ошибки у alert на странице не была пройдено",
                "При заполнении данных произошла ошибка", errorAlert.getText());
    }

    @After
    public void after(){
        driver.quit();
    }

    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void waitUtilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private void fillInputField(WebElement element, String value) {
        scrollToElementJs(element);
        waitUtilElementToBeClickable(element);
        element.click();
        element.sendKeys(value);
        Assert.assertEquals("Поле было заполнено некорректно",
                value, element.getAttribute("value"));
    }

    private void checkErrorMessageAtField(WebElement element, String errorMessage) {
        element = element.findElement(By.xpath("./..//span"));
        Assert.assertEquals("Проверка ошибки у поля не была пройдена",
                errorMessage, element.getText());
    }
}