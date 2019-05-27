package com.example.spaceinvaders;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LeaderboardList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;

    public LeaderboardList(Activity context, List<User> userList){
        super(context, R.layout.leaderboard_layout, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.leaderboard_layout,null, true);

        TextView textViewNick = (TextView) listViewItem.findViewById(R.id.Nick);
        TextView textViewScore = (TextView) listViewItem.findViewById(R.id.Score);

        User user = userList.get(position);
        textViewNick.setText(user.getNick());
        textViewScore.setText(Integer.toString(user.getScore()));

        return listViewItem;
    }














}
