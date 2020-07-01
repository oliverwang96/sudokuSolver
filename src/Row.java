import java.util.ArrayList;

public class Row<T> extends GroupedItems<T> {

    private int id;

    public Row(ArrayList availableItems, int id) {
        super(availableItems);
        this.id = id;
    }
}
