/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.admin;

import com.view.login.Login;
import com.view.login.logout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;
import koneksi.pageUtil;

/**
 *
 * @author Asus
 */
public class admin_rab extends javax.swing.JFrame {
    private Connection conn;
    private DefaultTableModel tbl;
    /**
     * Creates new form admin_project
     */
    public admin_rab() {
        initComponents();
        setLocationRelativeTo(null);
        conn = new koneksi().getConnection();
        budgetRadioBtn.setActionCommand("Budget");
        additionalRadioBtn.setActionCommand("Additional");
        
        loadTipeComboBox();
        tabel();
    }
    
    public class ProjectModel {
    public String idProject;
    public String namaProject;
    public int jumlahRumah;
    public int type;
    public String location;

    public ProjectModel(String idProject, String namaProject, int jumlahRumah, int type, String location) {
            this.idProject = idProject;
            this.namaProject = namaProject;
            this.jumlahRumah = jumlahRumah;
            this.type = type;
            this.location = location;
        }
    }

    
    private Map<String, ProjectModel> projectMap = new HashMap<>();
    
    private String getIdRabFromProject(String idProject) {
        try {
            String sql = "SELECT id_rab FROM rab WHERE id_project = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idProject);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("id_rab");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // jika tidak ada RAB
    }


    

    private void loadTipeComboBox() {
        try {
            String sql = "SELECT * FROM project ORDER BY nama_project";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            Vector<String> tipeList = new Vector<>();
            tipeList.add("Pilih Project");

            projectMap.clear();

            while (rs.next()) {
                String idProject   = rs.getString("id_project");
                String namaProject = rs.getString("nama_project");
                int jumlahRumah    = rs.getInt("jumlah_rumah");
                int type        = rs.getInt("type");
                String location    = rs.getString("location");

                tipeList.add(namaProject);

                // simpan semua data project
                projectMap.put(namaProject, new ProjectModel(
                    idProject,
                    namaProject,
                    jumlahRumah,
                    type,
                    location
                ));
            }

            comboBoxProject.setModel(new DefaultComboBoxModel<>(tipeList));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error memuat daftar project: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    
        public void tabel() {
        tbl = new DefaultTableModel();
        tbl.addColumn("Type");
        tbl.addColumn("Category");
        tbl.addColumn("Nama Material");
        tbl.addColumn("Unit");
        tbl.addColumn("Quantity");
        tbl.addColumn("Harga");
        tbl.addColumn("Total");

        try {
            // 1. Ambil project yang dipilih di combo box
            String selectedNamaProject = comboBoxProject.getSelectedItem().toString();

            if (selectedNamaProject == null || selectedNamaProject.equals("Pilih Project")) {
                // kalau belum pilih project, bisa kosongkan table atau langsung return
                displayAreaMaterial.setModel(tbl);
                return;
            }
            
            ProjectModel data = projectMap.get(selectedNamaProject);

            // 2. Ambil id_project dari map
            String idProject = data.idProject;

            // 3. Query JOIN: isirab + rab, filter pakai id_project
            String sql = 
                "SELECT * " +
                "FROM isirab i " +
                "JOIN rab r ON i.id_rab = r.id_rab " +
                "WHERE r.id_project = ? " +
                "ORDER BY i.nama_material";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idProject);
            ResultSet rs = pst.executeQuery();

            // 4. Isi tabel
            while (rs.next()) {
                tbl.addRow(new Object[] {
                    rs.getString("type"),
                    rs.getString("category"),
                    rs.getString("nama_material"),
                    rs.getString("satuan"),
                    rs.getString("jumlah"),
                    rs.getString("harga_satuan"),
                    rs.getString("harga_total")
                });
            }

            // 5. Set model ke JTable (di luar while)
            displayAreaMaterial.setModel(tbl);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Koneksi Database Gagal: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
        
        public void tabelCategory() {
        DefaultTableModel tblCat = new DefaultTableModel();
        tblCat.addColumn("Category");
        tblCat.addColumn("Total");

        try {
            // 1. Ambil project yang dipilih
            String selectedNamaProject = comboBoxProject.getSelectedItem().toString();

            if (selectedNamaProject == null || selectedNamaProject.equals("Pilih Project")) {
                // Kalau belum pilih project → kosongkan tabel dan keluar
                displayAreaCategory.setModel(tblCat);
                return;
            }

            // 2. Ambil id_project dari map
            ProjectModel data = projectMap.get(selectedNamaProject);
            String idProject = data.idProject;

            // 3. Query aggregate per category
            String sql =
                "SELECT i.category, SUM(i.harga_total) AS total_category " +
                "FROM isirab i " +
                "JOIN rab r ON i.id_rab = r.id_rab " +
                "WHERE r.id_project = ? " +
                "GROUP BY i.category " +
                "ORDER BY i.category";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idProject);
            ResultSet rs = pst.executeQuery();

            // 4. Isi model tabel
            while (rs.next()) {
                tblCat.addRow(new Object[] {
                    rs.getString("category"),
                    rs.getBigDecimal("total_category")  // atau getString kalau mau
                });
            }

            // 5. Set ke JTable
            displayAreaCategory.setModel(tblCat);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Error memuat total per category: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
        
        private void totalCost() {
            try {
                double quantity = Double.parseDouble(quantityField.getText());
                double unitPrice = Double.parseDouble(unitPriceField.getText());
                String namaDipilih = comboBoxProject.getSelectedItem().toString();
                ProjectModel data = projectMap.get(namaDipilih);

                // ambil value radiobutton
                String type = buttonGroup1.getSelection().getActionCommand();

                double total = quantity * unitPrice;

                if (type.equals("Additional")) {
                    int jumlahRumah = data.jumlahRumah;
                    total = total / jumlahRumah;
                }

                // langsung set ke textfield
                totalCostField.setText(String.valueOf(total));

            } catch (Exception e) {
                System.out.println("Error menghitung total: " + e.getMessage());
                totalCostField.setText("0");
            }
        }
        
        public void hitung() {
        double jumlahTotal = 0;

        try {
            for (int i = 0; i < displayAreaCategory.getRowCount(); i++) {
                Object valueObj = displayAreaCategory.getValueAt(i, 1);

                if (valueObj != null) {
                    String value = valueObj.toString().trim();

                    // hilangkan format koma / titik kalau ada formatting
                    value = value.replace(",", "").replace(" ", "");

                    double amount = Double.parseDouble(value);
                    jumlahTotal += amount;
                }
            }

            this.jumlahTotal.setText(String.valueOf(jumlahTotal));

        } catch (Exception e) {
            System.out.println("Error hitung total kategori: " + e.getMessage());
            this.jumlahTotal.setText("0");
        }
    }
        
        public void hitungPermter() {
            double jumlahTotal = Double.parseDouble(this.jumlahTotal.getText());
            String namaDipilih = comboBoxProject.getSelectedItem().toString();
            ProjectModel data = projectMap.get(namaDipilih);
            double typeRumah = data.type;
            
            double perMeter = 0;
            
            double perMeterPersegi = jumlahTotal/typeRumah;
            
            this.perMeter.setText(String.valueOf(perMeterPersegi));
        }






        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        totalCostField = new javax.swing.JTextField();
        quantityField = new javax.swing.JTextField();
        unitPriceField = new javax.swing.JTextField();
        comboBoxProject = new javax.swing.JComboBox<>();
        btnSupplier = new javax.swing.JLabel();
        btnProject = new javax.swing.JLabel();
        btnMaterial = new javax.swing.JLabel();
        btnUser = new javax.swing.JLabel();
        btnReport = new javax.swing.JLabel();
        additionalRadioBtn = new javax.swing.JRadioButton();
        budgetRadioBtn = new javax.swing.JRadioButton();
        username = new javax.swing.JLabel();
        categoryField = new javax.swing.JTextField();
        namaMaterialField = new javax.swing.JTextField();
        unitField = new javax.swing.JTextField();
        perMeter = new javax.swing.JLabel();
        jumlahTotal = new javax.swing.JLabel();
        btnSearchMaterial = new javax.swing.JLabel();
        btnTambah = new javax.swing.JLabel();
        btnHapus = new javax.swing.JLabel();
        btnLogout = new javax.swing.JLabel();
        btnCreate = new javax.swing.JLabel();
        btnCancel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayAreaMaterial = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        displayAreaCategory = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalCostField.setBorder(null);
        getContentPane().add(totalCostField, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 610, 250, 30));

        quantityField.setBorder(null);
        quantityField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityFieldActionPerformed(evt);
            }
        });
        getContentPane().add(quantityField, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 460, 250, 30));

        unitPriceField.setBorder(null);
        unitPriceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitPriceFieldActionPerformed(evt);
            }
        });
        getContentPane().add(unitPriceField, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 540, 250, 30));

        comboBoxProject.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(comboBoxProject, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, 340, -1));

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

        btnUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUser.setPreferredSize(new java.awt.Dimension(172, 32));
        btnUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUserMouseClicked(evt);
            }
        });
        getContentPane().add(btnUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 208, -1, -1));

        btnReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReport.setPreferredSize(new java.awt.Dimension(172, 32));
        btnReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReportMouseClicked(evt);
            }
        });
        getContentPane().add(btnReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 448, -1, -1));

        buttonGroup1.add(additionalRadioBtn);
        additionalRadioBtn.setContentAreaFilled(false);
        additionalRadioBtn.setName("Additinal"); // NOI18N
        getContentPane().add(additionalRadioBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 180, -1, -1));

        buttonGroup1.add(budgetRadioBtn);
        budgetRadioBtn.setContentAreaFilled(false);
        budgetRadioBtn.setName("Budget"); // NOI18N
        getContentPane().add(budgetRadioBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 180, -1, -1));

        username.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        username.setText("admin");
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(1014, 76, 130, 20));

        categoryField.setBorder(null);
        categoryField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryFieldActionPerformed(evt);
            }
        });
        getContentPane().add(categoryField, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 242, 250, 30));

        namaMaterialField.setBorder(null);
        namaMaterialField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaMaterialFieldActionPerformed(evt);
            }
        });
        getContentPane().add(namaMaterialField, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 315, 214, 30));

        unitField.setBorder(null);
        getContentPane().add(unitField, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 390, 250, 30));

        perMeter.setText("12000");
        getContentPane().add(perMeter, new org.netbeans.lib.awtextra.AbsoluteConstraints(1075, 670, 110, 20));

        jumlahTotal.setText("1200");
        getContentPane().add(jumlahTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1075, 650, 110, 20));
        getContentPane().add(btnSearchMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 310, 40, 40));

        btnTambah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTambahMouseClicked(evt);
            }
        });
        getContentPane().add(btnTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 656, 60, 30));
        getContentPane().add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 656, 50, 30));

        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new java.awt.Dimension(172, 32));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 736, -1, -1));
        getContentPane().add(btnCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 730, 210, 40));
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 730, 90, 40));

        displayAreaMaterial.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(displayAreaMaterial);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 180, 530, 300));

        displayAreaCategory.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(displayAreaCategory);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 490, 530, 140));

        jLabel2.setText("jLabel2");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jLabel2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel2KeyPressed(evt);
            }
        });
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/admin/admin_addRAB.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupplierMouseClicked
        pageUtil.goTo(this, new admin_supplier());
    }//GEN-LAST:event_btnSupplierMouseClicked

    private void btnProjectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProjectMouseClicked
        // TODO add your handling code here:
        pageUtil.goTo(this, new admin_project());
    }//GEN-LAST:event_btnProjectMouseClicked

    private void btnMaterialComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_btnMaterialComponentAdded
        // TODO add your handling code here:
        pageUtil.goTo(this, new admin_material());
    }//GEN-LAST:event_btnMaterialComponentAdded

    private void btnMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMaterialMouseClicked
        // TODO add your handling code here:
        pageUtil.goTo(this, new admin_material());
    }//GEN-LAST:event_btnMaterialMouseClicked

    private void btnUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUserMouseClicked
        // TODO add your handling code here:
        pageUtil.goTo(this, new admin_user());
    }//GEN-LAST:event_btnUserMouseClicked

    private void btnReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReportMouseClicked
        // TODO add your handling code here:
        pageUtil.goTo(this, new admin_report_RAB());
    }//GEN-LAST:event_btnReportMouseClicked

    private void categoryFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryFieldActionPerformed

    private void namaMaterialFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaMaterialFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaMaterialFieldActionPerformed

    private void btnLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogoutMouseClicked
        // TODO add your handling code here:
        Login LOGIN = new Login();
        logout.logout(this, LOGIN);
    }//GEN-LAST:event_btnLogoutMouseClicked

    private void jLabel2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2KeyPressed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        tabel();
        tabelCategory();
        hitung();
        hitungPermter();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void quantityFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityFieldActionPerformed
        // TODO add your handling code here:
        totalCost();
    }//GEN-LAST:event_quantityFieldActionPerformed

    private void unitPriceFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitPriceFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitPriceFieldActionPerformed

    private void btnTambahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTambahMouseClicked
        // TODO add your handling code here:
      
    String sql = "INSERT INTO isirab " +
                 "(id_rab, id_material, type, category, nama_material, satuan, jumlah, harga_satuan, harga_total) " +
                 "VALUES (?,?,?,?,?,?,?,?,?)";
    
    String namaDipilih = comboBoxProject.getSelectedItem().toString();
    ProjectModel data = projectMap.get(namaDipilih);

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        String idRab        = getIdRabFromProject(data.idProject);
        String idMaterial   = "MTR0000";
        String category     = categoryField.getText().trim();
        String namaMaterial = namaMaterialField.getText().trim();
        String satuan       = unitField.getText().trim();
        int jumlah          = Integer.parseInt(quantityField.getText().trim());
        double hargaSatuan  = Double.parseDouble(unitPriceField.getText().trim());
        double hargaTotal   = Double.parseDouble(totalCostField.getText().trim());

        // ambil type dari radio button (Budget / Additional)
        ButtonModel selected = buttonGroup1.getSelection();
        String type;
        if (selected == null) {
            type = "Budget"; // default kalau belum ada yang kepilih
        } else {
            type = selected.getActionCommand(); // "Budget" atau "Additional"
        }

        // set parameter ke PreparedStatement
        pst.setString(1, idRab);
        pst.setString(2, idMaterial);
        pst.setString(3, type);
        pst.setString(4, category);
        pst.setString(5, namaMaterial);
        pst.setString(6, satuan);
        pst.setInt(7, jumlah);
        pst.setDouble(8, hargaSatuan);
        pst.setDouble(9, hargaTotal);

        pst.executeUpdate();

        JOptionPane.showMessageDialog(this,
            "Data material berhasil ditambahkan ke RAB",
            "Sukses",
            JOptionPane.INFORMATION_MESSAGE);

        // refresh tabel detail & category kalau kamu pakai dua tabel tadi
        tabel();          // untuk displayAreaMaterial
        tabelCategory();  // untuk displayAreaCategory
        hitung();
        hitungPermter();

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "Quantity / harga tidak valid (bukan angka).\n" + e.getMessage(),
            "Input Error",
            JOptionPane.ERROR_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this,
            "Gagal menyimpan ke isirab: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
    }


    }//GEN-LAST:event_btnTambahMouseClicked

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
            java.util.logging.Logger.getLogger(admin_rab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admin_rab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admin_rab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admin_rab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admin_rab().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton additionalRadioBtn;
    private javax.swing.JLabel btnCancel;
    private javax.swing.JLabel btnCreate;
    private javax.swing.JLabel btnHapus;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnMaterial;
    private javax.swing.JLabel btnProject;
    private javax.swing.JLabel btnReport;
    private javax.swing.JLabel btnSearchMaterial;
    private javax.swing.JLabel btnSupplier;
    private javax.swing.JLabel btnTambah;
    private javax.swing.JLabel btnUser;
    private javax.swing.JRadioButton budgetRadioBtn;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField categoryField;
    private javax.swing.JComboBox<String> comboBoxProject;
    private javax.swing.JTable displayAreaCategory;
    private javax.swing.JTable displayAreaMaterial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jumlahTotal;
    private javax.swing.JTextField namaMaterialField;
    private javax.swing.JLabel perMeter;
    private javax.swing.JTextField quantityField;
    private javax.swing.JTextField totalCostField;
    private javax.swing.JTextField unitField;
    private javax.swing.JTextField unitPriceField;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
