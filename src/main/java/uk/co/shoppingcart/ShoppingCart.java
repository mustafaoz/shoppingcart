package uk.co.shoppingcart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShoppingCart {

    private static final double TAX_COEFFICIENT = 1.125;

    List<ShoppingCartItem> itemList;

    public ShoppingCart() {
        itemList = new ArrayList<ShoppingCartItem>();
    }

    public void addItem(ShoppingCartItem inputItem) throws ShoppingCartException {

        Product productToBeAdded =
                Optional.ofNullable(
                        Optional.ofNullable(inputItem).orElseThrow( () -> new ShoppingCartException("Null item can not be added.")).getProduct()
                ).orElseThrow(() -> new ShoppingCartException("Null product can not be added."));

        if ( productToBeAdded.getId() <= 0 ) {
            throw new ShoppingCartException("A product with invalid id can not be added.");
        }
        else if (productToBeAdded.getName() == null || productToBeAdded.getName().equals("")) {
            throw new ShoppingCartException("A product with no name can not be added.");
        }
        else if (productToBeAdded.getPrice() == null) {
            throw new ShoppingCartException("A product with no price can not be added.");
        }

        // Make sure whether the product is present in the cart already.
        ShoppingCartItem itemFound = itemList.stream().filter(x -> x.getProduct().equals(inputItem.getProduct())).findFirst().orElseGet( () -> null );
        if (itemFound != null) {
            // Make sure the same products have the same prices.
            if( !itemFound.getProduct().getPrice().equals(inputItem.getProduct().getPrice()) ) {
                throw new ShoppingCartException("Price can not be different for the same products.");
            }
            // If it's safe to add, update the quantity of the product already in the cart.
            itemFound.setQuantity(itemFound.getQuantity() + inputItem.getQuantity());
        }
        else {
            // If the same product has not been added before, add it to the cart.
            itemList.add(inputItem);
        }
    }

    public String calculateTotalPrice() {
        return BigDecimal.valueOf(
                itemList.stream()
                        .map(x -> x.getProduct().getPrice().doubleValue() * x.getQuantity())
                        .reduce((x, y) -> x.doubleValue() + y.doubleValue())
                        .orElse(0.00)
        ).setScale(2, RoundingMode.HALF_UP).toString();
    }

    public String calculateTotalPriceIncludingTax() {
        return BigDecimal.valueOf(Double.valueOf(calculateTotalPrice()) * TAX_COEFFICIENT)
                .setScale(2, RoundingMode.HALF_UP)
                .toString();
    }

    public int countItems() {
        return itemList.size();
    }

    public List<ShoppingCartItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ShoppingCartItem> itemList) {
        this.itemList = itemList;
    }
}
