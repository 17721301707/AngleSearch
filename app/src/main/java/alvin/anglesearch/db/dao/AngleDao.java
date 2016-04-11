package alvin.anglesearch.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alvin.anglesearch.db.bean.Angle;
import alvin.anglesearch.db.bean.Bulk;
import alvin.anglesearch.db.bean.Dimension;
import alvin.anglesearch.tools.DataBaseHelper;

/**
 * Created by alvin on 2016/3/25.
 */
public class AngleDao {
    private Dao<Angle, Integer> angleOpe;
    private DataBaseHelper helper;

    public AngleDao(Context context) {
        try {
            helper = DataBaseHelper.getInstance(context);
            angleOpe = helper.getAngleDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加角度
     *
     * @param
     */
    public void add(Angle angle) throws SQLException {
        angleOpe.create(angle);
    }

    /**
     * 根据不锈钢编号删除
     *
     * @param id 要删除的不锈钢块的编号
     */
    public void del(int id) {
        try {
            angleOpe.deleteBuilder().where().eq("angle_id", id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void del(Angle angle) throws SQLException {
        angleOpe.delete(angle);
    }

    public void update(Angle angle) throws  SQLException
    {
        angleOpe.update(angle);
    }

    /**
     * 通过尺寸id查询
     *
     * @param dimension_id
     * @return
     */
    public List<Angle> queryById(int dimension_id) {
        List<Angle> angleList = new ArrayList<>();
        try {
            angleList = angleOpe.queryBuilder().where().eq("dimension_id", dimension_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return angleList;
    }

    public List<Angle> queryAllWithDimensionBulk()
    {
        List<Angle> angleList = new ArrayList<>();
        try {
            angleList = angleOpe.queryForAll();
            if (angleList != null) {
                for (Angle angle : angleList) {
                    helper.getDao(Dimension.class).refresh(angle.getDimension());
                    Dimension dimension = angle.getDimension();
                    if(dimension!=null)
                    {
                        helper.getDao(Bulk.class).refresh(dimension.getBulk());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  angleList;
    }

    /**
     * 通过尺寸ID获取角度，并获取关联尺寸
     *
     * @param dimension_id
     * @return
     */
    public List<Angle> queryByIdWithDimension(int dimension_id) {
        List<Angle> angleList = new ArrayList<>();
        try {
            angleList = angleOpe.queryBuilder().where().eq("dimension_id", dimension_id).query();
            if (angleList != null) {
                for (Angle angle : angleList) {
                    helper.getDao(Dimension.class).refresh(angle.getDimension());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return angleList;
    }
}
