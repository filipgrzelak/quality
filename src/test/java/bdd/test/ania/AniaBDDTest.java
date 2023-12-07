package bdd.test.ania;

import bdd.test.BaseBDDTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AniaBDDTest extends BaseBDDTest {

    public static final String URL_USERS_ADD = "https://practicesoftwaretesting.com/#/admin/users/add";
    public static final String URL_USERS = "https://practicesoftwaretesting.com/#/admin/users";

    @Given("I am logged in as an admin to access users page")
    public void i_am_logged_in_as_admin_to_access_users_page() {
        loginAsAdmin();
    }

    @When("I navigate to the add new user page")
    public void i_navigate_to_the_add_new_user_page() {
        driver.get(URL_USERS_ADD);
    }

    @When("I enter user details and save")
    public void i_enter_user_details_and_save() {
        WebElement firstNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstname")));
        WebElement lastNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lastname")));
        WebElement dateOfBirthInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dob")));
        WebElement addressInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));
        WebElement postcodeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postcode")));
        WebElement cityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city")));
        WebElement stateInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("state")));
        WebElement countryInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country")));
        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phone")));
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        WebElement failedLoginAttemptsInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("failed_login_attempts")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

        Select selectCountry = new Select(countryInput);

        WebElement saveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary")));

        firstNameInput.sendKeys("Test Name");
        lastNameInput.sendKeys("Test Surname");
        dateOfBirthInput.sendKeys("24122003");
        addressInput.sendKeys("Test address 123");
        postcodeInput.sendKeys("12-123");
        cityInput.sendKeys("Test City");
        stateInput.sendKeys("Test State");
        selectCountry.selectByValue("FJ");
        phoneInput.sendKeys("123123123");
        emailInput.sendKeys("janedoe123@mail.com");
        failedLoginAttemptsInput.sendKeys("3");
        passwordInput.sendKeys("example123");

        saveButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
    }

    @Then("New user should be visible in the users list and delete user")
    public void new_user_should_be_visible_in_the_users_list_and_delete_user() {
        driver.get(URL_USERS);

        By tableCol1 = By.xpath("//td[text()='Test Name Test Surname']");
        By tableCol2 = By.xpath("//td[text()='janedoe123@mail.com']");

        WebElement row1 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol1));
        WebElement row2 = wait.until(ExpectedConditions.visibilityOfElementLocated(tableCol2));

        assertTrue(row1.isDisplayed(), "Expected row1 found");
        assertTrue(row2.isDisplayed(), "Expected row2 found");

        List<WebElement> userRows = driver.findElements(By.xpath("//table[@class='table table-hover']//tbody//tr"));

        if (!userRows.isEmpty()) {
            WebElement newlyAddedUser = userRows.get(userRows.size() - 1);

            WebElement deleteButton = newlyAddedUser.findElement(By.xpath(".//button[@class='btn btn-sm btn-danger']"));

            deleteButton.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("toast-body")));
        } else {
            System.out.println("No rows found in the table.");
        }

        driver.close();
    }
}
