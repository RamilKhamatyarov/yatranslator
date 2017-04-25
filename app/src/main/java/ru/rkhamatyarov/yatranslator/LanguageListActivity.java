package ru.rkhamatyarov.yatranslator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.Map;

/**
 * Created by Asus on 18.04.2017.
 */

enum LanguageDir {
    FROM, TO
}

public class LanguageListActivity extends ListActivity {
    private YandexLanguges yandexLanguges;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Intent from main activity
        Intent mainIntent = getIntent();
        final LanguageDir langDir = (LanguageDir) mainIntent.getSerializableExtra("langDir");
        String fullLangDir = mainIntent.getStringExtra("fullLangDir");
        Log.d("langDir", langDir.toString());

        if (langDir.equals(LanguageDir.FROM)) {
            setListLang(fullLangDir.substring(0, 2));
            Log.d("fullLangDirs", fullLangDir.substring(0, 2));
        } else if (langDir.equals(LanguageDir.TO)) {
            setListLang(fullLangDir.substring(3, 5));
            Log.d("fullLangDirs", fullLangDir.substring(3, 5));
        }

        ListView languageListView = getListView();
        languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String value = (String)parent.getItemAtPosition(position);
                String langFrom = getLangIndex(value);

                Log.d("LanguageListActivity", "from onItemClick : " + value);

                Intent intent = new Intent();
                Bundle extras = new Bundle();

                if (langDir.equals(LanguageDir.FROM)) {
                    extras.putString("LangFrom", value);
                    extras.putString("LangFromInd", langFrom);
                } else if (langDir.equals(LanguageDir.TO)) {
                    extras.putString("LangTo", value);
                    extras.putString("LangToInd", langFrom);
                }

                intent.putExtras(extras);

                setResult(RESULT_OK, intent);

                finish();
//                startActivity(getIntent());
            }
        });
    }

    private void setListLang(String langDir){
        Log.d("LanguageListActivity", " getLanguages()");

        RequestImpl requestImpl = new RequestImpl(this);

        Log.d("LanguageListActivity", " new RequestImpl()");

        try {
            requestImpl.getRequest(langDir, null, RequestOptions.langList, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        Log.d("LanguageListActivity", result.toString());

                        ObjectMapper objectMapper = new ObjectMapper();
                        yandexLanguges = objectMapper.readValue(result.toString(), YandexLanguges.class);

                        ListView listLang = getListView();

                        ArrayList<String> langList = new ArrayList<String>(yandexLanguges.getLangs().values());
                        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                                LanguageListActivity.this,
                                android.R.layout.simple_list_item_1,
                                langList);
                        Log.d("LanguageListActivity", "after listAdapter");
                        listLang.setAdapter(listAdapter);

                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            });
            Log.d("LanguageListActivity", "getRequest");
        }catch (WrongOptionException exc){
            exc.printStackTrace();
        }
    }

    private String getLangIndex(String value) {
        Map<String, String> langMap = yandexLanguges.getLangs();

        for (Map.Entry<String, String> s : langMap.entrySet())
            if (value.equals(s.getValue())) return s.getKey();

        return null;
    }

}
