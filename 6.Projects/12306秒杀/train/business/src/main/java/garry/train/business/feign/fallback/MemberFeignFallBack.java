package garry.train.business.feign.fallback;

import garry.train.business.feign.MemberFeign;
import garry.train.business.form.TicketSaveForm;
import org.springframework.stereotype.Component;

/**
 * @author Garry
 * 2024-11-01 21:30
 */
@Component
public class MemberFeignFallBack implements MemberFeign {
    @Override
    public String hello() {
        return "helloFallBack";
    }

    @Override
    public String save(TicketSaveForm form) {
        return "saveFallBack";
    }

    @Override
    public String queryList(Integer pageNum, Integer pageSize) {
        return "queryListFallBack";
    }
}
