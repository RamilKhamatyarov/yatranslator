package ru.rkhamatyarov.yatranslator;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Asus on 10.04.2017.
 * http://suvitruf.ru/2013/06/11/3243/
 */
enum RequestOptions {
    langList, translateWords
}
public class RequestImpl {
    private static final String API_KEY = "trnsl.1.1.20170410T191622Z.1924682d4ac612ed.c866bf96a58850d63ea61096e71cac7941eaca47";
    private Context context;

    public RequestImpl(Context context){
        this.context = context;
    }



    public void getRequest(String langDirs, String wordsLine, final RequestOptions option, final VolleyCallback callback) throws WrongOptionException {
        Log.d("RequestImpl", langDirs);
        // Get layout
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.activity_main, null);
//        final TextView mTextView = (TextView) view.findViewById(R.id.translated_words);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        StringBuilder request = null;

        Log.d("RequestImpl", " after Volley.newRequestQueue(context)");


        if (option.equals(RequestOptions.langList)) {
            Log.d("langDirs : ", langDirs);
            request = getLangListReq(langDirs);
        } else if (option.equals(RequestOptions.translateWords)) {
            request = getTranslateWords(langDirs, wordsLine);
        } else throw new WrongOptionException();


        String req = request.toString();
        // Replace all whitespaces to %20
        req = req.replaceAll("\t", "%20").replaceAll("\n", "%20").replaceAll(" ", "%20");
        Log.d("RequestImpl", req);
        // If obj null, not working
        final JSONObject obj = new JSONObject();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,req,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray[] jsonArray = new JSONArray[1];

                            if (option.equals(RequestOptions.translateWords)) jsonArray[0] = response.getJSONArray("text");
//                            else jsonArray[0] = response.getJSONObject("langs").getJSONArray("af");

//                            Log.d("onResponse", jsonArray[0].getString(0));
//                            mTextView.setText(response.getString("text"));
                            if (option.equals(RequestOptions.translateWords))
                                MainActivity.translatedWords.setText(jsonArray[0].get(0).toString());
                            else if (option.equals(RequestOptions.langList)) {

                                Log.d("RequestImpl", response.toString());
                                callback.onSuccess(response.toString());

                            }
                        } catch (org.json.JSONException exc) {
                            exc.printStackTrace();
                        } catch (java.lang.Throwable exc){
                            exc.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("onErrorResponse", error.toString());
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);


    }

    private StringBuilder getLangListReq(String langFrom) {
        String url = "https://translate.yandex.net/api/v1.5/tr.json/getLangs";

        StringBuilder request = new StringBuilder(url);
        request.append("?key=").append(API_KEY).append("&ui=").append(langFrom);

        Log.d("Request for lang list", request.toString());
        return request;
    }

    private StringBuilder getTranslateWords (String langFrom, String wordsLine) {
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate";

        StringBuilder request = new StringBuilder(url);
        request.append("?key=").append(API_KEY).append("&text=").append(wordsLine).append("&lang=").append(langFrom);

        Log.d("Request", request.toString());
        return request;
    }
}
