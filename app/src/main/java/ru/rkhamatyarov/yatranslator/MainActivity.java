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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mTextMessage;
    private EditText editText;
    private Button btn;
    private Button btnLangFrom;
    private String wordsLine = null;

    private final int REQUEST_LANG_FROM = 1;
    private final int REQUEST_LANG_TO = 2;

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
                case R.id.navigation_dashboard:
//                    editText.setText(R.string.title_dashboard);
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

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Buton in toolbar panel
        btnLangFrom = (Button) findViewById(R.id.langFrom);
        btnLangFrom.setOnClickListener(this);

        mTextMessage = (LinearLayout) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        editText = (EditText) mTextMessage.findViewById(R.id.edittext);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);

        translatedWords = (TextView) findViewById(R.id.translated_words);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.button:
                wordsLine = editText.getText().toString();
                Log.d("Translate button", wordsLine);
                RequestImpl requestImpl = new RequestImpl(MainActivity.this);
                try {
                    requestImpl.getRequest("en-ru", wordsLine, RequestOptions.translateWords, null);
                }catch (WrongOptionException exc){
                    exc.printStackTrace();
                };
                break;
            case R.id.langFrom:
                intent = new Intent(this, LanguageListActivity.class);
                startActivityForResult(intent, REQUEST_LANG_FROM);
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case REQUEST_LANG_FROM:
                    String langFrom = data.getStringExtra("English");
                    btnLangFrom.setText(langFrom);
                    break;
//                case REQUEST_LANG_TO:
//                    int align = data.getIntExtra("alignment", Gravity.LEFT);
//                    tvText.setGravity(align);
//                    break;
            }
        } else {
            Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
        }
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

}
