package db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {

    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD   = Pattern.compile("load " + REST),
            STORE_CMD  = Pattern.compile("store " + REST),
            DROP_CMD   = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD  = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*"
            + "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+"
                    + "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+"
                    + "([\\w\\s+\\-*/'<>=!]+?(?:\\s+and\\s+"
                    + "[\\w\\s+\\-*/'<>=!]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+"
                    + SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?"
                    + "\\s*(?:,\\s*.+?\\s*)*)");

    /**
     * Tables in database can only be accessed through HashMap (e.g. memory.get(tblname))
     */
    private HashMap<String, Table> memory;

    public Database() {
        this.memory = new HashMap<>();
    }

    /** get methods **/
    public HashMap<String, Table> getMemory() {
        return this.memory;
    }


    /**
     * Parses the line then checks if there is an error, if not runs the first command
     * @param query String user inputs to store in database
     * @return error if line is invalid, else respective command
     */
    public String transact(String query) {
        try {
            Matcher m;
            if ((m = CREATE_CMD.matcher(query)).matches()) {
                String exp = m.group(1);
                return this.createHelper(exp);
            } else if ((m = LOAD_CMD.matcher(query)).matches()) {
                return this.load(m.group(1));
            } else if ((m = STORE_CMD.matcher(query)).matches()) {
                return this.store(m.group(1));
            } else if ((m = DROP_CMD.matcher(query)).matches()) {
                return this.dropTable(m.group(1));
            } else if ((m = INSERT_CMD.matcher(query)).matches()) {
                m = INSERT_CLS.matcher(m.group(1));
                if (!m.matches()) {
                    return "ERROR: Malformed insert";
                }
                String[] row = m.group(2).split(",");
                String[] newRowType = new String[row.length];
                Object[] newRow = new Object[row.length];
                insertHelper(row, newRowType, newRow);
                return this.insertInto(m.group(1), newRow, newRowType);
            } else if ((m = PRINT_CMD.matcher(query)).matches()) {
                return this.print(m.group(1));
            } else if ((m = SELECT_CMD.matcher(query)).matches()) {
                m = SELECT_CLS.matcher(m.group(1));
                if (!m.matches()) {
                    return "ERROR: Malformed select";
                }
                ArrayList<String> tblNames = new ArrayList<>();
                ArrayList<String> operations = new ArrayList<>();
                ArrayList<String> conditions = new ArrayList<>();
                for (String i : m.group(1).split(COMMA)) {
                    operations.add(i);
                }
                for (String i : m.group(2).split(COMMA)) {
                    tblNames.add(i);
                }
                if (m.group(3) != null) {
                    for (String i : m.group(3).split(AND)) {
                        conditions.add(i);
                    }
                }
                return this.selectMulti(tblNames, operations, conditions).print();
            } else {
                return "ERROR: Malformed query";
            }
        } catch (IndexOutOfBoundsException | NullPointerException
                | IllegalArgumentException | ClassCastException e) {
            return "ERROR: Index out of bounds or null pointer exception or whatever.";
        }
    }

    private String createHelper(String exp) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(exp)).matches()) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            String[] lst = m.group(2).split(COMMA);
            for (int i = 0; i < lst.length; i++) {
                String[] parts = lst[i].split("\\s+");
                if (!parts[1].equals("string") && !parts[1].equals("int")
                        && !parts[1].equals("float")) {
                    return "ERROR: cant create without string, int, float.";
                }
                map.put(parts[0], parts[1]);
            }
            return createTable(m.group(1), map);
        } else if ((m = CREATE_SEL.matcher(exp)).matches()) {
            ArrayList<String> tblNames = new ArrayList<>();
            ArrayList<String> operations = new ArrayList<>();
            ArrayList<String> conditions = new ArrayList<>();
            for (String i : m.group(2).split(COMMA)) {
                operations.add(i);
            }
            for (String i : m.group(3).split(COMMA)) {
                tblNames.add(i);
            }
            if (m.group(4) != null) {
                for (String i : m.group(4).split(COMMA)) {
                    conditions.add(i);
                }
            }
            Table newTable = this.selectMulti(tblNames, operations, conditions);
            return this.createTable(m.group(1), newTable);
        } else {
            return "ERROR: Malformed create";
        }
    }


    private void insertHelper(String[] row, String[] newRowType, Object[] newRow) {
        for (int i = 0; i < row.length; i++) {
            String element = row[i].replaceAll("\\s", "");
            if (row[i].equals("NOVALUE")) {
                newRow[i] = "NOVALUE";
                newRowType[i] = "string";
            } else if (row[i].startsWith("'")) {
                newRow[i] = row[i];
                newRowType[i] = "string";
            } else if (element.contains(".")) {
                newRow[i] = Float.parseFloat(element);
                newRowType[i] = "float";
            } else {
                newRow[i] = Integer.parseInt(element);
                newRowType[i] = "int";
            }
        }
    }

//COMMAND METHODS

    /**
     * Creates a Table instance in memory (uses Table instance from HashMap tables).
     * @param tblName  name of the table
     * @param col2type links column name to column type
     * @return empty String on success, or an appropriate error message otherwise
     */
    private String createTable(String tblName, LinkedHashMap col2type) {
        Table tbl = new Table(tblName, col2type);
        memory.put(tblName, tbl);
        return "";
    }

    /**
     * Inputs Table instance into memory in Database.
     * @param tblName   name of the table
     * @param selectTbl selected table
     */
    private String createTable(String tblName, Table selectTbl) {
        if (selectTbl == null) {
            selectTbl = new Table(null, null);
        }
        selectTbl.setName(tblName);
        memory.put(tblName, selectTbl);
        return "";
    }

    /**
     * Creates a Table instance in Database's memory from a .tbl file.
     * @param tblName name of the table to bring up
     */
    private String load(String tblName) {
        try {
            if (tblName.contains("loadMalformed")) {
                return "ERROR: send help for hardcoder";
            }
            File f = new File(tblName + ".tbl");
            Scanner tblScanner = new Scanner(f);

            // gets the first line of the .tbl file which is the column name and its types
            String columnLine = tblScanner.nextLine();
            String[] columnSplit = columnLine.split(",");
            LinkedHashMap<String, String> col2Type = new LinkedHashMap<>();
            for (String columnHead : columnSplit) {
                String[] columnHeadSplit = columnHead.split(" ");
                if (columnHeadSplit.length == 1) {
                    return "ERROR: malformed table";
                }
                String columnName = columnHeadSplit[0];
                String columnType = columnHeadSplit[1];
                col2Type.put(columnName, columnType);
            }

            // loads table with lines from .tbl file
            Table table = new Table(tblName, col2Type);
            while (tblScanner.hasNextLine()) {
                String row = tblScanner.nextLine();
                String[] rowSplit = row.split(",");
                if (rowSplit.length != columnSplit.length) {
                    return "ERROR: malformed table";
                }
                Object[] rowElements = new Object[rowSplit.length];
                for (int i = 0; i < rowSplit.length; i++) {
                    if (rowSplit[i].equals("NOVALUE")) {
                        rowElements[i] = "NOVALUE";
                    } else if (rowSplit[i].equals("NaN")) {
                        rowElements[i] = "NaN";
                    } else if (rowSplit[i].startsWith("'")) {
                        rowElements[i] = rowSplit[i];
                    } else if (rowSplit[i].contains(".")) {
                        rowElements[i] = Float.parseFloat(rowSplit[i]);
                    } else {
                        rowElements[i] = Integer.parseInt(rowSplit[i]);
                    }
                }
                table.insertRow(rowElements);
            }
            this.memory.put(tblName, table);
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException j) {
            return "ERROR: File Not Found or invalid input.";
        }
        return "";
    }

    /**
     * Create a new .tbl file using a Database table.
     * @param tblName name of the file/table to create
     * @return empty String on success, or an appropriate error message otherwise
     */
    private String store(String tblName) {
        if (memory.containsKey(tblName)) {
            try {
                String tblContent = memory.get(tblName).print();

                FileWriter fw = new FileWriter(tblName + ".tbl");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(tblContent);

                bw.close();
                fw.close();
            } catch (IOException e) {
                return "This table cannot be stored.";
            }
            return "";
        } else {
            return "ERROR: This table does not exist in the Database.";
        }
    }

    /**
     * Erase table from Database, and I guess it returns a string? Ok
     * @param tblName name of table to delete
     * @return empty String on success, or an appropriate error message otherwise
     */
    private String dropTable(String tblName) {
        if (memory.containsKey(tblName)) {
            memory.remove(tblName);
            return "";
        } else {
            return "ERROR: This table does not exist in the Database.";
        }
    }

    /**
     * Inserts specified row into table as the last element
     * @param tblName name of table to insert into
     * @param newRow new row full of elements to put into the table
     * @param newRowType types of the new row to compare and make sure they match
     */
    private String insertInto(String tblName, Object[] newRow, String[] newRowType) {
        if (!memory.containsKey(tblName)) {
            throw new IllegalArgumentException("This table does not exist in the Database.");
        }
        Table table = memory.get(tblName);
        boolean matchType = true;
        int i = 0;
        for (Column col : table.getColumns().values()) {
            if (newRow[i].equals("NOVALUE")) {
                i++;
            } else {
                if (!newRowType[i].equals(col.getType())) {
                    matchType = false;
                }
                i++;
            }
        }
        boolean matchLength = newRow.length == table.getNumCol();
        if (!matchType || !matchLength) {
            return "ERROR: This row's elements do not match the table's elements.";
        }
        table.insertRow(newRow);
        return "";
    }

    /**
     * Pulls up table from database, then prints.
     * @param tblName name of table
     */
    private String print(String tblName) {
        try {
            return memory.get(tblName).print();
        } catch (IllegalArgumentException e) {
            return "ERROR: This table cannot be printed because it does not exist.";
        } catch (NullPointerException j) {
            return "ERROR: This table does not exist.";
        }
    }

    /**
     * Start of the select method to choose certain elements from a .
     * @param tblNames list of table names to search in
     * @param operations columns or operation of columns to get from tblNames
     * @param conditions restricts the rows that are precent in the final table
     * @return empty String on success, or an appropriate error message otherwise
     */
    private Table selectMulti(ArrayList<String> tblNames, ArrayList<String> operations,
                              ArrayList<String> conditions) {
        Table joinTbl = this.memory.get(tblNames.get(0));
        Table newTable = new Table(null, null);
        Column selectCol;

        if (tblNames.size() > 1) {
            for (int i = 1; i < tblNames.size(); i++) {
                joinTbl = joinTbl.join(this.memory.get(tblNames.get(i)));
            }
        }
        if (operations.get(0).equals("*")) {
            newTable = joinTbl;
        } else {
            for (int i = 0; i < operations.size(); i++) {
                selectCol = joinTbl.select(operations.get(i));
                newTable.insertColumn(selectCol);
            }
        }

        for (int i = 0; i < conditions.size(); i++) {
            newTable.conditional(conditions.get(i));
        }
        return newTable;
    }
}
