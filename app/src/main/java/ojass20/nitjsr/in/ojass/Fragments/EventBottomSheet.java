package ojass20.nitjsr.in.ojass.Fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import ojass20.nitjsr.in.ojass.Adapters.ViewPagerAdapter;
import ojass20.nitjsr.in.ojass.R;

public class EventBottomSheet extends Fragment {
    public static final String TAG = "EventBottomSheet";
    BottomSheetBehavior bottomSheetBehavior;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    public static EventBottomSheet newInstance() {
        return new EventBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_info_layout, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Log.e("this", "I'm lost");

        int drawable_resources[] = {R.drawable.ic_event_about, R.drawable.ic_event_details, R.drawable.ic_event_rules, R.drawable.ic_event_coordinator, R.drawable.ic_event_prize};
        for(int i = 0;i < tabLayout.getTabCount();i ++){
            tabLayout.getTabAt(i).setIcon(drawable_resources[i]);
        }

        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        Log.e("Hey", "i'm called");
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new AboutFragment(), "About");
        adapter.addFragment(new DetailsFragment(), "Details");
        adapter.addFragment(new RulesFragment(), "Rules");
        adapter.addFragment(new CoordianatorFragment(), "Coordinator");
        adapter.addFragment(new PrizeFragment(), "Prize");
        viewPager.setAdapter(adapter);
        adapter = null;
    }


}
