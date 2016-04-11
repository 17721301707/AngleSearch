package alvin.anglesearch.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import alvin.anglesearch.R;
import alvin.anglesearch.adapter.SearchAdapter;
import alvin.anglesearch.db.bean.Angle;
import alvin.anglesearch.db.bean.Dimension;
import alvin.anglesearch.manager.AngleManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.len1)
    EditText mEtLen1;
    @Bind(R.id.len2)
    EditText mEtLen2;
    @Bind(R.id.acc)
    EditText mEtAcc;
    @Bind(R.id.commit)
    Button mBtCommit;
    @Bind(R.id.list)
    ListView mLvList;
    private List<Angle> mListData;
    private SearchAdapter mAdapter;
    private AngleManager mAngleManager;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mAngleManager = new AngleManager(this);
        mAdapter = new SearchAdapter(this, mListData);
        mLvList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        initList();
        super.onResume();
    }

    private void initList() {
        if (mListData == null) {
            mListData = new ArrayList<>();
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mListData = mAngleManager.getAllAngleWithDimensionBulk();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.refresh(mListData);
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @OnClick(R.id.commit)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                }
                String tmpLen1 = mEtLen1.getText().toString();
                String tmpLen2 = mEtLen2.getText().toString();
                String tmpAcc = mEtAcc.getText().toString();
                final float searchLen1 = tmpLen1.equals("") ? -1 : Float.valueOf(tmpLen1);
                final float searchLen2 = tmpLen2.equals("") ? -1 : Float.valueOf(tmpLen2);
                final float searchAcc = tmpAcc.equals("") ? 0 : Float.valueOf(tmpAcc);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        mListData = mAngleManager.getAllAngleWithDimensionBulk();
                        mListData = search(mListData, searchLen1, searchLen2, searchAcc);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        mAdapter.refresh(mListData);
                    }
                }.execute();
                break;
        }
    }

    /**
     * 通过尺寸查找
     *
     * @param length1  用于查找的尺寸，默认值为-1，表示查找所有
     * @param length2  用于查找的尺寸，默认值为-1，表示查找所有
     * @param accuracy 用于超找的精度，length-accuracy<=length<=length+accuracy
     *                 精度超找单位为1毫米,默认值为0
     * @return 满足条件尺寸对象的list
     */
    public List<Angle> search(List<Angle> angleList, Float length1, Float length2, Float accuracy) {
        //设置默认值
        float len1 = length1 == null ? -1 : length1;
        float len2 = length2 == null ? -1 : length2;
        float acc = accuracy == null ? 0 : accuracy;
        List<Angle> angles = new ArrayList<>();
        if (angleList != null && angleList.size() > 0 && ((len1 != -1) || (len2 != -1))) {
            for (int i = 0; i < angleList.size(); i++) {
                Dimension dimension = angleList.get(i).getDimension();
                if (checklen(dimension, len1, len2, acc)) {
                    angles.add(angleList.get(i));
                }
            }
        }
        return angles;
    }

    private Boolean checklen(Dimension dimension, float len1, float len2, float acc) {
        Boolean result = false;
        if ((len1 == -1) && (len2 != -1)) {
            result = compare(dimension, len2, null, acc);
        } else if ((len2 == -1) && (len1 != -1)) {
            result = compare(dimension, len1, null, acc);
        } else {
            result = compare(dimension, len1, len2, acc);
        }
        return result;
    }

    private Boolean compare(Dimension dimension, Float len, Float len2, Float acc) {
        Boolean result = false;
        if (dimension != null) {
            float compLen1 = dimension.getLength1();
            float compLen2 = dimension.getLength2();
            if ((len != null) && (len2 == null)) {
                if (((len <= compLen1 + acc) && (len >= compLen1 - acc))
                        || ((len <= compLen2 + acc) && (len >= compLen2 - acc))) {
                    result = true;
                }
            } else if ((len != null) && (len2 != null)) {
                if (((len <= compLen1 + acc) && (len >= compLen1 - acc)
                        && (len2 <= compLen2 + acc) && (len2 >= compLen2 - acc))
                        || ((len2 <= compLen1 + acc) && (len2 >= compLen1 - acc)
                        && (len <= compLen2 + acc) && (len >= compLen2 - acc))) {
                    result = true;
                }
            }
        }

        return result;
    }

}
