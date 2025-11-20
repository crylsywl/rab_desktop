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
import java.sql.DriverManager;
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
public class admin_material extends javax.swing.JFrame {

    private Connection conn = new koneksi().getConnection();
    private DefaultTableModel tabmode;
    private String currentUser;
    /**
     * Creates new form admin_project
     */
    
    public admin_material(){
        initComponents();
        setLocationRelativeTo(null);
        loadTable();   // tampilkan data saat form dibuka
        clearForm();
        nama();
        autonumber();
    }
     protected void autonumber(){ 
    try { 
        String sql = "SELECT id_material FROM material order by id_material asc"; 
        Statement st = koneksi.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql); 
        idMaterialField.setText("MTR0001"); 
        while (rs.next()) {
            String idKaryawan = rs.getString("id_material");
            if (idKaryawan.length() > 3) {
                String angka = idKaryawan.substring(3); // karena "USR" panjangnya 3
                if (!angka.isEmpty()) {
                    int SPLR = Integer.parseInt(angka) + 1;

                    String Nol = "";
                    if (SPLR < 10) {
                        Nol = "000";
                    } else if (SPLR < 100) {
                        Nol = "00";
                    } else if (SPLR < 1000) {
                        Nol = "0";
                    }

                    idMaterialField.setText("MTR" + Nol + SPLR);
                }
            }
        } 
    }catch(Exception e){ 
        JOptionPane.showMessageDialog(null, "Auto Number Gagal" +e); 
    } 
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
    
    
  
    
     private void clearForm() {
        idMaterialField.setText("");
        supplierNameField.setText("");
        materialNameField.setText("");
        specificationField.setText("");
        unitField.setText("");
        stockField.setText("");
        priceField.setText("");
        searchField.setText("");
    }

    // ====== METHOD UNTUK LOAD DATA KE JTABLE ======
    private void loadTable() {
        String[] kolom = {
            "ID Material", "Nama Supplier", "Nama Material",
            "Spesifikasi", "Satuan", "Stok", "Price"
        };
        tabmode = new DefaultTableModel(null, kolom);
        jTable1.setModel(tabmode);

        if (conn == null) {
            return;
        }

        String sql = "SELECT id_material, nama_supplier, nama_material, "
                   + "spesifikasi, satuan, stok, price FROM material";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id_material");
                String supplier = rs.getString("nama_supplier");
                String material = rs.getString("nama_material");
                String spesifikasi = rs.getString("spesifikasi");
                String satuan = rs.getString("satuan");
                String stok = rs.getString("stok");
                String price = rs.getString("price");

                String[] data = {id, supplier, material, spesifikasi, satuan, stok, price};
                tabmode.addRow(data);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menampilkan data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchData(String key) {
        String[] kolom = {
            "ID Material", "Nama Supplier", "Nama Material",
            "Spesifikasi", "Satuan", "Stok", "Price"
        };
        tabmode = new DefaultTableModel(null, kolom);
        jTable1.setModel(tabmode);

        if (conn == null) {
            return;
        }

        String sql = "SELECT id_material, nama_supplier, nama_material, "
                + "spesifikasi, satuan, stok, price FROM material "
                + "WHERE id_material LIKE ? OR nama_supplier LIKE ? "
                + "OR nama_material LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String keyword = "%" + key + "%";
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id_material");
                    String supplier = rs.getString("nama_supplier");
                    String material = rs.getString("nama_material");
                    String spesifikasi = rs.getString("spesifikasi");
                    String satuan = rs.getString("satuan");
                    String stok = rs.getString("stok");
                    String price = rs.getString("price");

                    String[] data = {id, supplier, material, spesifikasi, satuan, stok, price};
                    tabmode.addRow(data);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mencari data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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

        btnUser = new javax.swing.JLabel();
        btnSupplier = new javax.swing.JLabel();
        btnProject = new javax.swing.JLabel();
        btnMaterial = new javax.swing.JLabel();
        btnRab = new javax.swing.JLabel();
        btnReport = new javax.swing.JLabel();
        btnLogout = new javax.swing.JLabel();
        idMaterialField = new javax.swing.JTextField();
        supplierNameField = new javax.swing.JTextField();
        materialNameField = new javax.swing.JTextField();
        specificationField = new javax.swing.JTextField();
        unitField = new javax.swing.JTextField();
        stockField = new javax.swing.JTextField();
        priceField = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JLabel();
        btnEdit = new javax.swing.JLabel();
        btnHapus = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        btnSearch = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnRefresh = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUserMouseClicked(evt);
            }
        });
        getContentPane().add(btnUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 160, 30));

        btnSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierMouseClicked(evt);
            }
        });
        getContentPane().add(btnSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 257, 160, 30));

        btnProject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProjectMouseClicked(evt);
            }
        });
        getContentPane().add(btnProject, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 303, 170, 30));

        btnMaterial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(btnMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 352, 170, 30));

        btnRab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRabMouseClicked(evt);
            }
        });
        getContentPane().add(btnRab, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 400, 170, 30));

        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
        });
        getContentPane().add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 448, 170, 30));

        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 733, 100, 40));

        idMaterialField.setBorder(null);
        idMaterialField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idMaterialFieldActionPerformed(evt);
            }
        });
        getContentPane().add(idMaterialField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 160, 270, 30));

        supplierNameField.setBorder(null);
        supplierNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierNameFieldActionPerformed(evt);
            }
        });
        getContentPane().add(supplierNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 233, 270, 30));

        materialNameField.setBorder(null);
        materialNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                materialNameFieldActionPerformed(evt);
            }
        });
        getContentPane().add(materialNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 307, 270, 30));

        specificationField.setBorder(null);
        specificationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                specificationFieldActionPerformed(evt);
            }
        });
        getContentPane().add(specificationField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 380, 270, 30));

        unitField.setBorder(null);
        unitField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitFieldActionPerformed(evt);
            }
        });
        getContentPane().add(unitField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 456, 270, 30));

        stockField.setBorder(null);
        stockField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockFieldActionPerformed(evt);
            }
        });
        getContentPane().add(stockField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 529, 270, 30));

        priceField.setBorder(null);
        priceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceFieldActionPerformed(evt);
            }
        });
        getContentPane().add(priceField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 603, 270, 30));

        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanMouseClicked(evt);
            }
        });
        getContentPane().add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(269, 652, 90, 30));

        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 652, 70, 30));

        btnHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusMouseClicked(evt);
            }
        });
        getContentPane().add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(463, 653, 80, 30));

        searchField.setBorder(null);
        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });
        getContentPane().add(searchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(618, 158, 230, 30));

        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMouseClicked(evt);
            }
        });
        getContentPane().add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(852, 152, 40, 40));

        username.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usernameMouseClicked(evt);
            }
        });
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 76, 100, 20));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 220, 590, 530));

        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 150, 30, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/admin/admin_Material.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idMaterialFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idMaterialFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idMaterialFieldActionPerformed

    private void supplierNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierNameFieldActionPerformed

    private void materialNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_materialNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_materialNameFieldActionPerformed

    private void specificationFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_specificationFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_specificationFieldActionPerformed

    private void unitFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitFieldActionPerformed

    private void stockFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stockFieldActionPerformed

    private void priceFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceFieldActionPerformed

    private void btnSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseClicked
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Tidak ada koneksi ke database");
            return;
        }

        String id = idMaterialField.getText().trim();
        String supplier = supplierNameField.getText().trim();
        String material = materialNameField.getText().trim();
        String spesifikasi = specificationField.getText().trim();
        String satuan = unitField.getText().trim();
        String stok = stockField.getText().trim();
        String price = priceField.getText().trim();

        if (id.isEmpty() || supplier.isEmpty() || material.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "ID Material, Nama Supplier, dan Nama Material wajib diisi",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO material "
                + "(id_material, nama_supplier, nama_material, spesifikasi, satuan, stok, price) "
                + "VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, supplier);
            ps.setString(3, material);
            ps.setString(4, spesifikasi);
            ps.setString(5, satuan);
            ps.setInt(6, Integer.parseInt(stok));
            ps.setBigDecimal(7, new java.math.BigDecimal(price));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            clearForm();
            loadTable();
            autonumber();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Stok harus angka dan Price harus angka (desimal)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSimpanMouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Tidak ada koneksi ke database");
            return;
        }

        String id = idMaterialField.getText().trim();
        String supplier = supplierNameField.getText().trim();
        String material = materialNameField.getText().trim();
        String spesifikasi = specificationField.getText().trim();
        String satuan = unitField.getText().trim();
        String stok = stockField.getText().trim();
        String price = priceField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data di tabel atau isi ID Material untuk di-update",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "UPDATE material SET "
                + "nama_supplier=?, nama_material=?, spesifikasi=?, satuan=?, stok=?, price=? "
                + "WHERE id_material=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplier);
            ps.setString(2, material);
            ps.setString(3, spesifikasi);
            ps.setString(4, satuan);
            ps.setInt(5, Integer.parseInt(stok));
            ps.setBigDecimal(6, new java.math.BigDecimal(price));
            ps.setString(7, id);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate");
            clearForm();
            loadTable();
            autonumber();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Stok harus angka dan Price harus angka (desimal)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mengupdate data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditMouseClicked

    private void btnHapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusMouseClicked
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Tidak ada koneksi ke database");
            return;
        }

        String id = idMaterialField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data di tabel atau isi ID Material yang akan dihapus",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opsi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus data dengan ID: " + id + "?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (opsi == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM material WHERE id_material=?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                clearForm();
                loadTable();
                autonumber();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnHapusMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        Login LOGIN = new Login();
        logout.logout(this, LOGIN);
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        String key = searchField.getText().trim();
        if (key.isEmpty()) {
            loadTable();
        } else {
            searchData(key);
        }
    }//GEN-LAST:event_btnSearchMouseClicked

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        String key = searchField.getText().trim();
        if (key.isEmpty()) {
            loadTable();
        } else {
            searchData(key);
        }
    }//GEN-LAST:event_searchFieldActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int row = jTable1.getSelectedRow();
        if (row == -1) return;

        idMaterialField.setText(tabmode.getValueAt(row, 0).toString());
        supplierNameField.setText(tabmode.getValueAt(row, 1).toString());
        materialNameField.setText(tabmode.getValueAt(row, 2).toString());
        specificationField.setText(tabmode.getValueAt(row, 3).toString());
        unitField.setText(tabmode.getValueAt(row, 4).toString());
        stockField.setText(tabmode.getValueAt(row, 5).toString());
        priceField.setText(tabmode.getValueAt(row, 6).toString());
    }//GEN-LAST:event_jTable1MouseClicked

    private void usernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameMouseClicked

    private void btnReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseClicked
        admin_report_RAB reportPage = new admin_report_RAB();
        reportPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnReportMouseClicked

    private void btnRabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRabMouseClicked
        admin_rab rabPage = new admin_rab();
        rabPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRabMouseClicked

    private void btnProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProjectMouseClicked
        admin_project projectPage = new admin_project();
        projectPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnProjectMouseClicked

    private void btnSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierMouseClicked
       admin_supplier supplierPage = new admin_supplier();
        supplierPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSupplierMouseClicked

    private void btnUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserMouseClicked
        admin_user userPage = new admin_user();
        userPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnUserMouseClicked

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        // TODO add your handling code here:
        clearForm();
        loadTable();
        autonumber();
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
            java.util.logging.Logger.getLogger(admin_material.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admin_material.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admin_material.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admin_material.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admin_material().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnEdit;
    private javax.swing.JLabel btnHapus;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnMaterial;
    private javax.swing.JLabel btnProject;
    private javax.swing.JLabel btnRab;
    private javax.swing.JLabel btnRefresh;
    private javax.swing.JLabel btnReport;
    private javax.swing.JLabel btnSearch;
    private javax.swing.JLabel btnSimpan;
    private javax.swing.JLabel btnSupplier;
    private javax.swing.JLabel btnUser;
    private javax.swing.JTextField idMaterialField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField materialNameField;
    private javax.swing.JTextField priceField;
    private javax.swing.JTextField searchField;
    private javax.swing.JTextField specificationField;
    private javax.swing.JTextField stockField;
    private javax.swing.JTextField supplierNameField;
    private javax.swing.JTextField unitField;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
