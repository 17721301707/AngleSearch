package alvin.anglesearch.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by alvin on 2016/3/24.
 */
@DatabaseTable(tableName="tab_dimension")
public class Dimension extends BaseBean{
    //唯一ID
    @DatabaseField(generatedId = true)
    private int id;
    //所属不锈钢尺寸,通过外键关联
    @DatabaseField(canBeNull = false,foreign = true,columnName = "number")
    private Bulk bulk;
    //角度边长1,单位毫米
    @DatabaseField(columnName = "length1",canBeNull = false)
    private float length1;
    //角度边长2，单位毫米
    @DatabaseField(columnName = "length2",canBeNull = false)
    private float length2;
    //该尺寸编号或名字
    @DatabaseField(columnName = "name")
    private String name;
    //备注
    @DatabaseField(columnName = "remark")
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bulk getBulk() {
        return bulk;
    }

    public void setBulk(Bulk bulk) {
        this.bulk = bulk;
    }

    public float getLength1() {
        return length1;
    }

    public void setLength1(float length1) {
        this.length1 = length1;
    }

    public float getLength2() {
        return length2;
    }

    public void setLength2(float length2) {
        this.length2 = length2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
