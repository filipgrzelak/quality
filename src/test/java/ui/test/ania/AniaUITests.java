package ui.test.ania;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ui.test.BaseUITest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AniaUITests extends BaseUITest {

    public static final String URL_PRODUCTS_ADD = "https://practicesoftwaretesting.com/#/admin/products/add";
    public static final String URL_PRODUCTS = "https://practicesoftwaretesting.com/#/admin/products";
    public static final String URL_BRANDS_ADD = "https://practicesoftwaretesting.com/#/admin/brands/add";
    public static final String URL_BRANDS = "https://practicesoftwaretesting.com/#/admin/brands";
    public static final String URL_STATISTICS = "https://practicesoftwaretesting.com/#/admin/reports/statistics";
    public static final String URL_HOME_QUERY = "https://practicesoftwaretesting.com/#/";

    @Test
    void addNewProductUITest() {
        loginAsAdmin();

        driver.get(URL_PRODUCTS_ADD);

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement descriptionInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("description")));
        WebElement stockInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("stock")));
        WebElement priceInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("price")));
        WebElement locationOfferInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("is_location_offer")));
        WebElement itemForRentInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("is_rental")));
        WebElement brandInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("brand_id")));
        WebElement categoryInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("category_id")));
        WebElement imageInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("product_image_id")));

        Select selectBrand = new Select(brandInput);
        Select selectCategory = new Select(categoryInput);
        Select selectImage = new Select(imageInput);

        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary")));

        nameInput.sendKeys("Test Product");
        descriptionInput.sendKeys("Great test product");
        stockInput.sendKeys("1");
        priceInput.sendKeys("123");
        locationOfferInput.click();
        itemForRentInput.click();
        selectBrand.selectByVisibleText("Brand name 1");
        selectCategory.selectByVisibleText("Hand Tools");
        selectImage.selectByValue("01HFW6ET93N9BW7PF1B161PBVN");

        saveButton.click();

        WebElement alertElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-danger")));

        String classAttribute = alertElement.getAttribute("class");
        assertTrue(classAttribute.contains("alert-danger"), "The element does not have the 'alert-danger' class");
    }

    @Test
    void addNewBrandUITest() {
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

        List<WebElement> brandsRows = driver.findElements(By.xpath("//table[@class='table table-hover']//tbody//tr"));

        if (!brandsRows.isEmpty()) {
            WebElement lastBrand = brandsRows.get(brandsRows.size() - 1);

            WebElement deleteButton = lastBrand.findElement(By.xpath(".//button[@class='btn btn-sm btn-danger']"));
            deleteButton.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-body")));
        } else {
            System.out.println("No rows found in the table.");
        }
    }

    @Test
    void checkBestSellingCategoryUITest() {
        loginAsAdmin();

        driver.get(URL_STATISTICS);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("table")));

        List<WebElement> allTables = driver.findElements(By.className("table"));
        WebElement bestSellingCategoriesTable = allTables.get(0);

        WebElement topRow = wait.until(ExpectedConditions.visibilityOf(bestSellingCategoriesTable.findElement(By.xpath("//tr[1]"))));
        WebElement topCategoryName = wait.until(ExpectedConditions.visibilityOf(topRow.findElement(By.xpath("//td[1]"))));
        WebElement topTotalEarned = wait.until(ExpectedConditions.visibilityOf(topRow.findElement(By.xpath("//td[2]"))));

        assertEquals("Drill", topCategoryName.getText());
        assertEquals("$119.24", topTotalEarned.getText());
    }

    @Test
    void checkSearchQueryFilteringUITest() {
        driver.get(URL_HOME_QUERY);

        WebElement queryInput = wait.until((ExpectedConditions.visibilityOfElementLocated(By.className("form-control"))));
        WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Search']")));

        queryInput.sendKeys("Bolt Cutters");
        searchButton.click();

        WebElement filterResultProductName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h5[text()=' Bolt Cutters ']")));

        assertTrue(filterResultProductName.isDisplayed(), "Expected filtered product found");
    }
}
