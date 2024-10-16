package garry.train.business.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author Garry
 * 2024-10-16 10:59
 */
@Data
public class MarkdownPageForm {

    /**
     * md 页面对应的 MarkdownEnum.code
     */
    @NotBlank(message = "【md文件编号】不能为空")
    private String mdPageCode;
}
