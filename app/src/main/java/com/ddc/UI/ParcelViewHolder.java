package com.ddc.UI;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.R;

public class ParcelViewHolder extends RecyclerView.ViewHolder {

    TextView id;
    TextView type;
    TextView recipient;
    Parcel parcel;

    public ParcelViewHolder(@NonNull View itemView) {
        super(itemView);

        id = itemView.findViewById(R.id.parcel_id_tv);
        type = itemView.findViewById(R.id.parcel_type_tv);
        recipient = itemView.findViewById(R.id.parcel_recipient_tv);
    }

    public void fillView(Parcel _parcel) {
        parcel = _parcel;
        id.setText(parcel.getParcelID());
        type.setText(parcel.getType().toString());
        recipient.setText(parcel.getRecipientPhone());
        /*switch (parcel.getType()){
            case Envelope:
                type.setText(R.string.envelope);
                break;
            case SmallPackage:
                type.setText(R.string.small_package);
                break;
            default:
                type.setText(R.string.large_package);
        }*/
    }
}