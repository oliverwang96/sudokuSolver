import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        int n = 9;
        ArrayList<Cell<Integer>> cellsList = new ArrayList<>();
        ArrayList<Integer> itemsList = new ArrayList<>();

        for (int i = 0; i < n; i ++) {
            itemsList.add(i+1);
        }


        // Create the Sudoku grid with n * n cells, unfilled.
        for (int i = 0; i < n*n; i++) {

            Cell<Integer> cell = new Cell<>(false, i, itemsList);
            cellsList.add(cell);
            //cell.fill(i%4);
        }


        //ArrayList<Column> rowsList = new ArrayList<>();
        ArrayList<Row<Integer>> rowsList = addRows(itemsList, cellsList, n);


        ArrayList<Column<Integer>> columnsList = addColumns(itemsList, cellsList, n);

        ArrayList<Box<Integer>> boxesList = addBoxes(itemsList, cellsList, n);

        printGrid(cellsList, n);

        //loadQuestion(cellsList);
        loadQuestionFromFile(cellsList, "D:\\xiaohao\\Java\\Java_Programs\\SudokuSolver\\TestGrid3.txt", n);

        printGrid(cellsList,n);

        analyzeGrid(cellsList);

        //printPossibleValues(cellsList);

        boolean[] statusUpdate = {true, true};
        while (statusUpdate[0]) {

            int filledCellsCount = countFilledCells(cellsList);
            statusUpdate = solveSudoku(cellsList, rowsList, columnsList, boxesList);
            System.out.println(statusUpdate[0]);
            printGrid(cellsList, n);
            //filledCellsCountIteration = countFilledCells(cellsList) - filledCellsCount;
        }


        solveByBacktrack(cellsList);

        printGrid(cellsList, 9);
    }

    private static ArrayList<Row<Integer>> addRows(ArrayList<Integer> itemsList, ArrayList<Cell<Integer>> cellsList, int n) {
        // Create the n Row objects for n rows the Sudoku grid
        ArrayList<Row<Integer>> rowsList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Row<Integer> row = new Row<>(itemsList, i);
            for (int j = 0; j < n; j ++) {

                Cell<Integer> cell = cellsList.get(i*n+j);
                cell.setRowId(i);
                cell.setParentRow(row);
                row.addCell(cell);
                //System.out.println("Row " + i + " Cell " + (i*n+j));

            }
            rowsList.add(row);
        }
        return rowsList;
    }

    private static ArrayList<Column<Integer>> addColumns(ArrayList<Integer> itemsList, ArrayList<Cell<Integer>> cellsList, int n) {
        ArrayList<Column<Integer>> columnsList = new ArrayList<>();

        // Create the n Column objects for n columns of the Sudoku grid
        for (int i = 0; i < n; i++) {
            Column<Integer> column = new Column<>(itemsList, i);
            for (int j = 0; j < n; j ++) {

                Cell<Integer> cell = cellsList.get(i+j*n);
                cell.setColumnId(i);
                cell.setParentColumn(column);
                column.addCell(cell);
                //System.out.println("Column " + i + " Cell " + (i+j*n));

            }
            columnsList.add(column);
        }
        return columnsList;
    }

    private static ArrayList<Box<Integer>> addBoxes(ArrayList<Integer> itemsList, ArrayList<Cell<Integer>> cellsList, int n) {
        ArrayList<Box<Integer>> boxesList = new ArrayList<>();

        // Create the n BOx objects for n boxes of the Sudoku grid
        for (int i = 0; i < n; i++) {
            Box<Integer> box = new Box<>(itemsList, i);

            // Find the index for the topleft cell to be added to the box.
            int startInd = (int) (i % (Math.sqrt(n))* Math.sqrt(n) + Math.floor(i/ Math.sqrt(n))*n*Math.sqrt(n));
            //System.out.println((i + "StartInd = " + startInd));

            // Need to sqrt(n) rows of cells, each row containing sqrt(n) cells.
            for (int j = 0; j < Math.sqrt(n); j ++) {

                int rowStartInd = (int) (startInd + j * n);

                // Add a row of cells to Box
                for (int k = 0; k < Math.sqrt(n); k++) {

                    Cell<Integer> cell = cellsList.get(rowStartInd+k);
                    cell.setBoxId(i);
                    cell.setParentBox(box);
                    box.addCell(cell);
                    //box.addCell(cellsList.get(rowStartInd+k));
                    //System.out.println("Box " + i + " Cell " + (rowStartInd+k));
                }


                //System.out.println("Column " + i + " Cell " + (i+j*n));

            }
            boxesList.add(box);
        }
        return boxesList;
    }

    private static void printGrid(ArrayList<Cell<Integer>> cellsList, int n) {


        for (int i = 0; i < n+Math.sqrt(n)+1; i ++) {
            System.out.print("-");
        }
        System.out.print("\n");

        // Iterate every row of boxes
        for (int boxRowNum = 0; boxRowNum < Math.sqrt(n); boxRowNum ++) {

            // Iterate through every row
            for (int cellRowNum = 0; cellRowNum < Math.sqrt(n); cellRowNum ++) {

                System.out.print("|");

                // Iterate through the following loop sqrt(n) times to print a line.

                for (int j = 0; j < Math.sqrt(n); j ++) {

                    for (int cellNum = 0; cellNum < Math.sqrt(n); cellNum ++) {
                        Cell cell = cellsList.get((int) (boxRowNum * Math.sqrt(n)*n + cellRowNum*n + j*Math.sqrt(n) + cellNum));

                        if (cell.getValue() == null) {
                            System.out.print(" ");
                        } else {
                            System.out.print(cell.getValue());
                        }

                    }
                    System.out.print("|");
                }


                System.out.print("\n");
            }

            // Print horizontal box separator
            for (int i = 0; i < n+Math.sqrt(n)+1; i ++) {
                System.out.print("-");
            }

            System.out.print("\n");
        }


    }

    private static void loadQuestion(ArrayList<Cell<Integer>> cellsList) {

        fill(cellsList, 3, 2, false);
        fill(cellsList, 4, 6, false);
        fill(cellsList, 6, 7, false);
        fill(cellsList, 8, 1, false);
        fill(cellsList, 9, 6, false);
        fill(cellsList, 10, 8, false);
        fill(cellsList, 13, 7, false);
        fill(cellsList, 16, 9, false);
        fill(cellsList, 18, 1, false);
        fill(cellsList, 19, 9, false);
        fill(cellsList, 23, 4, false);
        fill(cellsList, 24, 5, false);
        fill(cellsList, 27, 8, false);
        fill(cellsList, 28, 2, false);
        fill(cellsList, 30, 1, false);
        fill(cellsList, 34, 4, false);
        fill(cellsList, 38, 4, false);
        fill(cellsList, 39, 6, false);
        fill(cellsList, 41, 2, false);
        fill(cellsList, 42, 9, false);
        fill(cellsList, 46, 5, false);
        fill(cellsList, 50, 3, false);
        fill(cellsList, 52, 2, false);
        fill(cellsList, 53, 8, false);
        fill(cellsList, 56, 9, false);
        fill(cellsList, 57, 3, false);
        fill(cellsList, 61, 7, false);
        fill(cellsList, 62, 4, false);
        fill(cellsList, 64, 4, false);
        fill(cellsList, 67, 5, false);
        fill(cellsList, 70, 3, false);
        fill(cellsList, 71, 6, false);
        fill(cellsList, 72, 7, false);
        fill(cellsList, 74, 3, false);
        fill(cellsList, 76, 1, false);
        fill(cellsList, 77, 8, false);

        //return cellsList;

    }

    private static void loadQuestionFromFile(ArrayList<Cell<Integer>> cellsList, String filename, int n) {

        try {
            FileReader fileReader = new FileReader(filename);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            for (int row = 0; row < n; row ++) {

                String line =  bufferedReader.readLine();

                if (line.length() == n) {

                    for (int col = 0; col < n; col ++) {
                        Integer item = ((int) line.charAt(col)) - 48;
                        if (item != 0) {
                            fill(cellsList, row * n + col, item, false);
                        }
                    }
                }

            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }


    }

    private static void fill(ArrayList<Cell<Integer>> cellsList, int ind, Integer value, boolean toUpdateSolver) {
        cellsList.get(ind).fill(value);


        if (toUpdateSolver) {
            updateSolver(cellsList.get(ind), value);
        };

        //return cellsList;
    }

    private static boolean shallowFill(ArrayList<Cell<Integer>> cellsList, int ind, Integer value, boolean toUpdateSolver) {

        // Shallow fill fills a cell without certainty that the value filled in the cell is correct.
        cellsList.get(ind).shallowFill(value);

        return false;
    }

    private static void analyzeGrid(ArrayList<Cell<Integer>> cellsList) {

        //
        // Go through every row, column, and box and:
        // 1. Update the filled values and unfilled values in that row, column, or box.
        // 2. Find all the filled cell, then remove the value of the filled cell from the list of possible values in
        // every other cell in the same row, same column, or same box.

        for (Cell<Integer> cell : cellsList) {

            if (cell.isFilled()) {
                updateSolver(cell, cell.getValue());
            }
        }

        /*
        for (Row<Integer> row: rowsList) {

            // Retrieve all the filled cells.
            ArrayList<Cell<Integer>> filledCellsList = new ArrayList<>();

            //System.out.println(row.getCellsList().get(1));
            //ArrayList<Integer> filledItems = new ArrayList<>();
            //ArrayList<Integer> unfilledItems = new ArrayList<>();

            for (Cell<Integer> cell : row.getCellsList()) {

                if (cell.isFilled()) {
                    //filledCellsList.add(cell);
                    //filledItems.add(cell.getValue());
                    updateSolver(cell, cell.getValue());

                    //row.removeUnfilledItems(cell.getValue());
                    //row.addFilledItems(cell.getValue());

                    // Remove this item from the possible items of all cells in the same row.


                }

            }

            //row.setFilledItems(filledItems);
            //row.setUnfilledItems(unfilledItems);


            for (Cell<Integer> cell : row.getCellsList()) {

                if ( !cell.isFilled()) {


                    for (Integer item: row.getFilledItems()) {

                        cell.removePossibleValues(item);

                    }
                }
            }


            //System.out.println(row.getUnfilledItems());

        }

        // For Columns

        for (Column<Integer> column: columnsList) {

            // Retrieve all the filled cells.
            ArrayList<Cell<Integer>> filledCellsList = new ArrayList<>();

            //System.out.println(row.getCellsList().get(1));
            ArrayList<Integer> filledItems = new ArrayList<>();
            ArrayList<Integer> unfilledItems = new ArrayList<>();

            for (Cell<Integer> cell : column.getCellsList()) {

                if (cell.isFilled()) {
                    filledCellsList.add(cell);
                    filledItems.add(cell.getValue());

                   column.removeUnfilledItems(cell.getValue());
                }

            }

            column.setFilledItems(filledItems);
            //row.setUnfilledItems(unfilledItems);


            for (Cell<Integer> cell : column.getCellsList()) {

                if ( !cell.isFilled()) {


                    for (Integer item: filledItems) {

                        cell.removePossibleValues(item);

                    }
                }
            }
            //System.out.println(column.getUnfilledItems());

        }

        // For Boxes


        for (Box<Integer> box: boxesList) {

            // Retrieve all the filled cells.
            ArrayList<Cell<Integer>> filledCellsList = new ArrayList<>();

            //System.out.println(row.getCellsList().get(1));
            ArrayList<Integer> filledItems = new ArrayList<>();
            ArrayList<Integer> unfilledItems = new ArrayList<>();

            for (Cell<Integer> cell : box.getCellsList()) {

                if (cell.isFilled()) {
                    filledCellsList.add(cell);
                    filledItems.add(cell.getValue());

                    box.removeUnfilledItems(cell.getValue());
                }

            }

            box.setFilledItems(filledItems);
            //row.setUnfilledItems(unfilledItems);


            for (Cell<Integer> cell : box.getCellsList()) {

                if ( !cell.isFilled()) {


                    for (Integer item: filledItems) {

                        cell.removePossibleValues(item);

                    }
                }
            }

        }
        */
    }

    private static boolean[] solveSudoku(ArrayList<Cell<Integer>> cellsList, ArrayList<Row<Integer>> rowsList, ArrayList<Column<Integer>> columnsList, ArrayList<Box<Integer>> boxesList ) {

        boolean madeProgress = false;
        boolean validGrid = true;


        // Eliminate Possibilities by Insufficiency: https://dingo.sbs.arizona.edu/~sandiway/sudoku/insufficiency.html
        for (int n = 2; n <= 3; n++) {
            // For Rows
            for (Row<Integer> row : rowsList) {
                Map<ArrayList<Cell<Integer>>, ArrayList<Integer>> sufficientPossibleValuesCellsMap = row.checkSufficientPossibleValues(n);
                if (sufficientPossibleValuesCellsMap.isEmpty()) {
                    continue;
                }


                for (ArrayList<Cell<Integer>> sufficientPossibleValuesCells : sufficientPossibleValuesCellsMap.keySet()) {

                    ArrayList<Integer> possibleValues = sufficientPossibleValuesCellsMap.get(sufficientPossibleValuesCells);
                    for (Cell<Integer> cell : row.getCellsList()) {
                        if (!(sufficientPossibleValuesCells.contains(cell))) {

                            for (int i = 0; i < n; i++) {

                                if (cell.getPossibleValues().contains(possibleValues.get(i))) {
                                    boolean removed = cell.removePossibleValues((possibleValues.get(i)));
                                    madeProgress = removed;
                                    if (cell.getPossibleValues().size() == 0) {
                                        validGrid = false;
                                        boolean[] statusUpdate = {madeProgress, validGrid};
                                        return statusUpdate;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // For Columns
            for (Column<Integer> column : columnsList) {
                Map<ArrayList<Cell<Integer>>, ArrayList<Integer>> sufficientPossibleValuesCellsMap = column.checkSufficientPossibleValues(n);
                if (sufficientPossibleValuesCellsMap.isEmpty()) {
                    continue;
                }


                for (ArrayList<Cell<Integer>> sufficientPossibleValuesCells : sufficientPossibleValuesCellsMap.keySet()) {

                    ArrayList<Integer> possibleValues = sufficientPossibleValuesCellsMap.get(sufficientPossibleValuesCells);
                    for (Cell<Integer> cell : column.getCellsList()) {
                        if (!(sufficientPossibleValuesCells.contains(cell))) {

                            for (int i = 0; i < n; i++) {

                                if (cell.getPossibleValues().contains(possibleValues.get(i))) {
                                    boolean removed = cell.removePossibleValues((possibleValues.get(i)));
                                    madeProgress = removed;
                                    if (cell.getPossibleValues().size() == 0) {
                                        validGrid = false;
                                        boolean[] statusUpdate = {madeProgress, validGrid};
                                        return statusUpdate;
                                    }
                                }
                            }
                        }
                    }
                }
            }



            // For boxes
            for (Box<Integer> box : boxesList) {
                Map<ArrayList<Cell<Integer>>, ArrayList<Integer>> sufficientPossibleValuesCellsMap = box.checkSufficientPossibleValues(n);
                if (sufficientPossibleValuesCellsMap.isEmpty()) {
                    continue;
                }


                for (ArrayList<Cell<Integer>> sufficientPossibleValuesCells : sufficientPossibleValuesCellsMap.keySet()) {

                    ArrayList<Integer> possibleValues = sufficientPossibleValuesCellsMap.get(sufficientPossibleValuesCells);
                    for (Cell<Integer> cell : box.getCellsList()) {
                        if (!(sufficientPossibleValuesCells.contains(cell))) {

                            for (int i = 0; i < n; i++) {

                                if (cell.getPossibleValues().contains(possibleValues.get(i))) {
                                    boolean removed = cell.removePossibleValues((possibleValues.get(i)));
                                    madeProgress = removed;
                                    if (cell.getPossibleValues().size() == 0) {
                                        validGrid = false;
                                        boolean[] statusUpdate = {madeProgress, validGrid};
                                        return statusUpdate;
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }

        // Go through every GroupedItems (Row, Column, Box), and get all the unfilled numbers.
        // For each unfilled number
        // Check each cell, see if number is within one of the unfilled numbers
        // If only one cell's unfilled number contains the number, fill in.


        // Go through each row.
        for (Row<Integer> row:rowsList) {

            ArrayList<Integer> unfilledItems = (ArrayList<Integer>) row.getUnfilledItems().clone();
            //System.out.println(unfilledItems);

            // For each unfilled number.
            for (Integer unfilledItem:unfilledItems) {

                int availableCell = 0;
                int cellIdToFill = -1;

                ArrayList<Cell<Integer>> rowCellsList = row.getCellsList();

                for (Cell<Integer> cell: rowCellsList) {
                    //System.out.println(cell.getPossibleValues());
                    if (cell.getPossibleValues().contains(unfilledItem)) {
                        availableCell ++;
                        cellIdToFill = cell.getId();
                    }
                }

                // If there is only one possible location for the item, fill in.
                if (availableCell == 1 && !cellsList.get(cellIdToFill).isFilled()) {
                    fill(cellsList, cellIdToFill, unfilledItem, true);
                    madeProgress = true;
                }


            }
        }


        // Go through each column.
        for (Column<Integer> column:columnsList) {

            ArrayList<Integer> unfilledItems = (ArrayList<Integer>) column.getUnfilledItems().clone();
            //System.out.println(unfilledItems);

            // For each unfilled number.
            for (Integer unfilledItem:unfilledItems) {

                int availableCell = 0;
                int cellIdToFill = -1;

                ArrayList<Cell<Integer>> columnCellsList = column.getCellsList();

                for (Cell<Integer> cell: columnCellsList) {
                    //System.out.println(cell.getPossibleValues());
                    if (cell.getPossibleValues().contains(unfilledItem)) {
                        availableCell ++;
                        cellIdToFill = cell.getId();
                    }
                }

                // If there is only one possible location for the item, fill in.
                if (availableCell == 1 && !cellsList.get(cellIdToFill).isFilled()) {
                    fill(cellsList, cellIdToFill, unfilledItem, true);
                    madeProgress = true;
                }


            }
        }

        // Go through each box.
        for (Box<Integer> box:boxesList) {

            ArrayList<Integer> unfilledItems = (ArrayList<Integer>) box.getUnfilledItems().clone();
            //System.out.println(unfilledItems);

            // For each unfilled number.
            for (Integer unfilledItem:unfilledItems) {

                int availableCell = 0;
                int cellIdToFill = -1;

                ArrayList<Cell<Integer>> boxCellsList = box.getCellsList();

                for (Cell<Integer> cell: boxCellsList) {
                    //System.out.println(cell.getPossibleValues());
                    if (cell.getPossibleValues().contains(unfilledItem)) {
                        availableCell ++;
                        cellIdToFill = cell.getId();
                    }
                }

                // If there is only one possible location for the item, fill in.
                if (availableCell == 1 && !cellsList.get(cellIdToFill).isFilled()) {
                    fill(cellsList, cellIdToFill, unfilledItem, true);
                    madeProgress = true;
                }


            }
        }

        // If a cell has only one possible value, fill in.
        for (Cell<Integer> cell:cellsList) {
            if (cell.getPossibleValues().size() == 1 && !cell.isFilled()) {
                fill(cellsList, cell.getId(), cell.getPossibleValues().get(0), true);
                madeProgress = true;
            }
        }


        ///System.out.println(cellsList.get(16).getPossibleValues());
        ///System.out.println(cellsList.get(12).getPossibleValues());
        ///System.out.println(cellsList.get(17).getPossibleValues());
        boolean[] statusUpdate = {madeProgress, validGrid};
        return statusUpdate;



    }

    public static void updateSolver(Cell<Integer> cell, Integer value) {

        //
        // Since a new cell has been filled:
        // 1. Update the filled values and unfilled values of the row, column, or box in which the cell belong
        // 2. Find all the filled cell, then remove the value of the filled cell from the list of possible values in
        // every other cell in the same row, same column, or same box.

        Row<Integer> row = cell.getParentRow();
        row.removeUnfilledItems(cell.getValue());
        row.addFilledItems(cell.getValue());

        Column<Integer> column = cell.getParentColumn();
        column.removeUnfilledItems(cell.getValue());
        column.addFilledItems(cell.getValue());

        Box<Integer> box = cell.getParentBox();
        box.removeUnfilledItems(cell.getValue());
        box.addFilledItems(cell.getValue());

        for (Cell<Integer> cellToUpdate : row.getCellsList()) {

            if ( !cellToUpdate.isFilled()) {
                cellToUpdate.removePossibleValues(value);
            }
        }
        for (Cell<Integer> cellToUpdate : column.getCellsList()) {

            if ( !cellToUpdate.isFilled()) {
                cellToUpdate.removePossibleValues(value);
            }
        }
        for (Cell<Integer> cellToUpdate : box.getCellsList()) {

            if ( !cellToUpdate.isFilled()) {
                cellToUpdate.removePossibleValues(value);
            }
        }



    }

    private static boolean shallowUpdateSolver(Cell<Integer> cell, Integer value) {
        //
        // Since a new cell has been filled:
        // 1. Update the filled values and unfilled values of the row, column, or box in which the cell belong
        // 2. Find all the filled cell, then remove the value of the filled cell from the list of possible values in
        // every other cell in the same row, same column, or same box.

        Row<Integer> row = cell.getParentRow();
        row.removeUnfilledItems(cell.getValue());
        row.addFilledItems(cell.getValue());

        Column<Integer> column = cell.getParentColumn();
        column.removeUnfilledItems(cell.getValue());
        column.addFilledItems(cell.getValue());

        Box<Integer> box = cell.getParentBox();
        box.removeUnfilledItems(cell.getValue());
        box.addFilledItems(cell.getValue());

        for (Cell<Integer> cellToUpdate : row.getCellsList()) {

            if ( !cellToUpdate.isFilled()) {
                cellToUpdate.removePossibleValues(value);
            }
        }
        for (Cell<Integer> cellToUpdate : column.getCellsList()) {

            if ( !cellToUpdate.isFilled()) {
                cellToUpdate.removePossibleValues(value);
            }
        }
        for (Cell<Integer> cellToUpdate : box.getCellsList()) {

            if ( !cellToUpdate.isFilled()) {
                cellToUpdate.removePossibleValues(value);
            }
        }

        return false;

    }

    public static void printPossibleValues(ArrayList<Cell<Integer>> cellsList) {

        for (Cell cell: cellsList) {
            System.out.println("Cell # " + cell.getId() + " has possible values: " );

            for (int i = 0; i < cell.getPossibleValues().size(); i++) {
                System.out.print(cell.getPossibleValues().get(i));
            }
            System.out.println("\n");

        }
    }

    public static void solveByBacktrack(ArrayList<Cell<Integer>> cellsList) {
        // Find all the unfilled cells, rank them by the number of possible values. Starting from the cell with the
        // smallest number of  possible values, fill in number and try.

        ArrayList<Cell<Integer>> unfilledCellsList = new ArrayList<>();

        for (Cell<Integer> cell:cellsList) {
            if (!cell.isFilled()) {
                unfilledCellsList.add(cell);
            }
        }
        Collections.sort(unfilledCellsList);
        for (Cell<Integer> cell: unfilledCellsList) {
            System.out.println(cell.getId());
        }


        backtrackRecursion(cellsList, unfilledCellsList, 0);


    }

    public static boolean backtrackRecursion(ArrayList<Cell<Integer>> cellsList, ArrayList<Cell<Integer>> unfilledCellsList, int n) {

        // Solve using backtracking by implementing a recursion.

        // n: Currently trying index n in the list of unfilled Cells.

        // If there s not more unfilled cells, then the sudoku is solved, return true.
        if (n >= unfilledCellsList.size()) {
            System.out.println("Solved!");
            return true;
        }

        Cell<Integer> cellToFill = unfilledCellsList.get(n);

        ArrayList<Integer> possibleValues = cellToFill.getPossibleValues();


        for (int i = 0; i < possibleValues.size(); i++) {

            // Fill the value in
            shallowFill(cellsList, cellToFill.getId(), possibleValues.get(i), false);
            //printGrid(cellsList, 9);


            if (isValidGrid(cellToFill)) {

                // If the grid is value, call self, start trying the next cell.
                if (backtrackRecursion(cellsList, unfilledCellsList, n+1)) {
                    // This will execute when the grid is completely solved.
                    return true;
                };

            } else {
                // If the filling the cell made the grid invalid, unfill the cell.
                cellToFill.shallowUnfill();
            }


        }


        // If, after trying to fill all the possible numbers in the next unfilled cell, none of the numbers can
        // result in a valid grid, then the number that was filled in the current cell must in invalid as well.
        // Unfill and return false.
        cellToFill.shallowUnfill();
        return false;


    }

    public static int countFilledCells(ArrayList<Cell<Integer>> cellsList) {

        int count = 0;
        for (Cell<Integer> cell:cellsList) {
            if (cell.isFilled()){
                count ++;

            }
        }
        return count;
    }

    public static boolean isValidGrid(Cell<Integer> cellToFill) {

        // Check for validity of Grid in row

        for (Cell<Integer> cellToCheck:cellToFill.getParentRow().getCellsList()) {

            // If the cellToFill and the cellToCheck has the same ID, they are the same cell.

            if (!(cellToCheck.getId() == cellToFill.getId())) {

                if (cellToCheck.getValue() == cellToFill.getValue()) {
                    // The grid is invalid.
                    return false;
                }
            }
        }

        // Check for validity of Grid in column
        for (Cell<Integer> cellToCheck:cellToFill.getParentColumn().getCellsList()) {

            // If the cellToFill and the cellToCheck has the same ID, they are the same cell.

            if (!(cellToCheck.getId() == cellToFill.getId())) {

                if (cellToCheck.getValue() == cellToFill.getValue()) {
                    // The grid is invalid.
                    return false;
                }
            }
        }

        // Check for validity of Grid in box
        for (Cell<Integer> cellToCheck:cellToFill.getParentBox().getCellsList()) {

            // If the cellToFill and the cellToCheck has the same ID, they are the same cell.

            if (!(cellToCheck.getId() == cellToFill.getId())) {

                if (cellToCheck.getValue() == cellToFill.getValue()) {
                    // The grid is invalid.
                    return false;
                }
            }
        }

        return true;

    }


}
