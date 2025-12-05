/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.admin;

import com.view.login.Login;
import com.view.login.logout;
import koneksi.pageUtil;
import com.view.admin.admin_user;
import com.view.login.userSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;
import report.RAB_Report;

/**
 *
 * @author Asus
 */
public class admin_report_RAB extends javax.swing.JFrame {
        private Connection conn = new koneksi().getConnection();
            private DefaultTableModel tbl;
    /**
     * Creates new form admin_project
     */
    public admin_report_RAB() {
        initComponents();
        nama();
        loadRABData(); 
        setLocationRelativeTo(null);
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
    
    /**
     * Method untuk load data RAB ke table
     */
    protected void loadRABData(){
        tbl = new DefaultTableModel();
        tbl.addColumn("ID RAB");
        tbl.addColumn("ID Project");
        tbl.addColumn("Nama Project");
        tbl.addColumn("Total");
        tbl.addColumn("Per Meter");
        
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM rab");
            
            while (rs.next()){
                tbl.addRow(new Object[]{
                    rs.getString("id_rab"),
                    rs.getString("id_project"),
                    rs.getString("nama_project"),
                    rs.getBigDecimal("total"),
                    rs.getBigDecimal("permeter")
                });
            }
            displayAreaRab.setModel(tbl);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading RAB data: " + e.getMessage());
        }
    }
    
    /**
     * Method untuk mendapatkan selected row dan menampilkan di printField
     */
    private void getSelectedRow() {
        int row = displayAreaRab.getSelectedRow();
        if (row >= 0) {
            String idRab = displayAreaRab.getValueAt(row, 0).toString();
            printField.setText(idRab);
        } else {
            JOptionPane.showMessageDialog(null, "Pilih RAB yang akan di-print!");
        }
    }
    
    /**
     * Method untuk handle print button
     */
    private void printSelectedRAB() {
    int row = displayAreaRab.getSelectedRow();
    if (row >= 0) {
        String idRab = displayAreaRab.getValueAt(row, 0).toString();
        String namaProject = displayAreaRab.getValueAt(row, 2).toString();
        
        printField.setText(idRab);
        
        // Ambil data user (id dan nama)
        Map<String, String> userMap = getUserMap(); // Key: nama, Value: id_user
        
        if (userMap.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tidak ada user yang ditemukan!");
            return;
        }
        
        // Buat array nama untuk ditampilkan di combo box
        String[] userNames = userMap.keySet().toArray(new String[0]);
        JComboBox<String> userComboBox = new JComboBox<>(userNames);
        
        int result = JOptionPane.showConfirmDialog(
            null, 
            userComboBox, 
            "Pilih User untuk Print", 
            JOptionPane.OK_CANCEL_OPTION
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String selectedUserName = (String) userComboBox.getSelectedItem();
            String selectedUserId = userMap.get(selectedUserName);
            printRABReport(idRab, selectedUserId, namaProject);
        }
        
    } else {
        JOptionPane.showMessageDialog(null, "Pilih RAB yang akan di-print!");
    }
}

private Map<String, String> getUserMap() {
    Map<String, String> userMap = new LinkedHashMap<>(); // LinkedHashMap untuk menjaga urutan
    
    try {
        Connection conn = koneksi.getConnection();
        String sql = "SELECT id_user, nama FROM user ORDER BY nama";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            String id = rs.getString("id_user");
            String nama = rs.getString("nama");
            userMap.put(nama, id); // Key: nama, Value: id_user
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error mengambil data user: " + e.getMessage());
    }
    
    return userMap;
}
    
    /**
     * Method untuk print report (akan diimplementasikan nanti)
     */
    private void printRABReport(String idRab,String idUser, String namaProject) {
        // Implementasi print report menggunakan JasperReports
        try {
            // Code untuk generate report akan ditambahkan di sini
            
        new RAB_Report().printRABById(idRab, idUser);
            JOptionPane.showMessageDialog(null, 
                "Print RAB: " + idRab + "\n" +
                "Project: " + namaProject);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error printing report: " + e.getMessage());
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

        btnPrint = new javax.swing.JLabel();
        printField = new javax.swing.JTextField();
        btnLogout = new javax.swing.JLabel();
        btnUser = new javax.swing.JLabel();
        btnSupplier = new javax.swing.JLabel();
        btnProject = new javax.swing.JLabel();
        btnMaterial = new javax.swing.JLabel();
        btnRab = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayAreaRab = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrintMouseClicked(evt);
            }
        });
        getContentPane().add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(828, 172, 76, 35));

        printField.setBorder(null);
        printField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printFieldActionPerformed(evt);
            }
        });
        getContentPane().add(printField, new org.netbeans.lib.awtextra.AbsoluteConstraints(583, 172, 242, 35));

        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 740, 160, 35));

        btnUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUserMouseClicked(evt);
            }
        });
        getContentPane().add(btnUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 160, 35));

        btnSupplier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupplierMouseClicked(evt);
            }
        });
        getContentPane().add(btnSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 160, 35));

        btnProject.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProjectMouseClicked(evt);
            }
        });
        getContentPane().add(btnProject, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 160, 35));

        btnMaterial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMaterialMouseClicked(evt);
            }
        });
        getContentPane().add(btnMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 160, 35));

        btnRab.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRabMouseClicked(evt);
            }
        });
        getContentPane().add(btnRab, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 160, 35));

        username.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        username.setText("ADMIN");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 70, 90, 30));

        displayAreaRab.setModel(new javax.swing.table.DefaultTableModel(
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
        displayAreaRab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                displayAreaRabMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(displayAreaRab);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 230, 910, 510));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/admin/admin_ReportRAB.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void printFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_printFieldActionPerformed

    private void btnUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserMouseClicked
        pageUtil.goTo(this, new admin_user());        // TODO add your handling code here:
    }//GEN-LAST:event_btnUserMouseClicked

    private void btnSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierMouseClicked
        pageUtil.goTo(this, new admin_supplier());        // TODO add your handling code here:
    }//GEN-LAST:event_btnSupplierMouseClicked

    private void btnProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProjectMouseClicked
        pageUtil.goTo(this, new admin_project());        // TODO add your handling code here:
    }//GEN-LAST:event_btnProjectMouseClicked

    private void btnMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaterialMouseClicked
        pageUtil.goTo(this, new admin_material());        // TODO add your handling code here:
    }//GEN-LAST:event_btnMaterialMouseClicked

    private void btnRabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRabMouseClicked
        pageUtil.goTo(this, new admin_rab());        // TODO add your handling code here:
    }//GEN-LAST:event_btnRabMouseClicked

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        // TODO add your handling code here:
        Login LOGIN = new Login();
        logout.logout(this, LOGIN);        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void displayAreaRabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_displayAreaRabMouseClicked
        getSelectedRow();        // TODO add your handling code here:
    }//GEN-LAST:event_displayAreaRabMouseClicked

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        printSelectedRAB();        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintMouseClicked

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
            java.util.logging.Logger.getLogger(admin_report_RAB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admin_report_RAB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admin_report_RAB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admin_report_RAB.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admin_report_RAB().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnMaterial;
    private javax.swing.JLabel btnPrint;
    private javax.swing.JLabel btnProject;
    private javax.swing.JLabel btnRab;
    private javax.swing.JLabel btnSupplier;
    private javax.swing.JLabel btnUser;
    private javax.swing.JTable displayAreaRab;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField printField;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
