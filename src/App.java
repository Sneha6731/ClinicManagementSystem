import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class App {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== CLINIC MANAGEMENT SYSTEM LOGIN ===");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        try {
            Connection conn = config.DBConnection.getConnection();
            if (conn != null) {
                String query = "SELECT name, role FROM users WHERE email = ? AND password = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, email);
                pst.setString(2, password);
                ResultSet rs = pst.executeQuery();
                
                if (rs.next()) {
                    System.out.println("\n--- Login Successful! ---");
                    System.out.println("Welcome, " + rs.getString("name") + " (" + rs.getString("role") + ")");
                    
                    // Login ke baad main menu dikhana
                    showMenu(conn);
                } else {
                    System.out.println("\n--- Invalid Email or Password! Login Failed. ---");
                }
                
                rs.close();
                pst.close();
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main Menu jahan se Patient Add hoga
   // Main Menu jahan options dikhenge
    // Main Menu jahan options dikhenge
    public static void showMenu(Connection conn) {
        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Register New Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Book Appointment"); // Naya option
            System.out.println("4. Exit"); // Exit ab 4 par gaya
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // buffer clear
            
            if (choice == 1) {
                registerPatient(conn);
            } else if (choice == 2) {
                viewAllPatients(conn);
            } else if (choice == 3) {
                bookAppointment(conn); // Naye function ko call kiya
            } else if (choice == 4) {
                System.out.println("Thank you for using Clinic Management System!");
                break;
            } else {
                System.out.println("Invalid option! Try again.");
            }
        }
    }
    // Patient Register karne ka method
    public static void registerPatient(Connection conn) {
        System.out.println("\n--- Enter Patient Details ---");
        System.out.print("Patient Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Gender (Male/Female): ");
        String gender = scanner.nextLine();
        
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        
        System.out.print("Address: ");
        String address = scanner.nextLine();
        
        try {
            String query = "INSERT INTO patients (name, age, gender, phone, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setString(3, gender);
            pst.setString(4, phone);
            pst.setString(5, address);
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("\n🎉 Patient registered successfully in database!");
            }
            pst.close();
        } catch (Exception e) {
            System.out.println("Error while registering patient.");
            e.printStackTrace();
        }
    }
    // Database se saare patients fetch karke dikhane ka method
    public static void viewAllPatients(Connection conn) {
        System.out.println("\n=== PATIENT LIST ===");
        
        try {
            // SQL Query saare patients nikalne ke liye
            String query = "SELECT * FROM patients";
            java.sql.Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            System.out.println("-----------------------------------------------------------------------");
            System.out.printf("%-5s | %-20s | %-5s | %-10s | %-15s | %-15s\n", "ID", "Name", "Age", "Gender", "Phone", "Address");
            System.out.println("-----------------------------------------------------------------------");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                // dhyan dein agar aapke table mein column ka naam alag hai (jaise id ya patient_id)
                int id = rs.getInt(1); 
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                
                // Ek line mein patient ka data print karna
                System.out.printf("%-5d | %-20s | %-5d | %-10s | %-15s | %-15s\n", id, name, age, gender, phone, address);
            }
            
            if (!hasData) {
                System.out.println("No patients registered yet.");
            }
            System.out.println("-----------------------------------------------------------------------");
            
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println("Error while fetching patients.");
            e.printStackTrace();
        }
    }
    // Appointment book karne ka method
    // Appointment book karne ka updated method
   // Appointment book karne ka sahi method
   // Appointment book karne ka bilkul sahi method (Table ke hisab se)
   // Appointment book karne ka bilkul sahi method (Table ke hisab se)
    public static void bookAppointment(Connection conn) {
        System.out.println("\n--- Book New Appointment ---");
        
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();
        
        // Yahan ab hum Doctor ka naam nahi, balki Doctor ki ID (number) maangenge
        System.out.print("Enter Doctor ID (e.g., 1): ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); // buffer clear
        
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appointmentDate = scanner.nextLine();
        
        try {
            // Aapke table ke columns: patient_id, doctor_id, appointment_date, status
            String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            
            pst.setInt(1, patientId);
            pst.setInt(2, doctorId);
            pst.setString(3, appointmentDate);
            pst.setString(4, "Pending"); // Status hum default 'Pending' rakh rahe hain
            
            int rows = pst.executeUpdate();
            if (rows > 0) {
                System.out.println("\n🎉 Appointment booked successfully in database!");
            }
            pst.close();
        } catch (Exception e) {
            System.out.println("Error while booking appointment.");
            e.printStackTrace();
        }
    }
}