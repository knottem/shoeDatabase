package se.na.shoedatabase.dao;

import se.na.shoedatabase.model.shoe.Category;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.shoe.Shoe;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Repository {

    ArrayList<Shoe> shoes = new ArrayList<>();
    Connect connect = new Connect();

    private static Repository repository;

    public static Repository getRepository() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }


    public Customer getCustomer(Long answer, String pass) {
        Customer customer = null;
        String sql = """
                select customer.id, firstname, lastname, ssn, pass, streetname, streetnumber, zipcode, city from customer
                inner join address on address.id = addressId
                where ssn=? and pass=?""";
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
        return customer;
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

    public ArrayList<Integer> getOrderNumbers(Customer customer){
        ArrayList<Integer> test = new ArrayList<>();
        String sql = """
                select orders.id, orders.created, shoeid, quantity from orders
                inner join ordersmap o on orders.id = o.orderId
                where orders.customerId=?
                """;
        try(Connection con = connect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, customer.getId());
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                test.add(rs.getInt("id"));
                test.add(rs.getInt("shoeid"));
                test.add(rs.getInt("quantity"));
            }


        } catch (SQLException | IOException e){
            e.printStackTrace();
        }
        return test;
    }
    public ArrayList<Orders> getOrdersForCustomer(Customer customer, ArrayList<Shoe> shoeArrayList){
        ArrayList<Integer> temp = new ArrayList<>();
        ArrayList<Orders> ordersArrayList = new ArrayList<>();
        String sql = """
                select ordersmap.orderid, ordersmap.shoeId, ordersmap.quantity, ordersmap.created from orders
                inner join ordersmap on orders.id = ordersmap.orderid
                where customerId = ?
                """;
        try(Connection con = connect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, customer.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int test = rs.getInt("orderid");
                Timestamp timestamp = rs.getTimestamp("created");
                temp.add(test);
                if(ordersArrayList.stream().noneMatch(s -> s.getId() == test)) {
                    ordersArrayList.add(new Orders(test, timestamp));
                }
                temp.add(rs.getInt("shoeid"));
                temp.add(rs.getInt("quantity"));
            }
            ordersArrayList.forEach(s -> {
                for (int i = 0; i < temp.size(); i+=3) {
                    if(s.getId() == temp.get(i)){
                        for (Shoe shoe : shoeArrayList) {
                            if (shoe.getId() == temp.get(i + 1)) {
                                s.getShoes().add(shoe);
                                for (int j = 0; j < s.getShoes().size(); j++) {
                                    if(s.getShoes().get(j).getId() == temp.get(i+1)){
                                        s.getShoes().get(j).setQuantity(temp.get(i+2));
                                    }
                                }
                            }
                        }
                        s.setCustomer(customer);
                    }
                }
            });


        } catch (SQLException | IOException e){
            e.printStackTrace();
        }
        return ordersArrayList;
    }
    public ArrayList<Orders> getOrders(int orderid, int customerid) {
        ArrayList<Orders> orders = new ArrayList<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        orders.add(new Orders(orderid, timestamp));
        String sql = """
                select shoe.id, shoebrand.name, shoecolor.colorswe, shoesize.euSize, ordersmap.quantity, shoe.price from orders
                inner join ordersmap on orders.id = ordersmap.orderid
                inner join shoe on shoe.id = ordersmap.shoeid
                inner join shoebrand on brandid = shoebrand.id
                inner join shoecolor on colorid = shoecolor.id
                inner join shoesize on sizeid = shoesize.id
                where orders.Id = ? and orders.customerId = ?""";
        String sql2 = """
                select category.name, categorymap.shoeid from category
                inner join categorymap on category.id = categorymap.categoryid
                inner join shoe on shoe.id = categorymap.shoeid
                """;
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
        try (Connection con = connect.getConnectionDB();
             PreparedStatement stmt = con.prepareStatement(sql2)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                for (Orders order : orders) {
                    if (order.getId() == orderid){
                        for (int i = 0; i < order.getShoes().size(); i++) {
                            if (rs.getInt("shoeid") == order.getShoes().get(i).getId()) {
                                order.getShoes().get(i).getCategories().add(new Category(rs.getString("name")));
                            }
                        }
                    }
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = """
                select customer.id, firstname, lastname, ssn, pass, streetname, streetnumber, zipcode, city from customer
                inner join address on address.id = addressId""";
        try (Connection connection = connect.getConnectionDB();
             PreparedStatement preparedStt = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStt.executeQuery();
            while (rs.next()) {
                 customers.add(new Customer(rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getLong("ssn"),
                        rs.getString("pass"),
                        rs.getString("streetname"),
                        rs.getInt("streetnumber"),
                        rs.getString("city"),
                        rs.getInt("zipcode")));
            }
            connection.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    public ArrayList<Orders> getAllOrders(ArrayList<Shoe> shoeArrayList, ArrayList<Customer> customers) {
        ArrayList<Orders> ordersArrayList = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();
        String sql = """
                select ordersmap.orderid, ordersmap.shoeId, ordersmap.quantity, orders.customerId, ordersmap.created from orders
                inner join ordersmap on orders.id = ordersmap.orderid
                """;
        try (Connection con = connect.getConnectionDB();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int test = rs.getInt("orderid");
                temp.add(test);
                Timestamp timestamp = rs.getTimestamp("created");
                if(ordersArrayList.stream().noneMatch(s -> s.getId() == test)) {
                    ordersArrayList.add(new Orders(test, timestamp));
                }
                temp.add(rs.getInt("shoeid"));
                temp.add(rs.getInt("quantity"));
                temp.add(rs.getInt("customerid"));

            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        ordersArrayList.forEach(s -> {
            for (int i = 0; i < temp.size(); i+=4) {
                if(s.getId() == temp.get(i)){
                    for (Shoe shoe : shoeArrayList) {
                        if (shoe.getId() == temp.get(i + 1)) {
                            s.getShoes().add(shoe);
                            for (int j = 0; j < s.getShoes().size(); j++) {
                                if(s.getShoes().get(j).getId() == temp.get(i+1)){
                                    s.getShoes().get(j).setQuantity(temp.get(i+2));
                                }
                            }
                        }
                    }
                    for (Customer customer : customers) {
                        if (customer.getId() == temp.get(i + 3)) {
                            s.setCustomer(customer);
                        }
                    }
                }
            }
        });
        ordersArrayList.sort((o1, o2) -> (int) o1.getId() - o2.getId());
        return ordersArrayList;
    }

}
