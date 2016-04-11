package alvin.anglesearch.manager;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import alvin.anglesearch.db.bean.Bulk;
import alvin.anglesearch.db.bean.Dimension;
import alvin.anglesearch.db.dao.DimensionDao;

/**
 * Created by alvin on 2016/3/25.
 */
public class DimensionManager {
    private DimensionDao dimensionDao;

    public DimensionManager(Context context) {
        dimensionDao = new DimensionDao(context);
    }

    /**
     * 增加尺寸
     * @param dimension
     * @return
     */
    public Boolean create(Dimension dimension) {
        try {
            dimensionDao.add(dimension);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean modify(Dimension dimension)
    {
        try {
            dimensionDao.update(dimension);
            return  true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 通过ID删除尺寸
     * @param id
     * @return
     */
    public Boolean remove(int id){
        try {
            dimensionDao.del(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean remove(Dimension dimension)
    {
        try {
            dimensionDao.del(dimension);
            return  true;
        } catch (SQLException e) {
            return  false;
        }
    }

    /**
     * 通过所属不锈钢编号查询尺寸
     * @param bulk
     * @return
     */
    public List<Dimension> getDimensionByBulk(Bulk bulk)
    {
        return dimensionDao.queryByNumber(bulk);
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
    public List<Dimension> getDimensionWithLength(Float length1, Float length2, Float accuracy) {
        //先获取到所有尺寸
        List<Dimension> dimensionList = dimensionDao.listDimension();
        //设置默认值
        float len1 = length1 == null ? -1 : length1;
        float len2 = length2 == null ? -1 : length2;
        float acc = accuracy == null ? 0 : accuracy;
        if (dimensionList != null) {
            if ((len1 != -1) && (len2 == -1)) {//匹配一条边
                dimensionList = checkLen(dimensionList, len1, acc);
            } else if ((len2 != -1) && (len1 == -1)) {//匹配一条边
                dimensionList = checkLen(dimensionList, len2, acc);
            } else {//匹配两条边
                dimensionList = checkLen(dimensionList, len1, acc);
                dimensionList = checkLen(dimensionList, len2, acc);
            }
            if (dimensionList.size() == 0)
                dimensionList = null;
        }
        return dimensionList;
    }

    /**
     * 按尺寸的一条边匹配Dimension
     *
     * @param srcList 用来匹配的尺寸对象list
     * @param len     需要配对的边长
     * @param acc     匹配时的精度
     * @return 匹配完成后返回符合匹配的list
     */
    private List<Dimension> checkLen(List<Dimension> srcList, float len, float acc) {
        for (int i = 0; i < srcList.size(); i++) {
            Dimension dimension = srcList.get(i);
            if ((len < dimension.getLength1() - acc) || (len > dimension.getLength1() + acc)) {
                srcList.remove(i);
            }
        }
        return srcList;
    }
}
