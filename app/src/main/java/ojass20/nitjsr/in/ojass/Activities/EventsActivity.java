package ojass20.nitjsr.in.ojass.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ojass20.nitjsr.in.ojass.Adapters.EventsGridAdapter;
import ojass20.nitjsr.in.ojass.Fragments.EventBottomSheet;
import ojass20.nitjsr.in.ojass.Models.EventsDisplayModel;
import ojass20.nitjsr.in.ojass.Utils.Constants;
import ojass20.nitjsr.in.ojass.Utils.PinchAlphaInterface;
import ojass20.nitjsr.in.ojass.Utils.ZoomLayout;
import ojass20.nitjsr.in.ojass.R;

import static ojass20.nitjsr.in.ojass.Utils.Constants.SubEventsList;
import static ojass20.nitjsr.in.ojass.Utils.Constants.SubEventsMap;

public class EventsActivity extends AppCompatActivity implements PinchAlphaInterface, EventsGridAdapter.ItemClickInterface {
    private static final int NO_OF_COLUMNS = 3;
    private static final float INIT_ALPHA = 0.6f;
    ArrayList<EventsDisplayModel> data = new ArrayList<>();
    EventsGridAdapter mAdapter;
    int width;
    float alphaVal = 0;
    RecyclerView gridLayout;

    private Toolbar toolbar;
    private LinearLayout event_search_layout;
    private ImageView events_search_cleartext_button;
    private AutoCompleteTextView event_search_text;

    private MenuItem search_menu_item;

    private static float limitAlpha = 1.4f;
    private static boolean bottomSheetOpen = false;
    private static int toolbarIconColor = Color.BLACK;

    ArrayList<String> subevents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        init();

        getSupportActionBar().setTitle("Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        setZoomableGridView();
        bottomSheetOpen = false;

        handleSearchStuffs();

    }
    public void handleSearchStuffs(){
        events_search_cleartext_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(event_search_text.getText().toString().compareTo("")==0){
                    event_search_layout.setVisibility(View.GONE);
                    search_menu_item.setVisible(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                    closeKeyboard();
                }else{
                event_search_text.setText("");
                }
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, subevents);

        event_search_text.setSingleLine(true);
        event_search_text.setThreshold(0);
        event_search_text.setAdapter(adapter);

        event_search_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(EventsActivity.this, "Open activity of "+subevents[position], Toast.LENGTH_SHORT).show();
                int mainEventPosition = 0, pos = -1;
                for (Map.Entry<Integer, ArrayList<String>> entry : SubEventsList.entrySet()) {
                    ArrayList<String> temp = entry.getValue();
                    for (int j = 0; j < temp.size(); j++) {
                        if (temp.get(j).equalsIgnoreCase((String) parent.getItemAtPosition(position))) {
                            mainEventPosition = entry.getKey();
                            pos = j;
                            break;
                        }
                    }
                    if (pos != -1)
                        break;
                }
                event_search_layout.setVisibility(View.GONE);
                search_menu_item.setVisible(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                Intent intent = new Intent(EventsActivity.this, SubEventActivity.class);
                intent.putExtra("event_id", mainEventPosition);
                ArrayList<String> temp = new ArrayList<>();
                temp.add((String) parent.getItemAtPosition(position));
                intent.putStringArrayListExtra("sub_event_name", temp);
                startActivity(intent);
                closeKeyboard();
                event_search_text.setText("");
            }
        });
    }


    private void hideBottomSheet() {
        bottomSheetOpen = false;

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_layout_for_search);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.no_anim, R.anim.slide_out_bottom);
        transaction.remove(f).commit();

        //change toolbar
//        Drawable backArrow =  getResources().getDrawable(R.drawable.ic_arrow_back);
//        backArrow.setColorFilter(toolbarIconColor, PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setLogo(null);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        toolbar.setTitle("Events");
    }

    private void setZoomableGridView() {
        gridLayout.setLayoutManager(new GridLayoutManager(this, NO_OF_COLUMNS));

        //getting width of device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        //getting event names and images
        data.clear();
        for (int i = 0; i < Constants.eventNames.size(); i++) {
            data.add(new EventsDisplayModel(Constants.eventImg[i], Constants.eventNames.get(i), INIT_ALPHA));
        }
        if(data.size()==0){
            //go to main activity
            Intent i = new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();

        }
        mAdapter = new EventsGridAdapter(this, width, data,this);
        gridLayout.setAdapter(mAdapter);
    }

    void init() {
        subevents = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : SubEventsMap.entrySet()) {

            for (int i = 0; i < entry.getValue().size(); i++)
                subevents.add(entry.getValue().get(i));
        }
        gridLayout = findViewById(R.id.events_grid);
        toolbar = findViewById(R.id.events_toolbar);
        setSupportActionBar(toolbar);
        event_search_layout = toolbar.findViewById(R.id.event_search_layout_change);
        events_search_cleartext_button = toolbar.findViewById(R.id.event_search_clear_text_button);
        event_search_text = toolbar.findViewById(R.id.event_search_text);
    }

    @Override
    public void ScaleFactorToAlpha(float scaleFactor) {
//        Log.e("TAG"," "+scaleFactor);
        alphaVal = (float) ((scaleFactor - 1) * (1.0 / (limitAlpha - 1))) + INIT_ALPHA;
        if (alphaVal > 1) {
            alphaVal = 1;
        } else if (alphaVal < 0) {
            alphaVal = 0;
        }
        data.clear();
//        Log.e("TAG"," "+alphaVal);
        for (int i = 0; i < Constants.eventNames.size(); i++) {
            data.add(new EventsDisplayModel(Constants.eventImg[i], Constants.eventNames.get(i), alphaVal));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_menu, menu);
        //TODO: implement search
        final MenuItem searchItem = menu.findItem(R.id.search_events);
        search_menu_item = searchItem;
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                event_search_layout.setVisibility(View.VISIBLE);
                searchItem.setVisible(false);
                event_search_text.requestFocus();
                showKeyboard();
                //Toast.makeText(EventsActivity.this, "Level is working", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetOpen) {
            hideBottomSheet();
            search_menu_item.setVisible(true);
            Log.e("onBackPressed: ", "level 23");
        } else if (event_search_layout.getVisibility() == View.VISIBLE) {
            event_search_layout.setVisibility(View.GONE);
            search_menu_item.setVisible(true);
        } else {
            //finishAfterTransition();
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (bottomSheetOpen) {
                hideBottomSheet();
                search_menu_item.setVisible(true);
            } else {
                if (event_search_layout.getVisibility() == View.VISIBLE) {
                    closeKeyboard();
                    event_search_layout.setVisibility(View.GONE);
                    search_menu_item.setVisible(true);
                } else {
                    finish();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void EventsItemClick(int position, ImageView iv) {
        Intent intent = new Intent(this, SubEventActivity.class);
        intent.putExtra("event_id",position);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
        iv,"SubEventImg");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        startActivity(intent,options.toBundle());

    }
}