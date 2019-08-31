package com.administrator.learndemo.algorithm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.administrator.learndemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 集合覆盖问题:
 * 假设存在如下表的需要付费的广播台，以及广播台信号可以覆盖的地区。 如何选择最少的广播台，让所有的地区都可以接收到信号。


 目前并没有算法可以快速计算得到准备的值， 而使用贪婪算法，则可以得到非常接近的解，并且效率高:

 选择策略上，因为需要覆盖全部地区的最小集合:

 (1) 选出一个广播台，即它覆盖了最多未覆盖的地区即便包含一些已覆盖的地区也没关系 (2) 重复第一步直到覆盖了全部的地区

 这是一种近似算法（approximation algorithm，贪婪算法的一种）。在获取到精确的最优解需要的时间太长时，便可以使用近似算法，判断近似算法的优劣标准如下:

 速度有多快
 得到的近似解与最优解的接近程度
 在本例中贪婪算法是个不错的选择，不仅运行速度快，本例运行时间O(n²),最坏的情况，假设n个广播台，每个广播台就覆盖1个地区,n个地区，总计需要查询n*n=O(n²)
 */

public class BroadcaseCoverActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcase_cover);

        mTextView = (TextView) this.findViewById(R.id.text_view);


        new Thread() {

            @Override
            public void run() {
                startcomptuer();
            }

        }.start();
    }

    private void startcomptuer() {

        //初始化广播台信
        HashMap<String,HashSet<String>> broadcasts = new HashMap<String,HashSet<String>>();
        broadcasts.put("K1", new HashSet(Arrays.asList(new String[] {"ID","NV","UT"})));
        broadcasts.put("K2", new HashSet(Arrays.asList(new String[] {"WA","ID","MT"})));
        broadcasts.put("K3", new HashSet(Arrays.asList(new String[] {"OR","NV","CA"})));
        broadcasts.put("K4", new HashSet(Arrays.asList(new String[] {"NV","UT"})));
        broadcasts.put("K5", new HashSet(Arrays.asList(new String[] {"CA","AZ"})));

        //需要覆盖的全部地区
        HashSet<String> allAreas = new HashSet<>(Arrays.asList(new String[]{"ID","NV","UT","WA","MT","OR","CA","AZ"}));
        //所选择的广播台列表
        List<String> selects = new ArrayList<String>();

        HashSet<String> tempSet = new HashSet<>();

        String maxKey = null;

        while (allAreas.size() > 0) {
            maxKey = null;
            for (String key: broadcasts.keySet()) {
                tempSet.clear();
                HashSet<String> areas = broadcasts.get(key);
                tempSet.addAll(areas);

                //求出2个集合的交集，此时tempSet会被赋值为交集的内容，所以使用临时变量
                tempSet.retainAll(allAreas);
                //如果该集合包含的地区数量比原来的集合多

//                Log.i("DEBUG_TEST","s集合的交集 is:" + tempSet);
                if (tempSet.size() > 0) {
                    if (maxKey == null) {
                        maxKey = key;
                    } else if (tempSet.size() > broadcasts.get(maxKey).size()) {
                        maxKey = key;
                    }
                }
            }

            Log.i("DEBUG_TEST","select key is:" + maxKey);
            if (maxKey != null) {
                selects.add(maxKey);
                allAreas.removeAll(broadcasts.get(maxKey));
                broadcasts.remove(maxKey);
            }
//
//            Log.i("DEBUG_TEST","s集合的剩余 allAreas is:" + allAreas);
//
//            Log.i("DEBUG_TEST","s集合的剩余 broadcasts is:" + broadcasts);
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("当前找到的最优解为：\n");

        Iterator iterator =  selects.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next() + "\t");
        }


        mTextView.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(sb.toString());
            }
        });

    }
}
