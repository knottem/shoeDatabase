package se.na.shoedatabase.dao;

import se.na.shoedatabase.model.shoe.Category;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.shoe.Shoe;
import se.na.shoedatabase.utility.Connect;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Repository {

    ArrayList<Shoe> shoes = new ArrayList<>();
    Connect connect = new Connect();

    public Customer getCustomer(Long answer, String pass) {
        Customer customer = null;
        String sql = "select customer.id, firstname, lastname, ssn, pass, streetname, streetnumber, zipcode, city from customer \n" +
                "inner join address on address.id = addressId where ssn=? and pass=?";
        try (Connection connection = connect.getConnectionDB();
             PreparedStatement preparedStt = connection.prepareStatement(sql)){
            preparedStt.setLong(1, answer);
            preparedStt.setString(2, pass);
            ResultSet rs = preparedStt.executeQuery();
            while(rs.next()) {
                customer = new Customer(rs.getInt("id"),
                                        rs.getString("firstname"),
                                        rs.getString("lastname"),
                                        rs.getLong("ssn"),
                                        rs.getString("pass"),
                                        rs.getString("streetname"),
                                        rs.getInt("streetnumber"),
                                        rs.getString("city"),
                                        rs.getInt("zipcode"));
            }
            connection.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(customer != null) {
            return customer;
        } else {
            return null;
        }
    }

    public int addOrder(int orderid, int customerid, int shoeid){
        ResultSet rs;
        String errormessage = "";
        String sql = "call AddToCart(?,?,?,?)";
        int neworderid = 0;
        try(Connection con = connect.getConnectionDB();
            CallableStatement stmt = con.prepareCall(sql)){
            if(orderid == 0) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, orderid);
            }
            stmt.setInt(2, customerid);
            stmt.setInt(3, shoeid);
            stmt.registerOutParameter(4,Types.INTEGER);
            rs = stmt.executeQuery();
            neworderid = stmt.getInt(4);

            while (rs != null && rs.next()) {
                errormessage = rs.getString("debug");
            }
            if(!errormessage.equals("")){
                System.out.println(errormessage);
            }
            assert rs != null;
            rs.close();

        } catch (SQLException e){
            System.out.println(e.getMessage() + "(" + e.getErrorCode()+ ")");
        } catch (Exception e){
            e.printStackTrace();
        }
        return neworderid;
    }

    public ArrayList<Shoe> getAllShoes(){
        String sql = """
                select shoe.id, price, shoebrand.name, shoecolor.colorswe, shoesize.euSize, shoe.quantity from shoe
                inner join shoebrand on brandid = shoebrand.id
                inner join shoecolor on colorid = shoecolor.id
                inner join shoesize on sizeid = shoesize.id
                """;
        String sql2 = """
                select category.name, categorymap.shoeid from category
                inner join categorymap on category.id = categorymap.categoryid
                inner join shoe on shoe.id = categorymap.shoeid
                """;
        shoes.clear();
        try(Connection con = connect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                shoes.add(new Shoe(
                        rs.getInt("id"),
                        rs.getInt("price"),
                        rs.getString("name"),
                        rs.getString("colorswe"),
                        rs.getInt("eusize"),
                        rs.getInt("quantity")));
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        try(Connection con = connect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement(sql2)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                for (Shoe shoe : shoes) {
                    if (rs.getInt("shoeid") == shoe.getId()) {
                        shoe.getCategories().add(new Category(rs.getString("name")));
                    }
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        return shoes;
    }

    public ArrayList<Orders> getOrders(int orderid, int customerid) {
        ArrayList<Orders> orders = new ArrayList<>();
        orders.add(new Orders(orderid));
        String sql = """
                select shoe.id, shoebrand.name, shoecolor.colorswe, shoesize.euSize, ordersmap.quantity, shoe.price from orders
                inner join ordersmap on orders.id = ordersmap.orderid
                inner join shoe on shoe.id = ordersmap.shoeid
                inner join shoebrand on brandid = shoebrand.id
                inner join shoecolor on colorid = shoecolor.id
                inner join shoesize on sizeid = shoesize.id
                where orders.Id = ? and orders.customerId = ?""";
        try (Connection con = connect.getConnectionDB();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, orderid);
            stmt.setInt(2, customerid);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                for (Orders order : orders) {
                    if (order.getId() == orderid) {
                        order.getShoes().add(new Shoe(
                                rs.getInt("id"),
                                rs.getInt("price"),
                                rs.getString("name"),
                                rs.getString("colorswe"),
                                rs.getInt("eusize"),
                                rs.getInt("quantity")));
                    }
                }

            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }
}
