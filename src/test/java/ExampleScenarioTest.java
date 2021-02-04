import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExampleScenarioTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void before() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");


        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        String baseUrl = "https://www.rgs.ru/";
        driver.get(baseUrl);

        new WebDriverWait(driver, 1).until(
                webDriver -> ((JavascriptExecutor) webDriver).
                        executeScript("return document.readyState").equals("complete"));
        wait = new WebDriverWait(driver, 10, 1000);
    }

    @Test
    public void exampleScenario() {

       //Стартуем
        String insuranceButtonXPath = "//a[contains(text(), 'Меню') and @class='hidden-xs']";
        WebElement element = driver.findElement(By.xpath(insuranceButtonXPath));
        //waitUtilElementToBeClickable(element);
        element.click();

       //Выбираем жмём кнопку Компаниям
        String sberInsuranceButtonXPath = "//a[@href='https://www.rgs.ru/products/juristic_person/index.wbp']";
        WebElement travellersInsuranceButton = driver.findElement(By.xpath(sberInsuranceButtonXPath));
        travellersInsuranceButton.click();

        //Проверяем что зашли на страницу компаниям
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Корпоративное страхование для компаний | Росгосстрах", driver.getTitle());

        //Переходим к здоровью
        String checkoutOnlineXPath = "//a[contains(text(), 'Здоровье') and @href='/products/juristic_person/health/index.wbp']";
        wait = new WebDriverWait(driver, 10);
        WebElement element1 =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkoutOnlineXPath)));
        element1.click();

        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "ДМС для сотрудников - добровольное медицинское страхование от Росгосстраха", driver.getTitle());

        //Нажимаем добровольное мед страхование
        String w = "//*[@href='/products/juristic_person/health/dms/index.wbp' and @class='list-group-item adv-analytics-navigation-line4-link']";
        WebElement element2 =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(w)));
        element2.click();

        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Добровольное медицинское страхование в Росгосстрахе", driver.getTitle());

        //открываем форму
        String ss = "//a[contains(text(), 'Отправить заявку') and @href='#']";
        WebElement element3 =
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ss)));
        element3.click();

        //Поле Фамилия
        String xpathlastName = "//*[@name='LastName']";
        WebElement checkoutOnlineButton123 = driver.findElement(By.xpath(xpathlastName));
        checkoutOnlineButton123.sendKeys("Имя");


        //Поле имя
        String xpathFirstName = "//*[@name='FirstName']";
        WebElement checkoutOnlineButton1234 = driver.findElement(By.xpath(xpathFirstName));
        checkoutOnlineButton1234.sendKeys("Фамилия");


        //Поле Отчество
        String xpathMiddleName = "//*[@name='MiddleName']";
        WebElement checkoutOnlineButton4 = driver.findElement(By.xpath(xpathMiddleName));
        checkoutOnlineButton4.sendKeys("Отчество");


        //Выбор региона
        String moscow = "//*[@name='Region']";
        WebElement checkoutOnlineButton8 = driver.findElement(By.xpath(moscow));
        checkoutOnlineButton8.sendKeys("М" + "\n");


        //Поле телефон
        String number = "//*[contains(@data-bind,'Phone')]";
        WebElement checkoutOnlineButton5 = driver.findElement(By.xpath(number));
        checkoutOnlineButton5.click();

        checkoutOnlineButton5.sendKeys("7777777777" + "\n");

        //Заполняем почту
        String forEmail = "//*[@name='Email']";
        WebElement checkoutOnlineButton7 = driver.findElement(By.xpath(forEmail));
        checkoutOnlineButton7.sendKeys("qwertyyyyyyyyyyyyyyy");

        //Выбираем дату
        String numberes = "//*[@name='ContactDate']";
        WebElement checkoutOnlineButton6 = driver.findElement(By.xpath(numberes));
        checkoutOnlineButton6.sendKeys("12122020 " + "\n");


        //Соглашаемся с условиями
        String agree = "//*[@type='checkbox']";
        WebElement galochka = driver.findElement(By.xpath(agree));
        galochka.click();

        //Отправляем заполненную форму
        String end = "//button[@id='button-m']";
        WebElement sendForm = driver.findElement(By.xpath(end));
        sendForm.click();

        //Проверяем что почта заполнена не верно
        String neee = "//*[contains(text(), 'Введите адрес электронной почты')]";
        checkErrorMessageAtField(driver.findElement(By.xpath(neee)), "Введите адрес электронной почты");
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