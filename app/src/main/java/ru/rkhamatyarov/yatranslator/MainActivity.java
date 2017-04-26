package ru.rkhamatyarov.yatranslator;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private final String LOG_TAG = "YaLog";

    private RelativeLayout mTextMessage;

    private String fullLangDir = "en-ru";
    private EditText editText;
    private Button btnLangFrom;
    private Button btnLangTo;
    private ImageButton btnFavorite;
    private String wordsLine = null;

    private final int REQUEST_LANG_FROM = 1;
    private final int REQUEST_LANG_TO = 2;

    private static final String EXTRA_LINE_FROM = "wordsFrom";
    private static final String EXTRA_LINE_TO = "wordsTo";
    private static final String EXTRA_LANG_DIR = "langDir";
    private static final String EXTRA_FULL_LANG_DIR = "fullLangDir";

    private static final String EXTRA_FULL_LANG_FROM = "LangFrom";
    private static final String EXTRA_FULL_LANG_TO = "LangTo";
    private static final String EXTRA_FULL_LANG_FROM_IND = "LangFromInd";
    private static final String EXTRA_FULL_LANG_TO_IND = "LangToInd";


    public static TextView translatedWords;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.translate:
//                    mTextMessage.setText(R.string.title_home);
//                    editText.setText(R.string.title_home);
                return true;
                case R.id.navigation_favorites:
                    return true;
                case R.id.navigation_notifications:
//                    editText.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar_main
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Butons in toolbar_main panel
        btnLangFrom = (Button) findViewById(R.id.langFrom);
        btnLangFrom.setOnClickListener(this);

        btnLangTo = (Button) findViewById(R.id.langTo);
        btnLangTo.setOnClickListener(this);

        btnFavorite = (ImageButton) findViewById(R.id.favoritesBtn);
        btnFavorite.setOnClickListener(this);

        mTextMessage = (RelativeLayout) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Edittext on entering letters
        editText = (EditText) mTextMessage.findViewById(R.id.edittext);
        editText.addTextChangedListener(this);

        translatedWords = (TextView) findViewById(R.id.translated_words);
//        translatedWords.setText("Hello");
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {

            case R.id.langFrom:
                intent = new Intent(this, LanguageListActivity.class);
                Bundle mainFromBundle = new Bundle();
                mainFromBundle.putSerializable(EXTRA_LANG_DIR, LanguageDir.FROM);
                mainFromBundle.putString(EXTRA_FULL_LANG_DIR, fullLangDir);
                intent.putExtras(mainFromBundle);

                startActivityForResult(intent, REQUEST_LANG_FROM);
                break;
            case R.id.langTo:
                intent = new Intent(this, LanguageListActivity.class);
                Bundle mainToBundle = new Bundle();
                mainToBundle.putSerializable(EXTRA_LANG_DIR, LanguageDir.TO);
                mainToBundle.putString(EXTRA_FULL_LANG_DIR, fullLangDir);
                intent.putExtras(mainToBundle);

                startActivityForResult(intent, REQUEST_LANG_TO);
                break;
            case R.id.favoritesBtn:
                intent = new Intent(this, FavoriteActivity.class);
                Bundle mainFavorBundle = new Bundle();
                mainFavorBundle.putString(EXTRA_LANG_DIR, fullLangDir);
                mainFavorBundle.putString(EXTRA_LINE_FROM, wordsLine);
                mainFavorBundle.putString(EXTRA_LINE_TO, wordsLine);
                intent.putExtras(mainFavorBundle);

                startActivityForResult(intent, 1);
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = " + resultCode);
        String langFromInd = null, langToInd = null;
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case REQUEST_LANG_FROM:
                    Bundle bundleFrom = data.getExtras();
                    String langFrom = bundleFrom.getString(EXTRA_FULL_LANG_FROM);
                    langFromInd = bundleFrom.getString(EXTRA_FULL_LANG_FROM_IND);

                    btnLangFrom.setText(langFrom);
                    break;
                case REQUEST_LANG_TO:
                    Bundle bundleTo = data.getExtras();
                    String langTo = bundleTo.getString(EXTRA_FULL_LANG_TO);
                    langToInd = bundleTo.getString(EXTRA_FULL_LANG_TO_IND);

                    btnLangTo.setText(langTo);
                    break;
            }
        } else {
            Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
        }
        setLangDir(langFromInd, langToInd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.language_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public String getLangDir() {
        return fullLangDir;
    }

    public void setLangDir(String langFrom, String langTo) {
        if (langFrom == null && langTo == null) return;
        else if (langFrom != null && langTo == null) {
            fullLangDir = langFrom + fullLangDir.substring(2,5);
        }
        else if (langFrom == null && langTo != null){
            fullLangDir = fullLangDir.substring(0,3) + langTo;
        }
        else {
            fullLangDir = langFrom + "-" + fullLangDir;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        wordsLine = editText.getText().toString();
        Log.d(LOG_TAG, wordsLine);
        RequestImpl requestImpl = new RequestImpl(MainActivity.this);
        try {
            Log.d(LOG_TAG, fullLangDir);
            requestImpl.getRequest(fullLangDir, wordsLine, RequestOptions.translateWords, null);
        }catch (WrongOptionException exc){
            exc.printStackTrace();
        };
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
