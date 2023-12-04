package bdd.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseBDDTest {
    public static final String WEBDRIVER_LOCATION = "src/test/resources/chromedriver.exe";
    public static final String URL_LOGIN = "https://practicesoftwaretesting.com/#/auth/login";
    public static final String ADMIN_EMAIL = "admin@practicesoftwaretesting.com";
    public static final String ADMIN_PASSWORD = "welcome01";
    public WebDriver driver;
    public WebDriverWait wait;

    public BaseBDDTest() {
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", WEBDRIVER_LOCATION);
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 10);
    }

    public void loginAsAdmin() {
        driver.get(URL_LOGIN);

        WebElement emailInput = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        WebElement passwordInput = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        WebElement loginButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("btnSubmit")));

        emailInput.sendKeys(ADMIN_EMAIL);
        passwordInput.sendKeys(ADMIN_PASSWORD);

        loginButton.click();

        wait.until(ExpectedConditions.urlContains("#/admin/dashboard"));
    }
}
