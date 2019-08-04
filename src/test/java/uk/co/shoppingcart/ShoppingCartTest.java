package uk.co.shoppingcart;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

public class ShoppingCartTest {

    ShoppingCart shoppingCart;

    @Before public void initialize() {
        shoppingCart= new ShoppingCart();
    }

    @Test
    public void countItems_EmptyShoppingCard_ZeroCount() {
        assertEquals(0, shoppingCart.countItems());
    }

    @Test
    public void addItem_TwoItemAdded_TwoItemsCounted() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.99)), 4));
        shoppingCart.addItem(new ShoppingCartItem(new Product(2, "Axe Deo", new BigDecimal(19.99)), 4));
        assertEquals(2, shoppingCart.countItems());
    }

    @Test(expected = ShoppingCartException.class)
    public void addItem_NullShoppingCartItem_ShoppingCartException() throws ShoppingCartException {
        shoppingCart.addItem(null);
    }

    @Test(expected = ShoppingCartException.class)
    public void addItem_ItemWithoutAProduct_ShoppingCartException() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(null, 4));
    }

    @Test(expected = ShoppingCartException.class)
    public void addItem_ProductWithInvalidId_ShoppingCartException() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(0, "Name", new BigDecimal(0.00)), 4));
    }

    @Test(expected = ShoppingCartException.class)
    public void addItem_ProductWithNoName_ShoppingCartException() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "", new BigDecimal(0.00)), 4));
    }

    @Test(expected = ShoppingCartException.class)
    public void addItem_ProductWithNullName_ShoppingCartException() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, null, new BigDecimal(0.00)), 4));
    }

    @Test(expected = ShoppingCartException.class)
    public void addItem_ProductWithNullPrice_ShoppingCartException() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Name", null), 4));
    }

    @Test
    public void calculateTotalPrice_EmptyShoppingCart_ZeroPrice() {
        assertEquals("0.00", shoppingCart.calculateTotalPrice());
    }

    @Test
    public void calculateTotalPrice_PriceNeededToBeRoundedUp_AccuratePrice() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.9550)), 1));
        assertEquals("39.96", shoppingCart.calculateTotalPrice());
    }

    @Test
    public void calculateTotalPrice_PriceNeededToBeRoundedDown_AccuratePrice() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.6440)), 1));
        assertEquals("39.64", shoppingCart.calculateTotalPrice());
    }

    // STEP 1 ACCEPTANCE CRITERIA
    @Test
    public void calculateTotalPrice_NonEmptyShoppingCart_AccuratePrice() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.99)), 5));
        assertEquals("199.95", shoppingCart.calculateTotalPrice());
    }

    @Test
    public void calculateTotalPrice_SameProductAddedTwice_ShouldCountOne() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.99)), 5));
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.99)), 3));
        assertEquals(1, shoppingCart.countItems());
    }

    @Test(expected = ShoppingCartException.class)
    public void calculateTotalPrice_SameProductAddedWithDifferentPrice_ShoppingCartException() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.99)), 5));
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(9.99)), 3));
    }

    // STEP 2 ACCEPTANCE CRITERIA
    @Test
    public void calculateTotalPrice_SameProductAddedTwice_AccuratePrice() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.99)), 5));
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.99)), 3));
        assertEquals("319.92", shoppingCart.calculateTotalPrice());
    }

    // STEP 3 ACCEPTANCE CRITERIA
    @Test
    public void calculateTotalPrice_TwoProductsAdded_AccuratePriceIncludingTax() throws ShoppingCartException {
        shoppingCart.addItem(new ShoppingCartItem(new Product(1, "Dove Soap", new BigDecimal(39.99)), 2));
        shoppingCart.addItem(new ShoppingCartItem(new Product(2, "Axe Deos", new BigDecimal(99.99)), 2));
        assertEquals("314.96", shoppingCart.calculateTotalPriceIncludingTax());
    }
}
