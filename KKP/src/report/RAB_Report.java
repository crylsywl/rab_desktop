/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import koneksi.koneksi;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class RAB_Report {
    private Connection conn = new koneksi().getConnection();
    
    public void printRABById(String idRab, String idUser) {
        try {
            // Load file .jasper
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                getClass().getResourceAsStream("/report/RAB_Report.jasper"));
            
            // Set parameter id_rab DAN user_id
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id_rab", idRab);
            parameters.put("user_id", idUser); // TAMBAHAN: PARAMETER USER
            System.out.println(idUser);
            
            // Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, parameters, conn);
            
            // Tampilkan preview
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error printing RAB report: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Method untuk export ke PDF dengan parameter user
    public void exportRABToPDF(String idRab, String idUser, String outputPath) {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                getClass().getResourceAsStream("/report/RAB_Report.jasper"));
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id_rab", idRab);
            parameters.put("user_id", idUser); // TAMBAHAN: PARAMETER USER
            System.out.println(idUser);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, parameters, conn);
            
            // Export ke PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
            JOptionPane.showMessageDialog(null, 
                "Report berhasil diexport ke: " + outputPath);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error exporting PDF: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}