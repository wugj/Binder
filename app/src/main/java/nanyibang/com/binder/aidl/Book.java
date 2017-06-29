package nanyibang.com.binder.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @name: nanyibang.com.binder.bean
 * @description:
 * @author：Administrator
 * @date: 2017-06-28 18:18
 * @company: 上海若美科技有限公司
 */

public class Book implements Parcelable {
    public int id;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public Book(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        return "[id : " + id + ", name : " + name + "]";
    }
}
