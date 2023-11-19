package ui.test.pawel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;



public class PawelUITests {

    public static final String WEBDRIVER_LOCATION = "src/test/resources/chromedriver.exe";
    WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", WEBDRIVER_LOCATION);
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void simpleTest() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("https://practicesoftwaretesting.com/#/auth/login");

        WebElement emailInput = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        WebElement passwordInput = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        WebElement loginButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("btnSubmit")));

        emailInput.sendKeys("admin@practicesoftwaretesting.com");
        passwordInput.sendKeys("welcome01");

        loginButton.click();

        wait.until(ExpectedConditions.urlContains("#/admin/dashboard"));

        driver.get("https://practicesoftwaretesting.com/#/admin/categories/add");

        WebElement nameInput = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement slugInput = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("slug")));
        WebElement saveButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary")));

        nameInput.sendKeys("Test Category");
        slugInput.sendKeys("test-category");

        saveButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));

        driver.get("https://practicesoftwaretesting.com/#/admin/categories");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//tr")));

        By tableCol1 = By.xpath("//td[text()='Test Category']");
        By tableCol2 = By.xpath("//td[text()='test-category']");
        WebElement row1 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol1));
        WebElement row2 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol2));
        assertTrue(row1.isDisplayed(), "Expected row1 found");
        assertTrue(row2.isDisplayed(), "Expected row2 found");
    }
}