package ui.test.filip;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ui.test.BaseUITest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilipUITests extends BaseUITest {

    public static final String URL_BRANDS_ADD = "https://practicesoftwaretesting.com/#/admin/brands/add";
    public static final String URL_BRANDS = "https://practicesoftwaretesting.com/#/admin/brands";

    @Test
    public void addNewBrandUITest() {

        loginAsAdmin();

        driver.get(URL_BRANDS_ADD);

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement slugInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("slug")));
        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary")));

        nameInput.sendKeys("Test Brand");
        slugInput.sendKeys("test-brand");

        saveButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));

        driver.get(URL_BRANDS);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//tr")));

        By tableCol1 = By.xpath("//td[text()='Test Brand']");
        By tableCol2 = By.xpath("//td[text()='test-brand']");

        WebElement row1 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol1));
        WebElement row2 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol2));

        assertTrue(row1.isDisplayed(), "Expected row1 found");
        assertTrue(row2.isDisplayed(), "Expected row2 found");

        deleteBrand("Test Brand", "test-brand");
    }

    @Test
    public void deleteExistingBrandUITest() {

        loginAsAdmin();

        addBrand();

        driver.get(URL_BRANDS);

        By rowsMatchingConditions = By.xpath("//tr[.//td[text()='test-brand'] and .//td[text()='Test Brand']]");

        WebElement selectedRow = wait.until(ExpectedConditions.visibilityOfElementLocated(rowsMatchingConditions));

        List<WebElement> childElements = selectedRow.findElements(By.xpath("./*"));

        childElements.get(3).findElement(By.className("btn-danger")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-body")));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//tr")));

        List<WebElement> matchingRows = driver.findElements(rowsMatchingConditions);

        assertEquals(0, matchingRows.size());
    }

    @Test
    public void editExistingBrand() {
        loginAsAdmin();

        addBrand();

        driver.get(URL_BRANDS);

        By rowsMatchingConditions = By.xpath("//tr[.//td[text()='test-brand'] and .//td[text()='Test Brand']]");

        WebElement selectedRow = wait.until(ExpectedConditions.visibilityOfElementLocated(rowsMatchingConditions));

        List<WebElement> childElements = selectedRow.findElements(By.xpath("./*"));

        childElements.get(3).findElement(By.className("btn-primary")).click();

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement slugInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("slug")));
        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary")));

        Awaitility.with().pollDelay(1000, TimeUnit.MILLISECONDS).await().until(() -> true);
        nameInput.clear();
        nameInput.sendKeys("Updated Brand");

        Awaitility.with().pollDelay(1000, TimeUnit.MILLISECONDS).await().until(() -> true);
        slugInput.clear();
        slugInput.sendKeys("updated-brand");

        Awaitility.with().pollDelay(1000, TimeUnit.MILLISECONDS).await().until(() -> true);
        saveButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));

        driver.get(URL_BRANDS);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//tr")));

        By updatedRowsMatchingConditions = By.xpath("//tr[.//td[text()='updated-brand'] and .//td[text()='Updated Brand']]");
        Awaitility.with().pollDelay(1000, TimeUnit.MILLISECONDS).await().until(() -> true);
        List<WebElement> updatedMatchingRows = driver.findElements(updatedRowsMatchingConditions);

        assertEquals(1, updatedMatchingRows.size());

        deleteBrand("Updated Brand", "updated-brand");
    }

    @Test
    public void searchExistingBrand() {
        loginAsAdmin();

        addBrand();

        driver.get(URL_BRANDS);
        Awaitility.with().pollDelay(1000, TimeUnit.MILLISECONDS).await().until(() -> true);

        WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table")));

        List<WebElement> rows = tableElement.findElements(By.xpath(".//tr[position() > 0]"));
        assertThat(rows.size() - 1, greaterThan(1));

        WebElement searchInput = driver.findElements(By.cssSelector("input")).get(0);
        WebElement searchButton = driver.findElements(By.cssSelector("button")).get(1);

        Awaitility.with().pollDelay(1000, TimeUnit.MILLISECONDS).await().until(() -> true);
        searchInput.sendKeys("Test Brand");
        searchButton.click();

        Awaitility.with().pollDelay(1000, TimeUnit.MILLISECONDS).await().until(() -> true);

        WebElement searchedTableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table")));

        List<WebElement> searchedRows = searchedTableElement.findElements(By.xpath(".//tr[position() > 0]"));
        List<WebElement> columns = searchedRows.get(1).findElements(By.xpath("./*"));

        assertEquals(1, searchedRows.size() - 1);
        assertEquals("Test Brand", columns.get(1).getText());
        assertEquals("test-brand", columns.get(2).getText());

        deleteBrand("Test Brand", "test-brand");
    }

    private void deleteBrand(String name, String slug) {

        driver.get(URL_BRANDS);

        By rowsMatchingConditions = By.xpath("//tr[.//td[text()='" + name + "'] and .//td[text()='" + slug + "']]");

        WebElement selectedRow = wait.until(ExpectedConditions.visibilityOfElementLocated(rowsMatchingConditions));

        List<WebElement> childElements = selectedRow.findElements(By.xpath("./*"));

        childElements.get(3).findElement(By.className("btn-danger")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-body")));
    }

    private void addBrand() {
        driver.get(URL_BRANDS_ADD);

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement slugInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("slug")));
        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary")));

        nameInput.sendKeys("Test Brand");
        slugInput.sendKeys("test-brand");

        saveButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
    }
}
