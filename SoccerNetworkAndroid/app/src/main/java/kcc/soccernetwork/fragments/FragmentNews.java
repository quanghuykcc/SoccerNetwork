package kcc.soccernetwork.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kcc.soccernetwork.R;
import kcc.soccernetwork.activities.MatchActivity;
import kcc.soccernetwork.adapters.MatchItemAdapter;
import kcc.soccernetwork.dialogs.SearchMatchDialog;
import kcc.soccernetwork.objects.FieldItem;
import kcc.soccernetwork.objects.Match;
import kcc.soccernetwork.objects.MatchItem;
import kcc.soccernetwork.objects.ResponceMessage;
import kcc.soccernetwork.utils.DividerItemDecoration;
import kcc.soccernetwork.utils.RecyclerClickListener;
import kcc.soccernetwork.utils.ServiceConnect;
import kcc.soccernetwork.utils.UtilConstants;

/**
 * Created by NhutDu on 03/04/2016.
 */
public class FragmentNews extends Fragment implements View.OnClickListener, SearchMatchDialog.SearchMatchListener{
    RecyclerView matchesRecyclerView;
    MatchItemAdapter matchItemAdapter;
    ArrayList<MatchItem> matchItemList;
    ProgressBar loadingPgb;
    @Override
    public void onSearchMatch(DialogFragment dialog) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_match:
                DialogFragment dialogFragment = new SearchMatchDialog(this);
                dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                dialogFragment.show(getActivity().getFragmentManager(), "");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        matchesRecyclerView = (RecyclerView) view.findViewById(R.id.rcv_matches);
        Button searchMatchBtn = (Button) view.findViewById(R.id.btn_search_match);
        loadingPgb = (ProgressBar) view.findViewById(R.id.pgb_loading);
        searchMatchBtn.setOnClickListener(this);
        matchItemList = new ArrayList<MatchItem>();
        matchItemAdapter = new MatchItemAdapter(getActivity(), matchItemList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        matchesRecyclerView.setLayoutManager(layoutManager);
        matchesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        matchesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        matchesRecyclerView.setAdapter(matchItemAdapter);
        matchesRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), matchesRecyclerView, new RecyclerClickListener.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent matchIntent = new Intent(getActivity(), MatchActivity.class);
                matchIntent.putExtra("MatchItem", matchItemAdapter.getMatchItemList().get(position));
                getActivity().startActivity(matchIntent);
                Snackbar.make(matchesRecyclerView, "Click Item: " + position, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Snackbar.make(matchesRecyclerView, "Long Click Item: " + position, Snackbar.LENGTH_LONG).show();
            }
        }));
        matchesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


        });
        new MatchGetterTask().execute();
        return view;
    }

    class MatchGetterTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            loadingPgb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "get_all_matches"));
            try {
                String jsonString = ServiceConnect.getJSONResponceFromUrl(UtilConstants.HOST_SERVICE, nameValuePairs);
                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new StringReader(jsonString));
                reader.setLenient(true);
                matchItemList = gson.fromJson(reader, new TypeToken<Collection<MatchItem>>(){}.getType());
                Collections.reverse(matchItemList);
                matchItemAdapter.setMatchItemList(matchItemList);
            }
            catch (Exception ex) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadingPgb.setVisibility(View.GONE);
            matchItemAdapter.notifyDataSetChanged();
        }
    }

}
