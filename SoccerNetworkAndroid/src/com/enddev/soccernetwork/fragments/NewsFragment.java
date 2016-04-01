package com.enddev.soccernetwork.fragments;

import java.util.ArrayList;

import com.enddev.soccernetwork.R;
import com.enddev.soccernetwork.activity.MatchActivity;
import com.enddev.soccernetwork.adapters.MatchListAdapter;
import com.enddev.soccernetwork.models.Match;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NewsFragment extends Fragment implements OnItemClickListener{
	ArrayList<Match> mMatchList;
	ListView mRoomLV;
	MatchListAdapter mMatchListAdapter;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_news, container, false);
		mRoomLV = (ListView) v.findViewById(R.id.lv_matches);
		
		//sample data;
		mMatchList = new ArrayList<Match>();
		for (int i = 0; i < 10; i++) {
			Match match = new Match();
			match.setMaximum_players("10");
			mMatchList.add(match);
		}
		
		mMatchListAdapter = new MatchListAdapter(getActivity(), R.layout.match_item_layout, mMatchList);
		mRoomLV.setAdapter(mMatchListAdapter);
		mRoomLV.setOnItemClickListener(this);
		return v;
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		startMatchActivity(null);
	}
	
	private void startMatchActivity(Match match) {
		Intent matchIntent = new Intent(getActivity(), MatchActivity.class);
		getActivity().startActivity(matchIntent);
	}

}
