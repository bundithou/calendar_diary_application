package com.example.newp1;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

/***
 *
 * DetailActivity (Diary view, or Diary page)
 *
 * show the only one diary
 */
public class DetailActivity extends AppCompatActivity {

    /**
     * @override
     * onCreate method
     *
     * declares initial objects for show the diary details
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Initialize the views.
        TextView TitleDate = findViewById(R.id.titleDate);
        TextView TitleName = findViewById(R.id.TitleDetail);
        TextView DateDetail = findViewById(R.id.date_detail);
        TextView detail = findViewById(R.id.subDetail);
        ImageView Image = findViewById(R.id.ImageDetail);

        // Set the text from the Intent extra.
        TitleDate.setText(getIntent().getStringExtra("title"));
        TitleName.setText(getIntent().getStringExtra("subtitle"));
        detail.setText(getIntent().getStringExtra("detail"));
        DateDetail.setText(getIntent().getStringExtra("title"));

        // Load the image using the Glide library and the Intent extra.
        String[] img = getIntent().getStringExtra("image_resource").split(",");
        if(img[0].equals("d")){
            Glide.with(this).load(
                    Integer.parseInt(img[1])).into(Image);
        }
        if(img[0].equals("u")){
            Glide.with(this).load(Uri.parse(img[1])).into(Image);
        }
    }
}
