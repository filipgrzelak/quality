package bdd.test.pawel;

import bdd.test.BaseBDDTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class PawelBDDTest extends BaseBDDTest {

    public static final String URL_CATEGORIES_ADD = "https://practicesoftwaretesting.com/#/admin/categories/add";
    public static final String URL_CATEGORIES = "https://practicesoftwaretesting.com/#/admin/categories";

    @Given("I am logged in as an admin")
    public void i_am_logged_in_as_an_admin() {
        loginAsAdmin();
    }

    @When("I navigate to the add new category page")
    public void i_navigate_to_the_add_new_category_page() {
        driver.get(URL_CATEGORIES_ADD);
    }

    @When("I enter category details and save")
    public void i_enter_category_details_and_save() {
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement slugInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("slug")));
        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary")));

        nameInput.sendKeys("Test Category");
        slugInput.sendKeys("test-category");

        saveButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
    }

    @Then("The new category should be visible in the categories list")
    public void the_new_category_should_be_visible_in_the_categories_list() {
        driver.get(URL_CATEGORIES);

        By tableCol1 = By.xpath("//td[text()='Test Category']");
        By tableCol2 = By.xpath("//td[text()='test-category']");

        WebElement row1 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol1));
        WebElement row2 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol2));

        assertTrue(row1.isDisplayed(), "Expected row1 found");
        assertTrue(row2.isDisplayed(), "Expected row2 found");
        driver.close();
    }
}
