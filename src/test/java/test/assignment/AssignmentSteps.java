package test.assignment;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AssignmentSteps {

    WebDriver driver;
    List<WebElement> products;
    WebDriverWait wait;
    String productName;
    String searchTermText;


    @Given("I am a user of the website")
    public void iAmAUserOfTheWebsite() {
        //Creates a chrome instance to run the tests
        driver = new ChromeDriver();
        driver.manage().window().maximize(); //maximises the window
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30)); //
        driver.get("https://intercomp.com.mt/");
    }

    @When("I visit the website")
    public void iVisitTheWebsite() {
        String title = driver.getTitle();
        Assertions.assertEquals("Dell, iPhone, Samsung, Huawei, Philips, Brother, DJI | Intercomp Malta", title);
    }

    //Category Related

    @And("I click on the {string} category")
    public void iClickOnTheCategory(String categoryName) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); //class to wait a maximum of 10 seconds, otherwise the test will fail
        String spanOfLinkButtonXPath = "//span[@class='cat-name' and contains(text(), '" + categoryName + "')]";
        WebElement spanOfLinkButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(spanOfLinkButtonXPath))); //finds the corresponding <span> element through the cat-name class
        WebElement parentAnchor = spanOfLinkButton.findElement(By.xpath("./parent::a")); //finds the clickable <a> class parent of the span cat-name
        parentAnchor.click();
    }

    @Then("I should be taken to {string} category")
    //Confirms whether the title of the page is the same as the expected title based on the category
    public void iShouldBeTakenToCategory(String categoryName) {
        String expectedTitle = categoryName + " - Intercomp Malta";
        String actualTitle = driver.getTitle();
        Assertions.assertEquals(expectedTitle, actualTitle);
    }

    @And("the category should show at least 10 products")
    //Every product for sale on the page is under the "product" class, so the test case simply counts whether the amount of elements with the "product" class name is greater than 10
    public void theCategoryShouldShowAtLeast10Products() {
        products = driver.findElements(By.className("product"));
        System.out.println(products.size() + " Products Found");
        Assertions.assertTrue(products.size() > 9);
    }

    @When("I click on the first product in the results")
    //Gets the name of the first product of the results before clicking on it
    public void iClickTheFirstProductInTheResults() {
        WebElement firstProduct = products.getFirst();
        productName = firstProduct.findElement(By.className("woocommerce-loop-product__title")).getText(); //Gets the name of the first product on the results
        WebElement firstProductAnchor = firstProduct.findElement(By.className("woocommerce-loop-product__link")); //Finds the clickable <a> tag of the product based on product name
        firstProductAnchor.click();
    }

    @Then("I should be taken to the details page for that product")
    //Checks whether the name of the product clicked on is the same as the product title the page is currently on
    public void iShouldBeTakenToTheDetailsPageForThatProduct() {
        String productTitleText = driver.findElement(By.className("product_title")).getText();
        Assertions.assertEquals(productTitleText, productName);
    }

    //Search Term Related

    @And("I search for a product using the term {string}")
    public void iSearchForAProductUsingTheTerm(String searchTerm) {
        searchTermText = searchTerm;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String searchInputXPath = "//input[@aria-label='Search input']";
        String searchButtonXPath = "//button[@aria-label='Search magnifier button']";
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(searchInputXPath)));
        searchInput.sendKeys(searchTerm);
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(searchButtonXPath)));
        searchButton.click();
    }

    @Then("I should see the search results")
    //Checks whether the search title matches the search terms inputted
    public void iShouldSeeTheSearchResults() {
        String expectedTitle = "You searched for " + searchTermText + " - Intercomp Malta";
        String actualTitle = driver.getTitle();
        Assertions.assertEquals(expectedTitle, actualTitle);
    }


    @And("there should be at least 5 products in the search results")
    public void thereShouldBeAtLeastProductsInTheSearchResults() {
        products = driver.findElements(By.className("product"));
        System.out.println(products.size() + " Products Found");
        Assertions.assertTrue(products.size() > 4);
    }


    @After
    public void closeBrowser() {
        driver.close();
        driver.quit();
    }
}