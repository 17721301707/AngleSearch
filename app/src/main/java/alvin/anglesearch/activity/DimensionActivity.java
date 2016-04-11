package alvin.anglesearch.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alvin.anglesearch.R;
import alvin.anglesearch.adapter.DimensionAdapter;
import alvin.anglesearch.db.bean.Bulk;
import alvin.anglesearch.db.bean.Dimension;
import alvin.anglesearch.manager.DimensionManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class DimensionActivity extends AppCompatActivity {
    @Bind(R.id.demension_list)
    ListView mLvList;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private DimensionAdapter mAdapter;
    private Bulk mBulk;
    private DimensionManager mDimensionManager;
    private Context mContext;
    private List<Dimension> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimension);
        ButterKnife.bind(this);
        Intent it = getIntent();
        if (it != null) {
            mBulk = (Bulk) it.getSerializableExtra("bulk");
        }
        mContext = getApplicationContext();
        mDimensionManager = new DimensionManager(mContext);
        if (mBulk != null) {
            setTitle(getResources().getString(R.string.data_number) + ":" + mBulk.getNumber()
                    + getResources().getText(R.string.dimension_title_end));
        } else {
            setTitle(R.string.dimension_title_no);
        }
        mAdapter = new DimensionAdapter(mContext, mListData);
        mLvList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        initList();
        super.onResume();
    }

    private void addOrUpdate(final Dimension dimension, final int position) {

        LayoutInflater inflater = LayoutInflater.from(DimensionActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_add_dimension, null);
        final Dialog dialog = new Dialog(DimensionActivity.this);

        dialog.setContentView(dialogView);
        final EditText mEtLen1 = (EditText) dialogView.findViewById(R.id.et_len1);
        final EditText mEtLen2 = (EditText) dialogView.findViewById(R.id.et_len2);
        final EditText mEtInfo = (EditText) dialogView.findViewById(R.id.et_angle);
        if (dimension != null) {
            mEtLen1.setText(String.valueOf(dimension.getLength1()));
            mEtLen1.setSelection(mEtLen1.getText().length());
            mEtLen2.setText(String.valueOf(dimension.getLength2()));
            mEtLen2.setSelection(mEtLen2.getText().length());
            mEtInfo.setText(dimension.getRemark());
            mEtInfo.setSelection(mEtInfo.getText().length());
            dialog.setTitle(R.string.modify_dimension);
        } else {
            dialog.setTitle(R.string.add_dimension);
        }
        final Button mBtCancel = (Button) dialogView.findViewById(R.id.cancel);
        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button mBtCommit = (Button) dialogView.findViewById(R.id.sure);
        mBtCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String len1 = mEtLen1.getText().toString();
                final String len2 = mEtLen2.getText().toString();
                final String info = mEtInfo.getText().toString();
                if (len1 != null && len2 != null && !len1.equals("") && !len2.equals("")) {
                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(Void... params) {
                            int result = -1;
                            if (dimension == null) {
                                Dimension dimension1 = new Dimension();
                                dimension1.setLength1(Float.valueOf(len1));
                                dimension1.setLength2(Float.valueOf(len2));
                                dimension1.setRemark(info);
                                dimension1.setBulk(mBulk);
                                if (mDimensionManager.create(dimension1)) {
                                    result = 0;
                                    mListData.add(dimension1);
                                }
                            } else {
                                dimension.setLength1(Float.valueOf(len1));
                                dimension.setLength2(Float.valueOf(len2));
                                dimension.setRemark(info);
                                if (mDimensionManager.modify(dimension)) {
                                    result = 1;
                                    mListData.remove(position);
                                    mListData.add(dimension);
                                }
                            }

                            return result;
                        }

                        @Override
                        protected void onPostExecute(Integer integer) {
                            switch (integer) {
                                case -1:
                                    Toast.makeText(mContext, R.string.add_dimension_fail, Toast.LENGTH_LONG).show();
                                    break;
                                case 0:
                                    Toast.makeText(mContext, R.string.add_success, Toast.LENGTH_LONG).show();
                                    mAdapter.refresh(mListData);
                                    dialog.dismiss();
                                    break;
                                case 1:
                                    Toast.makeText(mContext, R.string.modify_success, Toast.LENGTH_LONG).show();
                                    mAdapter.refresh(mListData);
                                    dialog.dismiss();
                                    break;
                            }
                            super.onPostExecute(integer);
                        }
                    }.execute();

                } else {
                    Toast.makeText(mContext, R.string.dimension_null, Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    /**
     * 初始化，获取所有尺寸数据
     */
    private void initList() {
        if (mListData == null) {
            mListData = new ArrayList<>();
        }

        if (mBulk != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    mListData = mDimensionManager.getDimensionByBulk(mBulk);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    mAdapter.refresh(mListData);
                }
            }.execute();
        }
    }

    @OnItemLongClick(R.id.demension_list)
    boolean onItemLongClick(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String title = getResources().getString(R.string.data_length1)
                + mListData.get(position).getLength1() + getResources().getString(R.string.data_length2)
                + mListData.get(position).getLength2();
        builder.setTitle(title);
        builder.setItems(R.array.dimension_menu_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        addOrUpdate(mListData.get(position), position);
                        break;
                    case 1:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DimensionActivity.this);
                        builder1.setTitle(R.string.dimension_menu_remove);
                        builder1.setMessage(title + "\n" + getResources().getString(R.string.dimension_del_alter));
                        builder1.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AsyncTask<Void, Void, Boolean>() {
                                    @Override
                                    protected Boolean doInBackground(Void... params) {
                                        return mDimensionManager.remove(mListData.get(position));
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean aBoolean) {
                                        super.onPostExecute(aBoolean);
                                        if (aBoolean) {
                                            Toast.makeText(DimensionActivity.this, R.string.remove_success, Toast.LENGTH_LONG).show();
                                            mListData.remove(position);
                                            mAdapter.refresh(mListData);
                                        } else {
                                            Toast.makeText(DimensionActivity.this, R.string.remove_fail, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }.execute();
                                dialog.dismiss();
                            }

                        });
                        builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder1.create().show();
                        break;
                }
            }
        });
        builder.show();
        return false;
    }

    @OnItemClick(R.id.demension_list)
    void onItemClick(int position) {
        Intent it = new Intent(this, AngleActivity.class);
        it.putExtra("dimension", mListData.get(position));
        startActivity(it);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        addOrUpdate(null, -1);
    }
}
