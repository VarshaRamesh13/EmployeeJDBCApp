import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String DB_URL = "jdbc:mysql://localhost:3306/employeeDB";
    static final String USER = "root";
    static final String PASS = "yourpassword"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            int choice;
            do {
                System.out.println("\n=== Employee Management ===");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1: addEmployee(conn, sc); break;
                    case 2: viewEmployees(conn); break;
                    case 3: updateEmployee(conn, sc); break;
                    case 4: deleteEmployee(conn, sc); break;
                }
            } while (choice != 5);

            conn.close();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addEmployee(Connection conn, Scanner sc) throws SQLException {
        sc.nextLine();
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter position: ");
        String position = sc.nextLine();
        System.out.print("Enter salary: ");
        double salary = sc.nextDouble();

        String sql = "INSERT INTO employee (name, position, salary) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, position);
        ps.setDouble(3, salary);
        ps.executeUpdate();
        System.out.println("✅ Employee added successfully.");
    }

    public static void viewEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM employee";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n--- Employee List ---");
        while (rs.next()) {
            System.out.printf("ID: %d | Name: %s | Position: %s | Salary: %.2f\n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getDouble("salary"));
        }
    }

    public static void updateEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter employee ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new position: ");
        String position = sc.nextLine();
        System.out.print("Enter new salary: ");
        double salary = sc.nextDouble();

        String sql = "UPDATE employee SET name=?, position=?, salary=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, position);
        ps.setDouble(3, salary);
        ps.setInt(4, id);
        int rows = ps.executeUpdate();
        System.out.println(rows > 0 ? "✅ Employee updated." : "⚠️ Employee not found.");
    }

    public static void deleteEmployee(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter employee ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM employee WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        int rows = ps.executeUpdate();
        System.out.println(rows > 0 ? "✅ Employee deleted." : "⚠️ Employee not found.");
    }
}