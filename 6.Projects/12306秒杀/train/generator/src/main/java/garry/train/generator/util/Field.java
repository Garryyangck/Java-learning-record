package garry.train.generator.util;

import lombok.Data;

/**
 * 读取数据库时，每一个字段都要读出来这些属性
 */
@Data
public class Field {
    /**
     * 字段名：course_id
     */
    private String name;

    /**
     * 字段名小驼峰：courseId
     */
    private String nameHump;

    /**
     * 字段名大驼峰：CourseId
     */
    private String nameBigHump;

    /**
     * 中文名：课程
     */
    private String nameCn;

    /**
     * 字段类型：char(8)
     */
    private String type;

    /**
     * java类型：String
     */
    private String javaType;

    /**
     * 注释：课程|ID
     */
    private String comment;

    /**
     * 是否可为空
     */
    private Boolean nullAble;

    /**
     * 字符串长度
     */
    private Integer length;

    /**
     * 是否是枚举
     */
    private Boolean enums;

    /**
     * 枚举常量 COURSE_LEVEL，用于生成前端的 js 静态文件
     */
    private String enumsConst;
}
