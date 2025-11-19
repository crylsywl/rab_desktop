/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view.login;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Asus
 */
public class logout {
     public static void logout(JFrame currentWindow, JFrame loginFrame) {
        int confirm = JOptionPane.showConfirmDialog(
            currentWindow,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Tampilkan form login
            loginFrame.setVisible(true);
            loginFrame.setLocationRelativeTo(null);
            
            // Tutup window saat ini
            currentWindow.dispose();
            
            JOptionPane.showMessageDialog(
                loginFrame,
                "Logout berhasil!",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
