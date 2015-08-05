package com.cooervo.filmography.controllers.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cooervo.filmography.controllers.alertdialogs.AlertDialogFragment;
import com.cooervo.filmography.controllers.alertdialogs.NoInternetConnectionDialog;
import com.cooervo.filmography.R;
import com.cooervo.filmography.controllers.http.AsyncDownloader;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * MainActivity receives user input and searches in theMovieDB API for any result
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //We use library Butterknife bind with less boilerplate code the views
    @Bind(R.id.nameEditText)EditText nameLabel;
    @Bind(R.id.search_button)TextView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Butterknife necessary method
        ButterKnife.bind(this);

        if ( !isNetworkAvailable() ){
            noInternetAlertMessage();
        }

        setTypeFaces();
    }

    /**
     * OnClick of Search Button make a simple validation to see if
     *  field actor name is greater than 2 chars if not it displays
     * a not valid notification (Toast) message to the user
     */
    @OnClick(R.id.search_button)
    public void searchButtonClick() {

        String actorNameToSearch = nameLabel.getText().toString();

        if(actorNameToSearch.length() >= 2){
            String apiKey = "deea9711e0770caae3fc592b028bb17e";
            String searchActorByNameUrlAndSortByPopularity = "http://api.themoviedb.org/3/search/person?api_key=" + apiKey + "&query=" + actorNameToSearch + "&sort_by=popularity";

            AsyncDownloader downloader = new AsyncDownloader(this, new ActorsActivity().getClass());
            downloader.execute(searchActorByNameUrlAndSortByPopularity);

        } else {
            Toast.makeText(this, "Invalid actor/actress name, please try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void noInternetAlertMessage() {
        NoInternetConnectionDialog dialog = new NoInternetConnectionDialog();
        dialog.show(getFragmentManager(), "no_internet_error_dialog");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void setTypeFaces() {
        Typeface latoBlack = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
        nameLabel.setTypeface(latoBlack);
        searchButton.setTypeface(latoBlack);
    }
}