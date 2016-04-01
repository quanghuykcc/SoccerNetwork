package com.enddev.soccernetwork.fragments;

import java.util.ArrayList;

import com.enddev.soccernetwork.R;
import com.enddev.soccernetwork.activity.MatchActivity;
import com.enddev.soccernetwork.adapters.MatchListAdapter;
import com.enddev.soccernetwork.dialog.FilterDialogFragment;
import com.enddev.soccernetwork.dialog.FilterDialogFragment.FilterDialogListener;
import com.enddev.soccernetwork.models.Match;
import com.enddev.soccernetwork.utils.LoginCheck;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class NewsFragment extends Fragment implements OnItemClickListener, OnClickListener, FilterDialogListener{
	ArrayList<Match> mMatchList;
	ListView mMatchLV;
	MatchListAdapter mMatchListAdapter;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_news, container, false);
		mMatchLV = (ListView) v.findViewById(R.id.lv_matches);
		
		//sample data;
		mMatchList = new ArrayList<Match>();
		for (int i = 0; i < 10; i++) {
			Match match = new Match();
			match.setMaximum_players("10");
			mMatchList.add(match);
		}
		
		mMatchListAdapter = new MatchListAdapter(getActivity(), R.layout.match_item_layout, mMatchList);
		mMatchLV.setAdapter(mMatchListAdapter);
		mMatchLV.setOnItemClickListener(this);
		Button searchBtn = (Button) v.findViewById(R.id.btn_search);
		searchBtn.setOnClickListener(this);
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
	
	private void showFilterDialog() {
		FilterDialogFragment filterDialog = new FilterDialogFragment(this);
		filterDialog.show(getActivity().getFragmentManager(), "");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:	
			showFilterDialog();
			break;

		default:
			break;
		}
		
	}
	@Override
	public void OnSearchClick() {
		// TODO Auto-generated method stub
		
	}

}
