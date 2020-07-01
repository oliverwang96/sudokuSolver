import java.util.ArrayList;

public class Cell<T> implements Comparable<Cell<T>> {

    private boolean isFilled;

    private int id;
    private int rowId;
    private int columnId;
    private int boxId;

    private Row<T> parentRow;
    private Column<T> parentColumn;
    private Box<T> parentBox;

    private T value;
    private ArrayList<T> possibleValues;

    public Cell(boolean filled, int id, ArrayList<T> itemsList) {
        this.isFilled = filled;
        this.id = id;
        this.possibleValues = new ArrayList<T>(itemsList);
        //System.out.println("Cell created with ID " + id);
    }

    public void fill(T value) {
        this.setValue(value);
        this.setFilled(true);
        this.possibleValues.clear();
        this.possibleValues.add(value);

    }

    public void shallowFill(T value) {
        this.setValue(value);
    }

    public void shallowUnfill() {
        this.setValue(null);
        this.isFilled = false;
    }

    public boolean removePossibleValues(T possibleValue) {
        // Returns:
        // True if the item was removed from the cell's possibleValues items list. It was in the list before.
        // False if the item was not removed the Cell's list. It already was in the list.


        // Returning a false from this method notifies the solver to NOT add the item back to
        // possibleValues when backtracking.
        if (this.possibleValues.contains(possibleValue)) {
            this.possibleValues.remove(possibleValue);
            return true;
        } else {
            return false;
        }
    }


    public ArrayList<T> getPossibleValues() {
        return possibleValues;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public int getId() {
        return id;
    }

    public T getValue() {
        return value;
    }

    public int getRowId() {
        return rowId;
    }

    public int getColumnId() {
        return columnId;
    }

    public int getBoxId() {
        return boxId;
    }

    public Row<T> getParentRow() {
        return parentRow;
    }

    public Column<T> getParentColumn() {
        return parentColumn;
    }

    public Box<T> getParentBox() {
        return parentBox;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public void setParentRow(Row<T> parentRow) {
        this.parentRow = parentRow;
    }

    public void setParentColumn(Column<T> parentColumn) {
        this.parentColumn = parentColumn;
    }

    public void setParentBox(Box<T> parentBox) {
        this.parentBox = parentBox;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int compareTo(Cell<T> cell) {

        // This comparator imposes ordering that are inconsistent with equals.
        // Compares two cells based on list of possible values, a cell with less possible values will be smaller.
        // For cells with the same number of possible values, each of its possible values will be compared.
        if (this.getPossibleValues().size() < cell.getPossibleValues().size()) {
            return -1;
        } else if (this.getPossibleValues().size() > cell.getPossibleValues().size()) {
            return 1;
        } else {
            int numPossibleValues = this.getPossibleValues().size();

            for (int i = 0; i < numPossibleValues; i ++) {
                if ( (Integer) this.getPossibleValues().get(i) < (Integer) cell.getPossibleValues().get(i)) {
                    return -1;
                } else if ((Integer) this.getPossibleValues().get(i) > (Integer) cell.getPossibleValues().get(i)) {
                    return 1;
                }
            }
            return 0;
        }
    }
}
