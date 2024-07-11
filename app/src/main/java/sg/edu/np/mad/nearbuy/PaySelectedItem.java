package sg.edu.np.mad.nearbuy;

public class PaySelectedItem {
    public int parentIndex = -1;
    public int nestedIndex = -1;

    public void clearSelection() {
        parentIndex = -1;
        nestedIndex = -1;
    }
}
