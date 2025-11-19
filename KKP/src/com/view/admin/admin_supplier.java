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
public class admin_supplier extends javax.swing.JFrame {
    private Connection conn = new koneksi().getConnection();
    private DefaultTableModel tabmode;
     private String currentUser;
     
    /**
     * Creates new form admin_supplier
     */
    public admin_supplier() {
      // default text kalau tidak lewat login
              initComponents();
        setLocationRelativeTo(null);


        loadTable();   // tampilkan data saat form dibuka
        kosong();
        nama();
        autonumber();
}
    
    protected void autonumber(){ 
    try { 
        String sql = "SELECT id_supplier FROM supplier order by id_supplier asc"; 
        Statement st = koneksi.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql); 
        idSupplierField.setText("SPLR0001"); 
        while (rs.next()) {
            String idKaryawan = rs.getString("id_supplier");
            if (idKaryawan.length() > 3) {
                String angka = idKaryawan.substring(4); // karena "USR" panjangnya 3
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

                    idSupplierField.setText("SPLR" + Nol + SPLR);
                }
            }
        } 
    }catch(Exception e){ 
        JOptionPane.showMessageDialog(null, "Auto Number Gagal" +e); 
    } 
    }

// constructor yang dipakai setelah login

    
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

    private void kosong() {
    idSupplierField.setText("");
    namaSupplierField.setText("");
    emailField.setText("");
    noTelpField.setText("");
    jTextArea1.setText("");
    searchField.setText("");
}

// untuk menampilkan semua data ke tabel
private void loadTable() {
    Object[] kolom = {"ID Supplier", "Nama Supplier", "Email", "No. Telp", "Alamat"};
    tabmode = new DefaultTableModel(null, kolom);
    Displayarea.setModel(tabmode);

    String sql = "SELECT * FROM supplier ORDER BY id_supplier ASC";
    try (PreparedStatement stat = conn.prepareStatement(sql);
         ResultSet rs = stat.executeQuery()) {

        while (rs.next()) {
            String a = rs.getString("id_supplier");
            String b = rs.getString("nama_supplier");
            String c = rs.getString("email");
            String d = rs.getString("no_telp");
            String e = rs.getString("alamat");

            String[] data = {a, b, c, d, e};
            tabmode.addRow(data);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Data gagal ditampilkan: " + e.getMessage());
    }
}
private void cariData(String key) {
    Object[] kolom = {"ID Supplier", "Nama Supplier", "Email", "No. Telp", "Alamat"};
    tabmode = new DefaultTableModel(null, kolom);
    Displayarea.setModel(tabmode);

    String sql = "SELECT * FROM supplier WHERE id_supplier LIKE ? OR nama_supplier LIKE ?";

    try (PreparedStatement stat = conn.prepareStatement(sql)) {
        stat.setString(1, "%" + key + "%");
        stat.setString(2, "%" + key + "%");

        try (ResultSet rs = stat.executeQuery()) {
            while (rs.next()) {
                String a = rs.getString("id_supplier");
                String b = rs.getString("nama_supplier");
                String c = rs.getString("email");
                String d = rs.getString("no_telp");
                String e = rs.getString("alamat");

                String[] data = {a, b, c, d, e};
                tabmode.addRow(data);
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Data gagal dicari: " + e.getMessage());
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

        namaSupplierField = new javax.swing.JTextField();
        noTelpField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();
        idSupplierField = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JLabel();
        btnEdit = new javax.swing.JLabel();
        btnSupplier = new javax.swing.JLabel();
        btnUser = new javax.swing.JLabel();
        btnHapus = new javax.swing.JLabel();
        btnProject = new javax.swing.JLabel();
        btnReport = new javax.swing.JLabel();
        btnRAB = new javax.swing.JLabel();
        alamatField = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnMaterial = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        username = new javax.swing.JLabel();
        btnLogout = new javax.swing.JLabel();
        displayarea = new javax.swing.JScrollPane();
        Displayarea = new javax.swing.JTable();
        btnSearch = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        namaSupplierField.setBorder(null);
        namaSupplierField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaSupplierFieldActionPerformed(evt);
            }
        });
        getContentPane().add(namaSupplierField, new org.netbeans.lib.awtextra.AbsoluteConstraints(271, 233, 270, 30));

        noTelpField.setBorder(null);
        noTelpField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noTelpFieldActionPerformed(evt);
            }
        });
        getContentPane().add(noTelpField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 379, 270, 30));

        emailField.setBorder(null);
        emailField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailFieldActionPerformed(evt);
            }
        });
        getContentPane().add(emailField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 307, 270, 30));

        idSupplierField.setBorder(null);
        idSupplierField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idSupplierFieldActionPerformed(evt);
            }
        });
        getContentPane().add(idSupplierField, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 154, 260, 39));

        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSimpanMouseClicked(evt);
            }
        });
        getContentPane().add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(272, 564, 83, 30));

        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 562, 69, 30));

        btnSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierMouseClicked(evt);
            }
        });
        getContentPane().add(btnSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 256, 168, 30));

        btnUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUserMouseClicked(evt);
            }
        });
        getContentPane().add(btnUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 209, 170, 30));

        btnHapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusMouseClicked(evt);
            }
        });
        getContentPane().add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(462, 563, 80, 30));

        btnProject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProjectMouseClicked(evt);
            }
        });
        getContentPane().add(btnProject, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 304, 170, 29));

        btnReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
        });
        getContentPane().add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 448, 168, 30));

        btnRAB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRAB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRABMouseClicked(evt);
            }
        });
        getContentPane().add(btnRAB, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 400, 170, 30));

        alamatField.setBorder(null);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(null);
        jTextArea1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextArea1MouseClicked(evt);
            }
        });
        alamatField.setViewportView(jTextArea1);

        getContentPane().add(alamatField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 450, 260, 90));

        btnMaterial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMaterialMouseClicked(evt);
            }
        });
        getContentPane().add(btnMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 352, 160, 30));

        searchField.setBorder(null);
        searchField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchFieldMouseClicked(evt);
            }
        });
        getContentPane().add(searchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 157, 230, 30));

        username.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usernameMouseClicked(evt);
            }
        });
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 77, 60, 20));

        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 740, 90, 30));

        Displayarea.setModel(new javax.swing.table.DefaultTableModel(
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
        Displayarea.setCellSelectionEnabled(true);
        Displayarea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DisplayareaMouseClicked(evt);
            }
        });
        displayarea.setViewportView(Displayarea);

        getContentPane().add(displayarea, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 200, 610, 560));

        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMouseClicked(evt);
            }
        });
        getContentPane().add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 150, 40, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/admin/admin_Supplier.png"))); // NOI18N
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idSupplierFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idSupplierFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idSupplierFieldActionPerformed

    private void namaSupplierFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaSupplierFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaSupplierFieldActionPerformed

    private void emailFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailFieldActionPerformed

    private void noTelpFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noTelpFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noTelpFieldActionPerformed

    private void jTextArea1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextArea1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextArea1MouseClicked

    private void btnSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSimpanMouseClicked
       String sql = "INSERT INTO supplier(id_supplier, nama_supplier, email, no_telp, alamat) "
               + "VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement stat = conn.prepareStatement(sql)) {

        stat.setString(1, idSupplierField.getText());
        stat.setString(2, namaSupplierField.getText());
        stat.setString(3, emailField.getText());
        stat.setString(4, noTelpField.getText());
        stat.setString(5, jTextArea1.getText());

        stat.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data berhasil disimpan");

        loadTable();
        kosong();
        autonumber();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Data gagal disimpan: " + e.getMessage());
    }
    }//GEN-LAST:event_btnSimpanMouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
       String sql = "UPDATE supplier SET nama_supplier=?, email=?, no_telp=?, alamat=? "
               + "WHERE id_supplier=?";

    try (PreparedStatement stat = conn.prepareStatement(sql)) {

        stat.setString(1, namaSupplierField.getText());
        stat.setString(2, emailField.getText());
        stat.setString(3, noTelpField.getText());
        stat.setString(4, jTextArea1.getText());
        stat.setString(5, idSupplierField.getText());

        stat.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data berhasil diubah");

        loadTable();
        kosong();
        autonumber();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Data gagal diubah: " + e.getMessage());
    }
    }//GEN-LAST:event_btnEditMouseClicked

    private void btnHapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusMouseClicked
        int ok = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data ini?", "Konfirmasi",
            JOptionPane.YES_NO_OPTION);

    if (ok == JOptionPane.YES_OPTION) {
        String sql = "DELETE FROM supplier WHERE id_supplier=?";

        try (PreparedStatement stat = conn.prepareStatement(sql)) {

            stat.setString(1, idSupplierField.getText());
            stat.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus");

            loadTable();
            kosong();
            autonumber();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Data gagal dihapus: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_btnHapusMouseClicked

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

    private void btnProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProjectMouseClicked
        admin_project projectPage = new admin_project();
        projectPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnProjectMouseClicked

    private void btnRABMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRABMouseClicked
        admin_rab rabPage = new admin_rab();
        rabPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRABMouseClicked

    private void btnReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseClicked
        admin_report_RAB reportPage = new admin_report_RAB();
        reportPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnReportMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        Login LOGIN = new Login();
        logout.logout(this, LOGIN);
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void searchFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchFieldMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldMouseClicked

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        String key = searchField.getText().trim();
    if (key.equals("")) {
        loadTable();   // kalau kosong, tampilkan semua
    } else {
        cariData(key);
    }
    }//GEN-LAST:event_btnSearchMouseClicked

    private void DisplayareaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DisplayareaMouseClicked
        int baris = Displayarea.getSelectedRow();
    if (baris >= 0) {
        idSupplierField.setText(Displayarea.getValueAt(baris, 0).toString());
        namaSupplierField.setText(Displayarea.getValueAt(baris, 1).toString());
        emailField.setText(Displayarea.getValueAt(baris, 2).toString());
        noTelpField.setText(Displayarea.getValueAt(baris, 3).toString());
        jTextArea1.setText(Displayarea.getValueAt(baris, 4).toString());
    }
    }//GEN-LAST:event_DisplayareaMouseClicked

    private void usernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameMouseClicked

    private void btnMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaterialMouseClicked
        admin_material materialPage = new admin_material();
        materialPage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnMaterialMouseClicked

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
            java.util.logging.Logger.getLogger(admin_supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admin_supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admin_supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admin_supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admin_supplier().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Displayarea;
    private javax.swing.JScrollPane alamatField;
    private javax.swing.JLabel btnEdit;
    private javax.swing.JLabel btnHapus;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnMaterial;
    private javax.swing.JLabel btnProject;
    private javax.swing.JLabel btnRAB;
    private javax.swing.JLabel btnReport;
    private javax.swing.JLabel btnSearch;
    private javax.swing.JLabel btnSimpan;
    private javax.swing.JLabel btnSupplier;
    private javax.swing.JLabel btnUser;
    private javax.swing.JScrollPane displayarea;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField idSupplierField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField namaSupplierField;
    private javax.swing.JTextField noTelpField;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
