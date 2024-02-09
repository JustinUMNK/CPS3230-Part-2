package test.assignment;

import nz.ac.waikato.modeljunit.*;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserModelTest implements FsmModel {
    //Linking the SUT
    private UserModelAdapter systemUnderTest = new UserModelAdapter();

    //State Variables
    private UserModelStates userPageState = UserModelStates.HOME;

    private Integer cartItems = 0;

    public UserModelStates getState() {
        return userPageState;
    }

    public void reset(final boolean var1) {
        systemUnderTest = new UserModelAdapter();
        systemUnderTest.openDriver();
        userPageState = UserModelStates.HOME;
        cartItems = 0;
    }

    public @Action void goToHomePage() {
        systemUnderTest.goToHomePage();
        userPageState = UserModelStates.HOME;

        assertEquals("The SUT state should be in the homepage", UserModelStates.HOME, getState());
    }

    public boolean goToHomePageGuard() {
        return !getState().equals(UserModelStates.HOME) && !getState().equals(UserModelStates.CHECKOUT);
    }

    public @Action void goToSearchResults() {
        systemUnderTest.goToSearchResults("laptop");
        userPageState = UserModelStates.SEARCH_RESULTS;

        assertEquals(   "The SUT state should be in the search results page", UserModelStates.SEARCH_RESULTS, getState());
    }

    public boolean goToSearchResultsGuard() {
        return !getState().equals(UserModelStates.SEARCH_RESULTS) && !getState().equals(UserModelStates.CHECKOUT);
    }

    public @Action void goToCategoryResults() {
        systemUnderTest.goToCategoryResults("Laptops");
        userPageState = UserModelStates.CATEGORY_RESULTS;

        assertEquals("The SUT state should be in the category results page", UserModelStates.CATEGORY_RESULTS, getState());
    }

    public boolean goToCategoryResultsGuard() {
        return !getState().equals(UserModelStates.CATEGORY_RESULTS) && !getState().equals(UserModelStates.CHECKOUT) && !getState().equals(UserModelStates.CART);
    }

    public @Action void goToProduct() {
        systemUnderTest.goToProduct();
        userPageState = UserModelStates.PRODUCT;

        assertEquals("The SUT state should be in the product page", UserModelStates.PRODUCT, getState());
    }

    public boolean goToProductGuard() {
        return getState().equals(UserModelStates.CATEGORY_RESULTS) || getState().equals(UserModelStates.SEARCH_RESULTS);
    }

    public @Action void addProductToCart() {
        Integer originalCartItems = cartItems;
        systemUnderTest.addProductToCart();
        cartItems = cartItems + 1;

        assertNotEquals("The current cart item count should differ from the original cart item count (taken before updating the state)", originalCartItems, cartItems);
    }

    public boolean addProductToCartGuard() {
        return getState().equals(UserModelStates.PRODUCT);
    }

    public @Action void goToCart() {
        systemUnderTest.goToCart();
        userPageState = UserModelStates.CART;

        assertEquals("The SUT state should be in the cart page", UserModelStates.CART, getState());
    }

    public boolean goToCartGuard() {
        return cartItems > 0 && !getState().equals(UserModelStates.CHECKOUT)  && !getState().equals(UserModelStates.CART);
    }

    public @Action void goToCheckout() {
        systemUnderTest.goToCheckout();
        userPageState = UserModelStates.CHECKOUT;

        assertEquals("The SUT state should be in the checkout page", UserModelStates.CHECKOUT, getState());
    }

    public boolean goToCheckoutGuard() {
        return getState().equals(UserModelStates.CART);
    }

    @Test
    public void UserModelTestRunner() {
        UserModelTest model = new UserModelTest();
        //GreedyTester tester = new GreedyTester(model);
        //LookaheadTester tester = new LookaheadTester(model);
        //RandomTester tester = new RandomTester(model);
        AllRoundTester tester = new AllRoundTester(model);
        tester.setRandom(new Random());
        //tester.buildGraph();
        tester.addListener(new VerboseListener());
        tester.addListener(new StopOnFailureListener());
        tester.addCoverageMetric(new TransitionCoverage());
        tester.addCoverageMetric(new StateCoverage());
        tester.addCoverageMetric(new ActionCoverage());
        tester.generate(30);
        tester.printCoverage();
    }
}
