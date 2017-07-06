package com.app.lvyerose.drag.dragpageeffect.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.lvyerose.drag.dragpageeffect.R;

import java.util.ArrayList;

/************************************************************************
 *                    .::::.                                            *
 *                  .::::::::.                                          *
 *                 :::::::::::  FUCK YOU                                *
 *             ..:::::::::::'                                           *
 *           '::::::::::::'                                             *
 *             .::::::::::                                              *
 *        '::::::::::::::..                                             *
 *             ..::::::::::::.                                          *
 *           ``::::::::::::::::                                         *
 *            ::::``:::::::::'        .:::.                             *
 *           ::::'   ':::::'       .::::::::.                           *
 *         .::::'      ::::     .:::::::'::::.                          *
 *        .:::'       :::::  .:::::::::' ':::::.                        *
 *       .::'        :::::.:::::::::'      ':::::.                      *
 *      .::'         ::::::::::::::'         ``::::.                    *
 *  ...:::           ::::::::::::'              ``::.                   *
 * ```` ':.          ':::::::::'                  ::::..                *
 *                    '.:::::'                    ':'````..             *
 *              ━━━━━━━━━━━━━━━━━━━━━                                   *
 ************************************************************************
 *
 * 项目名称：DragPageEffect
 * 类描述： 
 * 创建人：lvyerose
 * 邮箱：lvyerose@163.com
 * 创建时间：17/7/6
 *
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 *  ......
 */
public class DragBetweenFragment extends Fragment {
    private ViewPager viewPager;
    private int[] drawables = new int[]{
            R.mipmap.item_img_8,
            R.mipmap.item_img_5,
            R.mipmap.item_img_3,
            R.mipmap.item_img_6,
            R.mipmap.item_img_7,
            R.mipmap.item_img_1,
            R.mipmap.item_img_2,
            R.mipmap.item_img_4
    };

    private ArrayList<ImageView> imageViews = new ArrayList<>(8);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_between_layout, null);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.getLayoutParams().height = getScreenHeight(getActivity()) - 112;
        setData();
        return view;
    }

    private void setData() {
        for (int i = 0; i < drawables.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(drawables[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(imageViews.get(position));
                return imageViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews.get(position));
            }
        });
    }

    /**
     * 获取屏幕的高度px
     *
     * @param context 上下文
     * @return 屏幕高px
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.heightPixels;
    }

    private boolean isFirst = true;

    public void startUI(int height) {
        if (isFirst) {
            isFirst = false;
            //进行第一次加载数据调用
            //手动设置高度充满全屏
            viewPager.getLayoutParams().height = getScreenHeight(getActivity()) - height;

        }
    }
}
