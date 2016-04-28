package kcc.soccernetwork.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.activities.FieldActivity;
import kcc.soccernetwork.adapters.FieldItemAdapter;
import kcc.soccernetwork.objects.FieldItem;
import kcc.soccernetwork.objects.MatchItem;
import kcc.soccernetwork.utils.DividerItemDecoration;
import kcc.soccernetwork.utils.RecyclerClickListener;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

public class FragmentFields extends Fragment implements View.OnClickListener{
    RecyclerView fieldRecyclerView;
    ArrayList<FieldItem> fieldItemList;
    FieldItemAdapter fieldItemAdapter;
    ProgressBar loadingPgb;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_field:
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fields,container,false);
        fieldRecyclerView = (RecyclerView) view.findViewById(R.id.rcv_fields);
        Button searchFieldButton = (Button) view.findViewById(R.id.btn_search_field);
        loadingPgb = (ProgressBar) view.findViewById(R.id.pgb_loading);
        searchFieldButton.setOnClickListener(this);
        fieldItemList = new ArrayList<FieldItem>();
        fieldItemAdapter = new FieldItemAdapter(getActivity(), fieldItemList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        fieldRecyclerView.setLayoutManager(layoutManager);
        fieldRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fieldRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        fieldRecyclerView.setAdapter(fieldItemAdapter);
        fieldRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), fieldRecyclerView, new RecyclerClickListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent fieldIntent = new Intent(getActivity(), FieldActivity.class);
                fieldIntent.putExtra("FieldItem", fieldItemAdapter.getFieldItemList().get(position));
                getActivity().startActivity(fieldIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        new FieldGetterTask().execute();
        return view;
    }

    class FieldGetterTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            loadingPgb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "get_all_fields"));
            String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(jsonString));
            reader.setLenient(true);
            fieldItemList = gson.fromJson(reader, new TypeToken<Collection<FieldItem>>(){}.getType());
            fieldItemAdapter.setFieldItemList(fieldItemList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadingPgb.setVisibility(View.GONE);
            fieldItemAdapter.notifyDataSetChanged();
        }
    }
}
