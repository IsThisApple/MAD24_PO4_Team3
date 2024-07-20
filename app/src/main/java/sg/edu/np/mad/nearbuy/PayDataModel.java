package sg.edu.np.mad.nearbuy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PayDataModel implements Parcelable {
    private List<String> nestedList;
    private String itemText;
    private boolean isExpandable;

    public PayDataModel(List<String> nestedList, String itemText) {
        this.nestedList = nestedList;
        this.itemText = itemText;
        this.isExpandable = false;
    }

    protected PayDataModel(Parcel in) {
        nestedList = in.createStringArrayList();
        itemText = in.readString();
        isExpandable = in.readByte() != 0;
    }

    public static final Creator<PayDataModel> CREATOR = new Creator<PayDataModel>() {
        @Override
        public PayDataModel createFromParcel(Parcel in) {
            return new PayDataModel(in);
        }

        @Override
        public PayDataModel[] newArray(int size) {
            return new PayDataModel[size];
        }
    };

    public List<String> getNestedList() {
        return nestedList;
    }

    public String getItemText() {
        return itemText;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(nestedList);
        dest.writeString(itemText);
        dest.writeByte((byte) (isExpandable ? 1 : 0));
    }

}