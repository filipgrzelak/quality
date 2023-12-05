package bdd.test.filip;

import bdd.test.BaseBDDTest;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilipBDDTest extends BaseBDDTest {

    public static final String URL_BRANDS = "https://practicesoftwaretesting.com/#/admin/brands";
    public static final String URL_BRANDS_ADD = "https://practicesoftwaretesting.com/#/admin/brands/add";

    @Given("I am logged in as an admin")
    public void loginAsAdminState(){
        loginAsAdmin();
    }

    @And("Add example brand to testing deleting brand")
    public void addExampleBrand() {
        driver.get(URL_BRANDS_ADD);

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
        WebElement slugInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("slug")));
        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary")));

        nameInput.sendKeys("Test Brand");
        slugInput.sendKeys("test-brand");

        saveButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
    }

    @When("Delete existing test brand")
    public void deleteExistingBrandUITest() {

        driver.get(URL_BRANDS);

        By rowsMatchingConditions = By.xpath("//tr[.//td[text()='test-brand'] and .//td[text()='Test Brand']]");

        WebElement selectedRow = wait.until(ExpectedConditions.visibilityOfElementLocated(rowsMatchingConditions));

        List<WebElement> childElements = selectedRow.findElements(By.xpath("./*"));

        childElements.get(3).findElement(By.className("btn-danger")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-body")));
    }

    @Then("Should not find deleted object")
    public void checkingIfDeletedObjectIsNotVisible() {

        By rowsMatchingConditions = By.xpath("//tr[.//td[text()='test-brand'] and .//td[text()='Test Brand']]");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table//tr")));

        List<WebElement> matchingRows = driver.findElements(rowsMatchingConditions);

        assertEquals(0, matchingRows.size());

        driver.close();
    }


}
