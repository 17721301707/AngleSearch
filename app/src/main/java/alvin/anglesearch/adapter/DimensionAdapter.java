package alvin.anglesearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alvin.anglesearch.R;
import alvin.anglesearch.db.bean.Dimension;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alvin on 2016/3/28.
 */
public class DimensionAdapter extends BaseAdapter {
    private Context mContext;
    //item布局文件id
    protected LayoutInflater mInflater;
    private List<Dimension> mListItem;

    public DimensionAdapter(Context context, List<Dimension> listData) {
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
            convertView = mInflater.inflate(R.layout.dimension_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Dimension angle = mListItem.get(position);
        holder.info.setText(String.valueOf(angle.getRemark()));
        holder.infoLen1.setText(String.valueOf(angle.getLength1()) + mContext.getResources().getString(R.string.millimeter));
        holder.infoLen2.setText(String.valueOf(angle.getLength2()) + mContext.getResources().getString(R.string.millimeter));
        return convertView;
    }

    /**
     * 数据刷新
     *
     * @param listData
     */
    public void refresh(List<Dimension> listData) {
        mListItem = listData;
        notifyDataSetChanged();
    }


    class ViewHolder {
        @Bind(R.id.info_len1)
        TextView infoLen1;
        @Bind(R.id.info_len2)
        TextView infoLen2;
        @Bind(R.id.info)
        TextView info;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
