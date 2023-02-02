package se.na.shoedatabase.interfaces;

import se.na.shoedatabase.model.Orders;

@FunctionalInterface
public interface OrderSearchInterface {

    boolean search(Orders o, String answer);
}
