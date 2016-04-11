package alvin.anglesearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import alvin.anglesearch.R;
import alvin.anglesearch.db.bean.Angle;
import alvin.anglesearch.db.bean.Bulk;
import alvin.anglesearch.db.bean.Dimension;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by alvin on 2016/3/31.
 */
public class SearchAdapter extends BaseAdapter {

    private Context mContext;
    //item布局文件id
    protected LayoutInflater mInflater;
    private List<Angle> mListItem;

    public SearchAdapter(Context context, List<Angle> listData) {
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
            convertView = mInflater.inflate(R.layout.all_data_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Angle angle = mListItem.get(position);
        Dimension dimension = angle.getDimension();
        Bulk bulk = dimension.getBulk();
        holder.number.setText(bulk.getNumber());
        holder.len1.setText(String.valueOf(dimension.getLength1()));
        holder.len2.setText(String.valueOf(dimension.getLength2()));
        holder.angle.setText(String.valueOf(angle.getAngle()));
        holder.thick.setText(String.valueOf(angle.getThickness()));
        return convertView;
    }

    /**
     * 数据刷新
     *
     * @param listData
     */

    public void refresh(List<Angle> listData) {
        mListItem = listData;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @Bind(R.id.number)
        TextView number;
        @Bind(R.id.len1)
        TextView len1;
        @Bind(R.id.len2)
        TextView len2;
        @Bind(R.id.angle)
        TextView angle;
        @Bind(R.id.thick)
        TextView thick;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
