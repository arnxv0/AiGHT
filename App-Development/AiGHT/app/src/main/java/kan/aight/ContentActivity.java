package kan.aight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listViewFeed;

    TextView blackBarButtonSelected;
    ImageView feedButton;
    ImageView uploadButton;
    ImageView profileButton;

    RelativeLayout relativeLayoutUpload;
    EditText editTextTitle;
    EditText editTextContent;
    TextView textViewUpload;

    RelativeLayout relativeLayoutProfile;
    ListView profileContentListView;
    TextView profileUsername;
    TextView profileEmail;

    TextView signOutButton;

    TextView deleteDialogTitle;
    TextView deleteDialogConfirm;
    TextView deleteDialogCancel;

    ConstraintLayout constraintLayoutContentPage;

    ArrayList<String> usernameList = new ArrayList<>();
    ArrayList<String> contentList = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();

    ArrayList<String> userTitleList = new ArrayList<>();
    ArrayList<String> userContentList = new ArrayList<>();
    ArrayList<String> userPostIdList = new ArrayList<>();

    public void makeShortToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void refreshProfile(){

        userContentList.clear();
        userTitleList.clear();
        userPostIdList.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Content");
        query.addDescendingOrder("createdAt");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null) {

                    userTitleList.clear();
                    userContentList.clear();
                    userPostIdList.clear();

                    for(ParseObject object : objects) {

                        userTitleList.add(object.getString("title"));
                        userContentList.add(object.getString("userContent"));
                        userPostIdList.add(object.getObjectId());
                    }

                    CustomProfileAdapter profileAdapter = new CustomProfileAdapter();
                    profileContentListView.setAdapter(profileAdapter);

                } else {
                    makeShortToast(e.getMessage());
                }

            }
        });


    }

    public void refreshFeed(){

        usernameList.clear();
        titleList.clear();
        contentList.clear();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Content");
        query.setLimit(30);
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null) {
                    usernameList.clear();
                    titleList.clear();
                    contentList.clear();

                    for(ParseObject object : objects) {

                        usernameList.add(object.getString("username"));
                        titleList.add(object.getString("title"));
                        contentList.add(object.getString("userContent"));
                    }

                    CustomFeedAdapter customFeedAdapter = new CustomFeedAdapter();
                    listViewFeed.setAdapter(customFeedAdapter);

                } else {
                    makeShortToast(e.getMessage());
                }

            }
        });
    }

    public void showFeedScreen(){
        relativeLayoutUpload.setVisibility(View.GONE);
        relativeLayoutProfile.setVisibility(View.GONE);
        signOutButton.setVisibility(View.GONE);


        int[] location1 = new int[2];
        feedButton.getLocationOnScreen(location1);
        int x1 = location1[0];
        blackBarButtonSelected.animate().setDuration(300).translationX(x1);


        listViewFeed.setVisibility(View.VISIBLE);
        refreshFeed();
    }

    public void showUploadScreen(){

        listViewFeed.setVisibility(View.GONE);
        relativeLayoutProfile.setVisibility(View.GONE);
        signOutButton.setVisibility(View.GONE);

        int[] location1 = new int[2];
        uploadButton.getLocationOnScreen(location1);
        int x1 = location1[0];
        blackBarButtonSelected.animate().setDuration(300).translationX(x1);

        relativeLayoutUpload.setVisibility(View.VISIBLE);

    }

    public void showProfileScreen(){
        listViewFeed.setVisibility(View.GONE);
        relativeLayoutUpload.setVisibility(View.GONE);

        refreshProfile();

        int[] location1 = new int[2];
        profileButton.getLocationOnScreen(location1);
        int x1 = location1[0];
        blackBarButtonSelected.animate().setDuration(300).translationX(x1);

        signOutButton.setVisibility(View.VISIBLE);
        relativeLayoutProfile.setVisibility(View.VISIBLE);
        profileUsername.setText(ParseUser.getCurrentUser().getUsername());
        profileEmail.setText(ParseUser.getCurrentUser().getEmail());

        profileContentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.imageViewWriteButton){
            showUploadScreen();

        } else if(v.getId() == R.id.imageViewProfileButton){
            showProfileScreen();

        } else if(v.getId() == R.id.imageViewFeedButton){
            showFeedScreen();

        } else if (v.getId()==R.id.textViewUploadButton){

            String uploadTitle = editTextTitle.getText().toString();
            String uploadContent = editTextContent.getText().toString();

            if(uploadTitle.length() > 25 || uploadTitle.isEmpty()) {
                makeShortToast("Title should be between 1 to 25 letters!");
            } else if (uploadContent.isEmpty()){
                makeShortToast("Write more content!");

            } else {
                textViewUpload.setBackground(getDrawable(R.drawable.black_bar_color));
                ParseObject userPost = new ParseObject("Content");
                userPost.put("username", ParseUser.getCurrentUser().getUsername());
                userPost.put("userContent", uploadContent);
                userPost.put("title", uploadTitle);

                userPost.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            makeShortToast("Uploaded!");
                            editTextTitle.getText().clear();
                            editTextContent.getText().clear();
                            showFeedScreen();
                            textViewUpload.setBackground(getDrawable(R.drawable.custom_rectangle));

                        } else {
                            makeShortToast(e.getMessage());
                        }
                    }
                });
            }

        } else if (v.getId() == R.id.textViewLogoOut){
            signOutButton.setBackground(getDrawable(R.drawable.black_bar_color));
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listViewFeed = findViewById(R.id.listviewFeed);

        blackBarButtonSelected = findViewById(R.id.textViewBlackBarSelected);
        feedButton = findViewById(R.id.imageViewFeedButton);
        uploadButton = findViewById(R.id.imageViewWriteButton);
        profileButton = findViewById(R.id.imageViewProfileButton);

        relativeLayoutUpload = findViewById(R.id.relativeLayoutUpload);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextUploadContent);
        textViewUpload = findViewById(R.id.textViewUploadButton);

        relativeLayoutProfile = findViewById(R.id.relativeLayoutProfile);
        profileContentListView = findViewById(R.id.listViewProfileContent);
        profileUsername = findViewById(R.id.textViewFeedUsername);
        profileEmail = findViewById(R.id.textViewEmail);

        signOutButton = findViewById(R.id.textViewLogoOut);

        deleteDialogCancel = findViewById(R.id.textViewDeleteCancel);
        deleteDialogConfirm = findViewById(R.id.textViewDeletePost);
        deleteDialogTitle = findViewById(R.id.textViewDialogueTitle);

        constraintLayoutContentPage = findViewById(R.id.constraintLayoutContentPage);

        if(ParseUser.getCurrentUser() == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        uploadButton.setOnClickListener(this);
        feedButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        textViewUpload.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        constraintLayoutContentPage.setOnClickListener(this);

        refreshFeed();

    }

    class CustomFeedAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return usernameList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(ContentActivity.this).inflate(R.layout.custom_feed_layout, parent,  false);

            TextView textViewFeedUsername = convertView.findViewById(R.id.textViewFeedUsername);
            TextView textViewFeedTitle = convertView.findViewById(R.id.textViewFeedtitle);
            TextView textViewFeedContent = convertView.findViewById(R.id.textViewMultiLineContent);

            textViewFeedContent.setText(contentList.get(position));
            textViewFeedTitle.setText(titleList.get(position));
            textViewFeedUsername.setText(usernameList.get(position));

            return convertView;
        }
    }

    class CustomProfileAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userTitleList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(ContentActivity.this).inflate(R.layout.custom_profile_layout, parent,  false);

            TextView textViewFeedTitle = convertView.findViewById(R.id.textViewFeedtitle);
            TextView textViewFeedContent = convertView.findViewById(R.id.textViewMultiLineContent);

            textViewFeedContent.setText(userContentList.get(position));
            textViewFeedTitle.setText(userTitleList.get(position));

            return convertView;
        }
    }

    public void showDeleteDialog(final int i){

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.custom_dialog, null);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        deleteDialogCancel = view.findViewById(R.id.textViewDeleteCancel);
        deleteDialogConfirm = view.findViewById(R.id.textViewDeletePost);
        deleteDialogTitle = view.findViewById(R.id.textViewDialogueTitle);

        deleteDialogTitle.setText(userTitleList.get(i));


        deleteDialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Content");
                query.getInBackground(userPostIdList.get(i), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        try{
                            object.delete();
                            object.saveInBackground();
                            makeShortToast("Post deleted");
                            refreshProfile();
                        } catch (ParseException ex) {
                            makeShortToast("Try again later!");
                            ex.printStackTrace();
                        }
                    }
                });

                alertDialog.dismiss();
            }
        });

        deleteDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

}