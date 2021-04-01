package implementation;

import java.util.ArrayList;
import java.util.List;

public class ExpirationStack<Key, Value> {
    private List<StoredValue<Key, Value>> elements = new ArrayList<>();
}
