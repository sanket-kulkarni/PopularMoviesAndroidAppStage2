package com.example.sanketk.popularmoives;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.sanketk.popularmoives.adapters.MoviesAdapter;
import com.example.sanketk.popularmoives.database.FavMoviesContract;
import com.example.sanketk.popularmoives.model.ApiResponse;
import com.example.sanketk.popularmoives.model.Helper;
import com.example.sanketk.popularmoives.model.ResultsItem;
import com.example.sanketk.popularmoives.networking.API;
import com.example.sanketk.popularmoives.networking.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityFragment extends Fragment implements MoviesAdapter.OnItemClickListener {
    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private int listType = R.string.most_popular;
    private Dialog mDialog;
    private MoviesAdapter moviesArrayAdapter;
    private ArrayList<ResultsItem> resultsItemList;
    RecyclerView rvMoivesList;
    int numColumns=2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

        this.rvMoivesList = (RecyclerView) rootView.findViewById(R.id.rvMoivesList);
        initialiseRecyclerView();
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            listType= savedInstanceState.getInt("listType", 0);
            resultsItemList= savedInstanceState.getParcelableArrayList("resultsItemList");
            if( this.listType == R.string.most_popular || this.listType == R.string.top_rated )
            {
                webserviceGetInfo();
            }
            else if(listType==R.string.favorites )
            {
                loadFavMovies();
            }
        }
        else {
            webserviceGetInfo();
        }
        return rootView;
    }

    private void webserviceGetInfo() {
        if (Helper.isNetworkAvailble(getActivity())) {
            this.mDialog = Helper.showProgressDialog(getActivity());
            API api = (API) RetrofitClient.getClient().create(API.class);
            Call<ApiResponse> call = null;
            if (this.listType == R.string.most_popular) {
                call = api.getPopularMovies(getResources().getString(R.string.api_key));
            } else if (this.listType == R.string.top_rated) {
                call = api.getTopRatedMovies(getResources().getString(R.string.api_key));
            }
            call.enqueue(new Callback<ApiResponse>() {
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "response.isSuccessful() " + response.isSuccessful());
                        if (response.body() == null) {
                            MainActivityFragment.this.mDialog.dismiss();
                            return;
                        } else if (((ApiResponse) response.body()).getResults() == null) {
                            MainActivityFragment.this.mDialog.dismiss();
                            return;
                        } else {
                            MainActivityFragment.this.mDialog.dismiss();
                            Log.d(TAG, " response.body().getResults().size()   " + ((ApiResponse) response.body()).getResults().size());
                            MainActivityFragment.this.resultsItemList = (ArrayList<ResultsItem>) ((ApiResponse) response.body()).getResults();
                            MainActivityFragment.this.moviesArrayAdapter.resultsItemList = MainActivityFragment.this.resultsItemList;
                            MainActivityFragment.this.moviesArrayAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                    MainActivityFragment.this.mDialog.dismiss();
                    Log.d(TAG, " response.isSuccessful()2  " + response.isSuccessful());
                }

                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    MainActivityFragment.this.mDialog.dismiss();
                    Helper.showOkDialog(MainActivityFragment.this.getActivity(), t.getMessage());
                }
            });
        }
        else
        {
            Helper.showOkDialog(MainActivityFragment.this.getActivity(), "No Internet Connection");
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "Fragment.onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected id " + id);
        Log.d(TAG, "onOptionsItemSelected R.id.action_most_popular "+R.id.action_most_popular);
        Log.d(TAG, "onOptionsItemSelected R.id.action_top_rated "+R.id.action_top_rated);
        if (id == R.id.action_most_popular) {
            this.listType = R.string.most_popular;
            this.resultsItemList.clear();
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.most_popular);
            webserviceGetInfo();
            return true;
        }
        else if (id == R.id.action_favorites) {
            this.listType = R.string.favorites;
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.favorites);
            this.resultsItemList.clear();
            loadFavMovies();
            return true;
        }
        else {
            this.listType = R.string.top_rated;
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.top_rated);
            this.resultsItemList.clear();
            webserviceGetInfo();
            return true;
        }
    }







    private void initialiseRecyclerView() {
        if (this.resultsItemList == null) {
            this.resultsItemList = new ArrayList();
        }
        this.rvMoivesList.setLayoutManager(new GridLayoutManager(getActivity(), numColumns));
        this.moviesArrayAdapter = new MoviesAdapter(getActivity(), this.resultsItemList, this);
        this.rvMoivesList.setAdapter(this.moviesArrayAdapter);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        numColumns=(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
        Log.d(TAG,"onConfigurationChanged numColumns "+numColumns);
        super.onConfigurationChanged(newConfig);
        initialiseRecyclerView();
    }

    @Override
    public void onItemClick(ResultsItem resultsItem) {
        Intent intent = new Intent(getActivity(), MoiveDetailsActivity.class);
        intent.putExtra("resultsItem", resultsItem);
        startActivity(intent);
    }

    private void loadFavMovies() {
        resultsItemList.clear();

        Cursor cursor = getActivity().getContentResolver().query(FavMoviesContract.FavMoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                FavMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ID);

        if (null != cursor) {
            this.mDialog = Helper.showProgressDialog(getActivity());
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            rvMoivesList.setVisibility(View.VISIBLE);
            //mTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {


                    int titleColumnIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry.COLUMN_MOVIE_TITLE);
                    int movieIdColumnIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry.COLUMN_MOVIE_ID);
                    int releaseDateColumnIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry.COLUMN_RDATE);
                    int ratingsColumnIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry.COLUMN_RATINGS);
                    int synopsisColumnIndex = cursor.getColumnIndex(FavMoviesContract.FavMoviesEntry.COLUMN_SYNOPSIS);

                    ResultsItem movie = new ResultsItem();
                    movie.setTitle(cursor.getString(titleColumnIndex));
                    movie.setOriginalTitle(cursor.getString(titleColumnIndex));
                    movie.setId(Integer.parseInt(cursor.getString(movieIdColumnIndex)));
                    movie.setReleaseDate(cursor.getString(releaseDateColumnIndex));
                    movie.setVoteAverage(Double.parseDouble(cursor.getString(ratingsColumnIndex)));
                    movie.setOverview(cursor.getString(synopsisColumnIndex));

                    resultsItemList.add(movie);


                }

                moviesArrayAdapter.notifyDataSetChanged();
                mDialog.dismiss();
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            } else {
                rvMoivesList.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                mDialog.dismiss();
//                mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
//                mTvErrorMessageDisplay.setText(R.string.favorites_msg);
//                mPbLoadingIndicator.setVisibility(View.INVISIBLE);

            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("listType", listType);
        outState.putParcelableArrayList("resultsItemList", resultsItemList);

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            listType= savedInstanceState.getInt("listType", 0);
            resultsItemList= savedInstanceState.getParcelableArrayList("resultsItemList");
            if( this.listType == R.string.most_popular || this.listType == R.string.top_rated )
            {
                webserviceGetInfo();
            }
            else if(listType==R.string.favorites )
            {
                loadFavMovies();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(listType==R.string.favorites )
        {
            loadFavMovies();
        }

    }




}
