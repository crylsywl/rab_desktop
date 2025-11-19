/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koneksi;

/**
 *
 * @author Asus
 */
public class pageUtil {

    public static void goTo(javax.swing.JFrame currentPage, javax.swing.JFrame nextPage) {
        nextPage.setVisible(true);
        currentPage.dispose();
    }


}
