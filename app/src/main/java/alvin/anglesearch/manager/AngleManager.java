package alvin.anglesearch.manager;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import alvin.anglesearch.db.bean.Angle;
import alvin.anglesearch.db.dao.AngleDao;

/**
 * Created by alvin on 2016/3/25.
 */
public class AngleManager {
    private AngleDao angleDao;

    public AngleManager(Context context) {
        angleDao = new AngleDao(context);
    }

    public boolean create(Angle angle) {
        try {
            angleDao.add(angle);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean remove(Angle angle)
    {
        try {
            angleDao.del(angle);
            return  true;
        } catch (Exception e) {
            return  false;
        }
    }

    public  Boolean modify(Angle angle)
    {
        try {
            angleDao.update(angle);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Angle> getAllAngleWithDimensionBulk()
    {
        return angleDao.queryAllWithDimensionBulk();
    }

    public List<Angle> getAngleByDimensionId(int dimensionId)
    {
        return angleDao.queryById(dimensionId);
    }

    public List<Angle> getAngeByDimensionIdWithDimension(int dimensionId)
    {
        return angleDao.queryByIdWithDimension(dimensionId);
    }

}
