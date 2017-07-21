package db;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Table {
    private String name;
    private LinkedHashMap<String, Column> columns;
    private int numRow;
    private int numCol;

    public Table(String name, LinkedHashMap<String, String> col2Type) {
        this.name = name;
        this.columns = new LinkedHashMap<>();
        this.numRow = 1;
        if (col2Type != null) {
            for (String key : col2Type.keySet()) {
                this.columns.put(key, new Column(key, col2Type.get(key)));
                this.numCol += 1;
            }
        }
    }

    public String getName() {
        return this.name;
    }
    public LinkedHashMap<String, Column> getColumns() {
        return this.columns;
    }
    public int getNumRow() {
        return this.numRow;
    }
    public int getNumCol() {
        return this.numCol;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setNumRow(int numRow) {
        this.numRow = numRow;
    }
    public void setNumCol(int numCol) {
        this.numCol = numCol;
    }


    /**
     * Inserts a column to the table by inserting the entire column to columns
     */
    public void insertColumn(Column column) {
        this.columns.put(column.getName(), column);
        this.numCol += 1;
        if (this.numCol == 1) {
            this.numRow = column.getElements().size();
        }
    }

    /**
     * Inserts a row to the table by inserting an element to the end of each column.
     * @param row list of elements to be inserted to the table
     */
    public void insertRow(Object[] row) {
        int i = 0;
        for (Column col : this.columns.values()) {
            col.insertRowElement(row[i]);
            i++;
        }
        this.numRow += 1;
    }

    /**
     *
     */
    private void removeRow(int index) {
        for (Column col : this.columns.values()) {
            col.removeRowElement(index);
        }
        this.numRow -= 1;
    }

    /**
     * Prints a string representation of the table
     */
    public String print() {
        try {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < this.numRow; i++) {
                for (Column col : this.columns.values()) {
                    Object add;
                    if (col.getType().equals("float") && (i > 0)
                            && !(col.getElements().get(i)).equals("NaN")
                            && !(col.getElements().get(i)).equals("NOVALUE")) {
                        add = col.getElements().get(i);
                        add = String.format("%.03f", add);
                    } else {
                        add = col.getElements().get(i);
                    }
                    sb.append(add + ",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("\n");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();

        } catch (StringIndexOutOfBoundsException e) {
            return "ERROR: string index out of bounds";
        }
    }


    public void conditional(String condition) {
        String[] compare = condHelper(condition).split(" ");
        boolean comp1 = this.getColumns().containsKey(compare[0]);
        boolean comp2 = this.getColumns().containsKey(compare[2]);
        Column c1 = new Column(null, null);
        Column c2 = new Column(null, null);

        if (comp1 && comp2) {   // binary conditions: col compared to col
            c1 = this.getColumns().get(compare[0]);
            c2 = this.getColumns().get(compare[2]);
        } else if (comp1 && !comp2) {   // unary conditions: col compared to other
            c1 = this.getColumns().get(compare[0]);
            if (compare[2].contains("'")) {
                c2 = new Column(null, "string");
                for (int i = 0; i < c1.getElements().size(); i++) {
                    c2.insertRowElement(compare[2]);
                }
            } else if (compare[2].contains(".")) {
                c2 = new Column(null, "float");
                for (int i = 0; i < c1.getElements().size(); i++) {
                    c2.insertRowElement(Float.parseFloat(compare[2]));
                }
            } else {
                c2 = new Column(compare[0], "int");
                for (int i = 0; i < c1.getElements().size(); i++) {
                    c2.insertRowElement(Integer.parseInt(compare[2]));
                }
            }
        }
        cond(c1, c2, compare[1]);
    }

    private void cond(Column c1, Column c2, String compare) {
        Float term1 = null;
        Float term2 = null;
        for (int i = 1; i < this.numRow; i++) {
            if (c1.getType().equals("string") && c2.getType().equals("string")) {
                String string1 = (String) c1.getElements().get(i);
                String string2 = (String) c2.getElements().get(i);
                Integer difference = string2.compareTo(string1);
                if (string1.equals("NaN") && string2.equals("NaN")) {
                    difference = 0;
                } else if (string1.equals("NaN")) {
                    difference = -1;
                } else if (string2.equals("NaN")) {
                    difference = 1;
                }
                term2 = (float) difference;
                term1 = (float) 0.0;
            } else {
                if (c1.getElements().get(i).equals("NOVALUE")) {
                    this.removeRow(i);
                    continue;
                } else if (c1.getElements().get(i).equals("NaN")) {
                    if (c2.getType().equals("int")) {
                        Integer int2 = (int) c2.getElements().get(i);
                        term2 = (float) int2;
                    }
                    if (c2.getType().equals("float")) {
                        term2 = (float) c2.getElements().get(i);
                    }
                    if (c2.getElements().get(i).equals("NaN")) {
                        term1 = term2;
                    } else {
                        term1 = term2 + 1;
                    }
                } else {
                    if (c1.getType().equals("int")) {
                        Integer int1 = (int) c1.getElements().get(i);
                        term1 = (float) int1;
                    } else {
                        term1 = (float) c1.getElements().get(i);
                    }
                }
                if (c2.getElements().get(i).equals("NOVALUE")) {
                    this.removeRow(i);
                    continue;
                } else if (c2.getElements().get(i).equals("NaN")) {
                    if (c1.getType().equals("int")) {
                        Integer int1 = (int) c1.getElements().get(i);
                        term1 = (float) int1;
                    }
                    if (c1.getType().equals("float")) {
                        term1 = (float) c1.getElements().get(i);
                    }
                    if (c1.getElements().get(i).equals("NaN")) {
                        term2 = term1;
                    } else {
                        term2 = term1 + 1;
                    }
                } else {
                    if (c2.getType().equals("int")) {
                        Integer int2 = (int) c2.getElements().get(i);
                        term2 = (float) int2;
                    } else {
                        term2 = (float) c2.getElements().get(i);
                    }
                }

            }
            switch (compare) {
                case "<":
                    if (term1 >= term2) {
                        this.removeRow(i);
                        i--;
                    }
                    break;
                case "<=":
                    if (term1 > term2) {
                        this.removeRow(i);
                        i--;
                    }
                    break;
                case ">":
                    if (term1 <= term2) {
                        this.removeRow(i);
                        i--;
                    }
                    break;
                case ">=":
                    if (term1 < term2) {
                        this.removeRow(i);
                        i--;
                    }
                    break;
                case "==":
                    if (!term1.equals(term2)) {
                        this.removeRow(i);
                        i--;
                    }
                    break;
                case "!=":
                    if (term1.equals(term2)) {
                        this.removeRow(i);
                        i--;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("ERROR: Invalid operator.");
            }
        }
    }

    private String condHelper(String cond) {
        cond = cond.replaceAll("<", " < ");
        cond = cond.replaceAll("<=", " <= ");
        cond = cond.replaceAll(">", " > ");
        cond = cond.replaceAll(">=", " >= ");
        cond = cond.replaceAll("==", " == ");
        cond = cond.replaceAll("< =", " <= ");
        cond = cond.replaceAll("= =", " == ");
        cond = cond.replaceAll("> =", " >= ");
        cond = cond.replaceAll("= =", " == ");
        cond = cond.replaceAll("\\s+", " ");
        return cond;
    }

// START OF JOIN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Joins table with another table, but does not change the value of this table.
     */
    public Table join(Table joinWith) {
        // create arraylist of an arraylist of indices that have equal elements
        // creates linked hashmap of unique columns
        LinkedHashMap<String, Column> uniqueCol = this.uniqueColCombo(joinWith);
        ArrayList<ArrayList> indices = new ArrayList<>();
        for (String col: joinWith.columns.keySet()) {
            if (this.columns.keySet().contains(col)) {
                indices.add(this.columns.get(col).join(joinWith.columns.get(col)));
            }
        }

        // if there are no unique columns between the columns
        if (indices.size() == 0) {
            return this.joinCartesian(joinWith);
        }

        // creates arraylist of indices that are in all similar columns
        ArrayList<Integer> index = indices.get(0);
        for (int i = 1; i < indices.size(); i++) {
            for (int j = 0; j < index.size(); j++) {
                if (!indices.get(i).contains(index.get(j))) {
                    index.remove(j);
                }
            }
        }

        // creates hashmap of columns to column types
        LinkedHashMap<String, String> col2Type = this.col2TypeCombo(joinWith);

        Table newTable = new Table(null, col2Type);
        //for (Integer i: index) {
        for (int i = 0; i < index.size(); i++) {
            Object[] row = new Object[uniqueCol.size()];
            int j = 0;
            for (Column col: uniqueCol.values()) {
                row[j] = col.getElements().get(index.get(i));
                j++;
            }
            newTable.insertRow(row);
        }
        return newTable;
    }

    /**
     * Creates a cartesian product of the table with another joinWith table
     * @param joinWith table to join with
     * @return the table created by the cartesian join
     */
    private Table joinCartesian(Table joinWith) {
        LinkedHashMap<String, String> col2Type = this.col2TypeCombo(joinWith);
        Table newTable = new Table(null, col2Type);

        for (int i = 1; i < this.numRow; i++) {
            for (int j = 1; j < joinWith.numRow; j++) {
                Object[] row = new Object[col2Type.size()];
                int a = 0;
                for (Column col : this.columns.values()) {
                    row[a] = col.getElements().get(i);
                    a++;
                }
                int b = 0;
                for (Column col : joinWith.columns.values()) {
                    row[b + this.numCol] = col.getElements().get(j);
                    b++;
                }
                newTable.insertRow(row);
            }
        }
        return newTable;
    }

    /**
     * A map of all unique column names to columns
     * @param joinWith table to perform cartesian join with
     * @return map of combined strings to columns
     */
    private LinkedHashMap<String, Column> uniqueColCombo(Table joinWith) {
        LinkedHashMap<String, Column> uniqueCol = new LinkedHashMap<>();
        for (Column col: this.columns.values()) {
            uniqueCol.put(col.getName(), col);
        }
        for (Column col: joinWith.columns.values()) {
            uniqueCol.put(col.getName(), col);
        }
        return uniqueCol;
    }

    /**
     * A map of all unique column names to column type
     * @param joinWith table to be joined with
     * @return a map of combined columns to types
     */
    private LinkedHashMap<String, String> col2TypeCombo(Table joinWith) {
        LinkedHashMap<String, String> col2Type = new LinkedHashMap<>();
        for (Column col: this.columns.values()) {
            col2Type.put(col.getName(), col.getType());
        }
        for (Column col: joinWith.columns.values()) {
            col2Type.put(col.getName(), col.getType());
        }
        return col2Type;
    }

// END OF JOIN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// START OF SELECT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Column select(String operation) {
        operation = opParseoperator(operation);
        String[] op = operation.split(" ");
        if (op.length == 1) {
            Column select = this.getColumns().get(op[0]);
            return select.copy();
        } else {
            return this.selectColsAs(operation);
        }
    }

    private Column selectColsAs(String operation) {
        String[] op = operation.split(" ");
        if (this.getColumns().containsKey(op[0]) && this.getColumns().containsKey(op[2])) {
            return this.selectTwoCol(operation);
        } else {
            if (this.getColumns().containsKey(op[0])) {
                return this.stringFirst(operation);
            } else {
                return this.stringSecond(operation);
            }
        }
    }

    private Column stringFirst(String operation) {
        String[] op = operation.split(" ");
        Column col1 = (this.getColumns().get(op[0])).copy();
        Column col2;
        Column toReturn;
        if (op[2].contains(".")) {
            col2 = new Column("operands", "float");
            if (col1.getType().equals("int")) {
                for (int i = 1; i < col1.getElements().size(); i++) {
                    int intItem = (int) col1.getElements().get(i);
                    float floatItem = (float) intItem;
                    col1.getElements().set(i, floatItem);
                }
            }
            Float operandFloat = Float.parseFloat(op[2]);
            for (int i = 1; i < col1.getElements().size(); i++) {
                col2.getElements().add(operandFloat);
            }
            toReturn = new Column(op[4], "float");
            return selectFloFlo(col1, col2, op[1].charAt(0), toReturn);
        } else if (op[2].contains("'")) {
            col2 = new Column("operands", "string");
            for (int i = 1; i < col1.getElements().size(); i++) {
                col2.insertRowElement(op[2]);
            }
            toReturn = new Column(op[4], "string");
            return selectStrStr(col1, col2, op[1].charAt(0), toReturn);
        } else {
            Integer operandInt = Integer.parseInt(op[2]);
            col2 = new Column("operands", "int");
            for (int i = 1; i < col1.getElements().size(); i++) {
                col2.getElements().add(operandInt);
            }
            if (col1.getType().equals("float")) {
                for (int i = 1; i < col1.getElements().size(); i++) {
                    int intItem = (int) col2.getElements().get(i);
                    float floatItem = (float) intItem;
                    col2.getElements().set(i, floatItem);
                }
                toReturn = new Column(op[4], "float");
                return selectFloFlo(col1, col2, op[1].charAt(0), toReturn);
            }
            toReturn = new Column(op[4], "int");
            return selectIntInt(col1, col2, op[1].charAt(0), toReturn);
        }
    }

    private Column stringSecond(String operation) {
        String[] op = operation.split(" ");
        Column col1;
        Column col2 = (this.getColumns().get(op[2])).copy();
        Column toReturn;
        if (op[0].contains(".")) {
            col1 = new Column("operands", "float");
            if (col2.getType().equals("int")) {
                for (int i = 1; i < col2.getElements().size(); i++) {
                    int intItem = (int) col2.getElements().get(i);
                    float floatItem = (float) intItem;
                    col2.getElements().set(i, floatItem);
                }
            }
            Float operandInt = Float.parseFloat(op[0]);
            for (int i = 1; i < col2.getElements().size(); i++) {
                col1.getElements().add(operandInt);
            }
            toReturn = new Column(op[4], "float");
            return selectFloFlo(col1, col2, op[4].charAt(0), toReturn);
        } else if (op[0].contains("'")) {
            col1 = new Column("operands", "string");
            for (int i = 1; i < col2.getElements().size(); i++) {
                col1.insertRowElement(op[2]);
            }
            toReturn = new Column(op[4], "string");
            return selectStrStr(col1, col2, op[1].charAt(0), toReturn);
        } else {
            Integer operandInt = Integer.parseInt(op[0]);
            col1 = new Column("operands", "int");
            for (int i = 1; i < col2.getElements().size(); i++) {
                col1.getElements().add(operandInt);
            }
            if (col2.getType().equals("float")) {
                for (int i = 1; i < col2.getElements().size(); i++) {
                    int intItem = (int) col1.getElements().get(i);
                    float floatItem = (float) intItem;
                    col1.getElements().set(i, floatItem);
                }
                toReturn = new Column(op[4], "float");
                return selectFloFlo(col1, col2, op[4].charAt(0), toReturn);
            }
            toReturn = new Column(op[4], "int");
            return selectIntInt(col1, col2, op[4].charAt(0), toReturn);
        }
    }

    private Column selectTwoCol(String operation) {
        String[] op = operation.split(" ");
        Character operator = op[1].charAt(0);
        Column col1 = this.getColumns().get(op[0]);
        Column col2 = this.getColumns().get(op[2]);
        if (col1.getType().equals("string") && col2.getType().equals("string")) {
            Column toReturn = new Column(op[4], "string");
            return this.selectStrStr(col1, col2, operator, toReturn);
        } else if (col1.getType().equals("float") || col2.getType().equals("float")) {
            columnTransform(col1, col2);
            Column toReturn = new Column(op[4], "float");
            return this.selectFloFlo(col1, col2, operator, toReturn);
        } else {
            Column toReturn = new Column(op[4], "int");
            return this.selectIntInt(col1, col2, operator, toReturn);
        }
    }

    private Column selectStrStr(Column col1, Column col2, Character operator, Column toReturn) {
        if (!operator.equals('+')) {
            throw new IllegalArgumentException("String can only be added");
        }
        Object toAdd;
        for (int i = 1; i < col1.getElements().size(); i++) {
            String op0;
            String op1;
            if (col1.getElements().get(i).equals("NOVALUE")) {
                op0 = "";
            } else if (col1.getElements().get(i).equals("NaN")) {
                toAdd = "NaN";
                toReturn.insertRowElement(toAdd);
                continue;
            } else {
                op0 = (String) col1.getElements().get(i);
            }
            if (col2.getElements().get(i).equals("NOVALUE")) {
                op1 = "";
            } else if (col2.getElements().get(i).equals("NaN")) {
                toAdd = "NaN";
                toReturn.insertRowElement(toAdd);
                continue;
            } else {
                op1 = (String) col2.getElements().get(i);
            }

            if (col1.getElements().get(i).equals("NOVALUE")
                    && col2.getElements().get(i).equals("NOVALUE")) {
                toAdd = "NOVALUE";
                toReturn.insertRowElement(toAdd);
                continue;
            }
            toAdd = (op0 + op1).replaceAll("''", "");
            toReturn.insertRowElement(toAdd);
        }
        return toReturn;
    }


    private Column selectFloFlo(Column col1, Column col2, Character op, Column toReturn) {
        Object toAdd;
        for (int i = 1; i < col1.getElements().size(); i++) {
            Float op0;
            Float op1;
            if (col1.getElements().get(i).equals("NOVALUE")) {
                op0 = (float) 0.0;
            } else if (col1.getElements().get(i).equals("NaN")) {
                toAdd = "NaN";
                toReturn.insertRowElement(toAdd);
                continue;
            } else {
                op0 = (float) col1.getElements().get(i);
            }

            if (col2.getElements().get(i).equals("NOVALUE")) {
                op1 = (float) 0.0;
            } else if (col2.getElements().get(i).equals("NaN")) {
                toAdd = "NaN";
                toReturn.insertRowElement(toAdd);
                continue;
            } else {
                op1 = (float) col2.getElements().get(i);
            }

            if (col1.getElements().get(i).equals("NOVALUE")
                    && col2.getElements().get(i).equals("NOVALUE")) {
                toAdd = "NOVALUE";
                toReturn.insertRowElement(toAdd);
                continue;
            }
            switch (op) {
                case '+':
                    toAdd = op0 +  op1;
                    break;
                case '-':
                    toAdd = op0 - op1;
                    break;
                case '*':
                    toAdd = op0 * op1;
                    break;
                case '/':
                    if (op1 == 0.0) {
                        toAdd = "NaN";
                    } else {
                        toAdd = op0 / op1;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("ERROR: Invalid operator.");
            }
            toReturn.insertRowElement(toAdd);
        }
        return toReturn;
    }

    private Column selectIntInt(Column col1, Column col2, Character op, Column toReturn) {
        Object toAdd;
        for (int i = 1; i < col1.getElements().size(); i++) {
            Integer op0;
            Integer op1;
            if (col1.getElements().get(i).equals("NOVALUE")) {
                op0 = 0;
            } else if (col1.getElements().get(i).equals("NaN")) {
                toAdd = "NaN";
                toReturn.insertRowElement(toAdd);
                continue;
            } else {
                op0 = (int) col1.getElements().get(i);
            }

            if (col2.getElements().get(i).equals("NOVALUE")) {
                op1 = 0;
            } else if (col2.getElements().get(i).equals("NaN")) {
                toAdd = "NaN";
                toReturn.insertRowElement(toAdd);
                continue;
            } else {
                op1 = (int) col2.getElements().get(i);
            }

            if (col1.getElements().get(i).equals("NOVALUE")
                    && col2.getElements().get(i).equals("NOVALUE")) {
                toAdd = "NOVALUE";
                toReturn.insertRowElement(toAdd);
                continue;
            }
            switch (op) {
                case '+':
                    toAdd = op0 + op1;
                    break;
                case '-':
                    toAdd = op0 - op1;
                    break;
                case '*':
                    toAdd = op0 * op1;
                    break;
                case '/':
                    if (op1 == 0) {
                        toAdd = "NaN";
                    } else {
                        toAdd = (int) op0 / op1;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("ERROR: Invalid operator.");
            }
            toReturn.insertRowElement(toAdd);
        }
        return toReturn;
    }

    private void columnTransform(Column col1, Column col2) {
        for (int i = 1; i < col1.getElements().size(); i++) {
            if (col1.getType().equals("int") && col2.getType().equals("float")) {
                Object floatItem;
                if (col1.getElements().get(i).equals("NOVALUE")) {
                    floatItem = "NOVALUE";
                } else if (col1.getElements().get(i).equals("NaN")) {
                    floatItem = "NaN";
                } else {
                    int intItem = (int) col1.getElements().get(i);
                    floatItem = (float) intItem;
                }
                col1.getElements().set(i, floatItem);
            }
            if (col2.getType().equals("int") && col1.getType().equals("float")) {
                Object floatItem;
                if (col2.getElements().get(i).equals("NOVALUE")) {
                    floatItem = "NOVALUE";
                } else if (col2.getElements().get(i).equals("NaN")) {
                    floatItem = "NaN";
                } else {
                    int intItem = (int) col2.getElements().get(i);
                    floatItem = (float) intItem;
                }
                col2.getElements().set(i, floatItem);
            } else {
                continue;
            }
        }
    }

    private String opParseoperator(String opr) {
        opr = opr.replaceAll("\\+", " + ");
        opr = opr.replaceAll("\\-", " - ");
        opr = opr.replaceAll("\\*", " * ");
        opr = opr.replaceAll("\\/", " / ");
        opr = opr.replaceAll("\\s+", " ");
        return opr;
    }
// END OF SELECT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}
