
package me.tangobee.saras.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ArtModel {

    @SerializedName("created")
    @Expose
    private Integer created;


    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public ArtModel() {}

    public ArtModel(Integer created, List<Datum> data) {
        this.created = created;
        this.data = data;
    }
    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
