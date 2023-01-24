package se.na.shoedatabase.dao;

import se.na.shoedatabase.model.Customer;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.Shoe;
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
             PreparedStatement preparedStt = connection.prepareStatement(sql);){
            preparedStt.setLong(1, answer);
            preparedStt.setString(2, pass);
            ResultSet rs = preparedStt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                Long ssn = rs.getLong("ssn");
                String password = rs.getString("pass");
                String address = rs.getString("streetname");
                int adressNumber = rs.getInt("streetnumber");
                int zipcode = rs.getInt("zipcode");
                String city = rs.getString("city");
                customer = new Customer(id, firstname, lastname, ssn, password, address, adressNumber, city, zipcode);
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
            
          /*  if(rs != null) {
                while (rs.next()) {
                    errormessage = rs.getString("debug");
                }
            }
            if(!errormessage.equals("")){
                return errormessage;
            }

           */

        } catch (SQLException e){
            System.out.println(e.getMessage() + "(" + e.getErrorCode()+ ")");
        } catch (Exception e){
            e.printStackTrace();
        }
        return neworderid;
    }

    public ArrayList<Shoe> getAllShoes(){
        String sql = "select shoe.id, price, shoebrand.name, shoecolor.colorswe, shoesize.euSize, shoe.quantity from shoe\n" +
                "inner join shoebrand on brandid = shoebrand.id\n" +
                "inner join shoecolor on colorid = shoecolor.id\n" +
                "inner join shoesize on sizeid = shoesize.id\n" +
                "where quantity > 0;";
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return shoes;
    }

    public ArrayList<Orders> getOrders(int orderid, int customerid) {
        ArrayList<Orders> orders = new ArrayList<>();
        orders.add(new Orders(orderid));
        String sql = "select shoe.id, shoebrand.name, shoecolor.colorswe, shoesize.euSize, ordersmap.quantity, shoe.price from orders\n"+
        "inner join ordersmap on orders.id = ordersmap.orderid\n" +
        "inner join shoe on shoe.id = ordersmap.shoeid\n" +
        "inner join shoebrand on brandid = shoebrand.id\n" +
        "inner join shoecolor on colorid = shoecolor.id\n" +
        "inner join shoesize on sizeid = shoesize.id\n" +
        "where orders.Id = ? and orders.customerId = ?";
        try (Connection con = connect.getConnectionDB();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, orderid);
            stmt.setInt(2, customerid);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                for (int i = 0; i < orders.size(); i++) {
                    if(orders.get(i).getId() == orderid){
                        orders.get(i).getShoes().add(new Shoe(
                                rs.getInt("id"),
                                rs.getInt("price"),
                                rs.getString("name"),
                                rs.getString("colorswe"),
                                rs.getInt("eusize"),
                                rs.getInt("quantity")));
                    }
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }
}
