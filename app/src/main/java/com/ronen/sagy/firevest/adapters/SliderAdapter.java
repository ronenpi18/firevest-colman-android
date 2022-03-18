package com.ronen.sagy.firevest.adapters;
//

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.ronen.sagy.firevest.R;

import java.util.ArrayList;

public class SliderAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final Integer[] imgid;

    public SliderAdapter(Activity context, String[] maintitle, String[] subtitle, Integer[] imgid) {
        super(context, R.layout.adapter_tinder_card, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.imgid = imgid;

    }
//
//    public SliderAdapter(FragmentActivity activity, ArrayList<String> maintitle, ArrayList<String> subtitle, ArrayList<String> imgid) {
//        super(activity, R.layout.adapter_tinder_card, maintitle);
//        this.context = activity;
//
//
//    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
//        View rowView = inflater.inflate(R.layout.mylist, null, true);

        TextView titleText = (TextView) view.findViewById(R.id.nameAgeTxt);
        ImageView imageView = (ImageView) view.findViewById(R.id.profileImageView);
        TextView subtitleText = (TextView) view.findViewById(R.id.locationNameTxt);

        titleText.setText(maintitle[position]);
        imageView.setImageResource(imgid[position]);
        subtitleText.setText(subtitle[position]);

        return view;

    }
}
//import com.bumptech.glide.Glide;
//import com.ronen.sagy.firevest.R;
////import com.smarteist.autoimageslider.SliderViewAdapter;
//
//public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
//
//    private Context context;
//
//    public SliderAdapter(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_slider, null);
//        return new SliderAdapterVH(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
//
//        switch (position) {
//            case 0:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.bolt)
//                        .into(viewHolder.imageViewSlider);
//                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_1));
//                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_1));
//                break;
//            case 1:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.ic_like_24dp)
//                        .into(viewHolder.imageViewSlider);
//                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_2));
//                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_2));
//                break;
//            case 2:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.ic_location_on_blue_24dp)
//                        .into(viewHolder.imageViewSlider);
//                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_3));
//                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_3));
//                break;
//
//            case 3:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.ic_star_blue_24dp)
//                        .into(viewHolder.imageViewSlider);
//                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_4));
//                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_4));
//                break;
//
//            case 4:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.reverse)
//                        .into(viewHolder.imageViewSlider);
//                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_5));
//                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_5));
//                break;
//
//            case 5:
//                Glide.with(viewHolder.itemView)
//                        .load(R.drawable.ic_star_turquoise_24dp)
//                        .into(viewHolder.imageViewSlider);
//                viewHolder.textViewTitle.setText(context.getResources().getString(R.string.title_slider_6));
//                viewHolder.textViewDescription.setText(context.getResources().getString(R.string.description_slider_6));
//                break;
//        }
//
//    }
//
//    @Override
//    public int getCount() {
//        //slider view count could be dynamic size
//        return 6;
//    }
//
//    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
//
//        View itemView;
//        ImageView imageViewSlider;
//        TextView textViewDescription, textViewTitle;
//
//        SliderAdapterVH(View itemView) {
//            super(itemView);
//            imageViewSlider = itemView.findViewById(R.id.image_slider);
//            textViewTitle = itemView.findViewById(R.id.text_slider_title);
//            textViewDescription = itemView.findViewById(R.id.text_slider_description);
//            this.itemView = itemView;
//        }
//    }
//}


