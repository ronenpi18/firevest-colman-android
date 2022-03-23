package com.ronen.sagy.firevest.activities.fragments.swipe_view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.viewModel.DatabaseViewModel;

public final class SimpleCardStackAdapter extends CardStackAdapter {

    private DatabaseViewModel databaseViewModel;
    private String currentUserId;
    private LifecycleOwner lifecycleOwner;

    public SimpleCardStackAdapter(Context context, DatabaseViewModel databaseViewModel, String currentUserId, LifecycleOwner lifecycleOwner) {
        super(context);
        this.databaseViewModel = databaseViewModel;
        this.currentUserId = currentUserId;
        this.lifecycleOwner = lifecycleOwner;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    @Override
    public View getCardView(int position, CardModel model, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.std_card_inner, parent, false);
            assert convertView != null;
        }
        Glide.with(getContext())
                .load(model.getImageUrl())
                .placeholder(R.drawable.sample_img)
                .into(((ImageView) convertView.findViewById(R.id.image)));

        ((TextView) convertView.findViewById(R.id.title)).setText(model.getTitle());
        ((TextView) convertView.findViewById(R.id.description)).setText(model.getDescription());
        model.setOnClickListener(new CardModel.OnClickListener() {
            @Override
            public void OnClickListener() {
//                Toast.makeText(getContext(), "CLICKED", Toast.LENGTH_SHORT).show();
            }
        });
        model.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
            @Override
            public void onLike() {
                long tsLong = System.currentTimeMillis();
                String timeStamp = Long.toString(tsLong);
                Toast.makeText(getContext(), "LIKE!!!" + model.getUid(), Toast.LENGTH_SHORT).show();
                databaseViewModel.addChatDb(model.getUid(), currentUserId, "Hey, I would like to invest!", timeStamp);
                databaseViewModel.successAddChatDb.observe(lifecycleOwner, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            Toast.makeText(getContext(), "Sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Message can't be sent.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onDislike() {
            }
        });

        return convertView;
    }
}
