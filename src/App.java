import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Database se connect karte hain
        Connection conn = config.DBConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Terminal menu ko rok kar naya UI Window open karte hain
        createLoginWindow(conn);
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
    // Sunder UI Login Window
    public static void createLoginWindow(Connection conn) {
        // 1. Main Window (JFrame) banana
        JFrame frame = new JFrame("Clinic Management System - Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Window screen ke beech mein khulegi
        frame.setLayout(null);

        // 2. Heading Title
        JLabel titleLabel = new JLabel("Welcome to Clinic Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 204)); // Sunder Blue color
        titleLabel.setBounds(50, 20, 300, 30);
        frame.add(titleLabel);

        // 3. Email Input
        JLabel emailLabel = new JLabel("Email ID:");
        emailLabel.setBounds(50, 80, 80, 25);
        frame.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(140, 80, 200, 25);
        frame.add(emailField);

        // 4. Password Input
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 120, 80, 25);
        frame.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(140, 120, 200, 25);
        frame.add(passField);

        // 5. Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(140, 180, 100, 35);
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(loginButton);

        // Button click hone par kya hoga
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passField.getPassword());

                // Humare purane loginUser function ko call karenge jo database se check karta hai
                if (loginUser(conn, email, password)) {
                    JOptionPane.showMessageDialog(frame, "🎉 Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose(); // Login window band ho jayegi
                    
                    // Aage ka menu abhi temporary terminal par hi khulega
                    createDashboard(conn);
                } else {
                    JOptionPane.showMessageDialog(frame, "❌ Invalid Email or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Window ko screen par dikhana
        frame.setVisible(true);
    }
    // Login check karne ka sahi helper method
    public static boolean loginUser(Connection conn, String email, String password) {
        try {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                pst.close();
                return true; // Agar user mil gaya toh true
            }
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Agar nahi mila toh false
    }
    // Sunder Graphical Dashboard
    public static void createDashboard(Connection conn) {
        // 1. Main Dashboard Window (JFrame)
        JFrame dashboardFrame = new JFrame("Clinic Management System - Dashboard");
        dashboardFrame.setSize(500, 400);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setLocationRelativeTo(null); // Screen ke beech mein khulegi
        dashboardFrame.setLayout(null);

        // 2. Welcome Title Banner
        JLabel welcomeLabel = new JLabel("Doctor's Control Panel", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(0, 102, 204));
        welcomeLabel.setBounds(50, 30, 400, 40);
        dashboardFrame.add(welcomeLabel);

        // 3. Button 1: Register New Patient
        JButton btnRegister = new JButton("Register New Patient");
        btnRegister.setBounds(100, 100, 300, 45);
        btnRegister.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRegister.setBackground(Color.WHITE);
        dashboardFrame.add(btnRegister);

        // 4. Button 2: View All Patients
        JButton btnView = new JButton("View All Patients");
        btnView.setBounds(100, 160, 300, 45);
        btnView.setFont(new Font("Arial", Font.PLAIN, 16));
        btnView.setBackground(Color.WHITE);
        dashboardFrame.add(btnView);

        // 5. Button 3: Book Appointment
        JButton btnBook = new JButton("Book New Appointment");
        btnBook.setBounds(100, 220, 300, 45);
        btnBook.setFont(new Font("Arial", Font.PLAIN, 16));
        btnBook.setBackground(Color.WHITE);
        dashboardFrame.add(btnBook);

        // 6. Button 4: Logout / Exit
        JButton btnExit = new JButton("Logout");
        btnExit.setBounds(100, 290, 300, 40);
        btnExit.setFont(new Font("Arial", Font.BOLD, 14));
        btnExit.setBackground(new Color(255, 102, 102));
        btnExit.setForeground(Color.WHITE);
        dashboardFrame.add(btnExit);

        // --- BUTTON ACTIONS ---

        // Register Button Click
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(dashboardFrame, "Patient Registration UI Coming Soon!");
                // Yahan hum Patient Registration ka UI jodenge next step mein
            }
        });

        // View Patients Button Click
        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(dashboardFrame, "View Patients UI Coming Soon!");
                // Yahan hum Table format mein data dikhane ka UI jodenge
            }
        });

        // Book Appointment Button Click
        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(dashboardFrame, "Appointment Booking UI Coming Soon!");
                // Yahan hum Appointment form ka UI jodenge
            }
        });

        // Logout Button Click
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose();
                createLoginWindow(conn); // Wapas login screen par bhej dega
            }
        });

        // Window ko screen par dikhana
        dashboardFrame.setVisible(true);
    }
}