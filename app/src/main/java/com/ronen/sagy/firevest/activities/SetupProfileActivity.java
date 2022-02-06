package com.ronen.sagy.firevest.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ronen.sagy.firevest.R;
import com.ronen.sagy.firevest.Utils;
import com.ronen.sagy.firevest.custom_widgets.expending_view.ExpandingItem;
import com.ronen.sagy.firevest.custom_widgets.expending_view.ExpandingList;

public class SetupProfileActivity extends AppCompatActivity {
    private ExpandingList mExpandingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        mExpandingList = findViewById(R.id.expanding_list_main);
        createItems();
    }

    private void createItems() {
        addItem("About You", new String[]{"General", "Boat", "Candy", "Collection", "Sport", "Ball", "Head"}, R.color.pink, R.drawable.account_circle);


        addItem("John", new String[]{"House", "Boat", "Candy", "Collection", "Sport", "Ball", "Head"}, R.color.pink, R.drawable.ic_ghost);
        addItem("Mary", new String[]{"Dog", "Horse", "Boat"}, R.color.blue, R.drawable.ic_ghost);
        addItem("Ana", new String[]{"Cat"}, R.color.purple, R.drawable.ic_ghost);
        addItem("Peter", new String[]{"Parrot", "Elephant", "Coffee"}, R.color.yellow, R.drawable.ic_ghost);
        addItem("Joseph", new String[]{}, R.color.orange, R.drawable.ic_ghost);
        addItem("Paul", new String[]{"Golf", "Football"}, R.color.green, R.drawable.ic_ghost);
        addItem("Larry", new String[]{"Ferrari", "Mazda", "Honda", "Toyota", "Fiat"}, R.color.blue, R.drawable.ic_ghost);
        addItem("Moe", new String[]{"Beans", "Rice", "Meat"}, R.color.yellow, R.drawable.ic_ghost);
        addItem("Bart", new String[]{"Hamburger", "Ice cream", "Candy"}, R.color.purple, R.drawable.ic_ghost);
    }

    private void addItem(String title, String[] subItems, int colorRes, int iconRes) {
        //Let's create an item with R.layout.expanding_layout
        final ExpandingItem item = mExpandingList.createNewItem(R.layout.expanding_layout);

        //If item creation is successful, let's configure it
        if (item != null) {
            item.setIndicatorColorRes(colorRes);
            item.setIndicatorIconRes(iconRes);
            //It is possible to get any view inside the inflated layout. Let's set the text in the item
            ((TextView) item.findViewById(R.id.title)).setText(title);

            //We can create items in batch.
            item.createSubItems(subItems.length);
            for (int i = 0; i < item.getSubItemsCount(); i++) {
                //Let's get the created sub item by its index
                final View view = item.getSubItemView(i);

                //Let's set some values in
                configureSubItem(item, view, subItems[i]);
            }
            item.findViewById(R.id.add_more_sub_items).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInsertDialog(new Utils.OnItemCreated() {
                        @Override
                        public void itemCreated(String title) {
                            View newSubItem = item.createSubItem();
                            configureSubItem(item, newSubItem, title);
                        }
                    });
                }
            });

            item.findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandingList.removeItem(item);
                }
            });
        }
    }

    private void configureSubItem(final ExpandingItem item, final View view, String subTitle) {
        ((TextView) view.findViewById(R.id.sub_title)).setText(subTitle);
//        view.findViewById(R.id.remove_sub_item).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                item.removeSubItem(view);
//            }
//        });
    }

    private void showInsertDialog(final Utils.OnItemCreated positive) {
        final EditText text = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(text);
        builder.setTitle("Build!!!");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                positive.itemCreated(text.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }
}