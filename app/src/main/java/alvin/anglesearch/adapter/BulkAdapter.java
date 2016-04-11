package alvin.anglesearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alvin.anglesearch.R;
import alvin.anglesearch.db.bean.Bulk;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alvin on 2016/3/29.
 */
public class BulkAdapter extends BaseAdapter {
    private Context mContext;
    //item布局文件id
    protected LayoutInflater mInflater;
    private List<Bulk> mListItem;

    public BulkAdapter(Context context, List<Bulk> listData) {
        mContext = context;
        mListItem = listData;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (null == mListItem) {
            return 0;
        } else {
            return mListItem.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return mListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position >= getCount()) {
            return convertView;
        }

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bulk_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bulk bulk = mListItem.get(position);
        holder.dataNumber.setText(bulk.getNumber());
        //holder.add.setOnClickListener(new AddDimensionClickLisenser(bulk));
        return convertView;
    }

    /**
     * 数据刷新
     *
     * @param listData
     */
    public void refresh(List<Bulk> listData) {
        mListItem = listData;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @Bind(R.id.data_number)
        TextView dataNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
