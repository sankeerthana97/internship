import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class InventoryManagementSystemGUI extends JFrame {
    private List<Item> inventory;
    private List<Order> orders;
    private List<Supplier> suppliers;
    private DefaultListModel<String> stockListModel;
    private JList<String> stockList;
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;
    private DefaultListModel<String> supplierListModel;
    private JList<String> supplierList;
    private JTextField searchField;

    public InventoryManagementSystemGUI() {
        setTitle("Inventory Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inventory = new ArrayList<>();
        orders = new ArrayList<>();
        suppliers = new ArrayList<>();

        stockListModel = new DefaultListModel<>();
        stockList = new JList<>(stockListModel);

        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);

        supplierListModel = new DefaultListModel<>();
        supplierList = new JList<>(supplierListModel);

        searchField = new JTextField();
        searchField.setToolTipText("Search by Item Name");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchField.getText());
            }
        });

        JPanel stockPanel = new JPanel();
        stockPanel.setLayout(new BorderLayout());

        stockPanel.add(new JLabel("Stock"), BorderLayout.NORTH);
        stockPanel.add(new JScrollPane(stockList), BorderLayout.CENTER);

        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new BorderLayout());

        orderPanel.add(new JLabel("Orders"), BorderLayout.NORTH);
        orderPanel.add(new JScrollPane(orderList), BorderLayout.CENTER);

        JPanel supplierPanel = new JPanel();
        supplierPanel.setLayout(new BorderLayout());

        supplierPanel.add(new JLabel("Suppliers"), BorderLayout.NORTH);
        supplierPanel.add(new JScrollPane(supplierList), BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        searchPanel.add(searchField, BorderLayout.NORTH);

        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        JButton createOrderButton = new JButton("Create Order");
        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createOrder();
            }
        });

        JButton addSupplierButton = new JButton("Add Supplier");
        addSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });

        JButton restockButton = new JButton("Restock Item");
        restockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restockItem();
            }
        });

        JButton sortStockButton = new JButton("Sort Stock");
        sortStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortStock();
            }
        });

        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addItemButton);
        buttonPanel.add(createOrderButton);
        buttonPanel.add(addSupplierButton);
        buttonPanel.add(restockButton);
        buttonPanel.add(sortStockButton);
        buttonPanel.add(generateReportButton);

        setLayout(new BorderLayout());
        add(stockPanel, BorderLayout.WEST);
        add(orderPanel, BorderLayout.CENTER);
        add(supplierPanel, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private void addItem() {
        String name = JOptionPane.showInputDialog("Enter Item Name:");
        if (name != null && !name.isEmpty()) {
            try {
                int stock = Integer.parseInt(JOptionPane.showInputDialog("Enter Stock Level:"));
                double price = Double.parseDouble(JOptionPane.showInputDialog("Enter Price:"));
                Item item = new Item(name, stock, price);
                inventory.add(item);
                stockListModel.addElement(item.getName() + " - Stock: " + item.getStock() + " - Price: $" + item.getPrice());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input for stock or price.");
            }
        }
    }

    private void createOrder() {
        String name = JOptionPane.showInputDialog("Enter Item Name for Order:");
        if (name != null && !name.isEmpty()) {
            try {
                int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter Order Quantity:"));
                Order order = new Order(name, quantity);
                orders.add(order);
                orderListModel.addElement(order.getName() + " - Quantity: " + order.getQuantity());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input for order quantity.");
            }
        }
    }

    private void addSupplier() {
        String name = JOptionPane.showInputDialog("Enter Supplier Name:");
        if (name != null && !name.isEmpty()) {
            String contact = JOptionPane.showInputDialog("Enter Supplier Contact:");
            Supplier supplier = new Supplier(name, contact);
            suppliers.add(supplier);
            supplierListModel.addElement(supplier.getName() + " - Contact: " + supplier.getContact());
        }
    }

    private void restockItem() {
        String name = JOptionPane.showInputDialog("Enter Item Name for Restocking:");
        if (name != null && !name.isEmpty()) {
            try {
                int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter Restock Quantity:"));
                Item restockedItem = null;
                Supplier selectedSupplier = null;

                // Find the item to restock
                for (Item item : inventory) {
                    if (item.getName().equalsIgnoreCase(name)) {
                        restockedItem = item;
                        break;
                    }
                }

                // If the item exists, choose a supplier for restocking
                if (restockedItem != null) {
                    String[] supplierOptions = new String[suppliers.size()];
                    for (int i = 0; i < suppliers.size(); i++) {
                        supplierOptions[i] = suppliers.get(i).getName();
                    }

                    String selectedSupplierName = (String) JOptionPane.showInputDialog(
                            null,
                            "Select a Supplier for Restocking",
                            "Supplier Selection",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            supplierOptions,
                            supplierOptions[0]
                    );

                    if (selectedSupplierName != null) {
                        for (Supplier supplier : suppliers) {
                            if (supplier.getName().equalsIgnoreCase(selectedSupplierName)) {
                                selectedSupplier = supplier;
                                break;
                            }
                        }

                        if (selectedSupplier != null) {
                            // Update stock levels and track the supplier
                            restockedItem.restock(quantity, selectedSupplier.getName());
                            stockListModel.clear();
                            for (Item item : inventory) {
                                stockListModel.addElement(item.getName() + " - Stock: " + item.getStock() + " - Price: $" + item.getPrice());
                            }
                        }
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input for restock quantity.");
            }
        }
    }

    private void sortStock() {
        // Sort the inventory by item name
        Collections.sort(inventory, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item1.getName().compareTo(item2.getName());
            }
        });

        // Clear and update the stockListModel
        stockListModel.clear();
        for (Item item : inventory) {
            stockListModel.addElement(item.getName() + " - Stock: " + item.getStock() + " - Price: $" + item.getPrice());
        }
    }

    private void generateReport() {
        // Generate a report with all items in stock
        StringBuilder report = new StringBuilder("Inventory Report:\n");
        for (Item item : inventory) {
            report.append("Item: ").append(item.getName())
                    .append(", Stock: ").append(item.getStock())
                    .append(", Price: $").append(item.getPrice())
                    .append("\n");
        }

        // Display the report in a dialog
        JOptionPane.showMessageDialog(null, report.toString(), "Inventory Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void search(String query) {
        stockListModel.clear();
        for (Item item : inventory) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                stockListModel.addElement(item.getName() + " - Stock: " + item.getStock() + " - Price: $" + item.getPrice());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InventoryManagementSystemGUI();
        });
    }
}

class Item {
    private String name;
    private int stock;
    private double price;

    public Item(String name, int stock, double price) {
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public void restock(int quantity, String supplier) {
        stock += quantity;
    }
}

class Order {
    private String name;
    private int quantity;

    public Order(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}

class Supplier {
    private String name;
    private String contact;

    public Supplier(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}
