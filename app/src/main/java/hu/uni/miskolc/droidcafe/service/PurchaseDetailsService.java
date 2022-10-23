package hu.uni.miskolc.droidcafe.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PurchaseDetailsService {

    @GET("purchase")
    Call<List<PurchaseDataDTO>> listPurchaseDetails();

    @POST("purchase")
    Call<PurchaseDataDTO> createPurchaseData(@Body PurchaseDataDTO purchaseData);

    @GET("purchase/{id}")
    Call<PurchaseDataDTO> searchPurchase(@Path("id") int id);

    @GET("purchase")
    Call<List<PurchaseDataDTO>> searchPurchaseWithName(@Query("name") String name);
}
