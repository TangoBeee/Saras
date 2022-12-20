
package me.tangobee.saras.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ArtRequestModel {
    public ArtRequestModel(String prompt, Integer n, String size) {
        this.prompt = prompt;
        this.n = n;
        this.size = size;
    }

    @SerializedName("prompt")
    @Expose
    private String prompt;
    @SerializedName("n")
    @Expose
    private Integer n;
    @SerializedName("size")
    @Expose
    private String size;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}