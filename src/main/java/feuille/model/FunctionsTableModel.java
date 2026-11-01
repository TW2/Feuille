package feuille.model;

import feuille.io.js.Function;
import feuille.util.Loader;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class FunctionsTableModel extends DefaultTableModel {

    // Private list,
    // if you want to enter a value please use setValue
    // if you want to retrieve a value please use getValue
    private final List<Function> functions;

    public FunctionsTableModel() {
        super();
        functions = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        if(functions == null || functions.isEmpty()) {
            return 0;
        }else{
            return functions.size();
        }
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 0;
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch(column) {
            case 0 -> { return functions.get(row).isActive(); }
            case 1 -> { return functions.get(row).getName(); }
            case 2 -> { return functions.get(row).getCategory(); }
            default -> { return functions.get(row); }
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if(aValue instanceof Function v) {
            if(row == -1){
                functions.add(v);
            }else{
                functions.set(row, v);
            }
        }
    }

    public void removeRow(int row){
        functions.remove(row);
        fireTableDataChanged();
    }

    public boolean hasFunction(Function fun) {
        return functions.contains(fun);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Boolean.class : String.class;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0 -> { return Loader.language("model.active", "Active"); }
            case 1 -> { return Loader.language("model.name", "Name"); }
            case 2 -> { return Loader.language("model.category", "Category"); }
            default -> { return null; }
        }
    }
}
