package garry.train.business.controller;

import garry.train.common.util.HostHolder;
import garry.train.common.vo.PageVo;
import garry.train.common.vo.ResponseVo;
import garry.train.business.form.MessageQueryForm;
import garry.train.business.form.MessageSaveForm;
import garry.train.business.service.MessageService;
import garry.train.business.vo.MessageQueryVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author Garry
 * 2024-10-11 14:24
 */
@RestController
@RequestMapping(value = "/message")
public class MessageController {
    @Resource
    private MessageService messageService;

    @Resource
    private HostHolder hostHolder;

    /**
     * 接收新增和修改的请求，如果 form.id = null，则为新增；反之为修改
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody MessageSaveForm form) {
        form.setFromId(hostHolder.getMemberId()); // 从 hostHolder 中获取消息发出者id
        messageService.save(form);
        return ResponseVo.success();
    }

    @RequestMapping(value = "/query-list", method = RequestMethod.GET)
    public ResponseVo<PageVo<MessageQueryVo>> queryList(@Valid MessageQueryForm form) {
        form.setToId(hostHolder.getMemberId()); // 从 hostHolder 中获取消息接收者id
        PageVo<MessageQueryVo> vo = messageService.queryList(form);
        return ResponseVo.success(vo);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseVo delete(@PathVariable Long id) {
        messageService.delete(id);
        return ResponseVo.success();
    }
}
