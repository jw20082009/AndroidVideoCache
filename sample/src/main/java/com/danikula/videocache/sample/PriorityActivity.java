package com.danikula.videocache.sample;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_priority)
public class PriorityActivity extends FragmentActivity {

    PriorityFragment priorityFragment0, priorityFragment1, priorityFragment2, priorityFragment3,
        priorityFragment4, priorityFragment5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            priorityFragment0 =
                (PriorityFragment) PriorityFragment.build(
                    "https://res-snap-e.skyplan.online/slim-EC9nSEsnYB2HVcfixh8kpxc7w0wwKlT5U3XtXTq6tdQ.mp4");
            priorityFragment1 = (PriorityFragment) PriorityFragment.build(
                "https://res-snap-e.skyplan.online/slim-jA03vNPMB8hI5q80wzZ-rg6yeV5uU48JDd66r7UqRfc.mp4");
            priorityFragment2 =
                (PriorityFragment) PriorityFragment.build(
                    "https://res-snap-e.skyplan.online/slim-61QBSIi319aj-fhF9d8BbtTdVz-8xN7gBEg8uJB5nOY.mp4");
            priorityFragment3 = (PriorityFragment) PriorityFragment.build(
                "https://res-snap-e.skyplan.online/slim-PoKxi35fpcY1kuT-6K-Q-xLIZYw5DmtmV1SmfLDqDRY.mp4");
            priorityFragment4 = (PriorityFragment) PriorityFragment.build(
                "https://res-snap-e.skyplan.online/slim-DKu_0XgbAVQhge2WRX_uoY7qdcmeVv7y16pm-t4e8c0.mp4");
            priorityFragment5 = (PriorityFragment) PriorityFragment.build(
                "https://raw.githubusercontent.com/danikula/AndroidVideoCache/master/files/orange1.mp4");
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.videoContainer0, priorityFragment0)
                .commit();
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.videoContainer1, priorityFragment1)
                .commit();
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.videoContainer2, priorityFragment2)
                .commit();
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.videoContainer3, priorityFragment3)
                .commit();
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.videoContainer4, priorityFragment4)
                .commit();
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.videoContainer5, priorityFragment5)
                .commit();
        }
    }
}
