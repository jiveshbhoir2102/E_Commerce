package masstech_Java_Project;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ECommerceApplication 
{
	Connection con ;
	Scanner sc ;
	
	public ECommerceApplication() throws Exception
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommercedb","root", "");
		sc = new Scanner(System.in);
	}
	
	
	public boolean isAdmin(String username, String password) 
	{
	    return "victus".equalsIgnoreCase(username) && "root".equals(password);
	}

	public void authenticateUser(String username, String password) throws SQLException
	{
	    String sql = "Call userlogin('"+username+"', '"+password+"')";
	    PreparedStatement ps = con.prepareStatement(sql);

	    ResultSet rs = ps.executeQuery();
	    if (rs.next()) 
	    {
	        System.out.println(rs.getString("message"));
	    }
	}
		
	public void printAvailableProducts() throws SQLException 
	{
	    String sql = "Call getProductList()";

	    try (PreparedStatement stmt = con.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        System.out.println("\n📦 Available Products:");
	        System.out.println("--------------------------------------------------");
	        System.out.printf("%-5s %-20s %-10s %-10s\n", "ID", "Name", "Price", "Qty");
	        System.out.println("--------------------------------------------------");

	        boolean found = false;
	        while (rs.next()) {
	            found = true;
	            int id = rs.getInt("pid");
	            String name = rs.getString("pname");
	            double price = rs.getDouble("price");
	            int qty = rs.getInt("qty");

	            System.out.printf("%-5d %-20s %-10.2f %-10d\n", id, name, price, qty);
	        }

	        if (!found) {
	            System.out.println("No products available in stock.");
	        }
	    }
	}
	
	public void addProduct() throws SQLException {
	    System.out.print("Enter product name: ");
	    String name = sc.nextLine();

	    System.out.print("Enter product description: ");
	    String desc = sc.nextLine();

	    System.out.print("Enter product price: ");
	    double price = Double.parseDouble(sc.nextLine());

	    System.out.print("Enter product quantity: ");
	    int qty = Integer.parseInt(sc.nextLine());

	    String sql = "Call addProduct('"+name+"', '"+desc+"', '"+price+"', '"+qty+"')";
	    PreparedStatement stmt = con.prepareStatement(sql);

	    boolean hasResult = stmt.execute();

	    if (hasResult) 
	    {
	        ResultSet rs = stmt.getResultSet();
	        if (rs.next())
	        {
	            String message = rs.getString("message");
	            System.out.println("\n" + message);
	        }
	        else
	        {
	            System.out.println("Product added, but no confirmation message returned.");
	        }
	        rs.close();
	    } 
	    else
	    {
	        System.out.println("Procedure executed, but no result set returned.");
	    }
	}
	
	public void updateProduct() throws SQLException
	{
	    System.out.print("Enter product ID to update: ");
	    int pid = Integer.parseInt(sc.nextLine());

	    System.out.print("Enter new product name (or leave blank to keep unchanged): ");
	    String name = sc.nextLine();
	    name = name.isEmpty() ? null : name;

	    System.out.print("Enter new product description (or leave blank to keep unchanged): ");
	    String desc = sc.nextLine();
	    desc = desc.isEmpty() ? null : desc;

	    System.out.print("Enter new product price (or -1 to keep unchanged): ");
	    double price = Double.parseDouble(sc.nextLine());
	    Double finalPrice = (price < 0) ? null : price;

	    System.out.print("Enter new product quantity (or -1 to keep unchanged): ");
	    int qty = Integer.parseInt(sc.nextLine());
	    Integer finalQty = (qty < 0) ? null : qty;

	    String sql = "CALL updateProduct(?, ?, ?, ?, ?)";
	    PreparedStatement ps = con.prepareStatement(sql);

	    // Set parameters safely
	    ps.setInt(1, pid);

	    if (name == null) ps.setNull(2, java.sql.Types.VARCHAR);
	    else ps.setString(2, name);

	    if (desc == null) ps.setNull(3, java.sql.Types.VARCHAR);
	    else ps.setString(3, desc);

	    if (finalPrice == null) ps.setNull(4, java.sql.Types.DOUBLE);
	    else ps.setDouble(4, finalPrice);

	    if (finalQty == null) ps.setNull(5, java.sql.Types.INTEGER);
	    else ps.setInt(5, finalQty);

	 
	    boolean hasResult = ps.execute();
	    if (hasResult)
	    {
	        ResultSet rs = ps.getResultSet();
	        if (rs.next())
	        {
	            System.out.println("\n" + rs.getString("message"));
	        }
	    }
	}
	
	public void deleteProduct() throws SQLException
	{
	    System.out.print("Enter product ID to delete: ");
	    int pid = Integer.parseInt(sc.nextLine());

	    String sql = "Call deleteProduct('"+pid+"')";
	    PreparedStatement ps = con.prepareStatement(sql);
	  
	    boolean hasResult = ps.execute();
	    if (hasResult) 
	    {
	        ResultSet rs = ps.getResultSet();
	        if (rs.next()) 
	        {
	            System.out.println("\n" + rs.getString("message"));
	        }
	    }
	}
	
	public void registerUser() throws SQLException
	{
	    System.out.print("Enter username: ");
	    String username = sc.nextLine();

	    System.out.print("Enter email: ");
	    String email = sc.nextLine();

	    System.out.print("Enter contact number: ");
	    String contact = sc.nextLine();

	    System.out.print("Enter password: ");
	    String password = sc.nextLine();

	    System.out.print("Enter address: ");
	    String address = sc.nextLine();

	    String sql = "Call registerUser('"+username+"', '"+email+"', '"+contact+"', '"+password+"', '"+address+"')";
	    PreparedStatement ps = con.prepareStatement(sql);
	  
	    boolean hasResult = ps.execute();
	    if (hasResult) 
	    {
	        ResultSet rs = ps.getResultSet();
	        if (rs.next()) 
	        {
	            System.out.println("\n" + rs.getString("message"));
	        }
	    }
	}
	
	public void getUserList() throws SQLException 
	{
	    String sql = "{ call getUserList() }";
	    PreparedStatement ps = con.prepareStatement(sql);

	    boolean hasResult = ps.execute();
	    if (hasResult)
	    {
	        ResultSet rs = ps.getResultSet();
	        System.out.println("\n--- User List ---");
	        System.out.printf("%-5s %-15s %-25s %-15s %-30s %-10s%n",
                    "UID", "Username", "Email", "Contact", "Address", "Status");
	        System.out.println("---------------------------------------------------------------------------------------------");

	        while (rs.next())
	        {
	        	System.out.printf("%-5d %-15s %-25s %-15s %-30s %-10s%n",
                        rs.getInt("uid"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("contact"),
                        rs.getString("address"),
                        rs.getString("status"));
	        }

	    } 
	    else
	    {
	        System.out.println("No user data returned.");
	    }
	}
	
	public Integer userLogin() throws SQLException
	{
	    System.out.print("Enter username or email: ");
	    String identifier = sc.nextLine();

	    System.out.print("Enter password: ");
	    String password = sc.nextLine();

	    String sql = "call userlogin('" + identifier + "', '" + password + "')";
	    PreparedStatement ps = con.prepareStatement(sql);

	    boolean hasResult = ps.execute();
	    if (hasResult)
	    {
	        ResultSet rs = ps.getResultSet();
	        if (rs.next())
	        {
	            System.out.println("\n" + rs.getString("message"));
	            int uid = rs.getInt("userid");
	            return (uid != 0) ? uid : null; // Defensive: treat 0 as null if needed
	        }
	    }

	    return null; // Login failed or no result
	}
	
	public void viewCart(int uid) throws SQLException 
	{
	    String sql = "call viewcart('"+uid+"')";
	    PreparedStatement ps = con.prepareStatement(sql);
	  

	    ResultSet rs = ps.executeQuery();

	    System.out.println("\n🛒 Your Cart:");
	    boolean hasItems = false;

	    while (rs.next())
	    {
	        hasItems = true;
	        int pid = rs.getInt("pid");
	        String pname = rs.getString("pname");
	        double price = rs.getDouble("price");
	        int qty = rs.getInt("qty");
	        double total = rs.getDouble("totalprice");
	        Timestamp addedAt = rs.getTimestamp("added_at");

	        System.out.printf("• [%d] %s | ₹%.2f x %d = ₹%.2f | Added: %s%n",
	            pid, pname, price, qty, total, addedAt.toLocalDateTime());
	    }

	    if (!hasItems) 
	    {
	        System.out.println("🧺 Your cart is empty.");
	    }
	}
	
	public void addToCart(int uid) throws SQLException 
	{
	    System.out.print("\nEnter Product ID to add: ");
	    int pid = sc.nextInt();

	    System.out.print("Enter Quantity: ");
	    int qty = sc.nextInt();

	    String sql = "call addtocart('"+uid+"', '"+pid+"', '"+qty+"')";
	    PreparedStatement ps = con.prepareStatement(sql);
	 
	    ResultSet rs = ps.executeQuery();

	    if (rs.next()) 
	    {
	        String message = rs.getString("message");
	        System.out.println("🛒 " + message);
	    }
	}
    
	public void removeFromCart(int uid) throws SQLException
	{
	    System.out.print("Enter Product ID to remove: ");
	    int pid = sc.nextInt();

	    String sql = "call removefromcart('"+uid+"', '"+pid+"')";
	    PreparedStatement stmt = con.prepareStatement(sql);

	    boolean hasResult = stmt.execute();

	    if (hasResult)
	    {
	        ResultSet rs = stmt.getResultSet();
	        if (rs.next())
	        {
	            System.out.println(rs.getString("message"));
	        }
	    } 
	    else 
	    {
	        System.out.println("No response from procedure.");
	    }
	}
	
	public void blockUser() throws SQLException 
	{
	    System.out.print("Enter user id to block: ");
	    int uid = sc.nextInt();

	    String sql = "update user set status = 'inactive' where uid = '"+uid+"'";
	    PreparedStatement stmt = con.prepareStatement(sql);
	
	    int rows = stmt.executeUpdate();

	    if (rows > 0) 
	    {
	        System.out.println("User has been blocked.");
	    } 
	    else
	    {
	        System.out.println("Failed to block user.");
	    }
	}
	
	public void unblockUser() throws SQLException 
	{
	    System.out.print("Enter user id to unblock: ");
	    int uid = sc.nextInt();

	    String sql = "update user set status = 'active' where uid = '"+uid+"'";
	    PreparedStatement stmt = con.prepareStatement(sql);

	    int rows = stmt.executeUpdate();

	    if (rows > 0) 
	    {
	        System.out.println("User has been unblocked.");
	    } 
	    else
	    {
	        System.out.println("Failed to unblock user.");
	    }
	}
	
	public void adminMenu() throws Exception 
	{
	    boolean exit = false;
	    while (!exit) {
	        System.out.println("\n====== ADMIN DASHBOARD ======");
	        System.out.println("1. Add New Product");
	        System.out.println("2. View Product Inventory");
	        System.out.println("3. View Registered Users");
	        System.out.println("4. View Order History");
	        System.out.println("0. Logout");
	        System.out.print("Choose option: ");
	        int choice = sc.nextInt();
	        sc.nextLine(); // consume newline

	        switch (choice) {
	            case 1:
	                this.addProduct();
	                break;

	            case 2:
	                boolean productExit = false;
	                while (!productExit) {
	                    this.printAvailableProducts();
	                    System.out.println("\n--- Product Inventory Options ---");
	                    System.out.println("1. Edit Product");
	                    System.out.println("2. Delete Product");
	                    System.out.println("0. Back to Dashboard");
	                    System.out.print("Choose option: ");
	                    int productChoice = sc.nextInt();
	                    sc.nextLine();

	                    switch (productChoice) {
	                        case 1:
	                            this.updateProduct(); 
	                            break;
	                        case 2:
	                            this.deleteProduct(); 
	                            break;
	                        case 0:
	                            productExit = true;
	                            break;
	                        default:
	                            System.out.println("⚠️ Invalid choice. Try again.");
	                    }
	                }
	                break;

	            case 3:
	                boolean userExit = false;
	                while (!userExit) {
	                	
	                    this.getUserList();
	                    System.out.println("\n--- User Management Options ---");
	                    System.out.println("1. Block User");
	                    System.out.println("2. Unblock User");
	                    System.out.println("0. Back to Dashboard");
	                    System.out.print("Choose option: ");
	                    int userChoice = sc.nextInt();
	                    sc.nextLine();

	                    switch (userChoice) 
	                    {
	                        case 1:
	                            this.blockUser(); 
	                            break;
	                        case 2:
	                            this.unblockUser(); 
	                            break;
	                        case 0:
	                            userExit = true;
	                            break;
	                        default:
	                            System.out.println("⚠️ Invalid choice. Try again.");
	                    }
	                }
	                break;

	            case 4:
	                this.viewAllOrderHistory(); 
	                break;

	            case 0:
	                exit = true;
	                break;

	            default:
	                System.out.println("⚠️ Invalid choice. Please try again.");
	        }
	    }
	}
	
	public void userMenu(int uid) throws Exception
	{
	    boolean exit = false;
	    while (!exit) {
	        System.out.println("\n====== USER MENU ======");
	        System.out.println("1. Show Products");
	        System.out.println("2. View Cart");
	        System.out.println("3. View My Order History");
	        System.out.println("0. Exit");
	        System.out.print("Choose option: ");
	        int choice = sc.nextInt();
	        sc.nextLine(); // consume newline

	        switch (choice) 
	        {
	            case 1:
	                boolean productExit = false;
	                while (!productExit)
	                {
	                    this.printAvailableProducts();
	                    System.out.println("\n--- Product Options ---");
	                    System.out.println("1. Add to Cart");
	                    System.out.println("0. Back to Dashboard");
	                    System.out.print("Choose option: ");
	                    int productChoice = sc.nextInt();
	                    sc.nextLine();

	                    switch (productChoice)
	                    {
	                        case 1:
	                            this.addToCart(uid); 
	                            break;
	                        case 0:
	                            productExit = true;
	                            break;
	                        default:
	                            System.out.println("❌ Invalid choice. Try again.");
	                    }
	                }
	                break;

	            case 2:
	                boolean cartExit = false;
	                while (!cartExit) {
	                    this.viewCart(uid); 
	                    System.out.println("\n--- Cart Options ---");
	                    System.out.println("1. Place Order");
	                    System.out.println("2. Remove Item");
	                    System.out.println("0. Back to Dashboard");
	                    System.out.print("Choose option: ");
	                    int cartChoice = sc.nextInt();
	                    sc.nextLine();

	                    switch (cartChoice) 
	                    {
	                        case 1:
	                            this.makePaymentAndPlaceOrder(uid);
	                            break;
	                        case 2:
	                            this.removeFromCart(uid);
	                            break;
	                        case 0:
	                            cartExit = true;
	                            break;
	                        default:
	                            System.out.println("❌ Invalid choice. Try again.");
	                    }
	                }
	                break;

	            case 3:
	                this.viewMyOrderHistory(uid); 
	                break;

	            case 0:
	                exit = true;
	                break;

	            default:
	                System.out.println("❌ Invalid Choice");
	        }
	    }
	}
	
	public void makePaymentAndPlaceOrder(int uid) throws SQLException
	{
	    // Step 1: Get total cart value
	    PreparedStatement ps = con.prepareStatement("select sum(totalprice) from cart where uid = '"+uid+"'");
	    ResultSet rs = ps.executeQuery();
	    
	    double total = 0.0;
	    if (rs.next()) 
	    {
	        total = rs.getDouble(1);
	    }

	    if (total == 0.0)
	    {
	        System.out.println("Cart is empty. No payment required.");
	        return;
	    }

	    // Step 2: Fetch bank names
	    ps = con.prepareStatement("select bankname from bank where uid = '"+uid+"'");
	    rs = ps.executeQuery();
	    
	    List<String> bankList = new ArrayList<>();
	    System.out.println("Choose a bank for payment:");
	    int index = 1;
	    while (rs.next())
	    {
	        String bank = rs.getString("bankname");
	        bankList.add(bank);
	        System.out.println(index + ". " + bank);
	        index++;
	    }
	    
	    if (bankList.isEmpty()) {
	        System.out.println("No bank accounts found for user.");
	        createBankAccount(uid);
	        
	        // Re-fetch bank list after creation
	        ps = con.prepareStatement("select bankname from bank where uid = '"+uid+"'");
	     
	        rs = ps.executeQuery();
	        while (rs.next())
	        {
	            String bank = rs.getString("bankname");
	            bankList.add(bank);
	            System.out.println("- " + bank);
	        }
	       
	        if (bankList.isEmpty())
	        {
	            System.out.println("Bank creation failed. Cannot proceed.");
	            return;
	        }
	    }

	    // Step 3: Select bank by serial number
	    System.out.print("Enter bank number: ");
	    int choice = sc.nextInt();
	    sc.nextLine(); // consume newline

	    if (choice < 1 || choice > bankList.size())
	    {
	        System.out.println("Invalid selection.");
	        return;
	    }
	    String chosenBank = bankList.get(choice - 1);

	    // Step 4: Check balance
	    ps = con.prepareStatement("select balance from bank where uid = '"+uid+"' and bankname = '"+chosenBank+"'");
	  
	    rs = ps.executeQuery();
	    double balance = 0.0;
	    if (rs.next())
	    {
	        balance = rs.getDouble("balance");
	    }
	   
	    if (balance < total)
	    {
	        System.out.println("Insufficient balance in " + chosenBank + ". Payment failed.");
	        return;
	    }

	    // Step 5: Deduct balance
	    ps = con.prepareStatement("update bank set balance = balance - ? where uid = ? and bankname = ?");
	    ps.setDouble(1, total);
	    ps.setInt(2, uid);
	    ps.setString(3, chosenBank);
	    ps.executeUpdate();


	    System.out.println("Payment of ₹" + total + " successful via " + chosenBank);

	    // Step 6: Call place_order procedure
	    PreparedStatement ps1 = con.prepareStatement("call placeorder('"+uid+"')");
	   
	    ResultSet rs1 = ps1.executeQuery();
	    
	    if(rs1.next())
	    {
	    	System.out.println(rs1.getString("message"));
	    }
	}
	
	public void createBankAccount(int uid) throws SQLException 
	{
	    System.out.print("Enter account number: ");
	    String accountNo = sc.nextLine();

	    System.out.print("Enter bank name: ");
	    String bankName = sc.nextLine();

	    System.out.print("Enter IFSC code: ");
	    String ifscCode = sc.nextLine();

	    System.out.print("Enter initial balance: ");
	    double balance = sc.nextDouble();

	    String sql = "INSERT INTO bank (accountno, bankname, ifsccode, balance, uid) " +
	                 "VALUES (?, ?, ?, ?, ?)";
	    PreparedStatement ps = con.prepareStatement(sql);

	    ps.setString(1, accountNo);
	    ps.setString(2, bankName);
	    ps.setString(3, ifscCode);
	    ps.setDouble(4, balance);
	    ps.setInt(5, uid);

	    int r = ps.executeUpdate();
	    
	    if(r > 0)
	    {
	    	System.out.println("Bank account created.");
	    }
	}
	
	public void viewMyOrderHistory(int uid) throws Exception
	{
	    PreparedStatement ps = con.prepareStatement("call viewMyOrderHistory(?)");
	    ps.setInt(1, uid);
	    ResultSet rs = ps.executeQuery();

	    System.out.println("OrderID | OrderedAt           | PID | ProductName       | Price  | Qty | TotalPrice | DeliveryDate");
	    System.out.println("-----------------------------------------------------------------------------------------------");

	    boolean hasOrders = false;
	    while (rs.next())
	    {
	        hasOrders = true;
	        System.out.printf("%7d | %-19s | %4d | %-18s | ₹%-6.2f | %3d | ₹%-10.2f | %s%n",
	            rs.getInt("orderid"),
	            rs.getTimestamp("orderedat"),
	            rs.getInt("pid"),
	            rs.getString("pname"),
	            rs.getDouble("price"),
	            rs.getInt("qty"),
	            rs.getDouble("totalprice"),
	            rs.getDate("deliverydate")
	        );
	    }

	    if (!hasOrders)
	    {
	        System.out.println("No orders found.");
	    }
	}
	
	public void viewAllOrderHistory() throws SQLException
	{
	    PreparedStatement ps = con.prepareStatement("call viewAllOrderHistory()");
	    ResultSet rs = ps.executeQuery();

	    System.out.println("OrderID | OrderedAt           | UID | PID | ProductName       | Price  | Qty | TotalPrice | DeliveryDate");
	    System.out.println("----------------------------------------------------------------------------------------------------------");

	    boolean hasOrders = false;
	    while (rs.next())
	    {
	        hasOrders = true;
	        System.out.printf("%7d | %-19s | %3d | %3d | %-18s | ₹%-6.2f | %3d | ₹%-10.2f | %s%n",
	            rs.getInt("orderid"),
	            rs.getTimestamp("orderedat"),
	            rs.getInt("uid"),
	            rs.getInt("pid"),
	            rs.getString("pname"),
	            rs.getDouble("price"),
	            rs.getInt("qty"),
	            rs.getDouble("totalprice"),
	            rs.getDate("deliverydate")
	        );
	    }

	    if (!hasOrders) 
	    {
	        System.out.println("No orders found in the system.");
	    }
	}
	
	public static void main(String[] args) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		ECommerceApplication e = new ECommerceApplication();
		
		boolean running = true;

		while (running) 
		{
		    System.out.println("=== Main Menu ===");
		    System.out.println("1. Admin Login");
		    System.out.println("2. User Login");
		    System.out.println("3. Register");
		    System.out.println("0. Exit");
		    System.out.print("Select an option: ");
		    int choice = sc.nextInt();

		    switch (choice) 
		    {
		        case 1:
		            System.out.print("Enter Admin Username: ");
		            String adminUser = sc.next();
		            System.out.print("Enter Admin Password: ");
		            String adminPass = sc.next();

		            if (adminUser.equals("admin") && adminPass.equals("root")) 
		            {
		                e.adminMenu();
		            } 
		            else 
		            {
		                System.out.println("Invalid admin credentials.");
		            }
		            break;

		        case 2:
		        	Integer userid = e.userLogin(); 
		      
		        	if(userid != null)
		        	{
		        		e.userMenu(userid);
		        	}
		            break;

		        case 3:
		            e.registerUser();
		            break;

		        case 0:
		            System.out.println("Exiting... Thank You!");
		            running = false;
		            break;

		        default:
		            System.out.println("Invalid option. Please try again.");
		    }

		    System.out.println(); 
		}
		sc.close();
	
	}
}


