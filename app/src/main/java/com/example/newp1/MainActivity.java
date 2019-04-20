package com.example.newp1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/***
 * Main Activity for the Material Me app, a mock sports news application.
 */
public class MainActivity extends AppCompatActivity {

//    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Diary> mDiaryData;
    private DiaryAdapter mAdapter;
    private Context mContext;

    private String e_user = "";
    private String n_user = "";

    SharedPreferences sp;
    private FloatingActionButton fab_main;

    private Date currentTime;

    private String[] mDrawerTitle = {"refresh", "schedule", "day", "week", "month"};
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private TextView account_e;
    private TextView account_n;
    private NavigationView navbar;

    private usermanager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("login",MODE_PRIVATE);
        db = new usermanager(this);
        upgradedb();

        if(sp.getString("logged","") != null){
            Intent i = new Intent( MainActivity.this, login.class);
            startActivityForResult(i, login.RESULT_FIRST_USER);
        }

        initialnavsidebar();

        currentTime = Calendar.getInstance().getTime();

        fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFABMenu();
            }
        });

    }

    private void initialrecyclerview(){
        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerView);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ArrayList that will contain the data.
        mDiaryData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new DiaryAdapter(this, mDiaryData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();

        // Helper class for creating swipe to dismiss and drag and drop
        // functionality.
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            /**
             * Defines the drag and drop functionality.
             *
             * @param recyclerView The RecyclerView that contains the list items
             * @param viewHolder The DiaryViewHolder that is being moved
             * @param target The DiaryViewHolder that you are switching the
             *               original one with.
             * @return true if the item was moved, false otherwise
             */
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                // Get the from and to positions.
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                // Swap the items and notify the adapter.
                Collections.swap(mDiaryData, from, to);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            /**
             * Defines the swipe to dismiss functionality.
             *
             * @param viewHolder The viewholder being swiped.
             * @param direction The direction it is swiped in.
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                // Remove the item from the dataset.
                mDiaryData.remove(viewHolder.getAdapterPosition());
                // Notify the adapter.
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        // Attach the helper to the RecyclerView.
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == login.RESULT_FIRST_USER && resultCode == RESULT_OK && data != null) {
            n_user = data.getStringExtra("u_name");
            e_user = data.getStringExtra("u_email");

            System.out.println(e_user);

            View inflatedView = getLayoutInflater().inflate(R.layout.nav_header_main, null);
            account_e = (TextView) inflatedView.findViewById(R.id.nav_header_textView2) ;
            account_n = (TextView) inflatedView.findViewById(R.id.nav_header_textView) ;
            account_e.setText(e_user);
            account_n.setText(n_user);
            navbar.addHeaderView(inflatedView);

            initialrecyclerview();
            mock();
        }
    }

    private void initializeDataDB() {
        mDiaryData.clear();
        mDiaryData.addAll(db.getAlldiary(e_user));

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();

    }
    /**
     * Initialize the diary data from resources.
     */
    private void initializeData() {
        // Get the resources from the XML file.
        String[] monthList = getResources()
                .getStringArray(R.array.month_titles);
        String[] diaryList = getResources()
                .getStringArray(R.array.mock_date);
        String[] diaryInfo = getResources()
                .getStringArray(R.array.info);
        TypedArray diaryImageResources = getResources()
                .obtainTypedArray(R.array.images);

        // Clear the existing data (to avoid duplication).
        mDiaryData.clear();

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport
        for (int i = 0; i < diaryList.length; i++) {
            if(i==1){
                DateFormat dateFormat = new SimpleDateFormat("dd");
                DateFormat monthFormat = new SimpleDateFormat("MM");
                String formattedDate=dateFormat.format(currentTime);
                String formattedMonth=monthList[Integer.parseInt(monthFormat.format(currentTime))-1];
                mDiaryData.add(new Diary(
                        e_user,
                        formattedDate+" "+formattedMonth,
                        "title_mock",
                        diaryInfo[i],
                        "d,"+String.valueOf(diaryImageResources.getResourceId(i, 0))
                        )
                );
            } else {
                mDiaryData.add(new Diary(
                        e_user,
                        diaryList[i],
                        "title_mock",
                        diaryInfo[i],
                        "d,"+String.valueOf(diaryImageResources.getResourceId(i, 0))
                        )
                );
                //String text = "r,"+String.valueOf(diaryImageResources.getResourceId(i, 0));
                //System.out.println(text);
                //String[] text2 = text.split(",");
                //System.out.println("type: "+text2[0]);
                //System.out.println("type: "+text2[1]);
            }
            //System.out.println("user: "+e_user);
        }

        // Recycle the typed array.
        diaryImageResources.recycle();

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }

    /**
     * onClick method for th FAB that resets the data.
     *
     * @param view The button view that was clicked.
     */
    public void resetSports(View view) {
        //initializeData();
        initializeDataDB();
    }

    @SuppressLint("RestrictedApi")
    private void showFABMenu(){
        //if you need open the slide:
        mDrawerLayout.openDrawer(Gravity.LEFT);

        //fab_main.animate().rotation(90);

    }

    private void initialnavsidebar(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navbar = (NavigationView) findViewById(R.id.nav_view);
        navbar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                boolean c =false;
                Intent i;
                switch(id)
                {
                    default:
                        c = true;
                    case R.id.add_diary:
                        Toast.makeText(MainActivity.this, "add diary",Toast.LENGTH_SHORT).show();
                        i = new Intent( MainActivity.this, AddDiary.class);
                        i.putExtra("user", e_user);
                        startActivity(i);
                        break;
                    case R.id.refresh:
                        Toast.makeText(MainActivity.this, "refresh",Toast.LENGTH_SHORT).show();
                        resetSports(null);
                        break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Preferences",Toast.LENGTH_SHORT).show();
                        i = new Intent( MainActivity.this, SettingActivity.class);
                        startActivity(i);
                        break;
                    case R.id.schedule:
                        Toast.makeText(MainActivity.this, "schedule",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.month:
                        Toast.makeText(MainActivity.this, "month",Toast.LENGTH_SHORT).show();
                        i = new Intent( MainActivity.this, monthView.class);
                        startActivity(i);
                        break;

                }
                return c;
            }
        });

    }

    private void mock () {
        for(Diary d : mDiaryData){
            DateFormat fmt = new SimpleDateFormat("dd LLLL");
            //System.out.println(fmt.parse(d.getDate()).toString());
            db.insertDiary(
                    currentTime,
                    d.getTitle(),
                    d.getInfo(),
                    String.valueOf(d.getImageResource()),
                    d.getUser()
            );
        }
    }

    private void upgradedb(){
        db.onUpgrade(db.getWritableDatabase(),1,1);
        db.addUser(new user(0, "admin", "admin@mail.com", "1234"));
    }
}