package gui.model;

import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;

public class TableCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        //if(column > 1 && column < 6)
        //{
        	cellComponent.setBackground(java.awt.Color.white);
        	cellComponent.setForeground(java.awt.Color.black);
        //} else 
        //{
        //	cellComponent.setBackground(java.awt.Color.lightGray);
        //	cellComponent.setForeground(java.awt.Color.darkGray);
        //}
        
        return cellComponent;
    }
}
