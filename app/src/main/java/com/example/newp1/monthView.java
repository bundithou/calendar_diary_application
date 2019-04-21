package com.example.newp1;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.CalendarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/***
 *
 * MonthViewActivity (Month view, or Month page)
 *
 * show list of diary cards(card view) in the selected date
 */
public class monthView extends AppCompatActivity {
    //Member varisbles
    CalendarView calendarView;
    private RecyclerView mRecyclerView;
    private ArrayList<Diary> mDiaryData;
    private DiaryAdapter mAdapter;
    private Date currentTime;
    String e_user="";
    usermanager db;

    /**
     * @override
     * onCreate method
     *
     * declares initial objects
     * Calendar, Database, Time, object on layout
     *
     * set listener of calendar to get the list of diary
     *
     * call month layout
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        db = new usermanager(this);
        e_user = getIntent().getStringExtra("user");
        currentTime = Calendar.getInstance().getTime();
        initialrecyclerview();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                initializeData(dayOfMonth, month+1);
            }
        });

    }

    /**
     * recycler view initializer method
     * initialize recycler view object
     * based on date selected
     * declare layout and prepare data
     * */
    private void initialrecyclerview(){
        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ArrayList that will contain the data.
        mDiaryData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new DiaryAdapter(this, mDiaryData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        DateFormat dateFormat = new SimpleDateFormat("dd-MM");
        //DateFormat monthFormat = new SimpleDateFormat("MM");
        String date[] = dateFormat.format(currentTime).split("-");
        initializeData(Integer.valueOf(date[0]), Integer.valueOf(date[1]));

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

    /**
     * get diaries on certain date
     *
     * @param day
     * @param month
     * */
    private void initializeData(int day, int month) {
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

        //
        List<Diary> list = db.getAlldiary(e_user);

        System.out.println("day: "+day);
        System.out.println("month: "+month);
        for(Diary d: list){

                String strdate[] = d.getDate().split("-");
                System.out.println(strdate[1]+", "+strdate[2]);

                if(Integer.valueOf(strdate[2]) == day
                     &&  Integer.valueOf(strdate[1]) == (month)){

                    mDiaryData.add(d);
                }

        }


        // Recycle the typed array.
        diaryImageResources.recycle();

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }
}
