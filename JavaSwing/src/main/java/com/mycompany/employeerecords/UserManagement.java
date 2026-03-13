/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords;
import com.mycompany.employeerecords.service.PasswordRenderer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
/**
 *
 * @author Hanna
 */
public class UserManagement extends JFrame{
    private JTable table;
    private DefaultTableModel model;

    private final String FILE_PATH = "src/docs/EmployeeAccounts.csv";

    public UserManagement() {

        setTitle("MotorPH - User Management");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245,247,250));
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("MotorPH User Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));

        add(title, BorderLayout.NORTH);

        // Table Model
        model = new DefaultTableModel(
                new String[]{"Employee Number", "Password"},0);

        table = new JTable(model);

        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN,14));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD,14));
        table.getTableHeader().setBackground(new Color(52,152,219));
        table.getTableHeader().setForeground(Color.WHITE);

        // Password column hidden
        table.getColumnModel()
                .getColumn(1)
                .setCellRenderer(new PasswordRenderer());

        loadUserAccounts();

        JScrollPane scroll = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
        tablePanel.setBackground(Color.WHITE);

        tablePanel.add(scroll,BorderLayout.CENTER);

        add(tablePanel,BorderLayout.CENTER);

        // Buttons
        JButton addBtn = createButton("+ Add User", new Color(52,152,219));
        JButton editBtn = createButton("Edit", new Color(46,204,113));
        JButton deleteBtn = createButton("Delete", new Color(231,76,60));
        JButton saveBtn = createButton("Save", new Color(155,89,182));

        addBtn.addActionListener(e -> addUser());
        editBtn.addActionListener(e -> editUser());
        deleteBtn.addActionListener(e -> deleteUser());
        saveBtn.addActionListener(e -> saveUsers());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        buttonPanel.setBackground(new Color(245,247,250));

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(saveBtn);

        add(buttonPanel,BorderLayout.SOUTH);

        setVisible(true);
    }
    
         private JButton createButton(String text, Color color){

            JButton btn = new JButton(text);
            btn.setFocusPainted(false);
            btn.setBackground(color);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI",Font.BOLD,13));
            btn.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));

            return btn;
        }
    
    
        private void loadUserAccounts() {

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;
            boolean skipHeader = true;

            while((line = br.readLine()) != null){

                if(skipHeader){
                    skipHeader = false;
                    continue;
                }

                String[] parts = line.split(",");

                model.addRow(new Object[]{
                        parts[0],
                        parts[1]
                });
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Error loading users.");
        }
    }
        
        private void addUser(){

        JTextField empField = new JTextField();
        JTextField passField = new JTextField();

        Object[] message = {
                "Employee Number:", empField,
                "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Add User", JOptionPane.OK_CANCEL_OPTION);

        if(option == JOptionPane.OK_OPTION){

            model.addRow(new Object[]{
                    empField.getText(),
                    passField.getText()
            });
        }
    }
        
        private void editUser(){

        int row = table.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a user first.");
            return;
        }

        String emp = model.getValueAt(row,0).toString();
        String pass = model.getValueAt(row,1).toString();

        JTextField empField = new JTextField(emp);
        JTextField passField = new JTextField(pass);

        Object[] message = {
                "Employee Number:", empField,
                "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Edit User", JOptionPane.OK_CANCEL_OPTION);

        if(option == JOptionPane.OK_OPTION){

            model.setValueAt(empField.getText(),row,0);
            model.setValueAt(passField.getText(),row,1);
        }
    }
        
        private void deleteUser(){

        int row = table.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select a user to delete.");
            return;
        }

        model.removeRow(row);
    }
        
        private void saveUsers(){

        try(PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))){

            pw.println("EmployeeNumber,Password");

            for(int i=0;i<model.getRowCount();i++){

                String emp = model.getValueAt(i,0).toString();
                String pass = model.getValueAt(i,1).toString();

                pw.println(emp + "," + pass);
            }

            JOptionPane.showMessageDialog(this,"Users saved successfully.");

        }catch(IOException e){

            JOptionPane.showMessageDialog(this,"Error saving users.");
        }
    }
}
