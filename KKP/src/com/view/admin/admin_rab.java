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
import javax.swing.event.DocumentListener;
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
    public String idMaterial, namaMaterial, satuan, price;
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
    
    public void itemTerpilihMaterial(){ 
        admin_popup_material mtr = new admin_popup_material(); 
        mtr.mtr= this;
        String id_Material= idMaterial;
        namaMaterialField.setText(namaMaterial);
        unitField.setText(satuan);
        unitPriceField.setText(price);
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
    
    private void clearForm() {
        categoryField.setText("");
        namaMaterialField.setText("");
        unitField.setText("");
        quantityField.setText("");
        unitPriceField.setText("");
        totalCostField.setText("");
        buttonGroup1.clearSelection(); // clear radio button Budget / Additional
        displayAreaMaterial.clearSelection();
    }
    
    private void clearFormAdd() {
        namaMaterialField.setText("");
        unitField.setText("");
        quantityField.setText("");
        unitPriceField.setText("");
        totalCostField.setText("");
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
        try {
            // Cek apakah kolom jumlahTotal kosong
            if (this.jumlahTotal.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Total kategori belum dihitung.", 
                    "Input Error", 
                    JOptionPane.WARNING_MESSAGE);
                perMeter.setText("0");
                return;
            }

            // Parse jumlahTotal
            double jumlahTotal = Double.parseDouble(this.jumlahTotal.getText().trim());

            // Cek apakah project sudah dipilih
            String namaDipilih = comboBoxProject.getSelectedItem().toString();
            if (namaDipilih == null || namaDipilih.equals("Pilih Project")) {
                JOptionPane.showMessageDialog(this, 
                    "Silakan pilih project terlebih dahulu.", 
                    "Input Error", 
                    JOptionPane.WARNING_MESSAGE);
                perMeter.setText("0");
                return;
            }

            // Ambil data project
            ProjectModel data = projectMap.get(namaDipilih);
            if (data == null) {
                JOptionPane.showMessageDialog(this, 
                    "Data project tidak ditemukan.", 
                    "Data Error", 
                    JOptionPane.ERROR_MESSAGE);
                perMeter.setText("0");
                return;
            }

            double typeRumah = data.type;

            // Cek pembagian 0
            if (typeRumah == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Type rumah bernilai 0. Tidak dapat menghitung per meter.", 
                    "Math Error", 
                    JOptionPane.ERROR_MESSAGE);
                perMeter.setText("0");
                return;
            }

            // Hitung hasil
            double perMeterPersegi = jumlahTotal / typeRumah;

            // Set ke field
            this.perMeter.setText(String.valueOf(perMeterPersegi));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Input tidak valid (harus angka): " + e.getMessage(),
                "Format Error", 
                JOptionPane.ERROR_MESSAGE);
            perMeter.setText("0");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Terjadi kesalahan: " + e.getMessage(),
                "Unknown Error", 
                JOptionPane.ERROR_MESSAGE);
            perMeter.setText("0");
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
        btnEdit = new javax.swing.JLabel();
        btnHapus = new javax.swing.JLabel();
        btnLogout = new javax.swing.JLabel();
        btnCreate = new javax.swing.JLabel();
        btnCancel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayAreaMaterial = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        displayAreaCategory = new javax.swing.JTable();
        btnSelect1 = new javax.swing.JLabel();
        btnSelect = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        totalCostField.setBorder(null);
        getContentPane().add(totalCostField, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 610, 250, 30));

        quantityField.setBorder(null);
        quantityField.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                quantityFieldInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
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
        getContentPane().add(comboBoxProject, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 125, 330, -1));

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

        btnSearchMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMaterialMouseClicked(evt);
            }
        });
        getContentPane().add(btnSearchMaterial, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 310, 40, 40));

        btnTambah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTambahMouseClicked(evt);
            }
        });
        getContentPane().add(btnTambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 656, 60, 30));

        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(452, 656, 50, 30));

        btnHapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHapusMouseClicked(evt);
            }
        });
        getContentPane().add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(394, 656, 50, 30));

        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new java.awt.Dimension(172, 32));
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogoutMouseClicked(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 736, -1, -1));

        btnCreate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCreateMouseClicked(evt);
            }
        });
        getContentPane().add(btnCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 730, 310, 40));
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
        displayAreaMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                displayAreaMaterialMouseClicked(evt);
            }
        });
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

        btnSelect1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSelect1MouseClicked(evt);
            }
        });
        getContentPane().add(btnSelect1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 120, 40, 30));

        btnSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSelectMouseClicked(evt);
            }
        });
        getContentPane().add(btnSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 120, 80, 30));

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
        String idMaterial   = this.idMaterial;
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

    private void btnSearchMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMaterialMouseClicked
        // TODO add your handling code here:
        admin_popup_material mtr = new admin_popup_material();
        mtr.mtr = this;
        mtr.setVisible(true);
        mtr.setResizable(false);
    }//GEN-LAST:event_btnSearchMaterialMouseClicked

    private void quantityFieldInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_quantityFieldInputMethodTextChanged
        totalCost();
    }//GEN-LAST:event_quantityFieldInputMethodTextChanged

    private void displayAreaMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_displayAreaMaterialMouseClicked
        // TODO add your handling code here:
    int row = displayAreaMaterial.getSelectedRow();
    if (row == -1) return;

    String type         = displayAreaMaterial.getValueAt(row, 0).toString();
    String category     = displayAreaMaterial.getValueAt(row, 1).toString();
    String namaMaterial = displayAreaMaterial.getValueAt(row, 2).toString();
    String unit         = displayAreaMaterial.getValueAt(row, 3).toString();
    String qty          = displayAreaMaterial.getValueAt(row, 4).toString();
    String harga        = displayAreaMaterial.getValueAt(row, 5).toString();
    String total        = displayAreaMaterial.getValueAt(row, 6).toString();

    categoryField.setText(category);
    namaMaterialField.setText(namaMaterial);
    unitField.setText(unit);
    quantityField.setText(qty);
    unitPriceField.setText(harga);
    totalCostField.setText(total);

    // INI WAJIB ADA
    if (type.equalsIgnoreCase("Budget")) {
        budgetRadioBtn.setSelected(true);
    } else if (type.equalsIgnoreCase("Additional")) {
        additionalRadioBtn.setSelected(true);
    }

    }//GEN-LAST:event_displayAreaMaterialMouseClicked

    private void btnHapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHapusMouseClicked
        // TODO add your handling code here:
            int selectedRow = displayAreaMaterial.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus.");
        return;
    }

    String selectedNamaProject = comboBoxProject.getSelectedItem().toString();
    if (selectedNamaProject.equals("Pilih Project")) {
        JOptionPane.showMessageDialog(this, "Silakan pilih project terlebih dahulu.");
        return;
    }

    int konfirmasi = JOptionPane.showConfirmDialog(
        this,
        "Yakin ingin menghapus data ini?",
        "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION
    );

    if (konfirmasi != JOptionPane.YES_OPTION) return;

    try {
        ProjectModel data = projectMap.get(selectedNamaProject);
        String idRab = getIdRabFromProject(data.idProject);

        // ambil value dari tabel
        String type         = displayAreaMaterial.getValueAt(selectedRow, 0).toString();
        String category     = displayAreaMaterial.getValueAt(selectedRow, 1).toString();
        String namaMaterial = displayAreaMaterial.getValueAt(selectedRow, 2).toString();
        String satuan       = displayAreaMaterial.getValueAt(selectedRow, 3).toString();
        int jumlah          = Integer.parseInt(displayAreaMaterial.getValueAt(selectedRow, 4).toString());
        double hargaSatuan  = Double.parseDouble(displayAreaMaterial.getValueAt(selectedRow, 5).toString());
        double hargaTotal   = Double.parseDouble(displayAreaMaterial.getValueAt(selectedRow, 6).toString());

        String sql = "DELETE FROM isirab WHERE id_rab=? AND type=? AND category=? AND nama_material=? AND satuan=? AND jumlah=? AND harga_satuan=? AND harga_total=?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, idRab);
        pst.setString(2, type);
        pst.setString(3, category);
        pst.setString(4, namaMaterial);
        pst.setString(5, satuan);
        pst.setInt(6, jumlah);
        pst.setDouble(7, hargaSatuan);
        pst.setDouble(8, hargaTotal);

        int deleted = pst.executeUpdate();

        if (deleted > 0) {
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");

            // refresh semua tampilan
            tabel();
            tabelCategory();
            hitung();
            hitungPermter();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan atau gagal dihapus.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btnHapusMouseClicked

    private void btnSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectMouseClicked
        // TODO add your handling code here:
        tabel();
        tabelCategory();
        hitung();
        hitungPermter();
        String selectedNamaProject = comboBoxProject.getSelectedItem().toString();
        ProjectModel data = projectMap.get(selectedNamaProject);
        String id = getIdRabFromProject(data.idProject);
        System.out.println(id);
    }//GEN-LAST:event_btnSelectMouseClicked

    private void btnSelect1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelect1MouseClicked
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnSelect1MouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
    int selectedRow = displayAreaMaterial.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan diubah.");
            return;
        }

        String selectedNamaProject = comboBoxProject.getSelectedItem().toString();
        if (selectedNamaProject.equals("Pilih Project")) {
            JOptionPane.showMessageDialog(this, "Silakan pilih project terlebih dahulu.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin mengubah data ini?",
            "Konfirmasi Ubah",
            JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi != JOptionPane.YES_OPTION) return;

        try {
            // 1. Ambil id_rab berdasarkan project
            ProjectModel data = projectMap.get(selectedNamaProject);
            String idRab = getIdRabFromProject(data.idProject);

            // 2. DATA LAMA dari tabel (untuk kondisi WHERE)
            String oldType         = displayAreaMaterial.getValueAt(selectedRow, 0).toString();
            String oldCategory     = displayAreaMaterial.getValueAt(selectedRow, 1).toString();
            String oldNamaMaterial = displayAreaMaterial.getValueAt(selectedRow, 2).toString();
            String oldSatuan       = displayAreaMaterial.getValueAt(selectedRow, 3).toString();
            int    oldJumlah       = Integer.parseInt(displayAreaMaterial.getValueAt(selectedRow, 4).toString());
            double oldHargaSatuan  = Double.parseDouble(displayAreaMaterial.getValueAt(selectedRow, 5).toString());
            double oldHargaTotal   = Double.parseDouble(displayAreaMaterial.getValueAt(selectedRow, 6).toString());

            // 3. DATA BARU dari form (yang sudah diedit user)
            String newCategory     = categoryField.getText().trim();
            String newNamaMaterial = namaMaterialField.getText().trim();
            String newSatuan       = unitField.getText().trim();
            int    newJumlah       = Integer.parseInt(quantityField.getText().trim());
            double newHargaSatuan  = Double.parseDouble(unitPriceField.getText().trim());
            double newHargaTotal   = Double.parseDouble(totalCostField.getText().trim());

            // ambil type baru dari radio button
            ButtonModel selected = buttonGroup1.getSelection();
            String newType;
            if (selected == null) {
                newType = "Budget"; // default
            } else {
                newType = selected.getActionCommand(); // "Budget" / "Additional"
            }

            String sql =
                "UPDATE isirab SET " +
                "type=?, category=?, nama_material=?, satuan=?, jumlah=?, harga_satuan=?, harga_total=? " +
                "WHERE id_rab=? AND type=? AND category=? AND nama_material=? AND satuan=? AND jumlah=? AND harga_satuan=? AND harga_total=?";

            PreparedStatement pst = conn.prepareStatement(sql);

            // 4. SET PARAMETER BARU (SET ...)
            pst.setString(1,  newType);
            pst.setString(2,  newCategory);
            pst.setString(3,  newNamaMaterial);
            pst.setString(4,  newSatuan);
            pst.setInt(5,     newJumlah);
            pst.setDouble(6,  newHargaSatuan);
            pst.setDouble(7,  newHargaTotal);

            // 5. SET PARAMETER LAMA (WHERE ...)
            pst.setString(8,  idRab);
            pst.setString(9,  oldType);
            pst.setString(10, oldCategory);
            pst.setString(11, oldNamaMaterial);
            pst.setString(12, oldSatuan);
            pst.setInt(13,    oldJumlah);
            pst.setDouble(14, oldHargaSatuan);
            pst.setDouble(15, oldHargaTotal);

            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah.");

                // refresh tampilan + hitungan
                tabel();
                tabelCategory();
                hitung();
                hitungPermter();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan atau gagal diubah.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Quantity / harga tidak valid (bukan angka).\n" + e.getMessage(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnEditMouseClicked

    private void btnCreateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCreateMouseClicked
        // TODO add your handling code here:
        String query = "UPDATE rab SET nama_project = ?, total = ?, permeter = ? WHERE id_rab = ?";

        try (Connection connection = koneksi.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // pastikan project dipilih
            String selectedNamaProject = comboBoxProject.getSelectedItem().toString();
            if (selectedNamaProject == null || selectedNamaProject.equals("Pilih Project")) {
                JOptionPane.showMessageDialog(this, "Silakan pilih project terlebih dahulu.");
                return;
            }

            ProjectModel data = projectMap.get(selectedNamaProject);
            if (data == null) {
                JOptionPane.showMessageDialog(this, "Data project tidak ditemukan.");
                return;
            }

            String namaProject = data.namaProject;
            String idRab = getIdRabFromProject(data.idProject);

            // parse angka dengan aman
            double total    = 0;
            double permeter = 0;

            try {
                total    = Double.parseDouble(jumlahTotal.getText().trim());
                permeter = Double.parseDouble(perMeter.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Total / permeter harus berupa angka.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // set parameter
            preparedStatement.setString(1, namaProject);
            preparedStatement.setDouble(2, total);
            preparedStatement.setDouble(3, permeter);
            preparedStatement.setString(4, idRab);  // WHERE id_project = ?

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data project berhasil diubah.");
                tabel();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengubah data.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Gagal mengubah data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCreateMouseClicked

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
    private javax.swing.JLabel btnEdit;
    private javax.swing.JLabel btnHapus;
    private javax.swing.JLabel btnLogout;
    private javax.swing.JLabel btnMaterial;
    private javax.swing.JLabel btnProject;
    private javax.swing.JLabel btnReport;
    private javax.swing.JLabel btnSearchMaterial;
    private javax.swing.JLabel btnSelect;
    private javax.swing.JLabel btnSelect1;
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
