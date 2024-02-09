package test.assignment;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.*;

public class UserModelAdapter {

    WebDriver driver;
    WebDriverWait wait;

    public UserModelAdapter() {

    }

    public void openDriver() {
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.get("https://intercomp.com.mt/");
    }

    public void closeDriver() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    public void goToHomePage() {
        WebElement homePageLink = driver.findElement(By.className("header-logo-link"));
        homePageLink.click();
    }

    public void goToSearchResults(String searchTerm) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String searchInputXPath = "//input[@aria-label='Search input']";
        String searchButtonXPath = "//button[@aria-label='Search magnifier button']";
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(searchInputXPath)));
        searchInput.clear();
        searchInput.sendKeys(searchTerm);
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(searchButtonXPath)));
        searchButton.click();
    }

    public void goToCategoryResults(String categoryName) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String spanOfLinkButtonXPath = "//span[@class='cat-name' and contains(text(), '" + categoryName + "')]";
        WebElement spanOfLinkButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(spanOfLinkButtonXPath)));
        WebElement parentAnchor = spanOfLinkButton.findElement(By.xpath("./parent::a"));
        parentAnchor.click();
    }

    public void goToProduct() {
        List<WebElement> products = driver.findElements(By.className("product"));
        WebElement firstProduct = products.getFirst();
        WebElement firstProductAnchor = firstProduct.findElement(By.className("woocommerce-loop-product__link"));
        firstProductAnchor.click();
    }

    //
    public void addProductToCart() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //The add to cart button was being intercepted by the accept cookies button. If the accept cookie button is found, it is clicked on and then the product is added to cart. If not, the add to cart button is pushed.
        List<WebElement> cookieNotice = driver.findElements(By.id("cn-accept-cookie")); //array of all elements which have the id cn-accept-cookie
        if (!cookieNotice.isEmpty()) { //checks if the array is empty i.e. the element doesn't appear
            WebElement cookieNoticeElement = cookieNotice.getFirst();
            if (cookieNoticeElement.isDisplayed() && cookieNoticeElement.isEnabled()) { //only click on the button if it is displayed and enabled
                cookieNoticeElement.click();
            }
        }
        WebElement addToCartButton = driver.findElement(By.xpath("//button[@name='add-to-cart']"));
        addToCartButton.click();
    }

    public void goToCart() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement miniCartButton = driver.findElement(By.id("minicart"));
        miniCartButton.click();
        WebElement goToCartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'View Cart')]"))); //waits for the view cart button to become clickable
        goToCartButton.click();
    }

    public void goToCheckout() {
        //The page has 2 checkout buttons which both use the class name wc-proceed-to-checkout. To distinguish between the one in the minicart and the one on the cart page, the specific checkout button which is a child of the element using the cart-collateral class is located.
        //In order to click the proceed to checkout button, the parent of the <a> tag had to be clicked on instead of the actual <a> tag.
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement cartTotals = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart-collaterals")));
        WebElement proceedToCheckoutButton = cartTotals.findElement(By.className("wc-proceed-to-checkout"));
        wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckoutButton));
        proceedToCheckoutButton.click();
        WebElement proceedToLavaMtButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Proceed to Lava.mt')]")));
        proceedToLavaMtButton.click();
    }
}


