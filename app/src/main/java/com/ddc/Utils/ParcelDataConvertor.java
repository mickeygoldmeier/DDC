package com.ddc.Utils;

import com.ddc.Model.Parcel.Parcel_Status;
import com.ddc.Model.Parcel.Parcel_Type;
import com.ddc.R;

public class ParcelDataConvertor {
    public static String typeTOString(Parcel_Type type) {
        switch (type) {
            case Envelope:
                return "מעטפה";
            case SmallPackage:
                return "חבילה קטנה";
            default:
                return "חבילה גדולה";
        }
    }

    public static String statusToString(Parcel_Status status) {
        if (status == null)
            status = Parcel_Status.Registered;

        switch (status) {
            case Registered:
                return "מחכה במחסן";
            case OnTheWay:
                return "בדרך אליך";
            case Delivered:
                return "הועברה אליך";
            case CollectionOffered:
                return "מחכה שתבחר חבר";
        }

        return "מממ יש פה באג...";
    }

    public static int typeToImageInt(Parcel_Type type) {
        switch (type) {
            case Envelope:
                return R.drawable.ic_envelpoe;
            default:
                return R.drawable.ic_parcel;
        }
    }
}
