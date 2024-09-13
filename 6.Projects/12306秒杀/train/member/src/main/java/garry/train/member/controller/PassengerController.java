package garry.train.member.controller;

import garry.train.common.vo.ResponseVo;
import garry.train.member.form.PassengerSaveForm;
import garry.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController // RestController = Controller + ResponseBody
@RequestMapping(value = "/passenger")
public class PassengerController {
    @Resource
    private PassengerService passengerService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseVo save(@Valid @RequestBody PassengerSaveForm form) {
        passengerService.save(form);
        return ResponseVo.success();
    }
}
