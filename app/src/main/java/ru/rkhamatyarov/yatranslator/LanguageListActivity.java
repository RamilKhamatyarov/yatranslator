package ru.rkhamatyarov.yatranslator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Asus on 18.04.2017.
 */

public class LanguageListActivity extends ListActivity {
//    private static YandexLanguges yandexLanguges;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        Log.d("LanguageListActivity :", " starting");
        super.onCreate(savedInstanceState);

        setListLang("en-ru");

        ListView languageListView = getListView();
        languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String value = (String)parent.getItemAtPosition(position);

                final Button langFromBtn = (Button) findViewById(R.id.langFrom);
                Log.d("LanguageListActivity", "from onItemClick : " + value);

                Intent intent = new Intent();
                intent.putExtra("English", value);
                setResult(RESULT_OK, intent);

                finish();
//                startActivity(getIntent());
            }
        });
    }

    private void setListLang(String langDir){
        Log.d("LanguageListActivity", " getLanguages()");

        Log.d("LanguageListActivity", " 28");

        RequestImpl requestImpl = new RequestImpl(this);

        Log.d("LanguageListActivity", " new RequestImpl()");

        try {
            requestImpl.getRequest(langDir, null, RequestOptions.langList, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        Log.d("LanguageListActivity", "55");
                        Log.d("LanguageListActivity", result.toString());
                        Log.d("LanguageListActivity", "57");
                        ObjectMapper objectMapper = new ObjectMapper();
                        YandexLanguges yandexLanguges = objectMapper.readValue(result.toString(), YandexLanguges.class);
                        Log.d("LanguageListActivity", "60");

                        ListView listLang = getListView();

                        ArrayList<String> langList = new ArrayList<String>(yandexLanguges.getLangs().values());
                        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                                LanguageListActivity.this,
                                android.R.layout.simple_list_item_1,
                                langList);
                        Log.d("LanguageListActivity :", " after listAdapter");
                        listLang.setAdapter(listAdapter);


                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            });
            Log.d("LanguageListActivity", " getRequest");
//            Log.d("JsonArray  : ", String.valueOf(jsonArray[0]));
        }catch (WrongOptionException exc){
            exc.printStackTrace();
        }
        //from Map<String, String> getLangs() to list
//        return new ArrayList<String>(yandexLanguges.getLangs().values());
    }

}
