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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alvin.anglesearch.R;
import alvin.anglesearch.adapter.AngleAdapter;
import alvin.anglesearch.db.bean.Angle;
import alvin.anglesearch.db.bean.Dimension;
import alvin.anglesearch.manager.AngleManager;

public class AngleActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    private AngleAdapter mAdapter;
    private AngleManager mAngleManager;
    private Context mContext;
    private ListView mLvList;
    private List<Angle> mListData;
    private Dimension mDimension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angle);
        Intent it = getIntent();
        if (it != null) {
            mDimension = (Dimension) it.getSerializableExtra("dimension");
        }
        bindView();

    }

    @Override
    protected void onResume() {
        initList();
        super.onResume();
    }

    private void bindView() {
        mLvList = (ListView) findViewById(R.id.list);
        mContext = getApplicationContext();
        mAngleManager = new AngleManager(mContext);
        if (mDimension != null) {
            setTitle(
                    getResources().getString(R.string.data_length1)
                            + mDimension.getLength1()
                            + "  "
                            + getResources().getString(R.string.data_length2)
                            + mDimension.getLength2()
                            + getResources().getString(R.string.angle_title_end));
        } else {
            setTitle(R.string.angle_title_no);
        }
        mAdapter = new AngleAdapter(mContext, mListData);
        mLvList.setAdapter(mAdapter);
        mLvList.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrUpdate(null, -1);
            }
        });
    }

    /**
     * 获取所有角度数据
     */
    private void initList() {
        if (mListData == null) {
            mListData = new ArrayList<>();
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mListData = mAngleManager.getAngleByDimensionId(mDimension.getId());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.refresh(mListData);
            }
        }.execute();
    }

    private void addOrUpdate(final Angle angle, final int position) {

        LayoutInflater inflater = LayoutInflater.from(AngleActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_add_angle, null);
        final Dialog dialog = new Dialog(AngleActivity.this);

        dialog.setContentView(dialogView);
        final EditText mEtLen1 = (EditText) dialogView.findViewById(R.id.et_angle);
        final EditText mEtLen2 = (EditText) dialogView.findViewById(R.id.et_thick);
        final EditText mEtInfo = (EditText) dialogView.findViewById(R.id.info);
        if (angle != null) {
            mEtLen1.setText(String.valueOf(angle.getAngle()));
            mEtLen1.setSelection(mEtLen1.getText().length());
            Float thickness = angle.getThickness();
            mEtLen2.setText(String.valueOf(thickness));
            mEtLen2.setSelection(mEtLen2.getText().length());
            mEtInfo.setText(angle.getRemark());
            mEtInfo.setSelection(mEtInfo.getText().length());
            dialog.setTitle(R.string.modify_angle);
        } else {
            dialog.setTitle(R.string.add_angle);
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
                if (len1 != null && !len1.equals("")) {
                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(Void... params) {
                            int result = -1;
                            if (angle == null) {
                                Angle angle1 = new Angle();
                                angle1.setAngle(Float.valueOf(len1));
                                angle1.setThickness(Float.valueOf(len2));
                                angle1.setRemark(info);
                                angle1.setDimension(mDimension);
                                if (mAngleManager.create(angle1)) {
                                    result = 0;
                                    mListData.add(angle1);
                                }
                            } else {
                                angle.setAngle(Float.valueOf(len1));
                                angle.setThickness(Float.valueOf(len2));
                                angle.setRemark(info);
                                if (mAngleManager.modify(angle)) {
                                    result = 1;
                                    mListData.remove(position);
                                    mListData.add(angle);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String title = getResources().getString(R.string.data_angel)
                + mListData.get(position).getAngle() + getResources().getString(R.string.data_thick)
                + mListData.get(position).getThickness();
        builder.setTitle(title);
        builder.setItems(R.array.dimension_menu_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        addOrUpdate(mListData.get(position), position);
                        break;
                    case 1:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AngleActivity.this);
                        builder1.setTitle(R.string.dimension_menu_remove);
                        builder1.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AsyncTask<Void, Void, Boolean>() {
                                    @Override
                                    protected Boolean doInBackground(Void... params) {
                                        return mAngleManager.remove(mListData.get(position));
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean aBoolean) {
                                        super.onPostExecute(aBoolean);
                                        if (aBoolean) {
                                            Toast.makeText(AngleActivity.this, R.string.remove_success, Toast.LENGTH_LONG).show();
                                            mListData.remove(position);
                                            mAdapter.refresh(mListData);
                                        } else {
                                            Toast.makeText(AngleActivity.this, R.string.remove_fail, Toast.LENGTH_LONG).show();
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
}
