package com.example.newp1;

import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/***
 *
 * Add Diary Activity
 *
 *  features:
 *  - import images from Resource(by random) and Gallery(External Storage)
 *
 *  after made a diary, show diary page
 */
public class AddDiary extends AppCompatActivity {
    //member variables
    private static int IMG_RESULT = 1;
    String ImageDecode;
    usermanager db;
    String user = "";
    ImageView cover;
    EditText textdetail;
    EditText texttitle;
    FloatingActionButton fab_save;
    Spinner dayspinner;
    Spinner monthspinner;
    private Date currentTime;
    private int currentday;
    private int currentmonth;
    private Date selectedTime;
    int iday;
    int imonth;
    boolean isAddCover;

    /**
     * @override
     * onCreate method
     *
     * declares initial objects
     * Time, SharePreference, Spinner
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        initialcurrenttime();
        initialobject();
        initialspinner();
        initiallistenner();
    }

    /**
     * object initializer method
     *
     * */
    private  void initialobject(){
        textdetail = (EditText) findViewById(R.id.text) ;
        texttitle = (EditText) findViewById(R.id.text_title);
        texttitle.setText("Untitled");

        cover = (ImageView) findViewById(R.id.imageView_navbar);
        isAddCover = false;

        fab_save = (FloatingActionButton) findViewById(R.id.fab_save);

        dayspinner = (Spinner) findViewById(R.id.dayspinner);
        monthspinner = (Spinner) findViewById(R.id.monthspinner);

        db = new usermanager(this);

        user = getIntent().getStringExtra("user");
    }

    /**
     * Time initializer method
     * */
    private void initialcurrenttime(){
        currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd");
        DateFormat monthFormat = new SimpleDateFormat("MM");
        currentday = Integer.valueOf(dateFormat.format(currentTime));
        currentmonth = Integer.valueOf(monthFormat.format(currentTime));
    }

    /**
     * Spinner initializer method
     *
     * mostly for month spinner
     *
     * */
    private void initialspinner(){

        // Create an ArrayAdapter using the string array and a default spinner layout
        String text="";
        for(int i=1;i<=12;i++){
            text += String.valueOf(i);
            text+=",";
        }
        String List[] = text.split(",");
        ArrayAdapter<String> monthadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, List);

        // Specify the layout to use when the list of choices appears
        monthadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        monthspinner.setAdapter(monthadapter);

        //initialize day spinner
        initialdayspinner(31);

        //set selection
        monthspinner.setSelection(currentmonth-1);
        dayspinner.setSelection(currentday-1);
    }

    /**
     * day spinner initializer method
     * for day spinner only
     * also called after selected month
     * for changing days in spinner
     *
     * @param limday (last day of month)
     * */
    private void initialdayspinner(int limday){
        String text="";
        for(int i=1;i<=limday;i++){
            text += String.valueOf(i);
            text+=",";
        }
        String List[] = text.split(",");
        ArrayAdapter<String> dayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, List);
        dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayspinner.setAdapter(dayadapter);
    }

    /**
     * set listener of objects
     * - make and save a diary in database
     * - import image to cover image view
     * - day and month selection
     * */
    private void initiallistenner(){
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textdetail.getText().toString().equals("")){
                    Toast.makeText(v.getContext(), "please write the detail", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if(texttitle.getText().toString().equals("")){
                    Toast.makeText(v.getContext(), "please write the title", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if(!isAddCover){
                    Toast.makeText(v.getContext(), "random cats into cover photo ", Toast.LENGTH_LONG)
                            .show();
                    int randomInt = ThreadLocalRandom.current().nextInt(1, 16);
                    TypedArray diaryImageResources = getResources()
                            .obtainTypedArray(R.array.images_cat);
                    // Set ImageView image from drawable resource
                    cover.setImageDrawable(getDrawable(diaryImageResources.getResourceId(randomInt-1, 0)));
                    ImageDecode = "d,"+String.valueOf(diaryImageResources.getResourceId(randomInt-1, 0));
                }
                //String day = dayspinner.get
                selectedTime = currentTime;
                if(currentday != iday || currentmonth != imonth){
                    selectedTime = new Date(2019, imonth, iday);
                }
                save(selectedTime, texttitle.getText().toString(), textdetail.getText().toString(), ImageDecode);
            }
        });
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addimage();
                TextView textcover = findViewById(R.id.textView_cover) ;
                textcover.setText("");
            }
        });

        monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int lim_day = 31;
                switch (position+1){
                    case 4:case 6:case 9: case 11:
                        lim_day = 30;
                        break;
                    case 2:
                        lim_day = 28;
                        break;
                }
                initialdayspinner(lim_day);
                dayspinner.setSelection(currentday-1);

                imonth = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //initialdayspinner(31);
            }
        });

        dayspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iday = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //initialdayspinner(31);
            }
        });
    }

    /**
     * add image method
     *
     * called startActivityForResult to pick a image
     * */
    private void addimage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMG_RESULT);
    }

    /**
     * save a diary to database
     *
     * param diary info
     * */
    private void save(Date date, String title, String detail, String image){
        Intent myIntent = new Intent(AddDiary.this,
                DetailActivity.class);

        DateFormat dateFormat = new SimpleDateFormat("dd");
        DateFormat monthFormat = new SimpleDateFormat("MMMM");
        myIntent.putExtra("title", dateFormat.format(date)+ " "+monthFormat.format(date));
        myIntent.putExtra("detail", detail);
        myIntent.putExtra("image_resource", image);
        myIntent.putExtra("subtitle", title);
        myIntent.putExtra("date", date.toString());
        startActivity(myIntent);

        db.insertDiary(date, title, detail, image, user);
        finish();
    }

    /**
     * used after AddImage method
     * - get intent data(Uri)
     *
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {

                Uri selectedImageUri = data.getData();

                ImageDecode = "u,"+selectedImageUri.toString();

                cover.setImageURI(selectedImageUri);
                isAddCover = true;
            }
        } catch (Exception e) {
            textdetail.setText(e.getLocalizedMessage());
        }

    }
}
