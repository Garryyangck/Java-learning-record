package garry.train.business.controller;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import garry.train.business.enums.MarkdownEnum;
import garry.train.business.form.MarkdownPageForm;
import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.vo.ResponseVo;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Garry
 * 2024-10-16 10:54
 */
@RestController
@RequestMapping(value = "/markdown")
public class MarkdownController {

    private final Parser parser;

    private final HtmlRenderer renderer;

    private final String mdBasePath = "business/src/main/java/garry/train/business/markdown/";

    {
        MutableDataSet options = new MutableDataSet();
        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseVo<String> page(@Valid MarkdownPageForm form) {
        String fileName = MarkdownEnum.getFileNameByCode(form.getMdPageCode());
        String fullFileName = mdBasePath + fileName;
        String markdown;
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fullFileName));
            markdown = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.BUSINESS_MARKDOWN_FILE_READ_FAILED);
        }
        String html = renderer.render(parser.parse(markdown));
        return ResponseVo.success(html);
    }
}
