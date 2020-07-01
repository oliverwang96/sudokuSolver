import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GroupedItems<T> {

    protected ArrayList<Cell<T>> cellsList;
    protected ArrayList<T> availableItems;
    protected ArrayList<T> filledItems;
    protected ArrayList<T> unfilledItems;


    public GroupedItems(ArrayList<T> availableItems) {
        // Does not initialize cellsList, will be added later.
        // Does not need to initialize filledItems, since it should be empty
        // Unfilled item is the same as availableItems.
        this.cellsList = new ArrayList<>();
        this.availableItems = new ArrayList<>(availableItems);
        this.filledItems = new ArrayList<>();
        this.unfilledItems = new ArrayList<>(availableItems);

    }

    protected void addCell (Cell<T> cell) {
        this.cellsList.add(cell);
    }

    public boolean addFilledItems(T item) {

        // Returns:
        // True if the item was added to the GroupedItem's filled items list. It was not in the list before.
        // False if the item was not added the GroupedItem's list. It already was in the list.

        // Returning a false from this method notifies the solver that an error has occured.
        // Returning a false from this method notifies the solver to NOT remove the item from
        // filledItems when backtracking.

        if (!filledItems.contains(item)) {
            this.filledItems.add(item);
            return true;
        }
        return false;
    }

    public void addUnfilledItems(T item) {
        if (unfilledItems.contains(item)) {
            this.unfilledItems.add(item);
        }
    }

    public void removeFilledItems(T item) {
        this.filledItems.remove(item);
    }

    public boolean removeUnfilledItems(T item) {

        // Returns:
        // True if the item was added to the GroupedItem's unfilled items list. It was not in the list before.
        // False if the item was not added the GroupedItem's list. It already was in the list.

        // Returning a false from this method notifies the solver that an error has occured.
        // Returning a false from this method notifies the solver to NOT add the item from
        // unfilledItems when backtracking.

        if (this.unfilledItems.contains(item)) {
            this.unfilledItems.remove(item);
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ArrayList<Cell<T>>> checkIdenticalPossibleValues(int n) {
        // Check for cells with identical list of possible values.
        // int n - check for n cells with identical lists of n possible values.

        // Check if two cells have identical list of two possible values.
        ArrayList<ArrayList<Cell<T>>> identicalPossibleValuesCellsList = new ArrayList<>();

        // Create a new ArrayList to store the cells, sorted by their list of possible values.
        // The elements (Cell objects) in this ArrayList point to the same Cell objects in memory as those Cell objects
        // in this.cellsList.
        ArrayList<Cell<T>> sortedCellsList = new ArrayList<>((ArrayList<Cell<T>>) this.cellsList);

        Collections.sort(sortedCellsList);

        for (int i = 0; i < this.cellsList.size() - n + 1; i++) {

            ArrayList<T> possibleValues1 = sortedCellsList.get(i).getPossibleValues();
            ArrayList<T> possibleValues2 = sortedCellsList.get(i+n-1).getPossibleValues();

            if (possibleValues1.size() == n && possibleValues2.size() == n && possibleValues1.containsAll(possibleValues2)) {

                ArrayList<Cell<T>> identicalPossibleValuesCells = new ArrayList<>();

                identicalPossibleValuesCells.add(sortedCellsList.get(i));
                identicalPossibleValuesCells.add(sortedCellsList.get(i+n-1));

                identicalPossibleValuesCellsList.add(identicalPossibleValuesCells);

                //System.out.println("I am here");

            }

        }

        return identicalPossibleValuesCellsList;
        /*
        if (n == 2) {
            for (int i = 0; i < this.cellsList.size()-1; i++) {

                ArrayList<T> possibleValues1 = this.cellsList.get(i).getPossibleValues();
                if (possibleValues1.size() == 2) {
                    for (int j = i+1; j < this.cellsList.size(); j++) {

                        ArrayList<T> possibleValues2 = this.cellsList.get(j).getPossibleValues();
                        if (possibleValues2.size() == 2 && possibleValues1.containsAll(possibleValues2)) {
                            ArrayList<Cell<T>> identicalPossibleValuesCells = new ArrayList<>();

                            identicalPossibleValuesCells.add(this.cellsList.get(i));
                            identicalPossibleValuesCells.add(this.cellsList.get(j));

                            identicalPossibleValuesCellsList.add(identicalPossibleValuesCells);
                        }
                    }



                }
            }
            return identicalPossibleValuesCellsList;
        }
        return identicalPossibleValuesCellsList;
        */

    }

    public Map<ArrayList<Cell<T>>,ArrayList<T>> checkSufficientPossibleValues(int n) {
        // Check is n cells has a total of n possible values.

        Map<ArrayList<Cell<T>>, ArrayList<T>> sufficientPossibleValuesCellsMap = new HashMap<>();

        ArrayList<ArrayList<Cell<T>>> sublistsList = this.findSublists(n);

        // Go through each sublist in the sublistsList.
        for (ArrayList<Cell<T>> sublist : sublistsList) {

            ArrayList<T> possibleValues = new ArrayList<>();

            // Iterate through every cell in each sublist.
            for (Cell<T> cell:sublist) {

                // Add all possibleValues of the cell to possibleValues variable, if not already in there.

                for (T possibleValue:cell.getPossibleValues()) {
                    if (!possibleValues.contains(possibleValue)) {
                        possibleValues.add(possibleValue);
                    }
                }
            }
            if (possibleValues.size() == n) {
                sufficientPossibleValuesCellsMap.put(sublist, possibleValues);
            }
        }
        return sufficientPossibleValuesCellsMap;

    }

    public ArrayList<ArrayList<Cell<T>>> findSublists (int n) {
        // Find all sublists of this.cellsList with size n.

        ArrayList<ArrayList<Cell<T>>> sublists = new ArrayList<>();

        // Hard code for each n, 2 <= n < cellsList.size()

        for (int i1 = 0; i1 < this.cellsList.size()-n+1; i1++) {
            for (int i2 = i1+1;  i2 < this.cellsList.size()-n+2; i2 ++ ) {

                ArrayList<Cell<T>> sublist = new ArrayList<>();
                sublist.add(this.cellsList.get(i1));
                sublist.add(this.cellsList.get(i2));

                if (n == 2) {
                    sublists.add(new ArrayList<>(sublist));
                }



                if (n >= 3) {

                    for (int i3 = i2+1; i3 < this.cellsList.size() - n + 3; i3++) {

                        ArrayList<Cell<T>> tempSublist3 = new ArrayList<>((ArrayList<Cell<T>>) sublist);
                        tempSublist3.add(this.cellsList.get(i3));
                        if (n == 3) {
                            sublists.add(new ArrayList<>(tempSublist3));
                        }

                        if (n >= 4) {
                            for (int i4 = i3+1; i4<this.cellsList.size() - n + 4; i4++) {
                                ArrayList<Cell<T>> tempSublist4 = new ArrayList<>((ArrayList<Cell<T>>) tempSublist3);
                                tempSublist4.add(this.cellsList.get(i4));
                                if (n == 4) {
                                    sublists.add(new ArrayList<>(tempSublist4));
                                }

                                if (n >= 5) {
                                    for (int i5 = i4+1; i5<this.cellsList.size() - n + 5; i5 ++) {

                                        ArrayList<Cell<T>> tempSublist5 = new ArrayList<>((ArrayList<Cell<T>>) tempSublist4);
                                        tempSublist5.add(this.cellsList.get(i5));
                                        if (n == 5) {
                                            sublists.add(new ArrayList<>(tempSublist5));
                                        }

                                        if (n >= 6) {
                                            for (int i6 = i5 + 1; i6 < this.cellsList.size() - n + 6; i6 ++) {

                                                ArrayList<Cell<T>> tempSublist6 = new ArrayList<>((ArrayList<Cell<T>>) tempSublist5);
                                                tempSublist6.add(this.cellsList.get(i6));
                                                if (n == 6) {
                                                    sublists.add(new ArrayList<>(tempSublist6));
                                                }

                                                if (n >= 7) {
                                                    for (int i7 = i6 + 1; i7 < this.cellsList.size() - n + 7; i7 ++) {

                                                        ArrayList<Cell<T>> tempSublist7 = new ArrayList<>((ArrayList<Cell<T>>) tempSublist6);
                                                        tempSublist7.add(this.cellsList.get(i7));
                                                        if (n == 7) {
                                                            sublists.add(new ArrayList<>(tempSublist7));
                                                        }

                                                        if (n >= 8) {
                                                            for (int i8 = i7 + 1; i8 < this.cellsList.size() - n + 8; i8 ++) {


                                                                ArrayList<Cell<T>> tempSublist8 = new ArrayList<>((ArrayList<Cell<T>>) tempSublist7);
                                                                tempSublist4.add(this.cellsList.get(i8));
                                                                if (n == 8) {
                                                                    sublists.add(new ArrayList<>(tempSublist8));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }



        return sublists;

    }

    public ArrayList<Cell<T>> getCellsList() {
        return cellsList;
    }

    public ArrayList<T> getAvailableItems() {
        return availableItems;
    }

    public ArrayList<T> getFilledItems() {
        return filledItems;
    }

    public ArrayList<T> getUnfilledItems() {
        return unfilledItems;
    }

    public void setFilledItems(ArrayList<T> filledItems) {
        this.filledItems = filledItems;
    }

    public void setUnfilledItems(ArrayList<T> unfilledItem) {
        this.unfilledItems = unfilledItem;
    }

}
