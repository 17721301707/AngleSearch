package alvin.anglesearch.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by alvin on 2016/3/24.
 */
@DatabaseTable(tableName = "tb_bulk")
public class Bulk extends  BaseBean{
    //每块不锈钢的唯一编号
    @DatabaseField(generatedId = true)
    private int id;
    //每块不锈钢的编号,编号唯一
    @DatabaseField(columnName = "number",unique = true,canBeNull = false)
    private String number;
    //备注
    @DatabaseField(columnName = "remark")
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
