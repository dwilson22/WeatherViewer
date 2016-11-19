package ca.danieljameswilson.weatherviewer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Daniel on 2016-11-19.
 */

public class WeatherDetails implements Parcelable {
    private String city, description;
    private int currantTemp, high, low;

    public WeatherDetails(int c, int h, int l, String d, String city){
        this.city = city;
        description = d;
        currantTemp = c;
        high = h;
        low = l;
    }

    protected WeatherDetails(Parcel in) {
        city = in.readString();
        description = in.readString();
        currantTemp = in.readInt();
        high = in.readInt();
        low = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getCity());
        dest.writeString(getDescription());
        dest.writeInt(getCurrantTemp());
        dest.writeInt(getHigh());
        dest.writeInt(getLow());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherDetails> CREATOR = new Creator<WeatherDetails>() {
        @Override
        public WeatherDetails createFromParcel(Parcel in) {
            return new WeatherDetails(in);
        }

        @Override
        public WeatherDetails[] newArray(int size) {
            return new WeatherDetails[size];
        }
    };

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public int getCurrantTemp() {
        return currantTemp;
    }

    public int getHigh() {
        return high;
    }

    public int getLow() {
        return low;
    }
}
