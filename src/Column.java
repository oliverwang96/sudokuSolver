import java.util.ArrayList;

public class Column<T> extends GroupedItems<T> {

    private int id;

    public Column(ArrayList availableItems, int id) {
        super(availableItems);
        this.id = id;
    }
}
