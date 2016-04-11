package alvin.anglesearch.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import alvin.anglesearch.db.bean.Bulk;
import alvin.anglesearch.tools.DataBaseHelper;

/**
 * Created by alvin on 2016/3/25.
 */
public class BulkDao {
    private Dao<Bulk, Integer> bulkOpe;
    private DataBaseHelper helper;

    public BulkDao(Context context) {
        try {
            helper = DataBaseHelper.getInstance(context);
            bulkOpe = helper.getBulkDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加不锈钢块
     *
     * @param bulk
     */
    public void add(Bulk bulk) throws SQLException {
        bulkOpe.create(bulk);
    }

    /**
     * 根据不锈钢编号删除
     *
     * @param number 要删除的不锈钢块的编号
     */
    public void del(String number) throws SQLException {

        bulkOpe.deleteBuilder().where().eq("number", number).query();

    }

    /**
     * 删除
     * @param bulk
     * @throws SQLException
     */
    public void del(Bulk bulk) throws  SQLException{
        bulkOpe.delete(bulk);
    }

    /**
     * 修改不锈钢块信息
     * @param bulk
     * @throws SQLException
     */
    public void modify(Bulk bulk)throws SQLException{
        bulkOpe.update(bulk);
    }

    /**
     * 返回所有不锈钢块
     *
     * @return
     */
    public List<Bulk> listBulk() {
        List<Bulk> bulkList = new ArrayList<>();
        try {
            bulkList = bulkOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bulkList;
    }

    /**
     * 通过不锈钢number查找
     * @param id
     * @return
     */
    public List<Bulk> searchByNumber(String id){
        List<Bulk> bulkList = new ArrayList<>();
        try {
            bulkList = bulkOpe.queryBuilder().where().eq("number",id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bulkList;
    }
}
