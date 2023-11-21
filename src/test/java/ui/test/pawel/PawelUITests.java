package ui.test.pawel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ui.test.BaseUITest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class PawelUITests extends BaseUITest {


    public static final String URL_CATEGORIES_ADD = "https://practicesoftwaretesting.com/#/admin/categories/add";
    public static final String URL_CATEGORIES = "https://practicesoftwaretesting.com/#/admin/categories";
    public static final String URL_PRODUCT = "https://practicesoftwaretesting.com/#/product/01HFM1H6ZJP3FVZEDMDNHEZ539";
    public static final String URL_CHECKOUT = "https://practicesoftwaretesting.com/#/checkout";
    public static final String URL_ORDERS = "https://practicesoftwaretesting.com/#/admin/orders";
    public static final String URL_CONTACT = "https://practicesoftwaretesting.com/#/contact";
    public static final String URL_MESSAGES = "https://practicesoftwaretesting.com/#/admin/messages";

    @Test
    public void addNewCategoryUITest() {

        loginAsAdmin();

        driver.get(URL_CATEGORIES_ADD);

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement slugInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("slug")));
        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary")));

        nameInput.sendKeys("Test Category");
        slugInput.sendKeys("test-category");

        saveButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));

        driver.get(URL_CATEGORIES);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//tr")));

        By tableCol1 = By.xpath("//td[text()='Test Category']");
        By tableCol2 = By.xpath("//td[text()='test-category']");

        WebElement row1 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol1));
        WebElement row2 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol2));

        assertTrue(row1.isDisplayed(), "Expected row1 found");
        assertTrue(row2.isDisplayed(), "Expected row2 found");
    }

    @Test
    public void addNewOrderUITest() {

        loginAsAdmin();

        driver.get(URL_PRODUCT);

        WebElement addToCartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-add-to-cart")));

        addToCartButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-body")));

        driver.get(URL_CHECKOUT);

        WebElement proceedToCheckoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='proceed-1']")));

        proceedToCheckoutButton.click();

        WebElement proceedToCheckoutButton2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='proceed-2']")));

        proceedToCheckoutButton2.click();

        WebElement addressInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));
        WebElement cityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city")));
        WebElement stateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("state")));
        WebElement countryInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country")));
        WebElement postCodeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postcode")));
        WebElement proceedToCheckoutButton3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='proceed-3']")));

        addressInput.clear();
        addressInput.sendKeys("Test Address");
        cityInput.clear();
        cityInput.sendKeys("Test City");
        stateInput.clear();
        stateInput.sendKeys("Test State");
        countryInput.clear();
        countryInput.sendKeys("Test Country");
        postCodeInput.clear();
        postCodeInput.sendKeys("1122AB");

        proceedToCheckoutButton3.click();

        WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("payment-method")));
        WebElement accountNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("account-name")));
        WebElement accountNumberInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("account-number")));
        WebElement confirmButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='finish']")));

        Select select = new Select(selectElement);
        select.selectByVisibleText("Credit Card");

        accountNameInput.sendKeys("Test Account");
        accountNumberInput.sendKeys("09876543ABC");

        confirmButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("help-block")));

        confirmButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("order-confirmation")));

        driver.get(URL_ORDERS);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tr")));

        By tableCol1 = By.xpath("//td[text()='Test Address']");
        By tableCol2 = By.xpath("//td[text()='$14.15']");

        WebElement row1 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol1));
        WebElement row2 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol2));

        assertTrue(row1.isDisplayed(), "Expected row1 found");
        assertTrue(row2.isDisplayed(), "Expected row2 found");
    }

    @Test
    public void editOrderStatusUITest() {

        loginAsAdmin();

        driver.get(URL_ORDERS);

        WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tr[2]")));
        List<WebElement> childElements = firstRow.findElements(By.xpath("./*"));

        if (childElements.get(3).getText().equals("COMPLETED")) {
            throw new RuntimeException("Order status is COMPLETED");
        }

        String savedStatus = childElements.get(3).getText();

        childElements.get(5).findElement(By.tagName("a")).click();

        WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='order-status']")));
        WebElement updateStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='update-status-submit']")));

        Select select = new Select(selectElement);
        select.selectByVisibleText("COMPLETED");
        updateStatus.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));

        driver.get(URL_ORDERS);

        WebElement firstRow2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tr[2]")));
        List<WebElement> childElements2 = firstRow2.findElements(By.xpath("./*"));

        assertEquals(childElements2.get(3).getText(), savedStatus);
    }

    @Test
    public void sendContactMessageUITest() {

        final String message = "Test message Test message Test message Test message Test message";

        loginAsAdmin();

        driver.get(URL_CONTACT);

        WebElement selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='subject']")));
        WebElement textArea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='message']")));
        WebElement sendButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='contact-submit']")));

        Select select = new Select(selectElement);
        select.selectByVisibleText("Customer service");

        textArea.sendKeys(message);
        sendButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));

        driver.get(URL_MESSAGES);

        WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tr[2]")));
        List<WebElement> childElements = firstRow.findElements(By.xpath("./*"));

        childElements.get(4).findElement(By.tagName("a")).click();

        WebElement messageText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("card-text")));

        wait.until(ExpectedConditions.textToBePresentInElement(messageText, message));

        assertEquals(message, messageText.getText());
    }
}