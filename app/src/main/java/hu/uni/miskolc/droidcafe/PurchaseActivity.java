package hu.uni.miskolc.droidcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import hu.uni.miskolc.droidcafe.dao.PurchaseDataDao;
import hu.uni.miskolc.droidcafe.dao.PurchaseDatabase;
import hu.uni.miskolc.droidcafe.model.PurchaseData;

public class PurchaseActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private ImageButton dataImgBtn;
    private Button dataBtn;

    private EditText name;
    private EditText address;
    private EditText phone;
    private EditText note;
    private String radioValue;
    private String spinnerValue;

    private PurchaseDataDao purchaseDataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        name = findViewById(R.id.name_text);
        address = findViewById(R.id.address_text);
        phone = findViewById(R.id.phone_text);
        note = findViewById(R.id.note_text);

        dataImgBtn = findViewById(R.id.save_button);
        dataBtn = findViewById(R.id.save_btn);

        Spinner spinner = findViewById(R.id.label_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.labels_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

        //A kiválasztott termékek megjelenítése
        Intent intent = getIntent();
        ArrayList<String> cakes = intent.getStringArrayListExtra("cakesNameList");
        String result = cakes
                .stream()
                .reduce("", (partialString, element) -> partialString +  element + ", ");
        TextView text = findViewById(R.id.order_textview);
        text.setText(result);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);    }

    @Override
    protected void onStart() {
        super.onStart();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        PurchaseDatabase purchaseDatabase = Room.databaseBuilder(this, PurchaseDatabase.class, "purchase_database").build();
        purchaseDataDao = purchaseDatabase.getPurchaseDataDao();

        dataBtn.setOnClickListener(v -> {
            ViewGroup layout = findViewById(R.id.open_file);
            if (fillTextField(layout)) {
                PurchaseData purchaseData = new PurchaseData(name.getText().toString(), address.getText().toString(), phone.getText().toString(), note.getText().toString());
                AsyncTask.execute(() -> {
                    purchaseDataDao.insertAll(purchaseData);

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"publiclog.txt");
                    try {
                        if (!file.exists()){
                            file.createNewFile();
                        }
                        FileWriter writer = new FileWriter(file, true);
                        BufferedWriter bw = new BufferedWriter(writer);
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        bw.write("Jelenleg ("+ now +") "+purchaseDataDao.getAll().size() +"" + " db cím van az adatbázisban \n" );
                        bw.flush();
                        bw.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(purchaseDataDao.getAll());

                    runOnUiThread(() -> {
                        displayToast("Save purchase");
                        name.setText(null);
                        address.setText(null);
                        phone.setText(null);
                        note.setText(null);
                    });
                });
            }
        });
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.sameday:
                if (checked)
                    // Same day service
                    radioValue = getString(R.string.same_day_messenger_service);
                    displayToast(radioValue);
                break;
            case R.id.nextday:
                if (checked)
                    // Next day delivery
                    radioValue = getString(R.string.next_day_ground_delivery);
                    displayToast(radioValue);
                break;
            case R.id.pickup:
                if (checked)
                    // Pick up
                    radioValue = getString(R.string.pick_up);
                    displayToast(radioValue);
                break;
            default:
                // Do nothing.
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        spinnerValue = adapterView.getItemAtPosition(position).toString();
        displayToast(spinnerValue);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        displayToast("Nothing selected");
    }

    private boolean fillTextField(ViewGroup layout) {
        boolean result = true;
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                if (!fillTextField((ViewGroup) child)) {
                    result = false;
                }
            } else if (child instanceof EditText) {
                EditText editText = (EditText) child;
                if (editText.getText().toString().trim().isEmpty()) {
                    result = false;
                    editText.setError("This field is mandatory");
                }
            }
        }
        return result;
    }

    public void btnCreateFile(View view) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/html");
        this.startActivity(intent);
    }

    public void btnOpenFile(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("text/html");
        this.startActivity(intent);
    }
}