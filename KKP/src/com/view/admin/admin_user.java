/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.admin;

import com.view.login.Login;
import com.view.login.logout;
import com.view.login.userSession;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;
import koneksi.pageUtil;

/**
 *
 * @author Asus
 */
public class admin_user extends javax.swing.JFrame {
    private DefaultTableModel tbl;
    private Connection conn = new koneksi().getConnection();
    /**
     * Creates new form admin_user
     */
    public admin_user() {
        initComponents();
        
        setLocationRelativeTo(null);
        tabel();
        autonumber();
        nama();
    }
    
    
    
    protected void nama(){
        try {
            String KD = userSession.getUserLogin(); // Get the employee code from the session
            String sql = "SELECT `nama` FROM user WHERE `id_user`='" + KD + "'";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            if(hasil.next()){
                username.setText(hasil.getString("nama"));
            } else {
                // Optional: Handle the case where no employee is found for the given KD
                JOptionPane.showMessageDialog(null, "Employee not found for code: " + KD);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Error retrieving employee name: " + e.getMessage());
            e.printStackTrace(); // It's good practice to print the stack trace for debugging
        }
    }
    
    protected void autonumber(){ 
    try { 
        String sql = "SELECT `id_user` FROM user order by `id_user` asc"; 
        Statement st = koneksi.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql); 
        idUserField.setText("USR0001"); 
        while (rs.next()) {
            String idKaryawan = rs.getString("id_user");
            if (idKaryawan.length() > 3) {
                String angka = idKaryawan.substring(3); // karena "USR" panjangnya 3
                if (!angka.isEmpty()) {
                    int USR = Integer.parseInt(angka) + 1;

                    String Nol = "";
                    if (USR < 10) {
                        Nol = "000";
                    } else if (USR < 100) {
                        Nol = "00";
                    } else if (USR < 1000) {
                        Nol = "0";
                    }

                    idUserField.setText("USR" + Nol + USR);
                }
            }
        } 
    }catch(Exception e){ 
        JOptionPane.showMessageDialog(null, "Auto Number Gagal" +e); 
    } 
    }
    
    public void tabel(){
        tbl = new DefaultTableModel();
        tbl.addColumn("Id User");
        tbl.addColumn("Nama");
        tbl.addColumn("Username");
        
        try {
            Statement st = (Statement)  koneksi.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user");
            
            while (rs.next()){
             tbl.addRow(new Object[]{
                rs.getString("Id_user"),
                rs.getString("nama"),
                rs.getString("username"),
         
            });
             displayArea.setModel(tbl); 
        }
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Koneksi Database Gagal" + e.getMessage());
        }
    }
    
    private static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            return null; // Atau throw IllegalArgumentException
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            // Konversi byte array ke string heksadesimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error hashing password: Algoritma tidak ditemukan.", "Hashing Error", JOptionPane.ERROR_MESSAGE);
            return null; 
        }
    }
    
    private void clear() {
        // Clear the input fields
        nameField.setText("");
        userNameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        searchField.setText("");
        }
     

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLogout = new javax.swing.JLabel();
        btnEdit = new javax.swing.JLabel();
        btnSimpan2 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        username = new javax.swing.JLabel();
        userNameField = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        idUserField = new javax.swing.JTextField();
        idUserField3 = new javax.swing.JTextField();
        confirmPasswordField = new javax.swing.JPasswordField();
        passwordField = new javax.swing.JPasswordField();
        btnSupplier = new javax.swing.JLabel();
        btnProject = new javax.swing.JLabel();
        btnRAB = new javax.swing.JLabel();
        btnMaterial = new javax.swing.JLabel();
        btnReport = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayArea = new javax.swing.JTable();
        btnRefresh = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new java.awt.Dimension(172, 32));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 736, -1, -1));

        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 502, 68, 33));

        btnSimpan2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpan2MouseClicked(evt);
            }
        });
        getContentPane().add(btnSimpan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 502, 84, 33));

        btnSearch.setToolTipText("");
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMouseClicked(evt);
            }
        });
        getContentPane().add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(852, 152, 40, 40));

        searchField.setBorder(null);
        getContentPane().add(searchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 158, 210, 30));

        username.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        username.setText("admin");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(1014, 76, 130, 20));

        userNameField.setBorder(null);
        getContentPane().add(userNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 308, 250, 30));

        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanMouseClicked(evt);
            }
        });
        getContentPane().add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(269, 502, 90, 33));

        nameField.setBorder(null);
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });
        getContentPane().add(nameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 233, 250, 30));

        idUserField.setBorder(null);
        idUserField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idUserFieldActionPerformed(evt);
            }
        });
        getContentPane().add(idUserField, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 160, 250, 30));

        idUserField3.setText("jTextField1");
        idUserField3.setBorder(null);
        getContentPane().add(idUserField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 160, 250, 30));

        confirmPasswordField.setBorder(null);
        getContentPane().add(confirmPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 456, 250, 30));

        passwordField.setBorder(null);
        getContentPane().add(passwordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 382, 250, 30));

        btnSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupplier.setPreferredSize(new java.awt.Dimension(172, 32));
        btnSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierMouseClicked(evt);
            }
        });
        getContentPane().add(btnSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 256, -1, -1));

        btnProject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProject.setPreferredSize(new java.awt.Dimension(172, 32));
        btnProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProjectMouseClicked(evt);
            }
        });
        getContentPane().add(btnProject, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 304, -1, -1));

        btnRAB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRAB.setPreferredSize(new java.awt.Dimension(172, 32));
        btnRAB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRABMouseClicked(evt);
            }
        });
        getContentPane().add(btnRAB, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 400, -1, -1));

        btnMaterial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMaterial.setPreferredSize(new java.awt.Dimension(172, 32));
        btnMaterial.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                btnMaterialComponentAdded(evt);
            }
        });
        btnMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMaterialMouseClicked(evt);
            }
        });
        getContentPane().add(btnMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 352, -1, -1));

        btnReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReport.setPreferredSize(new java.awt.Dimension(172, 32));
        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
        });
        getContentPane().add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 448, -1, -1));

        displayArea.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        displayArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                displayAreaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(displayArea);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 210, 570, 530));

        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 150, 30, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/admin/admin_user.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameFieldActionPerformed

    private void btnSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierMouseClicked
        pageUtil.goTo(this, new admin_supplier());
    }//GEN-LAST:event_btnSupplierMouseClicked

    private void btnProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProjectMouseClicked
        // TODO add your handling code here:
        pageUtil.goTo(this, new admin_project());
    }//GEN-LAST:event_btnProjectMouseClicked

    private void btnMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaterialMouseClicked
        // TODO add your handling code here:
                pageUtil.goTo(this, new admin_material());
    }//GEN-LAST:event_btnMaterialMouseClicked

    private void btnReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseClicked
        // TODO add your handling code here:
                pageUtil.goTo(this, new admin_report_RAB());
    }//GEN-LAST:event_btnReportMouseClicked

    private void btnRABMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRABMouseClicked
        // TODO add your handling code here:
                pageUtil.goTo(this, new admin_rab());
    }//GEN-LAST:event_btnRABMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        // TODO add your handling code here:
        Login LOGIN = new Login();
        logout.logout(this, LOGIN);
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnMaterialComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_btnMaterialComponentAdded
        // TODO add your handling code here:
                pageUtil.goTo(this, new admin_material());
    }//GEN-LAST:event_btnMaterialComponentAdded

    private void btnSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseClicked
         
        String id = idUserField.getText();
        String nama = nameField.getText().trim();
        String username = userNameField.getText().trim();
        // Jika passwordField adalah JPasswordField, gunakan: new String(passwordField.getPassword()).trim();
        String plainPassword = passwordField.getText().trim(); 
        String confirmPassword = confirmPasswordField.getText().trim(); 
        

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "username tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }
        if (plainPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        // Validasi confirm password
        if (confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Confirm Password tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            confirmPasswordField.requestFocus();
            return;
        }

        // Validasi kesesuaian password dan confirm password
        if (!plainPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Password dan Confirm Password tidak cocok!", "Input Error", JOptionPane.ERROR_MESSAGE);
            confirmPasswordField.setText("");
            passwordField.requestFocus();
            return;
        }

        String hashedPassword = hashPassword(plainPassword);
        if (hashedPassword == null) {
            // Pesan error sudah ditampilkan oleh hashPassword()
            return;
        }

        // Id Karyawan diasumsikan AUTO_INCREMENT oleh database
        String query = "INSERT INTO user (`id_user`,`nama`,username, `password`) VALUES (?, ?, ?, ?)";
        try (Connection connection = koneksi.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, nama);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, hashedPassword);
            
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data karyawan berhasil disimpan.");
            tabel(); // Muat ulang data tabel
            clear(); // Bersihkan field input
            autonumber();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        nameField.requestFocus();
    }//GEN-LAST:event_btnSimpanMouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
        // TODO add your handling code here:
        String idText = idUserField.getText();
        String nama = nameField.getText().trim();
        String username = userNameField.getText().trim();
        String plainPassword = passwordField.getText().trim(); 
        String confirmPassword = confirmPasswordField.getText().trim(); 
        

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "username tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return;
        }
        
        String id = idText;
        String query;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = koneksi.getConnection();

            if (!plainPassword.isEmpty()) {
                // Konfirmasi sebelum ganti password
                if (!plainPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "Password dan Confirm Password tidak cocok!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    confirmPasswordField.setText("");
                    passwordField.requestFocus();
                    return;
                }
                int confirm = JOptionPane.showOptionDialog(
                    this,
                    "Apakah Anda yakin ingin mengganti password?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Yakin", "Kembali"}, // Tombol custom
                    "Kembali" // Default focus
                );

                if (confirm != JOptionPane.YES_OPTION) {
                    // Batal mengganti password
                    return;
                }

                String hashedPassword = hashPassword(plainPassword);
                if (hashedPassword == null) return;

                query = "UPDATE user SET `nama` = ?, `username` = ?, `password` = ? WHERE `id_user` = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nama);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, hashedPassword);
                preparedStatement.setString(4, id);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Password telah diganti.");
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengubah data. ID tidak ditemukan atau tidak ada perubahan.", "Update Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Hanya ubah nama
                query = "UPDATE user SET `nama` = ?, `username` = ? WHERE `id_user` = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nama);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, id);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data user berhasil diubah.");
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengubah data. ID tidak ditemukan atau tidak ada perubahan.", "Update Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            tabel();    // Muat ulang tabel
            clear();    // Bersihkan field
            autonumber();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengubah data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnEditMouseClicked

    private void btnSimpan2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpan2MouseClicked
        // TODO add your handling code here:
        String idText = idUserField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID User tidak boleh kosong untuk menghapus data!", "Input Error", JOptionPane.ERROR_MESSAGE);
            idUserField.requestFocus();
            return;
        }
        
        String id;
        try {
            id = idText;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID User harus berupa angka.", "Input Error", JOptionPane.ERROR_MESSAGE);
            idUserField.requestFocus();
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data user dengan ID: " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String query = "DELETE FROM user WHERE `id_user` = ?";
        try (Connection connection = koneksi.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                 JOptionPane.showMessageDialog(this, "Data user berhasil dihapus.");
            } else {
                JOptionPane.showMessageDialog(this, "Data user dengan ID tersebut tidak ditemukan.", "Hapus Info", JOptionPane.INFORMATION_MESSAGE);
            }
            tabel();
            clear();
            autonumber();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSimpan2MouseClicked

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        // TODO add your handling code here:
        DefaultTableModel tbl = new DefaultTableModel();
        tbl.addColumn("Id User");
        tbl.addColumn("Nama");
        tbl.addColumn("Username");
    
    try {
        Statement st = (Statement) koneksi.getConnection().createStatement();
        String searchText = searchField.getText();
        
        String query = "SELECT * FROM user WHERE `nama` LIKE '%" + searchText + "%'";
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            tbl.addRow(new Object[]{
                rs.getString("id_user"),
                rs.getString("nama"),
                rs.getString("username"),
            });
            displayArea.setModel(tbl);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Koneksi Database Gagal" + e.getMessage());
    }
    }//GEN-LAST:event_btnSearchMouseClicked

    private void idUserFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idUserFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idUserFieldActionPerformed

    private void displayAreaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_displayAreaMouseClicked
        // TODO add your handling code here:
        int selectedRow = displayArea.getSelectedRow();

        // Get values from selected row
        String id = tbl.getValueAt(selectedRow, 0).toString();
        String nama = tbl.getValueAt(selectedRow, 1).toString();
        String username = tbl.getValueAt(selectedRow, 2).toString();
//        String pass = tbl.getValueAt(selectedRow, 2).toString();

        // Set values to form fields
        idUserField.setText(id);
        nameField.setText(nama);
        userNameField.setText(username);
//        passwordField.setText(pass);
        }

        private void txtcariKeyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                tabel();
            }
    }//GEN-LAST:event_displayAreaMouseClicked

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        // TODO add your handling code here:
        clear();
        autonumber();
        tabel();
    }//GEN-LAST:event_btnRefreshMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(admin_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admin_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admin_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admin_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admin_user().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnEdit;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnMaterial;
    private javax.swing.JLabel btnProject;
    private javax.swing.JLabel btnRAB;
    private javax.swing.JLabel btnRefresh;
    private javax.swing.JLabel btnReport;
    private javax.swing.JLabel btnSearch;
    private javax.swing.JLabel btnSimpan;
    private javax.swing.JLabel btnSimpan2;
    private javax.swing.JLabel btnSupplier;
    private javax.swing.JPasswordField confirmPasswordField;
    private javax.swing.JTable displayArea;
    private javax.swing.JTextField idUserField;
    private javax.swing.JTextField idUserField3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField searchField;
    private javax.swing.JTextField userNameField;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
