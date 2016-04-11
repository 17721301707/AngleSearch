package alvin.anglesearch.manager;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import alvin.anglesearch.db.dao.BulkDao;
import alvin.anglesearch.db.bean.Bulk;

/**
 * Created by alvin on 2016/3/25.
 */
public class BulkManager {
    private BulkDao bulkDao = null;

    public BulkManager(Context context) {
        bulkDao = new BulkDao(context);
    }

    /**
     * 获取所有不锈钢块
     *
     * @return
     */
    public List<Bulk> getAllBulk() {
        return bulkDao.listBulk();
    }

    /**
     * 添加不锈钢块
     *
     * @param bulk
     * @return
     */
    public Boolean create(Bulk bulk) {
        try {
            bulkDao.add(bulk);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 更新
     * @param bulk
     * @return
     */
    public Boolean update(Bulk bulk)
    {
        try {
            bulkDao.modify(bulk);
            return  true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 通过编号删除不锈钢块
     * @param
     * @return
     */
    public Boolean remove(String number){
        try {
            bulkDao.del(number);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean remove(Bulk bulk)
    {
        try {
            bulkDao.del(bulk);
            return  true;
        } catch (SQLException e) {
            return  false;
        }
    }

    /**
     * 通过number查找
     * @param number
     * @return true: 存在  false: 不存在
     */
    public  Boolean exist(String number){
        List<Bulk> bulkList =  bulkDao.searchByNumber(number);
        if(bulkList!=null&&bulkList.size()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}