package alvin.anglesearch.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import alvin.anglesearch.db.bean.Angle;
import alvin.anglesearch.db.bean.Bulk;
import alvin.anglesearch.db.bean.Dimension;

/**
 * Created by alvin on 2016/3/24.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {
    private final static int DATABASE_VERSION = 1;
    Dao<Bulk, Integer> mBulkDao;
    Dao<Dimension, Integer> mSizeDao;
    Dao<Angle,Integer> mAngleDao;
    private static final String DB_NAME = "sqlite-angle.db";
    private static DataBaseHelper mDbHelper;

    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }


    public static DataBaseHelper getInstance(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new DataBaseHelper(context);
        }
        return mDbHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Bulk.class);
            TableUtils.createTableIfNotExists(connectionSource, Dimension.class);
            TableUtils.createTableIfNotExists(connectionSource, Angle.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }



    public Dao<Bulk, Integer> getBulkDao() throws SQLException{
        if(mBulkDao == null){
            mBulkDao = getDao(Bulk.class);
        }
        return mBulkDao;
    }

    public Dao<Dimension, Integer> getDemensionDao() throws  SQLException{
        if(mSizeDao == null){
            mSizeDao = getDao(Dimension.class);
        }
        return mSizeDao;
    }

    public Dao<Angle, Integer> getAngleDao() throws  SQLException{
        if(mAngleDao == null){
            mAngleDao = getDao(Angle.class);
        }
        return mAngleDao;
    }


}
