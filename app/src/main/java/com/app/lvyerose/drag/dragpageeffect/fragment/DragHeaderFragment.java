package com.app.lvyerose.drag.dragpageeffect.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.lvyerose.drag.dragpageeffect.R;

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
public class DragHeaderFragment extends Fragment {
    private TextView contentTv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header_layout, null);
        contentTv = (TextView) view.findViewById(R.id.header_content_tv);
        contentTv.setText(content);
        return view;
    }


    private String content = "Main Page\n" +
            "Today's featured article\n" +
            "S. S. Rajamouli and Samantha Ruth Prabhu during the filming of the song \"Konchem Konchem\"\n" +
            "S. S. Rajamouli and Samantha Ruth Prabhu\n" +
            "Eega (The Fly) is an Indian bilingual fantasy film released on 6 July 2012, written by K. V. Vijayendra Prasad and directed by his son, S. S. Rajamouli. It was produced by Korrapati Ranganatha Sai's production house Varahi Chalana Chitram with an estimated budget of ₹260 to 400 million, simultaneously in Telugu and Tamil. The film stars Sudeep, Nani and Samantha Ruth Prabhu (pictured with the director). The narrative is in the form of a bedtime story told by a father to his daughter. Its protagonist is Nani, who is in love with his neighbour Bindu. Nani is murdered by a wealthy businessman named Sudeep, who is attracted to Bindu and considers Nani a rival. Nani reincarnates as a housefly and tries to protect Bindu while avenging his death. The film's production began in 2010 at Ramanaidu Studios in Hyderabad, and principal photography ran from early 2011 to early 2012. The two versions of the film, alongside a Malayalam-dubbed version titled Eecha, were released globally on approximately 1,100 screens. Screened at international film festivals, Eega won two National Film Awards, five Filmfare Awards, and three South Indian International Movie Awards. (Full article...)";

}
