import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class StudentAttendanceManagement extends JFrame {

    private JTextField nameField, regField, rollField, bunkField, totalField;
    private JComboBox<String> branchBox;
    private DefaultTableModel model;
    private JTable table;
    private java.util.List<Student> students = new ArrayList<>();

    public StudentAttendanceManagement() {
        setTitle("Student Attendance Manager");
        setSize(850, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(new Color(245, 240, 255)); // 🌸 very light lavender

        // ---------------- HEADER ----------------
        JPanel header = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(200, 180, 255),
                        getWidth(), 0, new Color(180, 220, 255));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(850, 70));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JLabel title = new JLabel("🎓 Student Attendance Manager");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(80, 50, 120));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // ---------------- FORM PANEL ----------------
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(245, 240, 255));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 10, 40));

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        nameField = createField("Name", labelFont, fieldFont, formPanel);
        regField = createField("Register No", labelFont, fieldFont, formPanel);
        rollField = createField("Roll No", labelFont, fieldFont, formPanel);

        JLabel branchLabel = new JLabel("Branch");
        branchLabel.setFont(labelFont);
        branchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(branchLabel);

        branchBox = new JComboBox<>(new String[]{"CSE", "ECE", "EEE", "MECH", "CIVIL"});
        branchBox.setFont(fieldFont);
        branchBox.setMaximumSize(new Dimension(300, 30));
        formPanel.add(branchBox);
        formPanel.add(Box.createVerticalStrut(10));

        JLabel totalLabel = new JLabel("Total Hours");
        totalLabel.setFont(labelFont);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(totalLabel);

        totalField = new JTextField();
        totalField.setFont(fieldFont);
        totalField.setMaximumSize(new Dimension(300, 30));
        totalField.setEditable(false);
        formPanel.add(totalField);
        formPanel.add(Box.createVerticalStrut(10));

        bunkField = createField("No. of Bunks", labelFont, fieldFont, formPanel);

        add(formPanel, BorderLayout.WEST);

        // auto-fill total hours when branch changes
        branchBox.addActionListener(e -> {
            String b = (String) branchBox.getSelectedItem();
            switch (b) {
                case "CSE": totalField.setText("289"); break;
                case "ECE": totalField.setText("275"); break;
                case "EEE": totalField.setText("270"); break;
                case "MECH": totalField.setText("254"); break;
                case "CIVIL": totalField.setText("256"); break;
            }
        });
        branchBox.setSelectedIndex(0);

        // ---------------- BUTTON PANEL ----------------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        buttonPanel.setBackground(new Color(245, 240, 255));

        JButton addBtn = createButton("➕ Add");
        JButton updateBtn = createButton("✏️ Update");
        JButton deleteBtn = createButton("🗑 Delete");
        JButton saveBtn = createButton("💾 Save");
        JButton loadBtn = createButton("📂 Load");
        JButton aboutBtn = createButton("💜 About");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(loadBtn);
        buttonPanel.add(aboutBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // ---------------- TABLE ----------------
        String[] cols = {"Name", "Reg No", "Roll No", "Branch", "Total Hours", "Bunks", "Attendance %", "Safe Bunks"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                double percent = Double.parseDouble(model.getValueAt(row, 6).toString());
                if (!isSelected) {
                    if (percent >= 80)
                        c.setBackground(new Color(220, 255, 220)); // green
                    else
                        c.setBackground(new Color(255, 220, 220)); // red
                } else {
                    c.setBackground(new Color(200, 230, 255));
                }
                return c;
            }
        });
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // ---------------- ACTIONS ----------------
        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        saveBtn.addActionListener(e -> saveToFile());
        loadBtn.addActionListener(e -> loadFromFile());
        aboutBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Developed by:\n🌸 Samanvi\n🌷 Rithikka\n🌼 Madhuvadani",
                "About", JOptionPane.INFORMATION_MESSAGE));

        createPopupMenu();
    }

    private JTextField createField(String labelText, Font labelFont, Font fieldFont, JPanel parent) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);

        JTextField field = new JTextField();
        field.setFont(fieldFont);
        field.setMaximumSize(new Dimension(300, 30));
        parent.add(field);
        parent.add(Box.createVerticalStrut(10));
        return field;
    }

    private JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBackground(new Color(210, 200, 255));
        b.setForeground(new Color(60, 40, 90));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(180, 160, 220), 1, true));
        b.setPreferredSize(new Dimension(110, 35));
        return b;
    }

    private void addStudent() {
        try {
            String name = nameField.getText().trim();
            String reg = regField.getText().trim();
            String roll = rollField.getText().trim();
            String branch = (String) branchBox.getSelectedItem();
            int total = Integer.parseInt(totalField.getText().trim());
            int bunks = Integer.parseInt(bunkField.getText().trim());

            if (name.isEmpty() || reg.isEmpty() || roll.isEmpty()) throw new Exception("All fields required!");
            if (bunks > total) throw new Exception("Bunks cannot exceed total hours!");

            Student s = new Student(name, reg, roll, branch, total, bunks);
            students.add(s);
            model.addRow(new Object[]{s.name, s.reg, s.roll, s.branch, s.total, s.bunks,
                    String.format("%.2f", s.percent), s.safeBunks});
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        try {
            String name = nameField.getText().trim();
            String reg = regField.getText().trim();
            String roll = rollField.getText().trim();
            String branch = (String) branchBox.getSelectedItem();
            int total = Integer.parseInt(totalField.getText().trim());
            int bunks = Integer.parseInt(bunkField.getText().trim());

            Student s = new Student(name, reg, roll, branch, total, bunks);
            students.set(row, s);

            for (int i = 0; i < 8; i++) {
                model.setValueAt(new Object[]{s.name, s.reg, s.roll, s.branch, s.total,
                        s.bunks, String.format("%.2f", s.percent), s.safeBunks}[i], row, i);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        students.remove(row);
        model.removeRow(row);
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("students.txt"))) {
            for (Student s : students) {
                bw.write(s.name + "," + s.reg + "," + s.roll + "," + s.branch + "," +
                        s.total + "," + s.bunks + "," + s.percent + "," + s.safeBunks);
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Data saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("students.txt"))) {
            model.setRowCount(0);
            students.clear();
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                Student s = new Student(p[0], p[1], p[2], p[3],
                        Integer.parseInt(p[4]), Integer.parseInt(p[5]));
                students.add(s);
                model.addRow(new Object[]{s.name, s.reg, s.roll, s.branch, s.total,
                        s.bunks, String.format("%.2f", s.percent), s.safeBunks});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading file!");
        }
    }

    private void createPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem info = new JMenuItem("📊 Show Attendance Info");
        JMenuItem motivate = new JMenuItem("💬 Motivation Boost");

        menu.add(info);
        menu.add(motivate);

        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { show(e); }
            public void mouseReleased(MouseEvent e) { show(e); }

            void show(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int r = table.rowAtPoint(e.getPoint());
                    table.setRowSelectionInterval(r, r);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        info.addActionListener(e -> showInfo());
        motivate.addActionListener(e -> motivate());
    }

    private void showInfo() {
        int r = table.getSelectedRow();
        if (r == -1) return;
        String name = model.getValueAt(r, 0).toString();
        String percent = model.getValueAt(r, 6).toString();
        String safe = model.getValueAt(r, 7).toString();
        JOptionPane.showMessageDialog(this,
                "📊 " + name + "'s Attendance\n\nAttendance: " + percent + "%\nSafe Bunks Left: " + safe,
                "Attendance Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void motivate() {
        int r = table.getSelectedRow();
        if (r == -1) return;
        String name = model.getValueAt(r, 0).toString();
        String[] quotes = {
                name + ", keep shining! 🌸",
                "You’re doing amazing, " + name + "! 💪",
                "Small steps every day, " + name + "!",
                "Believe in yourself, " + name + " 💫"
        };
        JOptionPane.showMessageDialog(this, quotes[new Random().nextInt(quotes.length)],
                "💬 Motivation Boost", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        nameField.setText("");
        regField.setText("");
        rollField.setText("");
        bunkField.setText("");
    }

    class Student {
        String name, reg, roll, branch;
        int total, bunks;
        double percent;
        int safeBunks;

        Student(String n, String r, String rl, String b, int t, int bu) {
            name = n; reg = r; roll = rl; branch = b; total = t; bunks = bu;
            percent = ((double)(t - bu) / t) * 100;
            safeBunks = (int)Math.max(Math.floor(t * 0.2 - bu), 0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentAttendanceManagement().setVisible(true));
    }
}