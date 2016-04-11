package alvin.anglesearch.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import alvin.anglesearch.R;
import alvin.anglesearch.db.bean.Bulk;
import alvin.anglesearch.manager.BulkManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BulkActivity extends AppCompatActivity{
    @Bind(R.id.et_bulk)
    EditText etBulk;
    @Bind(R.id.et_bulk_remark)
    EditText etBulkRemark;
    @Bind(R.id.bulk_result)
    TextView bulkResult;
    @Bind(R.id.sure)
    Button sure;
    @Bind(R.id.cancel)
    Button cancel;
    private BulkManager manager;
    private Bulk mBulk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk);
        ButterKnife.bind(this);
        Intent it = getIntent();
        if (it != null) {
            mBulk = (Bulk) it.getSerializableExtra("bulk");
        }
        manager = new BulkManager(this);
        if (mBulk != null) {
            etBulk.setText(mBulk.getNumber());
            bulkResult.setText(mBulk.getRemark());
            setTitle(R.string.bulk_menu_modify);
            etBulk.setSelection(etBulk.getText().length());
            etBulk.setSelection(etBulk.getText().length());
        } else {
            setTitle(R.string.add_bulk);
        }
    }

    @OnClick({R.id.sure, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sure:
                final String input_bulk = etBulk.getText().toString();
                final String input_info = bulkResult.getText().toString();
                if (input_bulk == null || input_bulk.equals("")) {
                    Toast.makeText(this, R.string.not_null, Toast.LENGTH_LONG).show();
                } else {
                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(Void... params) {

                            int result = -1;
                            if (manager.exist(input_bulk)) {
                                result = 0;
                            } else {
                                if (mBulk == null) {
                                    Bulk newBulk = new Bulk();
                                    newBulk.setNumber(input_bulk);
                                    newBulk.setRemark(input_info);
                                    if (manager.create(newBulk)) {
                                        result = 1;
                                    }
                                } else {
                                    mBulk.setNumber(input_bulk);
                                    mBulk.setRemark(input_info);
                                    if (manager.update(mBulk)) {
                                        result = 2;
                                    }
                                }

                            }
                            return result;
                        }

                        @Override
                        protected void onPostExecute(Integer integer) {
                            switch (integer) {
                                case -1:
                                    bulkResult.setText(R.string.add_fail);
                                    break;
                                case 0:
                                    bulkResult.setText(R.string.bulk_exit);
                                    break;
                                case 1:
                                    Toast.makeText(BulkActivity.this, R.string.add_success
                                            , Toast.LENGTH_LONG).show();
                                    finish();
                                    break;
                                case 2:
                                    Toast.makeText(BulkActivity.this, R.string.modify_success
                                            , Toast.LENGTH_LONG).show();
                                    finish();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }.execute();
                }
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
