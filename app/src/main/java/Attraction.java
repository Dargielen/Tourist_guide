import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dargielen on 24.03.2017.
 */

public class Attraction implements Parcelable {

    private String name, adress, short_description, long_description;
    private Long longitude, latitude;

    public Attraction(){};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeString(name);
        pc.writeString(adress);
        pc.writeString(short_description);
        pc.writeString(long_description);
        pc.writeLong(longitude);
        pc.writeLong(latitude);
    }

    public static final Parcelable.Creator<Attraction> CREATOR = new Parcelable.Creator<Attraction>() {
        public Attraction createFromParcel(Parcel pc) {
            return new Attraction(pc);
        }
        public Attraction[] newArray(int size) {
            return new Attraction[size];
        }
    };

    public Attraction(Parcel pc) {
        name = pc.readString();
        adress = pc.readString();
        short_description = pc.readString();
        long_description = pc.readString();
        longitude = pc.readLong();
        latitude = pc.readLong();
    }

}
