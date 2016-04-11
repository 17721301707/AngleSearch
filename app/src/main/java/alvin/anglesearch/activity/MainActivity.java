package alvin.anglesearch.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alvin.anglesearch.R;
import alvin.anglesearch.adapter.BulkAdapter;
import alvin.anglesearch.db.bean.Bulk;
import alvin.anglesearch.manager.BulkManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.list_data)
    ListView listData;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.fab2)
    FloatingActionButton fab2;
    private Context mContext;
    BulkManager manager;
    private BulkAdapter mAdapter;
    private List<Bulk> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = getApplicationContext();
        manager = new BulkManager(mContext);
        mAdapter = new BulkAdapter(mContext, null);
        listData.setAdapter(mAdapter);
    }


    @Override
    protected void onResume() {
        initListView();
        super.onResume();
    }

    private void initListView() {
        if (mListData == null) {
            mListData = new ArrayList<>();
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mListData = manager.getAllBulk();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.refresh(mListData);
            }
        }.execute();

    }

    @OnItemLongClick(R.id.list_data)
    boolean onItemLongClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String title = getResources().getString(R.string.bulk_menu_title)
                + mListData.get(position).getNumber();
        builder.setTitle(title);
        builder.setItems(R.array.home_menu_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent it = new Intent(MainActivity.this, BulkActivity.class);
                        it.putExtra("bulk", mListData.get(position));
                        startActivity(it);
                        break;
                    case 1:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setTitle(R.string.bulk_menu_remove);
                        builder1.setMessage(title + "\n" + getResources().getString(R.string.bulk_del_alter));
                        builder1.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new AsyncTask<Void, Void, Boolean>() {
                                    @Override
                                    protected Boolean doInBackground(Void... params) {
                                        return manager.remove(mListData.get(position));
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean aBoolean) {
                                        super.onPostExecute(aBoolean);
                                        if (aBoolean) {
                                            Toast.makeText(MainActivity.this, R.string.remove_success, Toast.LENGTH_LONG).show();
                                            mListData.remove(position);
                                            mAdapter.refresh(mListData);
                                        } else {
                                            Toast.makeText(MainActivity.this, R.string.remove_fail, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }.execute();
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
        builder.create().show();
        return false;
    }


    @OnItemClick(R.id.list_data)
    void onItemClick(int position) {
        Intent it = new Intent(this, DimensionActivity.class);
        it.putExtra("bulk", mListData.get(position));
        startActivity(it);
    }

    @OnClick({R.id.fab, R.id.fab2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, BulkActivity.class));
                break;
            case R.id.fab2:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }
    }
}
