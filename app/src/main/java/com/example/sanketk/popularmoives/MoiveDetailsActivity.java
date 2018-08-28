package com.example.sanketk.popularmoives;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.sanketk.popularmoives.adapters.ReviewAdapter;
import com.example.sanketk.popularmoives.adapters.TrailersAdapter;
import com.example.sanketk.popularmoives.database.FavMoviesContract;
import com.example.sanketk.popularmoives.model.Helper;
import com.example.sanketk.popularmoives.model.ResultsItem;
import com.example.sanketk.popularmoives.model.ReviewsResponse;
import com.example.sanketk.popularmoives.model.ReviewsResultsItem;
import com.example.sanketk.popularmoives.model.TrailerResultsItem;
import com.example.sanketk.popularmoives.model.TrailersResponse;
import com.example.sanketk.popularmoives.networking.API;
import com.example.sanketk.popularmoives.networking.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoiveDetailsActivity extends AppCompatActivity implements TrailersAdapter.OnItemClickListener{
    private static final String TAG = MoiveDetailsActivity.class.getSimpleName();
    private static final String imgUrl = "http://image.tmdb.org/t/p/w185/";
    ImageView ivMoviePoster;
    TextView tvMoiveTitle;
    TextView tvOverview;
    TextView tvReleaseDate;
    TextView tvVoteAverage;
    TextView tvTrailer;
    TextView tvReviews;
    RecyclerView rvTrailers;
    RecyclerView rvReviews;
    private List<TrailerResultsItem> trailerResultsItems;
    private List<ReviewsResultsItem> reviewsResultsItems;
    TrailersAdapter trailersAdapter;
    ReviewAdapter reviewAdapter ;
    private Dialog mDialog;
    ResultsItem resultsItem;
    boolean isFav;
    Button btnfavorite;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moive_details);

        this.tvMoiveTitle = (TextView) findViewById(R.id.tvMoiveTitle);
        this.ivMoviePoster = (ImageView) findViewById(R.id.ivMoviePoster);
        this.tvOverview = (TextView) findViewById(R.id.tvOverview);
        this.tvOverview.setMovementMethod(new ScrollingMovementMethod());
        this.tvVoteAverage = (TextView) findViewById(R.id.tvVoteAverage);
        this.tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        this.tvTrailer = (TextView) findViewById(R.id.tvTrailer);
        this.tvReviews = (TextView) findViewById(R.id.tvReviews);
        this.rvTrailers = (RecyclerView) findViewById(R.id.rvTrailers);
        this.rvReviews = (RecyclerView) findViewById(R.id.rvReviews);
        this.btnfavorite = (Button) findViewById(R.id.btnfavorite);
        if(getIntent().getParcelableExtra("resultsItem")!=null) {
            resultsItem=getIntent().getParcelableExtra("resultsItem");
            //Bundle bundle=getIntent().getExtras();
            Log.d(TAG,"onBindViewHolder resultsItem "+resultsItem);
            String strOriginalTitle=resultsItem.getOriginalTitle();
            String strOverview=resultsItem.getOverview();
            String strReleaseDate=resultsItem.getReleaseDate();
            String strVoterAvg=resultsItem.getVoteAverage()+ "/10";
            String strPosterPath=resultsItem.getPosterPath();
            this.tvMoiveTitle.setText(strOriginalTitle);
            this.tvOverview.setText(strOverview);
            this.tvVoteAverage.setText(strVoterAvg);
            this.tvReleaseDate.setText(strReleaseDate);
            if (!TextUtils.isEmpty(strPosterPath)) {
                Glide.with(this).load(imgUrl + strPosterPath).into(this.ivMoviePoster);
            }
            else
            {
                loadPicLocally();
            }
            if (Helper.isNetworkAvailble(this)){

                rvReviews.setVisibility(View.VISIBLE);
                rvTrailers.setVisibility(View.VISIBLE);
                tvReviews.setVisibility(View.VISIBLE);
                tvTrailer.setVisibility(View.VISIBLE);
            }else {
                rvReviews.setVisibility(View.GONE);
                rvTrailers.setVisibility(View.GONE);
                tvReviews.setVisibility(View.GONE);
                tvTrailer.setVisibility(View.GONE);
                loadPicLocally();

            }
            getSupportActionBar().setTitle(strOriginalTitle);        }
        isFavMovie();
        if (isFav) {
            btnfavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_24dp, 0, 0, 0);
            btnfavorite.setText(R.string.mark_as_unfavorite);

        } else {
            btnfavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_border_24dp, 0, 0, 0);
            btnfavorite.setText(R.string.mark_as_favorite);

        }

        btnfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFav) {

                    removeFromFav();


                } else {

                    addToFav();

                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialiseRecyclerViews();
        webserviceGetTrailers();
    }

    private void initialiseRecyclerViews() {
        if (this.trailerResultsItems == null) {
            this.trailerResultsItems = new ArrayList();
        }
        if (this.reviewsResultsItems == null) {
            this.reviewsResultsItems = new ArrayList();
        }
        this.rvTrailers.setLayoutManager(new LinearLayoutManager(this));
        rvTrailers.setNestedScrollingEnabled(false);
        this.trailersAdapter = new TrailersAdapter(this, this.trailerResultsItems, this);
        this.rvTrailers.setAdapter(this.trailersAdapter);

        this.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setNestedScrollingEnabled(false);
        this.reviewAdapter = new ReviewAdapter(this, this.reviewsResultsItems);
        this.rvReviews.setAdapter(this.reviewAdapter);
    }

    @Override
    public void onItemClick(TrailerResultsItem item) {
        watchYoutubeVideo(MoiveDetailsActivity.this,item.getKey());
    }

    @Override
    public void onShareClick(TrailerResultsItem item) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, resultsItem.getOriginalTitle());
        i.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + item.getKey());
        startActivity(Intent.createChooser(i, "Share "+resultsItem.getOriginalTitle()));
    }

    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    private void webserviceGetTrailers() {
        if (Helper.isNetworkAvailble(this)) {
            this.mDialog = Helper.showProgressDialog(this);
            API api = (API) RetrofitClient.getClient().create(API.class);
            Call<TrailersResponse> call = null;
                call = api.getMoviesTrailers(String.valueOf(resultsItem.getId()),getResources().getString(R.string.api_key));
            call.enqueue(new Callback<TrailersResponse>() {
                public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "response.isSuccessful() " + response.isSuccessful());
                        if (response.body() == null) {
                            MoiveDetailsActivity.this.mDialog.dismiss();
                            return;
                        } else if (((TrailersResponse) response.body()).getTrailerResults() == null) {
                            MoiveDetailsActivity.this.mDialog.dismiss();
                            return;
                        } else {
                            MoiveDetailsActivity.this.mDialog.dismiss();
                            Log.d(TAG, " response.body().getResults().size()   " + ((TrailersResponse) response.body()).getTrailerResults().size());
                            MoiveDetailsActivity.this.trailerResultsItems = ((TrailersResponse) response.body()).getTrailerResults();
                            MoiveDetailsActivity.this.trailersAdapter.trailerResultsItems = MoiveDetailsActivity.this.trailerResultsItems;
                            MoiveDetailsActivity.this.trailersAdapter.notifyDataSetChanged();
                            webserviceGetReviews();
                            return;
                        }
                    }
                    MoiveDetailsActivity.this.mDialog.dismiss();
                    Log.d(TAG, " response.isSuccessful()2  " + response.isSuccessful());

                }

                public void onFailure(Call<TrailersResponse> call, Throwable t) {
                    MoiveDetailsActivity.this.mDialog.dismiss();
                    Helper.showOkDialog(MoiveDetailsActivity.this, t.getMessage());
                }
            });
        }
        else
        {
            Helper.showOkDialog(MoiveDetailsActivity.this, getString(R.string.no_internet_connection));
        }
    }

    private void webserviceGetReviews() {
        if (Helper.isNetworkAvailble(this)) {
            this.mDialog = Helper.showProgressDialog(this);
            API api = (API) RetrofitClient.getClient().create(API.class);
            Call<ReviewsResponse> call = null;
            call = api.getMoviesReviews(String.valueOf(resultsItem.getId()),getResources().getString(R.string.api_key));
            call.enqueue(new Callback<ReviewsResponse>() {
                public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "response.isSuccessful() " + response.isSuccessful());
                        if (response.body() == null) {
                            MoiveDetailsActivity.this.mDialog.dismiss();
                            return;
                        } else if (((ReviewsResponse) response.body()).getReviewsResults() == null) {
                            MoiveDetailsActivity.this.mDialog.dismiss();
                            return;
                        } else {
                            MoiveDetailsActivity.this.mDialog.dismiss();
                            Log.d(TAG, " response.body().getResults().size()   " + ((ReviewsResponse) response.body()).getReviewsResults().size());
                            MoiveDetailsActivity.this.reviewsResultsItems = ((ReviewsResponse) response.body()).getReviewsResults();
                            MoiveDetailsActivity.this.reviewAdapter.reviewsResultsItems = MoiveDetailsActivity.this.reviewsResultsItems;
                            MoiveDetailsActivity.this.reviewAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                    MoiveDetailsActivity.this.mDialog.dismiss();
                    Log.d(TAG, " response.isSuccessful()2  " + response.isSuccessful());
                }

                public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                    MoiveDetailsActivity.this.mDialog.dismiss();
                    Helper.showOkDialog(MoiveDetailsActivity.this, t.getMessage());
                }
            });
        }
        else
        {
            Helper.showOkDialog(MoiveDetailsActivity.this, getString(R.string.no_internet_connection));
        }
    }

    private void isFavMovie() {

        Cursor cursor = getContentResolver().query(FavMoviesContract.FavMoviesEntry.CONTENT_URI, new String[]{FavMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ID}, FavMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(resultsItem.getId())}, null);
        if (null != cursor) {
            int cursorCount = cursor.getCount();
            if (cursorCount > 0) {
                isFav = true;


            } else {
                isFav = false;
            }



            Log.d(TAG,"isFavMovie:  isFav" + isFav);
            cursor.close();

        } else {
            Log.d(TAG,"isFavMovie: cursor is null");
        }
    }
    private  void loadPicLocally (){
        String filename = String.valueOf(resultsItem.getId());

        File picfile = new File(this.getFilesDir(), filename);

        if (picfile.exists()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(picfile));
                ivMoviePoster.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                ivMoviePoster.setImageResource(R.drawable.ic_error_outline_black_24dp);
                e.printStackTrace();
            }
        } else {
            ivMoviePoster.setImageResource(R.drawable.ic_error_outline_black_24dp);
        }
    }

    private void addToFav() {
        isFav = true;
        btnfavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_24dp, 0, 0, 0);
        btnfavorite.setText(R.string.mark_as_unfavorite);
        ContentValues values = new ContentValues();
        values.put(FavMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ID, resultsItem.getId());
        values.put(FavMoviesContract.FavMoviesEntry.COLUMN_MOVIE_TITLE,  resultsItem.getTitle());
        values.put(FavMoviesContract.FavMoviesEntry.COLUMN_RDATE,  resultsItem.getReleaseDate());
        values.put(FavMoviesContract.FavMoviesEntry.COLUMN_RATINGS, resultsItem.getVoteAverage());
        values.put(FavMoviesContract.FavMoviesEntry.COLUMN_SYNOPSIS, resultsItem.getOverview());

        Uri addToFavUri = getContentResolver().insert(FavMoviesContract.FavMoviesEntry.CONTENT_URI, values);

        Bitmap bitmap = ((GlideBitmapDrawable)ivMoviePoster.getDrawable().getCurrent()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        String filename = String.valueOf(resultsItem.getId());
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(byteArray);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(MoiveDetailsActivity.this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();

    }

    private void removeFromFav() {
        isFav = false;
        btnfavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_border_24dp, 0, 0, 0);
        btnfavorite.setText(R.string.mark_as_favorite);
        int rowsDeleted = getContentResolver().delete(FavMoviesContract.FavMoviesEntry.CONTENT_URI, FavMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(resultsItem.getId())});

        if (rowsDeleted > 0) {
            isFav = false;
            Toast.makeText(this, R.string.removed_from_favs, Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
