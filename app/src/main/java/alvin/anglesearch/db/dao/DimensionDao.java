package alvin.anglesearch.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alvin.anglesearch.db.bean.Bulk;
import alvin.anglesearch.db.bean.Dimension;
import alvin.anglesearch.tools.DataBaseHelper;

/**
 * Created by alvin on 2016/3/24.
 */
public class DimensionDao {
    private Dao<Dimension, Integer> dimensionOpe;
    private DataBaseHelper helper;

    public DimensionDao(Context context) {
        try {
            helper = DataBaseHelper.getInstance(context);
            dimensionOpe = helper.getDemensionDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有尺寸
     *
     * @return 所有尺寸的list
     */
    public List<Dimension> listDimension() {
        List<Dimension> dimensionList = new ArrayList<>();
        try {
            dimensionList = dimensionOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dimensionList;
    }

    /**
     * 增加尺寸
     *
     * @param dimension
     */
    public void add(Dimension dimension) throws SQLException {

        dimensionOpe.create(dimension);

    }

    public void update(Dimension dimension) throws  SQLException
    {
        dimensionOpe.update(dimension);
    }

    /**
     * 根据id删除该尺寸
     *
     * @param id 要删除的尺寸的id
     */
    public void del(int id) throws SQLException {

        dimensionOpe.deleteBuilder().where().eq("dimension_id", id).query();

    }

    public void del(Dimension dimension) throws  SQLException
    {
        dimensionOpe.delete(dimension);
    }

    public List<Dimension> queryByNumber(Bulk bulk) {
        List<Dimension> dimensionList = new ArrayList<>();
        try {
            dimensionList = dimensionOpe.queryBuilder().where().eq("number",bulk).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dimensionList;
    }
}
