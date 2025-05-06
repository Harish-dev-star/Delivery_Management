import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class Main extends JFrame {
    private JTextField packageIdField, senderField, receiverField, addressField, contactField;
    private JTextField deliveryDateField, weightField, deliveryTypeField;
    private static final String correctUsername = "delivary";
    private static final String correctPassword = "delivary12";

    public Main() {
        super("Delivery Management System");
        initializeUI();
    }

    private void initializeUI() {
        Font font = new Font("Arial", Font.PLAIN, 12);

        packageIdField = createStyledTextField(150, 25);
        senderField = createStyledTextField(150, 25);
        receiverField = createStyledTextField(150, 25);
        addressField = createStyledTextField(150, 25);
        contactField = createStyledTextField(150, 25);
        deliveryDateField = createStyledTextField(150, 25);
        weightField = createStyledTextField(150, 25);
        deliveryTypeField = createStyledTextField(150, 25);

        addEnterKeyListener(packageIdField, senderField);
        addEnterKeyListener(senderField, receiverField);
        addEnterKeyListener(receiverField, addressField);
        addEnterKeyListener(addressField, contactField);
        addEnterKeyListener(contactField, deliveryDateField);
        addEnterKeyListener(deliveryDateField, weightField);
        addEnterKeyListener(weightField, deliveryTypeField);

        JButton submitButton = createStyledButton("Add Delivery", new Color(50, 150, 250), 120, 30);
        JButton showButton = createStyledButton("Show Deliveries", new Color(100, 200, 100), 120, 30);
        JButton searchButton = createStyledButton("Search", new Color(255, 193, 7), 80, 30);
        JButton clearButton = createStyledButton("Clear", new Color(158, 158, 158), 80, 30);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 248, 255));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabelAndField(inputPanel, gbc, 0, "Package ID:", packageIdField);
        addLabelAndField(inputPanel, gbc, 1, "Sender:", senderField);
        addLabelAndField(inputPanel, gbc, 2, "Receiver:", receiverField);
        addLabelAndField(inputPanel, gbc, 3, "Address:", addressField);
        addLabelAndField(inputPanel, gbc, 4, "Contact:", contactField);
        addLabelAndField(inputPanel, gbc, 5, "Delivery Date:", deliveryDateField);
        addLabelAndField(inputPanel, gbc, 6, "Weight (kg):", weightField);
        addLabelAndField(inputPanel, gbc, 7, "Delivery Type:", deliveryTypeField);

        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(submitButton);
        buttonPanel.add(showButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.CENTER);

        submitButton.addActionListener(e -> addToDatabase());
        showButton.addActionListener(e -> new DeliveryListFrame());
        searchButton.addActionListener(e -> searchDelivery());
        clearButton.addActionListener(e -> clearFields());

        getContentPane().setBackground(new Color(225, 245, 254));
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JTextField createStyledTextField(int width, int height) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, height));
        field.setBackground(new Color(255, 255, 245));
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        return button;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void addEnterKeyListener(JTextField currentField, JTextField nextField) {
        currentField.addActionListener(e -> nextField.requestFocus());
    }

    private void addToDatabase() {
        String packageId = packageIdField.getText();
        String sender = senderField.getText();
        String receiver = receiverField.getText();
        String address = addressField.getText();
        String contact = contactField.getText();
        String deliveryDate = deliveryDateField.getText();
        String weightStr = weightField.getText();
        String deliveryType = deliveryTypeField.getText();

        if (packageId.isEmpty() || sender.isEmpty() || receiver.isEmpty() || address.isEmpty() ||
                contact.isEmpty() || deliveryDate.isEmpty() || weightStr.isEmpty() || deliveryType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            try (Connection connection = connectToDatabase()) {
                if (connection != null) {
                    String query = "INSERT INTO deliveries (package_id, sender, receiver, address, contact, delivery_date, weight, delivery_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, packageId);
                        stmt.setString(2, sender);
                        stmt.setString(3, receiver);
                        stmt.setString(4, address);
                        stmt.setString(5, contact);
                        stmt.setDate(6, Date.valueOf(deliveryDate));
                        stmt.setDouble(7, weight);
                        stmt.setString(8, deliveryType);

                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Delivery added successfully!");
                        } else {
                            JOptionPane.showMessageDialog(this, "Error adding delivery.");
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid weight. Please enter a number.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid delivery date format. Use YYYY-MM-DD.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }

        clearFields();
    }

    private void searchDelivery() {
        String searchTerm = JOptionPane.showInputDialog(this, "Enter Package ID to search:");
        if (searchTerm == null || searchTerm.trim().isEmpty()) return;

        try (Connection conn = connectToDatabase()) {
            if (conn != null) {
                String query = "SELECT * FROM deliveries WHERE package_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, searchTerm);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            packageIdField.setText(rs.getString("package_id"));
                            senderField.setText(rs.getString("sender"));
                            receiverField.setText(rs.getString("receiver"));
                            addressField.setText(rs.getString("address"));
                            contactField.setText(rs.getString("contact"));
                            deliveryDateField.setText(rs.getString("delivery_date"));
                            weightField.setText(String.valueOf(rs.getDouble("weight")));
                            deliveryTypeField.setText(rs.getString("delivery_type"));
                        } else {
                            JOptionPane.showMessageDialog(this, "No delivery found with ID: " + searchTerm);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void clearFields() {
        packageIdField.setText("");
        senderField.setText("");
        receiverField.setText("");
        addressField.setText("");
        contactField.setText("");
        deliveryDateField.setText("");
        weightField.setText("");
        deliveryTypeField.setText("");
        packageIdField.requestFocus();
    }

    private Connection connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/DeliveryDB";
            String username = "root";
            String password = "dbms";
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to database: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }

    private static class LoginPage extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordField;

        public LoginPage() {
            setTitle("Login - Delivery Management System");
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            Font font = new Font("Arial", Font.PLAIN, 12);

            JLabel usernameLabel = new JLabel("Username:");
            JLabel passwordLabel = new JLabel("Password:");

            usernameField = new JTextField(15);
            passwordField = new JPasswordField(15);
            usernameField.setFont(font);
            passwordField.setFont(font);

            JButton loginButton = new JButton("Login");
            loginButton.setPreferredSize(new Dimension(100, 25));
            loginButton.setBackground(new Color(76, 175, 80));
            loginButton.setForeground(Color.WHITE);
            loginButton.setFont(new Font("SansSerif", Font.BOLD, 12));

            gbc.gridx = 0;
            gbc.gridy = 0;
            add(usernameLabel, gbc);
            gbc.gridy = 1;
            add(passwordLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            add(usernameField, gbc);
            gbc.gridy = 1;
            add(passwordField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            add(loginButton, gbc);

            loginButton.addActionListener(e -> authenticate());

            setSize(300, 180);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }

        private void authenticate() {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                new Main();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        }
    }

    private class DeliveryListFrame extends JFrame {
        private JTable deliveryTable;
        private DefaultTableModel deliveryTableModel;
        private JTextField searchField;

        public DeliveryListFrame() {
            setTitle("All Deliveries");
            setLayout(new BorderLayout());

            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            searchField = new JTextField(20);
            searchField.setFont(new Font("Arial", Font.PLAIN, 12));

            JButton searchBtn = new JButton("Search");
            searchBtn.setPreferredSize(new Dimension(80, 25));
            searchBtn.setBackground(new Color(255, 193, 7));
            searchBtn.setForeground(Color.BLACK);
            searchBtn.setFont(new Font("SansSerif", Font.BOLD, 12));

            JButton refreshBtn = new JButton("Refresh");
            refreshBtn.setPreferredSize(new Dimension(80, 25));
            refreshBtn.setBackground(new Color(100, 200, 100));
            refreshBtn.setForeground(Color.WHITE);
            refreshBtn.setFont(new Font("SansSerif", Font.BOLD, 12));

            searchPanel.add(new JLabel("Search:"));
            searchPanel.add(searchField);
            searchPanel.add(searchBtn);
            searchPanel.add(refreshBtn);

            add(searchPanel, BorderLayout.NORTH);

            String[] columnNames = {
                    "Package ID", "Sender", "Receiver", "Address", "Contact",
                    "Delivery Date", "Weight", "Delivery Type"
            };

            deliveryTableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            deliveryTable = new JTable(deliveryTableModel);
            deliveryTable.setFont(new Font("Arial", Font.PLAIN, 12));
            deliveryTable.setRowHeight(22);
            deliveryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scrollPane = new JScrollPane(deliveryTable);
            add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

            JButton deleteButton = new JButton("Delete");
            deleteButton.setPreferredSize(new Dimension(100, 25));
            deleteButton.setBackground(new Color(220, 53, 69));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFont(new Font("SansSerif", Font.BOLD, 12));

            JButton editButton = new JButton("Edit");
            editButton.setPreferredSize(new Dimension(100, 25));
            editButton.setBackground(new Color(108, 117, 125));
            editButton.setForeground(Color.WHITE);
            editButton.setFont(new Font("SansSerif", Font.BOLD, 12));

            JButton closeButton = new JButton("Close");
            closeButton.setPreferredSize(new Dimension(100, 25));
            closeButton.setBackground(new Color(108, 117, 125));
            closeButton.setForeground(Color.WHITE);
            closeButton.setFont(new Font("SansSerif", Font.BOLD, 12));

            buttonPanel.add(deleteButton);
            buttonPanel.add(editButton);
            buttonPanel.add(closeButton);

            add(buttonPanel, BorderLayout.SOUTH);

            deleteButton.addActionListener(e -> deleteSelectedDelivery());
            editButton.addActionListener(e -> editSelectedDelivery());
            closeButton.addActionListener(e -> dispose());
            searchBtn.addActionListener(e -> searchDeliveries());
            refreshBtn.addActionListener(e -> loadDeliveriesFromDatabase());

            loadDeliveriesFromDatabase();

            setSize(900, 500);
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private void loadDeliveriesFromDatabase() {
            deliveryTableModel.setRowCount(0);

            try (Connection conn = connectToDatabase()) {
                if (conn != null) {
                    String query = "SELECT * FROM deliveries ORDER BY delivery_date DESC";
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {

                        while (rs.next()) {
                            Vector<String> row = new Vector<>();
                            row.add(rs.getString("package_id"));
                            row.add(rs.getString("sender"));
                            row.add(rs.getString("receiver"));
                            row.add(rs.getString("address"));
                            row.add(rs.getString("contact"));
                            row.add(rs.getString("delivery_date"));
                            row.add(String.format("%.2f kg", rs.getDouble("weight")));
                            row.add(rs.getString("delivery_type"));

                            deliveryTableModel.addRow(row);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading deliveries: " + e.getMessage());
            }
        }

        private void searchDeliveries() {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                loadDeliveriesFromDatabase();
                return;
            }

            deliveryTableModel.setRowCount(0);
            try (Connection conn = connectToDatabase()) {
                if (conn != null) {
                    String query = "SELECT * FROM deliveries WHERE package_id LIKE ? OR sender LIKE ? OR receiver LIKE ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        String likeTerm = "%" + searchTerm + "%";
                        stmt.setString(1, likeTerm);
                        stmt.setString(2, likeTerm);
                        stmt.setString(3, likeTerm);

                        try (ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                Vector<String> row = new Vector<>();
                                row.add(rs.getString("package_id"));
                                row.add(rs.getString("sender"));
                                row.add(rs.getString("receiver"));
                                row.add(rs.getString("address"));
                                row.add(rs.getString("contact"));
                                row.add(rs.getString("delivery_date"));
                                row.add(String.format("%.2f kg", rs.getDouble("weight")));
                                row.add(rs.getString("delivery_type"));

                                deliveryTableModel.addRow(row);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error searching deliveries: " + e.getMessage());
            }
        }

        private void deleteSelectedDelivery() {
            int selectedRow = deliveryTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a delivery to delete.");
                return;
            }

            String packageId = (String) deliveryTableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete Package ID: " + packageId + "?", "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = connectToDatabase()) {
                    if (conn != null) {
                        String query = "DELETE FROM deliveries WHERE package_id = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, packageId);
                            int rowsDeleted = stmt.executeUpdate();
                            if (rowsDeleted > 0) {
                                deliveryTableModel.removeRow(selectedRow);
                                JOptionPane.showMessageDialog(this, "Delivery deleted successfully!");
                            } else {
                                JOptionPane.showMessageDialog(this, "Error deleting delivery.");
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
                }
            }
        }

        private void editSelectedDelivery() {
            int selectedRow = deliveryTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a delivery to edit.");
                return;
            }

            String packageId = (String) deliveryTableModel.getValueAt(selectedRow, 0);
            JOptionPane.showMessageDialog(this, "Edit functionality for Package ID: " + packageId + " would go here");
        }
    }
}
