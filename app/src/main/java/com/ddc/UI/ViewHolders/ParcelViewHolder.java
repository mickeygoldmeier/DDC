package com.ddc.UI.ViewHolders;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ddc.Model.Parcel.Parcel;
import com.ddc.Model.Parcel.Parcel_Status;
import com.ddc.Model.Users.Person;
import com.ddc.R;
import com.ddc.UI.MyParcelsScreen.MyParcelsViewModel;
import com.ddc.Utils.ParcelDataConvertor;

import java.util.HashMap;

public class ParcelViewHolder extends RecyclerView.ViewHolder {

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
    private MyParcelsViewModel viewModel;

    public ParcelViewHolder(@NonNull View itemView) {
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

    public void fillView(Parcel parcel_, Context context, MyParcelsViewModel viewModel_, HashMap<String, Person> stringPersonHashMap) {
        this.parcel = parcel_;
        this.viewModel = viewModel_;

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
        for (String id : parcel.getOptionalDelivers()) {
            if(stringPersonHashMap.containsKey(id)) {
                Person person = stringPersonHashMap.get(id);
                id = person.getFirstName() + " " + person.getLastName();
            }
            button = new RadioButton(context);
            button.setText(id);
            button.setTextColor(Color.GRAY);
            button.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimaryDark)));
            optionalDelivers.addView(button);
        }

        chooseDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("בחירת חבר להעברה")
                            .setMessage("האם אתה בטוח שאתה רוצה שהחבר שבחרת יעביר עבורך את החבילה? זוהי פעולה בלתי הפיכה וממולץ להעביר חבילות רק עם חברים שאתה מכיר ושאתה יכול לסמוך עליהם")
                            .setPositiveButton("סומך עליו", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int radioButtonID = optionalDelivers.getCheckedRadioButtonId();
                                    View radioButton = optionalDelivers.findViewById(radioButtonID);
                                    int idx = optionalDelivers.indexOfChild(radioButton);
                                    String deliverID = parcel.getOptionalDelivers().get(idx);
                                    viewModel.chooseParcelDeliver(parcel, deliverID);
                                }
                            })
                            .setNegativeButton("אני מתחרט", null)
                            .setIcon(R.drawable.ic_local_shipping)
                            .show();
                } catch (Exception e) {
                }
            }
        });
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        extraInfo.setVisibility(expanded ? View.VISIBLE : View.GONE);
    }
}