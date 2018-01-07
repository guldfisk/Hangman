package dk.hardcorefight.hangman.Game;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dk.hardcorefight.hangman.R;

public class PickCardFragment
extends
    Fragment
implements
    Response.Listener<Printing>,
    Response.ErrorListener,
    WrongPrintingFieldsListener,
    View.OnClickListener
{

    private Set<CardPickedListener> listeners = Collections.synchronizedSet(new HashSet<CardPickedListener>());

    private ScryfallService mtgApi;

    private ArrayAdapter<String> searchResultAdaptor;

    public PickCardFragment() {
        // Required empty public constructor
    }

    public static PickCardFragment newInstance() {
        return new PickCardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mtgApi = new ScryfallService(this.getActivity());
    }

    private void getRandomCard() {
        this.mtgApi.getRandomPrinting(this, this, this);
    }

    @Override
    public View onCreateView (
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_pick_card, container, false);

        view.findViewById(R.id.SelectRandomCardButton).setOnClickListener(this);

        this.searchResultAdaptor = new ArrayAdapter<>(this.getActivity(), R.layout.list_item, new ArrayList<String>());
        ListView listView = view.findViewById(R.id.search_result_list);
        listView.setAdapter(this.searchResultAdaptor);

        view.findViewById(R.id.SearchButton).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String query = ((EditText) PickCardFragment.this.getView().findViewById(R.id.SearchInput)).getText().toString();
                    if (query.length() <= 2) {
                        return;
                    }
                    Log.e("cardpick", "searching for: " + query);
                    PickCardFragment.this.mtgApi.search(
                        query,
                        new Response.Listener<List<String>>() {
                            @Override
                            public void onResponse(List<String> response) {
                                Log.e("cardpick", Arrays.toString(response.toArray()));
                                PickCardFragment.this.searchResultAdaptor.clear();
                                for (String name : response) {
                                    PickCardFragment.this.searchResultAdaptor.add(name);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("cardpick", "search net error", error);
                            }
                        },
                        new WrongPrintingFieldsListener() {
                            @Override
                            public void onError(Exception e) {
                                Log.e("cardpick", "jsonerror", e);
                            }
                        }
                    );

                }
            }
        );

        listView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PickCardFragment.this.searching();
                    PickCardFragment.this.mtgApi.getPrinting(
                        (String) parent.getItemAtPosition(position),
                        new Response.Listener<Printing>() {
                            @Override
                            public void onResponse(final Printing printing) {
                                PickCardFragment.this.mtgApi.getImage(
                                    printing,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            PickCardFragment.this.pickCard(
                                                printing,
                                                bitmap
                                            );
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("cardpick", "no card image", error);
                                            PickCardFragment.this.unsearching();
                                            Toast.makeText(
                                                PickCardFragment.this.getActivity(),
                                                "Chosen card has no image",
                                                Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                                );
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("cardpick", "networking error", error);
                                PickCardFragment.this.unsearching();
                                Toast.makeText(
                                    PickCardFragment.this.getActivity(),
                                    "Invalid card chosen",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }
                        },
                        new WrongPrintingFieldsListener() {
                            @Override
                            public void onError(Exception e) {
                                Log.e("cardpick", "json error", e);
                                PickCardFragment.this.unsearching();
                                Toast.makeText(
                                    PickCardFragment.this.getActivity(),
                                    "Invalid card chosen",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    );
                }
            }
        );

        return view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("pickcard", "network", error);
        this.getRandomCard();
    }

    private void pickCard(Printing printing, Bitmap bitmap) {
        for (CardPickedListener listener : PickCardFragment.this.listeners) {
            listener.cardPicked(printing, bitmap);
        }
        this.unsearching();
    }

    @Override
    public void onResponse(final Printing printing) {
        this.mtgApi.getImage(
            printing,
            new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    PickCardFragment.this.pickCard(printing, bitmap);
                }
            },
            this
        );
    }

    @Override
    public void onError(Exception e) {
        Log.e("pickcard", "card fields", e);
        this.getRandomCard();
    }

    public void addCardPickedListener(CardPickedListener listener) {
        this.listeners.add(listener);
    }

    public void removeCardPickedListener(CardPickedListener listener) {
        this.listeners.remove(listener);
    }

    private void searching() {
        this.getView().findViewById(R.id.search_result_list).setVisibility(View.INVISIBLE);
        this.getView().findViewById(R.id.FetchingCardProgressBar).setVisibility(View.VISIBLE);
        if (this.getView() != null) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getView().getWindowToken(), 0);
        }
        this.getView().findViewById(R.id.SelectRandomCardButton).setEnabled(false);
    }

    private void unsearching() {
        this.getView().findViewById(R.id.search_result_list).setVisibility(View.VISIBLE);
        this.getView().findViewById(R.id.FetchingCardProgressBar).setVisibility(View.INVISIBLE);
        this.getView().findViewById(R.id.SelectRandomCardButton).setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        this.searching();
        this.getRandomCard();
    }

}
