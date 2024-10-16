package garry.train.business.enums;

import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;

/**
 * @author Garry
 * 2024-10-16 10:39
 */
public enum MarkdownEnum {

    WELCOME("1", "欢迎", "welcome.md"),

    ABOUT("2", "关于", "about.md"),
    ;

    private final String code;

    private final String desc;

    private final String fileName;

    MarkdownEnum(String code, String desc, String fileName) {
        this.code = code;
        this.desc = desc;
        this.fileName = fileName;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getFileName() {
        return fileName;
    }

    public static String getFileNameByCode(String code) {
        for (MarkdownEnum markdownEnum : MarkdownEnum.values()) {
            if (code.equals(markdownEnum.getCode())) {
                return markdownEnum.getFileName();
            }
        }
        throw new BusinessException(ResponseEnum.BUSINESS_MARKDOWN_FILE_NOT_FOUND);
    }
}
