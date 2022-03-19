package com.ronen.sagy.firevest.activities.fragments.swipe_view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class CardModel {

    private String imageUrl;
    private String uid;
    private String title;
    private String description;
    private Drawable cardImageDrawable;
    private Drawable cardLikeImageDrawable;
    private Drawable cardDislikeImageDrawable;

    private OnCardDismissedListener onCardDismissedListener = null;

    private OnClickListener onClickListener = null;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public interface OnCardDismissedListener {
        void onLike();

        void onDislike();
    }

    public interface OnClickListener {
        void OnClickListener();
    }

    public CardModel() {
        this(null, null, (Drawable) null);
    }

    public CardModel(String title, String description, Drawable cardImage) {
        this.title = title;
        this.description = description;
        this.cardImageDrawable = cardImage;
    }

    public CardModel(String title, String description, String imageUrl, String uid) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.uid = uid;
    }

    public CardModel(String title, String description, Bitmap cardImage) {
        this.title = title;
        this.description = description;
        this.cardImageDrawable = new BitmapDrawable(null, cardImage);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drawable getCardImageDrawable() {
        return cardImageDrawable;
    }

    public void setCardImageDrawable(Drawable cardImageDrawable) {
        this.cardImageDrawable = cardImageDrawable;
    }

    public Drawable getCardLikeImageDrawable() {
        return cardLikeImageDrawable;
    }

    public void setCardLikeImageDrawable(Drawable cardLikeImageDrawable) {
        this.cardLikeImageDrawable = cardLikeImageDrawable;
    }

    public Drawable getCardDislikeImageDrawable() {
        return cardDislikeImageDrawable;
    }

    public void setCardDislikeImageDrawable(Drawable cardDislikeImageDrawable) {
        this.cardDislikeImageDrawable = cardDislikeImageDrawable;
    }

    public void setOnCardDismissedListener(OnCardDismissedListener listener) {
        this.onCardDismissedListener = listener;
    }

    public OnCardDismissedListener getOnCardDismissedListener() {
        return this.onCardDismissedListener;
    }


    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }

    public OnClickListener getOnClickListener() {
        return this.onClickListener;
    }
}
