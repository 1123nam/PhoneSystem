/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phonesystem.edu.ultil;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;

/**
 *
 * @author HienTran
 */
public class TableEdit {

    public static void centerRendererTable(JTable tbl) {
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        int column = tbl.getColumnCount();
//        for (int i = 0; i < column; i++) {
//            tbl.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//        }

        tbl.getTableHeader().setBackground(new Color(97, 88, 152));
        tbl.getTableHeader().setForeground(new Color(240, 240, 240));
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        tbl.setFont(new Font("Sans Serif",Font.PLAIN,13));
    }
}
