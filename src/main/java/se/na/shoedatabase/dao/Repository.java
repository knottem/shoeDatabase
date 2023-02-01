package se.na.shoedatabase.dao;

import se.na.shoedatabase.model.Admin;
import se.na.shoedatabase.model.shoe.Category;
import se.na.shoedatabase.model.customer.Customer;
import se.na.shoedatabase.model.Orders;
import se.na.shoedatabase.model.shoe.Shoe;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

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
            rs.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        try(Connection con = connect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement(sql2)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                int id = rs.getInt("shoeid");
                String name = rs.getString("name");
                shoes.stream().filter(f -> f.getId() == id).forEach(s -> s.getCategories().add(new Category(name)));
            }
            rs.close();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        return shoes;
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
                    ordersArrayList.add(new Orders(test, timestamp, customer));
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
    public ArrayList<Orders> getOrders(int orderid, Customer customer) {
        ArrayList<Orders> orders = new ArrayList<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        orders.add(new Orders(orderid, timestamp, customer));
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
            stmt.setInt(2, customer.getId());
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
        ordersArrayList.sort(Comparator.comparingInt(Orders::getId));
        return ordersArrayList;
    }
    public Admin getAdmin(String name, String pass){
        Admin admin = null;
        String sql = "select id, name, password from admin where name=? and password=?";
        try (Connection connection = connect.getConnectionDB();
             PreparedStatement preparedStt = connection.prepareStatement(sql)){
            preparedStt.setString(1, name);
            preparedStt.setString(2, pass);
            ResultSet rs = preparedStt.executeQuery();
            while(rs.next()) {
               admin = new Admin(rs.getInt("id"), rs.getString("name"), rs.getString("password"));
            }
            connection.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }
    public Customer insertNewCustomer(String firstname, String lastname, Long ssn, String pass,
                                      String address, int gatuAddress, int postnummer, String postort) {
        int id = 0;
        try(Connection connection = connect.getConnectionDB();
            PreparedStatement ps = connection.prepareStatement("select * from customer where ssn=?")) {
            ps.setLong(1, ssn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Customer med personummer: " + ssn + " finns redan.");
            } else {
                try (PreparedStatement ps4 = connection.prepareStatement("select id from address where streetname=? and streetnumber=? and zipcode=? and city=?")) {
                    ps4.setString(1, address);
                    ps4.setInt(2, gatuAddress);
                    ps4.setInt(3, postnummer);
                    ps4.setString(4, postort);
                    ResultSet rs4 = ps4.executeQuery();
                    if (rs4.next()) {
                        id = rs4.getInt("id");
                    } else {
                        rs.close();
                        PreparedStatement ps2 = connection.prepareStatement("insert into address (streetname, streetnumber, zipcode, city) VALUES (?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);
                        ps2.setString(1, address);
                        ps2.setInt(2, gatuAddress);
                        ps2.setInt(3, postnummer);
                        ps2.setString(4, postort);
                        ps2.execute();
                        ResultSet rs2 = ps2.getGeneratedKeys();
                        while (rs2.next()) {
                            id = rs2.getInt(1);
                        }
                        ps2.close();
                        rs2.close();
                    }
                    rs4.close();

                    PreparedStatement ps3 = connection.prepareStatement("insert into customer (firstname, lastname, ssn, addressId, pass) values (?,?,?,?,?)");
                    ps3.setString(1, firstname);
                    ps3.setString(2, lastname);
                    ps3.setLong(3, ssn);
                    ps3.setInt(4, id);
                    ps3.setString(5, pass);
                    ps3.execute();

                    ps3.close();
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        return getCustomer(ssn, pass);
    }
}
