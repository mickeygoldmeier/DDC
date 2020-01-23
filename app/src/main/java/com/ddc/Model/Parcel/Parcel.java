package com.ddc.Model.Parcel;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ddc.Model.Address;
import com.ddc.Model.NotifyDataChange;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "parcels_table")
public class Parcel {

    @PrimaryKey
    @NonNull
    private String ParcelID;
    private Parcel_Type Type;
    private boolean Fragile;
    private double Weight;
    private Address DistributionCenterAddress;
    private String RecipientPhone;
    private String CompanyID;
    private Parcel_Status parcelStatus;
    private String selectedDeliver;
    private List<String> optionalDelivers;

    public Parcel() {
        optionalDelivers = new ArrayList<>();
    }

    public Parcel(Parcel_Type type, boolean fragile, double weight, Address distributionCenterAddress, String recipientPhone, String parcelID, String companyID) {
        Type = type;
        Fragile = fragile;
        Weight = weight;
        DistributionCenterAddress = distributionCenterAddress;
        RecipientPhone = recipientPhone;
        ParcelID = parcelID;
        CompanyID = companyID;
        parcelStatus = Parcel_Status.Registered;
    }

    public Parcel(Parcel_Type type, boolean fragile, double weight, Address distributionCenterAddress, String recipientPhone, String parcelID, String companyID,Parcel_Status parcel_status) {
        Type = type;
        Fragile = fragile;
        Weight = weight;
        DistributionCenterAddress = distributionCenterAddress;
        RecipientPhone = recipientPhone;
        ParcelID = parcelID;
        CompanyID = companyID;
        parcelStatus = parcel_status;
    }

    public Parcel_Status getParcelStatus() {
        return parcelStatus;
    }

    public void setParcelStatus(Parcel_Status parcelStatus) {
        this.parcelStatus = parcelStatus;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public Parcel_Type getType() {
        return Type;
    }

    public void setType(Parcel_Type type) {
        Type = type;
    }

    public boolean isFragile() {
        return Fragile;
    }

    public void setFragile(boolean fragile) {
        Fragile = fragile;
    }

    public double getWeight() {
        return Weight;
    }

    public String getSelectedDeliver() {
        return selectedDeliver;
    }

    public void setSelectedDeliver(String selectedDeliver) {
        this.selectedDeliver = selectedDeliver;
    }

    public List<String> getOptionalDelivers() {
        return optionalDelivers;
    }

    public void setOptionalDelivers(List<String> optionalDelivers) {
        this.optionalDelivers = optionalDelivers;
    }

    public void setWeight(double weight) throws Exception {
        if (weight <= 0)
            throw new Exception("weight cant be less or equal to 0");
        Weight = weight;
    }

    public Address getDistributionCenterAddress() {
        return DistributionCenterAddress;
    }

    public void setDistributionCenterAddress(Address distributionCenterAddress) {
        DistributionCenterAddress = distributionCenterAddress;
    }

    public String getRecipientPhone() {
        return RecipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        RecipientPhone = recipientPhone;
    }

    public String getParcelID() {
        return ParcelID;
    }

    public void setParcelID(String id) {
        ParcelID = id;
    }

    public void getIdFromDataBase() {
        ConfigDS.getConfigID(new NotifyDataChange<String>() {
            @Override
            public void OnDataChanged(String obj) {
                setParcelID(obj);
            }

            @Override
            public void onFailure(Exception exception) {

            }

        });
    }

    public void addOptionalDeliver(String id) {
        if (!optionalDelivers.contains(id))
            optionalDelivers.add(id);
    }
}


