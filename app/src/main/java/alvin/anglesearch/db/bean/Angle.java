package alvin.anglesearch.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by alvin on 2016/3/24.
 */
@DatabaseTable(tableName = "tb_angle")
public class Angle extends  BaseBean{
    //唯一ID
    @DatabaseField(generatedId = true)
    private int id ;
    //所属尺寸,外键关联
    @DatabaseField(canBeNull = false,foreign = true,columnName = "dimension_id")
    private Dimension dimension;
    //厚度，单位毫米
    @DatabaseField(columnName = "thick")
    private float thickness;
    //角度值
    @DatabaseField(columnName = "angle",canBeNull = false)
    private float angle;
    //该角度编号或名字
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

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
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
