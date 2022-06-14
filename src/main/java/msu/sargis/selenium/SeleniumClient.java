package msu.sargis.selenium;

import msu.sargis.captcha.CaptchaSolver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumClient {
    private ChromeDriver chromeDriver;
    private final WebDriverWait driverWait;
    private final CaptchaSolver captchaSolver;

    public SeleniumClient() {
        this.captchaSolver = CaptchaSolver.getInstance();
        init();
        this.driverWait = new WebDriverWait(chromeDriver, 30);
    }

    private void init(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        //options.addArguments("--headless");
        options.addArguments("disable-infobars");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.setAcceptInsecureCerts(true);
        //options.addArguments("--window-size=1280,800");
        options.setCapability("--ignore-certificate-errors", true);
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--disable-setuid-sandbox");
        options.addArguments("--mute-audio");
        chromeDriver = new ChromeDriver(options);
        chromeDriver.get("https://service2.diplo.de/rktermin/extern/appointment_showMonth.do?locationCode=mosk&realmId=875&categoryId=2428");
    }

    public boolean thereIsVisa() throws InterruptedException {
        chromeDriver.navigate().refresh();
        WebElement body = driverWait.until(webDriver -> webDriver.findElement(By.tagName("body")));
        String text = body.getText();
        if (text.contains("Введите код с картинки") || text.contains("Текст введен неверно")){
            recaptcha();
            return false;
        }
        System.out.println("text = " + text);
        return !text.contains("В настоящее время, к сожалению, свободных мест нет. " +
                "Обновление количества доступных для бронирования мест " +
                "производится регулярно.");
    }

    private void recaptcha() throws InterruptedException {
        WebElement captcha = chromeDriver.findElement(By.tagName("captcha"));
        WebElement div = captcha.findElement(By.tagName("div"));
        String style = div.getAttribute("style");
        int urlFirstIndex = style.indexOf("data:");
        int urlLastIndex = style.indexOf("\")");
        String base64URL = style.substring(urlFirstIndex, urlLastIndex);
        System.out.println("base64URL = " + base64URL);
        String result = captchaSolver.solveCaptcha(base64URL);
        System.out.println("result = " + result);
        WebElement captchaResultField = driverWait.until(chromeDriver->
                chromeDriver.findElement(By.xpath("//*[@id=\"appointment_captcha_month_captchaText\"]")));
        captchaResultField.sendKeys(result);
        captchaResultField.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
    }

    public void quit(){
        chromeDriver.quit();
    }
}
