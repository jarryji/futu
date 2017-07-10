package gui.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public abstract class FadingTableModel extends AbstractTableModel implements ActionListener
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private Timer _timer;
    private FadeCellRenderer _cellRenderer;
    protected JTable _table;
    Method _rowMethod;
    Method _colMethod;

    public FadingTableModel()
    {
        _timer = new Timer(1000, this);
        _timer.setRepeats(true);
        _timer.start();
        _cellRenderer = new FadeCellRenderer();
        initIndexAdjusters();
    }

    protected Color getColorAt(int rowIndex, int columnIndex)
    {
        if (isSuspect())
            return Color.GRAY;
        else if (isUpdated(rowIndex, columnIndex))
            return Color.RED;
        else
            return Color.BLACK;
    }

    public void actionPerformed(ActionEvent e)
    {
        fade();
    }

    public TableCellRenderer getCellRenderer()
    {
        return _cellRenderer;
    }

    private void initIndexAdjusters()
    {
        // Indexes have to be adjusted when a TableRowSorter is used,
        // which is only with JDK 1.6 or later.
        try
        {
            Class<JTable> tableClass = javax.swing.JTable.class;
            Class<?>[] argClass = { Integer.TYPE };
            _rowMethod = tableClass.getMethod("convertRowIndexToModel", argClass);
            _colMethod = tableClass.getMethod("convertColumnIndexToModel", argClass);
        }
        catch (Exception e)
        {
        }
    }

    protected int invoke(JTable table, Method m, int index)
    {
        if (m == null)
            return index; // not JDK 1.6 or later
        try
        {
            Object[] oArr2 = { new Integer(index) };
            Integer newIndex = (Integer)m.invoke(table, oArr2);
            return newIndex.intValue();
        }
        catch (Exception e)
        {
        }
        {
        }
        return index;
    }

    public abstract void fade();

    public abstract boolean isUpdated(int rowIndex, int columnIndex);

    public abstract boolean isSuspect();

    class FadeCellRenderer extends JLabel implements TableCellRenderer
    {
        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column)
        {
            table.convertColumnIndexToModel(column);
            int r = invoke(table, _rowMethod, row);
            int c = invoke(table, _colMethod, column);
            Color color = getColorAt(r, c);
            setForeground(color);
            setText(value.toString());
            return this;
        }

    }
}
