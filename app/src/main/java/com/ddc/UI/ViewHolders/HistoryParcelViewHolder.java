package com.ddc.UI.ViewHolders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.Parcel_Status;
import com.ddc.R;
import com.ddc.Utils.ParcelDataConvertor;

public class HistoryParcelViewHolder extends RecyclerView.ViewHolder {

    private Parcel parcel;
    private LinearLayout extraInfo;
    private ImageView typeImage;
    private TextView parcelID;
    private TextView typeName;
    private TextView location;
    private TextView sender;
    private TextView status;
    private RadioGroup optionalDelivers;
    private LinearLayout chooseDeliverMenu;
    private Button chooseDeliver;
    private boolean expanded;

    public HistoryParcelViewHolder(@NonNull View itemView) {
        super(itemView);

        extraInfo = itemView.findViewById(R.id.extra_info_ll);
        typeImage = itemView.findViewById(R.id.type_iv);
        parcelID = itemView.findViewById(R.id.parcel_id_tv);
        typeName = itemView.findViewById(R.id.parcel_type_tv);
        location = itemView.findViewById(R.id.parcel_location_tv);
        sender = itemView.findViewById(R.id.parcel_sender_tv);
        status = itemView.findViewById(R.id.parcel_status_tv);
        chooseDeliverMenu = itemView.findViewById(R.id.choose_deliver_ll);
        optionalDelivers = itemView.findViewById(R.id.optional_delivers_rg);
        chooseDeliver = itemView.findViewById(R.id.choose_deliver_btn);
        expanded = false;
    }

    public void fillView(Parcel parcel_, Context context) {
        this.parcel = parcel_;

        typeImage.setImageResource(ParcelDataConvertor.typeToImageInt(parcel.getType()));
        parcelID.setText(parcel.getParcelID());
        typeName.setText(ParcelDataConvertor.typeTOString(parcel.getType()));
        location.setText(parcel.getDistributionCenterAddress().toString());
        sender.setText(parcel.getCompanyID());
        status.setText(ParcelDataConvertor.statusToString(parcel.getParcelStatus()));

        if (parcel.getParcelStatus() == Parcel_Status.CollectionOffered)
            chooseDeliverMenu.setVisibility(View.VISIBLE);
        else
            chooseDeliverMenu.setVisibility(View.GONE);

        RadioButton button;
        optionalDelivers.removeAllViews();
        for (String name : parcel.getOptionalDelivers()) {
            button = new RadioButton(context);
            button.setText(name);
            button.setTextColor(Color.GRAY);
            button.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimaryDark)));
            optionalDelivers.addView(button);
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        extraInfo.setVisibility(expanded ? View.VISIBLE : View.GONE);
    }
}