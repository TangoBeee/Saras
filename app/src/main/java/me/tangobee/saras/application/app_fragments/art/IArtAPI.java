package me.tangobee.saras.application.app_fragments.art;

import me.tangobee.saras.model.ArtModel;
import me.tangobee.saras.model.ArtRequestModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IArtAPI {

    String BASE_URL = "https://api.openai.com/";

    @POST("/v1/images/generations")
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer <API_KEY>",
    })
    Call<ArtModel> getArt(@Body ArtRequestModel artRequestModel);
}
