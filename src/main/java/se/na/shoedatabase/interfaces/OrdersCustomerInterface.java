package se.na.shoedatabase.interfaces;

import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.customer.Customer;

import java.util.ArrayList;

public interface OrdersCustomerInterface {

    double total(ArrayList<Orders> orders, Customer c);
}
