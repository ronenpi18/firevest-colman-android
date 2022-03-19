package com.ronen.sagy.firevest.activities.fragments.swipe_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ronen.sagy.firevest.R;

public final class SimpleCardStackAdapter extends CardStackAdapter {

	public SimpleCardStackAdapter(Context context) {
		super(context);
	}

	@Override
	public View getCardView(int position, CardModel model, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.std_card_inner, parent, false);
			assert convertView != null;
		}
		Glide.with(getContext())
				.load(model.getImageUrl())
				.placeholder(R.drawable.sample_img)
				.into(((ImageView) convertView.findViewById(R.id.image)));

//		((ImageView) convertView.findViewById(R.id.image)).setImageDrawable(model.getCardImageDrawable());
		((TextView) convertView.findViewById(R.id.title)).setText(model.getTitle());
		((TextView) convertView.findViewById(R.id.description)).setText(model.getDescription());

		return convertView;
	}
}
