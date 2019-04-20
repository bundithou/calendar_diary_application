package com.example.newp1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;


class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    // Member variables.
    private ArrayList<Diary> mDiaryData;
    private Context mContext;


    DiaryAdapter(Context context, ArrayList<Diary> diaryData) {
        this.mDiaryData = diaryData;
        this.mContext = context;
    }



    @Override
    public DiaryAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent, false));
    }


    public void onBindViewHolder(DiaryAdapter.ViewHolder holder,
                                 int position) {
        // Get current sport.
        Diary currentDiary = mDiaryData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentDiary);
    }


    @Override
    public int getItemCount() {
        return mDiaryData.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mSubTitle;
        private ImageView mImage;


        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mTitleText = itemView.findViewById(R.id.title);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mSubTitle = itemView.findViewById(R.id.newsTitle);
            mImage = itemView.findViewById(R.id.Image);

            // Set the OnClickListener to the entire view.
            itemView.setOnClickListener(this);
        }

        void bindTo(Diary currentDiary){
            // Populate the textviews with data.
            mTitleText.setText(currentDiary.getDate());
            String detail = currentDiary.getInfo();
            if(detail.length() > 30){
                detail = detail.substring(0, 30) + "...";
            }
            mSubTitle.setText(currentDiary.getTitle());
            mInfoText.setText(detail);

            // Load the images into the ImageView using the Glide library.
            String[] img = currentDiary.getImageResource().split(",");
            if(img[0].equals("d")){
                Glide.with(mContext).load(
                        Integer.parseInt(img[1])).into(mImage);
            }
            if(img[0].equals("u")){
                System.out.println(img[1]);

                Uri fileUri = Uri.parse(img[1]);
                //System.out.println(fileUri.);
                //mContext.grantUriPermission("com.example.newp1", fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //revoke permisions
                //mContext.revokeUriPermission(fileUri, );

                /**
                 * permission error
                 * java.lang.SecurityException(Permission Denial: opening provider com.android.providers.media.MediaDocumentsProvider
                 * from ProcessRecord{51795fc 29810:com.example.newp1/u0a226} (pid=29810, uid=10226)
                 * requires android.permission.MANAGE_DOCUMENTS or android.permission.MANAGE_DOCUMENTS)
                 *
                 * */
                //Glide.with(mContext).load(fileUri).into(mImage);

                //String[] proj = {MediaStore.Images.Media.DATA};
                //Cursor cursor = mContext.getContentResolver().query(fileUri, proj, null, null, null);
                //int col = cursor.getColumnIndexOrThrow(proj[0]);
                //cursor.moveToFirst();
                //File f = new File(cursor.getString(col));
                //Glide.with(mContext).load(f).into(mImage);
                mImage.setImageURI(Uri.parse(img[1]));
                //Picasso.with(mContext).load(fileUri).into(mImage);
            }
            //mImage.setImageResource(currentDiary.getImageResource());
        }


        @Override
        public void onClick(View view) {
            Diary current = mDiaryData.get(getAdapterPosition());
            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            detailIntent.putExtra("title", current.getDate());
            detailIntent.putExtra("subtitle", current.getTitle());
            detailIntent.putExtra("detail", current.getInfo());
            detailIntent.putExtra("image_resource",
                    current.getImageResource());
            mContext.startActivity(detailIntent);
        }


    }
}