package seedcommando.com.yashaswi.pojos.documentpojo;

import com.google.gson.annotations.SerializedName;

public class Data {
   /* @SerializedName("MinDaysForDocumentRequired")
    private String MinDaysForDocumentRequired;*/
   /* @SerializedName("IsDocumentRequired")
    private String IsDocumentRequired;//LeavePolicyDetailID*/
    /*@SerializedName("LeavePolicyDetailID")
    private String LeavePolicyDetailID;*/
@SerializedName("IsDocumentRequired")
    private String IsDocumentRequired;
    @SerializedName("MinDaysForDocumentRequired")
    private String MinDaysForDocumentRequired;

    public String getIsDocumentRequired() {
        return IsDocumentRequired;
    }

    public void setIsDocumentRequired(String isDocumentRequired) {
        IsDocumentRequired = isDocumentRequired;
    }

    public String getMinDaysForDocumentRequired() {
        return MinDaysForDocumentRequired;
    }

    public void setMinDaysForDocumentRequired(String minDaysForDocumentRequired) {
        MinDaysForDocumentRequired = minDaysForDocumentRequired;
    }
}
