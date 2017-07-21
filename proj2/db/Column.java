package db;

import java.util.ArrayList;

public class Column {

    private String name;
    private ArrayList<Object> elements;
    private String type;
    private boolean changedFloat = false;

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
        this.elements = new ArrayList<>();
        this.elements.add(name + " " + type);
    }

    /**get methods**/
    public String getName() {
        return this.name;
    }
    public ArrayList getElements() {
        return this.elements;
    }
    public String getType() {
        return this.type;
    }
    public boolean getChangedFloat() {
        return this.changedFloat;
    }

    /**set methods**/
    public void setName(String name) {
        this.name = name;
    }
    public void setElements(ArrayList<Object> elements) {
        this.elements = elements;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setChangedFloat(boolean changedFloat) {
        this.changedFloat = changedFloat;
    }

    /**
     * Inserts an element as the last element of the column.
     * @param x element that is added
     */
    public void insertRowElement(Object x) {
        this.elements.ensureCapacity(1);
        this.elements.add(x);
    }

    public void removeRowElement(int index) {
        this.elements.remove(index);
    }

    public Column copy() {
        ArrayList<Object> copyElements = new ArrayList<>();
        for (int i = 0; i < this.getElements().size(); i++) {
            copyElements.add(this.elements.get(i));
        }

        Column copyCol = new Column(this.getName(), this.getType());
        copyCol.setElements(copyElements);
        return copyCol;
    }

    public ArrayList join(Column col) {
        ArrayList<Integer> index = new ArrayList<>();
        //for (Integer x: col.elements) {
        for (int i = 1; i < col.elements.size(); i++) {
            if (this.elements.contains(col.elements.get(i))) {
                index.add(i);
            }
        }
        return index;
    }
}
