package model;

//import Exception.ExceedMaxCapacityException;
import Account.Account;
import Account.AccountStatus;
import Exception.ExceedMaxCapacityException;
import Exception.NEIAException;
import dbaccess.Stock;
import java.util.ArrayList;

//import dbaccess.Stock;
public class BoBaShop {

    private String name;
//    private CustomerAccount[] customers;
    private ArrayList<CustomerAccount> customers;
    private StaffAccount staff;
    private Stock stock;

    public BoBaShop(String name, StaffAccount staff, int max) {
        this.name = name;
        this.stock = new Stock(max);
        this.customers = new ArrayList<>();
        this.staff = staff;
    }

    /////////////////////////////////////
    //////////// CUSTOMER ///////////////
    /////////////////////////////////////
    public void topUp(CustomerAccount ca, int amount) {
        for (CustomerAccount customer : customers) {
            if (customer.equals(ca)) {
                customer.topupAccMoney(amount);
            }
        }
    }

    public void order(Account ca, int id, int amount) {
        for (CustomerAccount customer : customers) {
            if (customer.equals(ca)) {
                if (stock.getProductById(id) != null) {
                    customer.addToCart(new OrderedProduct(id, amount, stock.getProductById(id)));
                }
            }
        }
    }
    
    public void makePayment(Account ca){
        for (CustomerAccount customer : customers) {
            if(customer.equals(ca)){
                customer.makePayment();
            }
        }
    }

    /////////////////////////////////////
    ////////////// STAFF ////////////////
    /////////////////////////////////////
    public void addNewProduct(Product p) {
        stock.insertProduct(p);
    }

    public void removeProductFromStock(int id) {
        stock.removeProduct(id);
    }

    public void restock(int id, int amount) {
        try {
            stock.restock(id, amount);
        } catch (ExceedMaxCapacityException ex) {
            System.out.println("Can't restock because: " + ex.getMessage());
        }
    }
    
    public void blacklistCustomer(Account ca){
        for (CustomerAccount customer : customers) {
            if(customer.equals(ca)){
                ca.setStatus(AccountStatus.BLACKLISTED);
            }
        }
    }

    ////////////////////////////////////////
    ////////////// GENERAL /////////////////
    ////////////////////////////////////////
    
    public void addCustomer(CustomerAccount ca) {
        customers.add(ca);
    }

    public boolean checkUsername(String username) {
        for (Account customer : customers) {
            if (customer.getId().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean isStockFull() {
        return stock.isFull();
    }

    private boolean auth(StaffAccount staff) {
        return this.staff.equals(staff);
    }

    public Product getProductFromStock(int id) {
        return stock.getProductById(id);
    }

    public GeneralList<OrderedProduct> getMenu() {
        return stock.showAll();
    }

    public void updateStock(int id, int amount) {
        try {
            stock.update(id, amount);
        } catch (NEIAException ex) {
            System.out.println(ex);
        }
    }
}
