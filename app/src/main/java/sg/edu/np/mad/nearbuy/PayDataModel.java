package sg.edu.np.mad.nearbuy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PayDataModel implements Parcelable {
    private List<String> nestedList; // List of nested items
    private String itemText; // Text associated with the item
    private boolean isExpandable; // Indicates if the item is expandable

    // Constructor to initialize PayDataModel
    public PayDataModel(List<String> nestedList, String itemText) {
        this.nestedList = nestedList;
        this.itemText = itemText;
        this.isExpandable = false; // Default to not expandable
    }

    // Constructor to create PayDataModel from Parcel
    protected PayDataModel(Parcel in) {
        nestedList = in.createStringArrayList(); // Read list from Parcel
        itemText = in.readString(); // Read item text from Parcel
        isExpandable = in.readByte() != 0; // Read expandable flag from Parcel
    }

    // Parcelable.Creator to create instances of PayDataModel from Parcel
    public static final Creator<PayDataModel> CREATOR = new Creator<PayDataModel>() {
        @Override
        public PayDataModel createFromParcel(Parcel in) {
            return new PayDataModel(in); // Create instance from Parcel
        }

        @Override
        public PayDataModel[] newArray(int size) {
            return new PayDataModel[size]; // Create array of PayDataModel
        }
    };

    // Getter for nestedList
    public List<String> getNestedList() {
        return nestedList;
    }

    // Getter for itemText
    public String getItemText() {
        return itemText;
    }

    // Getter for isExpandable
    public boolean isExpandable() {
        return isExpandable;
    }

    // Setter for isExpandable
    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    // Describe contents for Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    // Write PayDataModel to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(nestedList); // Write list to Parcel
        dest.writeString(itemText); // Write item text to Parcel
        dest.writeByte((byte) (isExpandable ? 1 : 0)); // Write expandable flag to Parcel
    }
}
