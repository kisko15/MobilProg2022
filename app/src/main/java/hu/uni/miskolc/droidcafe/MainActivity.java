package hu.uni.miskolc.droidcafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import hu.uni.miskolc.droidcafe.service.SelectedCakesDTO;

public class MainActivity extends AppCompatActivity {

    private String[] cakesName, descCakesData;
    private int imagesCakes[] = {R.drawable.donut_circle, R.drawable.icecream_circle, R.drawable.froyo_circle};
    private List<SelectedCakesDTO> cakesArrayList = new ArrayList<>();
    private RecyclerView recyclerView;

    FloatingActionButton mAddFab, mAddShoppingFab, mTakePictureFab, mPurchaseDetails;
    TextView addShoppingActionText;
    TextView addCameraActionText;
    TextView addPurchaseDetails;
    TextView nameText;
    Boolean isAllFabsVisible;
    ImageButton enBtn;
    ImageButton huBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enBtn = findViewById(R.id.en_btn);
        huBtn = findViewById(R.id.hu_btn);

        cakesName = getResources().getStringArray(R.array.cakes);
        descCakesData = getResources().getStringArray(R.array.cakes_description);
        nameText = findViewById(R.id.textintro);

        recyclerView = findViewById(R.id.recyclerView);

        mAddFab = findViewById(R.id.add_fab);
        mAddShoppingFab = findViewById(R.id.add_shopping_fab);
        mTakePictureFab = findViewById(R.id.add_camera_fab);
        mPurchaseDetails = findViewById(R.id.add_purchase_detail_fab);

        addShoppingActionText = findViewById(R.id.add_shopping_action_text);
        addCameraActionText = findViewById(R.id.add_camera_action_text);
        addPurchaseDetails = findViewById(R.id.add_details_action_text);

        mAddShoppingFab.setVisibility(View.GONE);
        mTakePictureFab.setVisibility(View.GONE);
        mPurchaseDetails.setVisibility(View.GONE);

        addShoppingActionText.setVisibility(View.GONE);
        addCameraActionText.setVisibility(View.GONE);
        addPurchaseDetails.setVisibility(View.GONE);

        isAllFabsVisible = false;

        Locale primaryLocale = this.getResources().getConfiguration().getLocales().get(0);
        String locale = primaryLocale.getDisplayName();
        Log.d("TAG", locale);

        enBtn.setOnClickListener(v -> {
            if (locale.equals("English")) {
                displayToast("The language is now english");
            } else {
                setLocale("en");
            }
        });

        huBtn.setOnClickListener(v -> {
            if (locale.equals("Hungarian")) {
                displayToast("A nyelv már magyar");
            } else {
                setLocale("hu");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //RecyclerView Cakes List settings
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(
                        recyclerView.getContext(),
                        DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        CakesAdapter cakesAdapter = new CakesAdapter(this, getData());
        recyclerView.setAdapter(cakesAdapter);

        //FAB onClick settings
        mAddFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                mAddShoppingFab.show();
                mTakePictureFab.show();
                mPurchaseDetails.show();
                addShoppingActionText.setVisibility(View.VISIBLE);
                addCameraActionText.setVisibility(View.VISIBLE);
                addPurchaseDetails.setVisibility(View.VISIBLE);

                isAllFabsVisible = true;
            } else {
                mAddShoppingFab.hide();
                mTakePictureFab.hide();
                mPurchaseDetails.hide();
                addShoppingActionText.setVisibility(View.GONE);
                addCameraActionText.setVisibility(View.GONE);
                addPurchaseDetails.setVisibility(View.GONE);

                isAllFabsVisible = false;
            }

        });
        mAddShoppingFab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
            List<String> cakesNameList = cakesArrayList
                    .stream()
                    .filter(SelectedCakesDTO::isSelected)
                    .map(SelectedCakesDTO::getName)
                    .collect(Collectors.toList());

            if (cakesNameList.isEmpty()) {
                displayToast("Not selected cake");
            } else {
                intent.putStringArrayListExtra("cakesNameList", (ArrayList<String>) cakesNameList);
                startActivity(intent);
            }
        });
        mTakePictureFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TakePicture.class);
            startActivity(intent);
        });
        mPurchaseDetails.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PurchaseDetailsActivity.class);
            startActivity(intent);
        });
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private List<SelectedCakesDTO> getData() {
        for (int i = 0; i < cakesName.length; i++) {
            cakesArrayList.add(new SelectedCakesDTO(cakesName[i], imagesCakes[i], descCakesData[i], false));
        }
        return cakesArrayList;
    }

    private void setLocale(String language) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        resources.updateConfiguration(configuration, displayMetrics);
        onConfigurationChanged(configuration);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(i);
        overridePendingTransition(0, 0);

    }
}
