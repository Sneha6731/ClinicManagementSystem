import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
              createBookAppointmentWindow(conn); // Naye GUI window ko call kiya// Naye function ko call kiya
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
        dashboardFrame.setSize(500, 550);
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
// 6. Button 4: View All Appointments
        JButton btnAppointments = new JButton("View All Appointments");
        btnAppointments.setBounds(100, 280, 300, 45); // Y-axis ko 280 kar diya taaki gap bana rahe
        btnAppointments.setFont(new Font("Arial", Font.PLAIN, 16));
        btnAppointments.setBackground(Color.WHITE);
        dashboardFrame.add(btnAppointments);
        // 7. Button 5: Cancel Appointment
        JButton btnCancelApp = new JButton("Cancel Appointment");
        btnCancelApp.setBounds(100, 340, 300, 45); // Y-axis ko 340 kar diya
        btnCancelApp.setFont(new Font("Arial", Font.PLAIN, 16));
        btnCancelApp.setBackground(Color.WHITE);
        dashboardFrame.add(btnCancelApp);
        // 6. Button 4: Logout / Exit
        JButton btnExit = new JButton("Logout");
        btnExit.setBounds(100, 400, 300, 40);
        btnExit.setFont(new Font("Arial", Font.BOLD, 14));
        btnExit.setBackground(new Color(255, 102, 102));
        btnExit.setForeground(Color.WHITE);
        dashboardFrame.add(btnExit);

        // --- BUTTON ACTIONS ---

        // Register Button Click
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRegisterPatientWindow(conn);
                // Yahan hum Patient Registration ka UI jodenge next step mein
                // Book Appointment Button Click
        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBookAppointmentWindow(conn);
            }
        });
            }
        });

        // View Patients Button Click
        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createViewPatientsWindow(conn);
                // Yahan hum Table format mein data dikhane ka UI jodenge
            }
        });
        // View Appointments Button Click
        btnAppointments.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createViewAppointmentsWindow(conn);
            }
        });
        // Cancel Appointment Button Click
        btnCancelApp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCancelAppointmentWindow(conn);
            }
        });

        // Book Appointment Button Click
        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBookAppointmentWindow(conn);
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
    // Sunder Patient Registration Window (Clean & Final)
    public static void createRegisterPatientWindow(Connection conn) {
        JFrame regFrame = new JFrame("Register New Patient");
        regFrame.setSize(400, 350);
        regFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        regFrame.setLocationRelativeTo(null);
        regFrame.setLayout(null);

        JLabel headLabel = new JLabel("Patient Registration Form", JLabel.CENTER);
        headLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headLabel.setBounds(50, 10, 300, 30);
        regFrame.add(headLabel);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(40, 60, 100, 25);
        regFrame.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(150, 60, 180, 25);
        regFrame.add(nameField);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(40, 100, 100, 25);
        regFrame.add(ageLabel);

        JTextField ageField = new JTextField();
        ageField.setBounds(150, 100, 180, 25);
        regFrame.add(ageField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(40, 140, 100, 25);
        regFrame.add(genderLabel);

        JTextField genderField = new JTextField();
        genderField.setBounds(150, 140, 180, 25);
        regFrame.add(genderField);

        JLabel phoneLabel = new JLabel("Contact No:");
        phoneLabel.setBounds(40, 180, 100, 25);
        regFrame.add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(150, 180, 180, 25);
        regFrame.add(phoneField);

        JButton saveButton = new JButton("Save Patient");
        saveButton.setBounds(120, 240, 150, 35);
        saveButton.setBackground(new Color(0, 153, 76));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        regFrame.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String ageStr = ageField.getText();
                String gender = genderField.getText();
                String phone = phoneField.getText();

                if (name.isEmpty() || ageStr.isEmpty() || gender.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(regFrame, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int age = Integer.parseInt(ageStr);

                    // 5 parameters wali sahi query
                    String query = "INSERT INTO patients (name, age, gender, phone, address) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pst = conn.prepareStatement(query);
                    pst.setString(1, name);
                    pst.setInt(2, age);
                    pst.setString(3, gender);
                    pst.setString(4, phone); 
                    pst.setString(5, "");    

                    int rows = pst.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(regFrame, "🎉 Patient Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        regFrame.dispose();
                    }
                    pst.close();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(regFrame, "Age must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(regFrame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        regFrame.setVisible(true);
    }
    // Appointment Booking Window
   // Appointment Booking Window
    public static void createBookAppointmentWindow(Connection conn) {
        JFrame appFrame = new JFrame("Book New Appointment");
        appFrame.setSize(450, 400);
        appFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);
        appFrame.setLayout(null);

        JLabel headLabel = new JLabel("Appointment Booking Form", JLabel.CENTER);
        headLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headLabel.setBounds(50, 10, 350, 30);
        appFrame.add(headLabel);

        // Patient ID
        JLabel idLabel = new JLabel("Patient ID:");
        idLabel.setBounds(40, 70, 120, 25);
        appFrame.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(180, 70, 200, 25);
        appFrame.add(idField);

        // Doctor ID (Badlaav yahan kiya hai)
        JLabel docLabel = new JLabel("Doctor ID (Number):");
        docLabel.setBounds(40, 120, 140, 25);
        appFrame.add(docLabel);

        JTextField docField = new JTextField();
        docField.setBounds(180, 120, 200, 25);
        appFrame.add(docField);

        // Appointment Date (YYYY-MM-DD)
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setBounds(40, 170, 140, 25);
        appFrame.add(dateLabel);

        JTextField dateField = new JTextField();
        dateField.setBounds(180, 170, 200, 25);
        appFrame.add(dateField);

        // Book Button
        JButton bookButton = new JButton("Book Appointment");
        bookButton.setBounds(130, 250, 180, 40);
        bookButton.setBackground(new Color(0, 102, 204));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        appFrame.add(bookButton);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String patientIdStr = idField.getText();
                String doctorIdStr = docField.getText(); // Doctor ID string le rahe hain
                String appDate = dateField.getText();

                if (patientIdStr.isEmpty() || doctorIdStr.isEmpty() || appDate.isEmpty()) {
                    JOptionPane.showMessageDialog(appFrame, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int patientId = Integer.parseInt(patientIdStr);
                    int doctorId = Integer.parseInt(doctorIdStr); // String ko number mein badla

                    // Sahi Query aapke table columns ke hissab se
                    String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
                    PreparedStatement pst = conn.prepareStatement(query);
                    pst.setInt(1, patientId);
                    pst.setInt(2, doctorId);
                    pst.setString(3, appDate);

                    int rows = pst.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(appFrame, "🎉 Appointment Booked Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        appFrame.dispose();
                    }
                    pst.close();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(appFrame, "Patient ID and Doctor ID must be numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(appFrame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        appFrame.setVisible(true);
    }
    // View All Patients Window
    public static void createViewPatientsWindow(Connection conn) {
        JFrame viewFrame = new JFrame("All Registered Patients");
        viewFrame.setSize(600, 400);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setLayout(new BorderLayout());

        JLabel headLabel = new JLabel("Registered Patients List", JLabel.CENTER);
        headLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        viewFrame.add(headLabel, BorderLayout.NORTH);

        // Table Columns (Aapke patients table ke columns ke hissab se)
        String[] columnNames = {"Patient ID", "Name", "Age", "Gender", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane, BorderLayout.CENTER);

        try {
            // Database se data nikalne ki query
            String query = "SELECT * FROM patients";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("patient_id"); // Agar aapke column ka naam sirf id hai toh "id" likhiye
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                String contact = rs.getString("phone");

                // Table mein row add kar rahe hain
                model.addRow(new Object[]{id, name, age, gender, contact});
            }
            rs.close();
            stmt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(viewFrame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        viewFrame.setVisible(true);
    }
    // View All Appointments Window
    public static void createViewAppointmentsWindow(Connection conn) {
        JFrame viewAppFrame = new JFrame("All Scheduled Appointments");
        viewAppFrame.setSize(650, 400);
        viewAppFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewAppFrame.setLocationRelativeTo(null);
        viewAppFrame.setLayout(new BorderLayout());

        JLabel headLabel = new JLabel("Scheduled Appointments List", JLabel.CENTER);
        headLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        viewAppFrame.add(headLabel, BorderLayout.NORTH);

        // Table Columns (Aapke appointments table ke columns ke hissab se)
        String[] columnNames = {"Appointment ID", "Patient ID", "Doctor ID", "Appointment Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        viewAppFrame.add(scrollPane, BorderLayout.CENTER);

        try {
            // Database se data nikalne ki query
            String query = "SELECT * FROM appointments";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int appId = rs.getInt("appointment_id");
                int patId = rs.getInt("patient_id");
                int docId = rs.getInt("doctor_id");
                String appDate = rs.getString("appointment_date");
                String status = rs.getString("status");

                // Table mein row add kar rahe hain
                model.addRow(new Object[]{appId, patId, docId, appDate, status});
            }
            rs.close();
            stmt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(viewAppFrame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        viewAppFrame.setVisible(true);
    }
    // Cancel Appointment Window
    public static void createCancelAppointmentWindow(Connection conn) {
        JFrame cancelFrame = new JFrame("Cancel Appointment");
        cancelFrame.setSize(400, 250);
        cancelFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cancelFrame.setLocationRelativeTo(null);
        cancelFrame.setLayout(null);

        JLabel headLabel = new JLabel("Cancel Appointment", JLabel.CENTER);
        headLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headLabel.setBounds(50, 10, 300, 30);
        cancelFrame.add(headLabel);

        // Appointment ID Input
        JLabel idLabel = new JLabel("Appointment ID:");
        idLabel.setBounds(40, 70, 120, 25);
        cancelFrame.add(idLabel);

        JTextField idField = new JTextField();
        idField.setBounds(180, 70, 180, 25);
        cancelFrame.add(idField);

        // Cancel Button
        JButton btnCancel = new JButton("Cancel Appointment");
        btnCancel.setBounds(100, 140, 200, 40);
        btnCancel.setBackground(Color.RED);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        cancelFrame.add(btnCancel);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String appIdStr = idField.getText();

                if (appIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(cancelFrame, "Please enter Appointment ID!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int appId = Integer.parseInt(appIdStr);

                    // Database query to delete the appointment
                    String query = "DELETE FROM appointments WHERE appointment_id = ?";
                    PreparedStatement pst = conn.prepareStatement(query);
                    pst.setInt(1, appId);

                    int rows = pst.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(cancelFrame, "🗑️ Appointment Cancelled Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        cancelFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(cancelFrame, "No appointment found with this ID!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    pst.close();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(cancelFrame, "Appointment ID must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(cancelFrame, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelFrame.setVisible(true);
    }

    
}