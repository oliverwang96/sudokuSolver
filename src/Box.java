import java.util.ArrayList;

public class Box<T> extends GroupedItems<T> {

    private int id;

    public Box(ArrayList availableItems, int id) {
        super(availableItems);
        this.id = id;
    }
}


