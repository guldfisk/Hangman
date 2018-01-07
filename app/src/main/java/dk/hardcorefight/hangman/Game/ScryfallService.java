package dk.hardcorefight.hangman.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScryfallService {

    private static final String randomEndpoint = "https://api.scryfall.com/cards/random";
    private static final String autoCompleteEndpoint = "https://api.scryfall.com/cards/autocomplete";
    private static final String printingEndpoint = "https://api.scryfall.com/cards/named";

    private RequestQueue queue;

    public ScryfallService(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public JsonRequest getRandomPrinting(
        final Response.Listener<Printing> listener,
        final Response.ErrorListener errorListener,
        final WrongPrintingFieldsListener wrongPrintingFieldsListener
    ) {
        JsonRequest jsonRequest = new JsonObjectRequest(
            Request.Method.GET,
            randomEndpoint,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        listener.onResponse(
                            new Printing(
                                response.getString("name"),
                                response.getString("set_name"),
                                response.getString("set"),
                                response.getJSONObject("image_uris").getString("art_crop")
                            )
                        );
                    } catch (JSONException e) {
                        wrongPrintingFieldsListener.onError(e);
                    }
                }
            },
            errorListener
        );

        this.queue.add(
            jsonRequest
        );

        return jsonRequest;
    }

    public JsonRequest search(
            String query,
            final Response.Listener<List<String>> listener,
            final Response.ErrorListener errorListener,
            final WrongPrintingFieldsListener wrongPrintingFieldsListener
    ) {
        JsonRequest jsonRequest = new JsonObjectRequest(
            Request.Method.GET,
            autoCompleteEndpoint+"/?q="+query.replace(" ", "%20"),
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    List<String> names = new ArrayList<>();
                    try {
                        JSONArray responses = response.getJSONArray("data");
                        for (int i = 0; i < responses.length(); i++) {
                            names.add(responses.getString(i));
                        }
                    } catch (JSONException e) {
                        wrongPrintingFieldsListener.onError(e);
                    }
                    listener.onResponse(names);
                }
            },
            errorListener
        );
        this.queue.add(jsonRequest);
        return jsonRequest;
    }

    public JsonRequest getPrinting(
            String name,
            final Response.Listener<Printing> listener,
            final Response.ErrorListener errorListener,
            final WrongPrintingFieldsListener wrongPrintingFieldsListener
    ) {
        JsonRequest jsonRequest = new JsonObjectRequest(
            Request.Method.GET,
            printingEndpoint+"/?exact="+name.replace(" ", "%20"),
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.e("apiservice", response.toString());
                        listener.onResponse(
                            new Printing(
                                response.getString("name"),
                                response.getString("set_name"),
                                response.getString("set"),
                                response.getJSONObject("image_uris").getString("art_crop")
                            )
                        );
                    } catch (JSONException e) {
                        wrongPrintingFieldsListener.onError(e);
                    }
                }
            },
            errorListener
        );
        this.queue.add(jsonRequest);
        return jsonRequest;
    }

    public ImageRequest getImage(
        Printing printing,
        Response.Listener<Bitmap> listener,
        Response.ErrorListener errorListener
    ) {
        ImageRequest imageRequest = new ImageRequest(
            printing.getImageCode(),
            listener,
            0,
            0,
            ImageView.ScaleType.FIT_CENTER,
            Bitmap.Config.ARGB_4444,
            errorListener
        );
        this.queue.add(
            imageRequest
        );
        return imageRequest;
    }

    public ImageRequest getImage(Printing printing, Response.Listener<Bitmap> listener) {
        return this.getImage(printing, listener, null);
    }

}
