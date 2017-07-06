package com.app.lvyerose.drag.dragpageeffect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.lvyerose.drag.dragpageeffect.fragment.DragBetweenFragment;
import com.app.lvyerose.drag.dragpageeffect.fragment.DragBottomFragment;
import com.app.lvyerose.drag.dragpageeffect.fragment.DragHeaderFragment;
import com.app.lvyerose.drag.dragpageeffect.view.DragMultiLayout;

public class MainActivity extends AppCompatActivity {
    private DragMultiLayout dragLayout;
    private DragHeaderFragment headFragment;
    private DragBetweenFragment betweenFragment;
    private DragBottomFragment bottomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        headFragment = new DragHeaderFragment();
        betweenFragment = new DragBetweenFragment();
        bottomFragment = new DragBottomFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.head_frame_layout, headFragment)
                .add(R.id.between_frame_layout, betweenFragment)
                .add(R.id.bottom_frame_layout, bottomFragment)
                .commit();

        DragMultiLayout.ShowNextPageNotifier nextListener = new DragMultiLayout.ShowNextPageNotifier() {
            @Override
            public void onDragNext(int nextIndex) {
                switch (nextIndex) {
                    case 1:
                        betweenFragment.startUI(getSupportActionBar().getHeight());
                        break;
                }
            }
        };
        dragLayout = (DragMultiLayout) findViewById(R.id.draw_layout);
        dragLayout.setNextPageListener(nextListener);
    }
}
