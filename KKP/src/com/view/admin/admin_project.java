/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.admin;
import com.view.login.Login;
import com.view.login.logout;
import com.view.login.userSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;
/**
 *
 * @author Asus
 */
public class admin_project extends javax.swing.JFrame {
        private DefaultTableModel tbl;
        private Connection conn = new koneksi().getConnection();

    /**
     * Creates new form admin_project
     */
    public admin_project() {
        initComponents();
        setLocationRelativeTo(null);
        tabel();
        nama();
    }
    protected void nama(){
        try {
            String KD = userSession.getUserLogin(); // Get the employee code from the session
            String sql = "SELECT nama FROM user WHERE id_user='" + KD + "'";
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
    private String generateProjectId() {
        String nextId = "PRJ0001"; // Default value
        
        try { 
            String sql = "SELECT id_project FROM project ORDER BY id_project DESC LIMIT 1"; 
            Statement st = koneksi.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql); 
            
            if (rs.next()) {
                String idProject = rs.getString("id_project");
                if (idProject != null && idProject.startsWith("PRJ")) {
                    String angka = idProject.substring(3); // karena "PRJ" panjangnya 3
                    if (!angka.isEmpty()) {
                        try {
                            int num = Integer.parseInt(angka) + 1;
                            String Nol = "";
                            if (num < 10) {
                                Nol = "000";
                            } else if (num < 100) {
                                Nol = "00";
                            } else if (num < 1000) {
                                Nol = "0";
                            }
                            nextId = "PRJ" + Nol + num;
                        } catch (NumberFormatException e) {
                            // Jika parsing gagal, tetap gunakan default
                        }
                    }
                }
            }
        } catch(Exception e){ 
            JOptionPane.showMessageDialog(null, "Auto Number Gagal: " + e); 
        }
        
        return nextId;
    }
    
    private String generateRabId() {
        String nextId = "RAB0001"; // Default ID jika tabel masih kosong

        try {
            String sql = "SELECT id_rab FROM rab ORDER BY id_rab DESC LIMIT 1";
            Statement st = koneksi.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                String lastId = rs.getString("id_rab");

                if (lastId != null && lastId.startsWith("RAB")) {
                    String numberPart = lastId.substring(3); // ambil angka setelah "RAB"

                    if (!numberPart.isEmpty()) {
                        try {
                            int num = Integer.parseInt(numberPart) + 1;

                            // Tentukan jumlah nol-nya
                            String zeros = "";
                            if (num < 10) {
                                zeros = "000";
                            } else if (num < 100) {
                                zeros = "00";
                            } else if (num < 1000) {
                                zeros = "0";
                            }

                            nextId = "RAB" + zeros + num;

                        } catch (NumberFormatException e) {
                            // Jika format angka rusak → gunakan default RAB0001
                        }
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Auto Number RAB Gagal: " + e.getMessage());
        }

        return nextId;
    }

    
    public void tabel(){
        tbl = new DefaultTableModel();
        tbl.addColumn("ID Project");
        tbl.addColumn("Nama Project");
        tbl.addColumn("Jumlah Rumah");
        tbl.addColumn("Type");
        tbl.addColumn("Location");
        
        try {
            Statement st = koneksi.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM project");
            
            while (rs.next()){
                tbl.addRow(new Object[]{
                    rs.getString("id_project"),
                    rs.getString("nama_project"),
                    rs.getString("jumlah_rumah"),
                    rs.getString("type"),
                    rs.getString("location")
                });
            }
            displayArea.setModel(tbl); 
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Koneksi Database Gagal: " + e.getMessage());
        }
    }
    
    private void simpanData() {                                     
        // Generate ID Project dan ID RAB
        String idProject = generateProjectId();
        String idRab = generateRabId();

        String namaProject = ProjectNameField.getText().trim();
        String jumlahRumah = NumberOfUnitField.getText().trim();
        String type = TypeField.getText().trim();
        String location = LocationField.getText().trim();

        // Validasi input
        if (namaProject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Project tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            ProjectNameField.requestFocus();
            return;
        }
        if (jumlahRumah.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah Rumah tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            NumberOfUnitField.requestFocus();
            return;
        }
        if (type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Type tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            TypeField.requestFocus();
            return;
        }
        if (location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Location tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            LocationField.requestFocus();
            return;
        }

        Connection connection = null;
        PreparedStatement pstProject = null;
        PreparedStatement pstRab = null;

        try {
            connection = koneksi.getConnection();
            connection.setAutoCommit(false); // ❗ Mulai transaksi

            // ===========================
            // 1) INSERT ke tabel project
            // ===========================
            String sqlProject = 
                "INSERT INTO project (id_project, nama_project, jumlah_rumah, type, location) " +
                "VALUES (?, ?, ?, ?, ?)";

            pstProject = connection.prepareStatement(sqlProject);
            pstProject.setString(1, idProject);
            pstProject.setString(2, namaProject);
            pstProject.setString(3, jumlahRumah);
            pstProject.setString(4, type);
            pstProject.setString(5, location);
            pstProject.executeUpdate();

            // =======================
            // 2) INSERT ke tabel RAB
            // =======================
            String sqlRab = 
                "INSERT INTO rab (id_rab, id_project) VALUES (?, ?)";

            pstRab = connection.prepareStatement(sqlRab);
            pstRab.setString(1, idRab);
            pstRab.setString(2, idProject);
            pstRab.executeUpdate();

            // Commit transaksi
            connection.commit();

            JOptionPane.showMessageDialog(this, 
                "Project & RAB berhasil disimpan!\nID Project: " + idProject + "\nID RAB: " + idRab,
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
            );

            tabel();       // reload table project
            clearFields(); // bersihkan input

        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback(); // ❗ batalkan transaksi jika gagal
            } catch (SQLException ex) {}

            JOptionPane.showMessageDialog(this, 
                "Gagal menyimpan data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE
            );

        } finally {
            try {
                if (pstProject != null) pstProject.close();
                if (pstRab != null) pstRab.close();
                if (connection != null) connection.setAutoCommit(true);
            } catch (SQLException ex) {}
        }
    }

    
    private void ubahData() {                                  
        int selectedRow = displayArea.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = tbl.getValueAt(selectedRow, 0).toString();
        String namaProject = ProjectNameField.getText().trim();
        String jumlahRumah = NumberOfUnitField.getText().trim();
        String type = TypeField.getText().trim();
        String location = LocationField.getText().trim();

        // Validasi input
        if (namaProject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Project tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            ProjectNameField.requestFocus();
            return;
        }
        if (jumlahRumah.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah Rumah tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            NumberOfUnitField.requestFocus();
            return;
        }
        if (type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Type tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            TypeField.requestFocus();
            return;
        }
        if (location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Location tidak boleh kosong!", "Input Error", JOptionPane.ERROR_MESSAGE);
            LocationField.requestFocus();
            return;
        }

        String query = "UPDATE project SET nama_project = ?, jumlah_rumah = ?, type = ?, location = ? WHERE id_project = ?";
        try (Connection connection = koneksi.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, namaProject);
            preparedStatement.setString(2, jumlahRumah);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, location);
            preparedStatement.setString(5, id);
            
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data project berhasil diubah.");
                tabel();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengubah data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengubah data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                 

    private void hapusData() {                                   
        int selectedRow = displayArea.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idProject = tbl.getValueAt(selectedRow, 0).toString();
        String namaProject = tbl.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus project:\n" + namaProject + "?", 
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String query = "DELETE FROM project WHERE id_project = ?";
        try (Connection connection = koneksi.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, idProject);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                 JOptionPane.showMessageDialog(this, "Data project berhasil dihapus.");
            } else {
                JOptionPane.showMessageDialog(this, "Data project tidak ditemukan.", "Hapus Info", JOptionPane.INFORMATION_MESSAGE);
            }
            tabel();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }                                  

    private void searchData() {                                    
        DefaultTableModel searchModel = new DefaultTableModel();
        searchModel.addColumn("ID Project");
        searchModel.addColumn("Nama Project");
        searchModel.addColumn("Jumlah Rumah");
        searchModel.addColumn("Type");
        searchModel.addColumn("Location");
        
        try {
            Statement st = koneksi.getConnection().createStatement();
            String searchText = SearchField.getText().trim();
            
            String query = "SELECT * FROM project WHERE " +
                          "nama_project LIKE '%" + searchText + "%' OR " +
                          "jumlah_rumah LIKE '%" + searchText + "%' OR " +
                          "type LIKE '%" + searchText + "%' OR " +
                          "location LIKE '%" + searchText + "%' OR " +
                          "id_project LIKE '%" + searchText + "%'";
            
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                searchModel.addRow(new Object[]{
                    rs.getString("id_project"),
                    rs.getString("nama_project"),
                    rs.getString("jumlah_rumah"),
                    rs.getString("type"),
                    rs.getString("location")
                });
            }
            displayArea.setModel(searchModel);
            
            if (searchModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada data yang ditemukan.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Koneksi Database Gagal: " + e.getMessage());
        }
    }

    private void clearFields() {
        ProjectNameField.setText("");
        NumberOfUnitField.setText("");
        TypeField.setText("");
        LocationField.setText("");
        SearchField.setText("");
    }
    
    private void displayAreaMouseClicked() {                                         
        int selectedRow = displayArea.getSelectedRow();
        if (selectedRow >= 0) {
            // Get values from selected row
            String nama = tbl.getValueAt(selectedRow, 1).toString();
            String jumlah = tbl.getValueAt(selectedRow, 2).toString();
            String type = tbl.getValueAt(selectedRow, 3).toString();
            String location = tbl.getValueAt(selectedRow, 4).toString();

            // Set values to form fields (kecuali ID)
            ProjectNameField.setText(nama);
            NumberOfUnitField.setText(jumlah);
            TypeField.setText(type);
            LocationField.setText(location);
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        NumberOfUnitField = new javax.swing.JTextField();
        ProjectNameField = new javax.swing.JTextField();
        TypeField = new javax.swing.JTextField();
        LocationField = new javax.swing.JTextField();
        SearchField = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JLabel();
        btnHapus = new javax.swing.JLabel();
        btnEdit = new javax.swing.JLabel();
        btnSearch = new javax.swing.JLabel();
        btnLogout = new javax.swing.JLabel();
        btnUser = new javax.swing.JLabel();
        btnMaterial = new javax.swing.JLabel();
        btnRab = new javax.swing.JLabel();
        btnReport = new javax.swing.JLabel();
        btnSupplier = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayArea = new javax.swing.JTable();
        btnRefresh = new javax.swing.JLabel();
        Baground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NumberOfUnitField.setBorder(null);
        NumberOfUnitField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NumberOfUnitFieldActionPerformed(evt);
            }
        });
        getContentPane().add(NumberOfUnitField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 230, 264, 33));

        ProjectNameField.setBorder(null);
        getContentPane().add(ProjectNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 160, 264, 37));

        TypeField.setBorder(null);
        getContentPane().add(TypeField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 310, 270, 30));

        LocationField.setBorder(null);
        getContentPane().add(LocationField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 380, 270, 30));

        SearchField.setBorder(null);
        SearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchFieldActionPerformed(evt);
            }
        });
        getContentPane().add(SearchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 160, 230, 30));

        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanMouseClicked(evt);
            }
        });
        getContentPane().add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 430, 90, 30));

        btnHapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusMouseClicked(evt);
            }
        });
        getContentPane().add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 430, 80, 30));

        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 430, 60, 30));

        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMouseClicked(evt);
            }
        });
        getContentPane().add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 150, 40, 40));

        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 740, 120, 30));

        btnUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUserMouseClicked(evt);
            }
        });
        getContentPane().add(btnUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 160, 30));

        btnMaterial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMaterialMouseClicked(evt);
            }
        });
        getContentPane().add(btnMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 160, 30));

        btnRab.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRabMouseClicked(evt);
            }
        });
        getContentPane().add(btnRab, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 160, 30));

        btnReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
        });
        getContentPane().add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 160, 30));

        btnSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierMouseClicked(evt);
            }
        });
        getContentPane().add(btnSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 160, 30));
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 70, 140, 30));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 200, 570, 540));

        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 150, 30, 40));

        Baground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/admin/admin_Project.png"))); // NOI18N
        Baground.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BagroundMouseClicked(evt);
            }
        });
        getContentPane().add(Baground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NumberOfUnitFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NumberOfUnitFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NumberOfUnitFieldActionPerformed

    private void SearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchFieldActionPerformed

    private void BagroundMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BagroundMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BagroundMouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
     ubahData();
    }//GEN-LAST:event_btnEditMouseClicked

    private void btnHapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusMouseClicked
            hapusData();
    }//GEN-LAST:event_btnHapusMouseClicked

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        searchData();
    }//GEN-LAST:event_btnSearchMouseClicked

    private void btnUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserMouseClicked
    admin_user userPage = new admin_user();
    userPage.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnUserMouseClicked

    private void btnSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierMouseClicked
    admin_supplier supplierPage = new admin_supplier();
    supplierPage.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnSupplierMouseClicked

    private void btnMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaterialMouseClicked
    admin_material materialPage = new admin_material();
    materialPage.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnMaterialMouseClicked

    private void btnRabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRabMouseClicked
    admin_rab rabPage = new admin_rab();
    rabPage.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnRabMouseClicked

    private void btnReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseClicked
    admin_report_RAB reportPage = new admin_report_RAB();
    reportPage.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnReportMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
Login LOGIN = new Login();
logout.logout(this, LOGIN);
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseClicked
            simpanData();
    }//GEN-LAST:event_btnSimpanMouseClicked

    private void displayAreaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_displayAreaMouseClicked
        displayAreaMouseClicked();
    }//GEN-LAST:event_displayAreaMouseClicked

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        // TODO add your handling code here:
        clearFields();
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
            java.util.logging.Logger.getLogger(admin_project.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admin_project.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admin_project.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admin_project.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admin_project().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Baground;
    private javax.swing.JTextField LocationField;
    private javax.swing.JTextField NumberOfUnitField;
    private javax.swing.JTextField ProjectNameField;
    private javax.swing.JTextField SearchField;
    private javax.swing.JTextField TypeField;
    private javax.swing.JLabel btnEdit;
    private javax.swing.JLabel btnHapus;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnMaterial;
    private javax.swing.JLabel btnRab;
    private javax.swing.JLabel btnRefresh;
    private javax.swing.JLabel btnReport;
    private javax.swing.JLabel btnSearch;
    private javax.swing.JLabel btnSimpan;
    private javax.swing.JLabel btnSupplier;
    private javax.swing.JLabel btnUser;
    private javax.swing.JTable displayArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
