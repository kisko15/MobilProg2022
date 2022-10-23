package hu.uni.miskolc.droidcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import hu.uni.miskolc.droidcafe.service.PurchaseDataDTO;
import hu.uni.miskolc.droidcafe.service.PurchaseDetailsService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PurchaseDetailsActivity extends AppCompatActivity {

    EditText etName, etAddress, etPhone, etNote;
    Button submitBtn;
    RecyclerView recyclerView;

    String sBaseUrl = "https://api.instantwebtools.net/v1/";
    String sName, sAddress, sPhone, sNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_details);

        etName = findViewById(R.id.name);
        etAddress = findViewById(R.id.address);
        etPhone = findViewById(R.id.phone);
        etNote = findViewById(R.id.note);
        submitBtn = findViewById(R.id.submit);
        recyclerView = findViewById(R.id.recycle_view);

        getPurchase();



    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("Ez az emberke van itt: " + searchPurchase(4));

        searchPurchaseWithName("Van").forEach(System.out::println);

        submitBtn.setOnClickListener(v -> {
            sName = etName.getText().toString().trim();
            sAddress = etAddress.getText().toString().trim();
            sPhone = etAddress.getText().toString().trim();
            sNote = etNote.getText().toString().trim();

            if (!sName.isEmpty() && !sAddress.isEmpty() && !sPhone.isEmpty() && !sNote.isEmpty()) {
                addPurchase();
            }
        });
    }

    private void getPurchase() {
        ProgressDialog dialog = ProgressDialog.show(this, "","Please wait...", true);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://my-json-server.typicode.com/kisko15/MobilProg2022/").addConverterFactory(GsonConverterFactory.create()).build();
        PurchaseDetailsService service = retrofit.create(PurchaseDetailsService.class);

        Call<List<PurchaseDataDTO>> allPurchaseDetails = service.listPurchaseDetails();
        allPurchaseDetails.enqueue(new Callback<List<PurchaseDataDTO>>() {
            @Override
            public void onResponse(Call<List<PurchaseDataDTO>> call, Response<List<PurchaseDataDTO>> response) {
                System.out.println(response.body());
                if (response.isSuccessful() && response.body() != null) {
                    dialog.dismiss();

                    GridLayoutManager layoutManager = new GridLayoutManager(PurchaseDetailsActivity.this, 2);

                    recyclerView.setLayoutManager(layoutManager);

                    recyclerView.setAdapter(new PurchaseDetailsAdapter(response.body()));

                }
            }

            @Override
            public void onFailure(Call<List<PurchaseDataDTO>> call, Throwable t) {

            }
        });
    }

    private void addPurchase() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://my-json-server.typicode.com/kisko15/MobilProg2022/").addConverterFactory(GsonConverterFactory.create()).build();
        PurchaseDetailsService service = retrofit.create(PurchaseDetailsService.class);

        PurchaseDataDTO addPurch = new PurchaseDataDTO(5, sName, sAddress, sPhone, sNote);
        Call<PurchaseDataDTO> postPurchase = service.createPurchaseData(addPurch);
        postPurchase.enqueue(new Callback<PurchaseDataDTO>() {
            @Override
            public void onResponse(Call<PurchaseDataDTO> call, Response<PurchaseDataDTO> response) {
                System.out.println(response.body());
                if (response.isSuccessful() && response.body() != null) {

                    etName.getText().clear();
                    etAddress.getText().clear();
                    etPhone.getText().clear();
                    etNote.getText().clear();

                    getPurchase();
                }
            }

            @Override
            public void onFailure(Call<PurchaseDataDTO> call, Throwable t) {

            }
        });
    }

    private PurchaseDataDTO searchPurchase(int id) {
        PurchaseDataDTO[] purchaseDataDTO = new PurchaseDataDTO[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://my-json-server.typicode.com/kisko15/MobilProg2022/").addConverterFactory(GsonConverterFactory.create()).build();
        PurchaseDetailsService service = retrofit.create(PurchaseDetailsService.class);

        Call<PurchaseDataDTO> searchPurchase = service.searchPurchase(id);
        searchPurchase.enqueue(new Callback<PurchaseDataDTO>() {
            @Override
            public void onResponse(Call<PurchaseDataDTO> call, Response<PurchaseDataDTO> response) {
                purchaseDataDTO[0] = response.body();

            }

            @Override
            public void onFailure(Call<PurchaseDataDTO> call, Throwable t) {

            }
        });


        return purchaseDataDTO[0];
    }

    private List<PurchaseDataDTO> searchPurchaseWithName(String name) {
        List<PurchaseDataDTO> purchaseDataDTOList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://my-json-server.typicode.com/kisko15/MobilProg2022/").addConverterFactory(GsonConverterFactory.create()).build();
        PurchaseDetailsService service = retrofit.create(PurchaseDetailsService.class);

        Call<List<PurchaseDataDTO>> searchPurchase = service.searchPurchaseWithName(name);
        searchPurchase.enqueue(new Callback<List<PurchaseDataDTO>>() {
            @Override
            public void onResponse(Call<List<PurchaseDataDTO>> call, Response<List<PurchaseDataDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    purchaseDataDTOList.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseDataDTO>> call, Throwable t) {

            }
        });


        return purchaseDataDTOList;
    }
}