package hu.uni.miskolc.droidcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DessertsDescriptionActivity extends AppCompatActivity {

    ImageView mainImageView;
    TextView name, description;

    String data1, data2;
    int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desserts_description);

        mainImageView = findViewById(R.id.imageView);
        name = findViewById(R.id.cakes_name);
        description = findViewById(R.id.cakes_description);

        getData();
        setData();
    }

    private void getData() {
        if (getIntent().hasExtra("cakesNames") && getIntent().hasExtra("cakesDesc") && getIntent().hasExtra("cakesImages")) {
            data1 = getIntent().getStringExtra("cakesNames");
            data2 = getIntent().getStringExtra("cakesDesc");
            img = getIntent().getIntExtra("cakesImages", 1);
        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_LONG).show();
        }
    }

    private void setData() {
        name.setText(data1);
        description.setText(data2);
        mainImageView.setImageResource(img);
    }
}