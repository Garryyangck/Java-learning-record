package garry.train.business.service;

import garry.train.common.form.ApiDetailQueryForm;
import garry.train.common.vo.ApiDetailVo;
import garry.train.common.vo.PageVo;

/**
 * @author Garry
 * 2024-10-14 21:26
 */
public interface ApiDetailService {

    PageVo<ApiDetailVo> queryList(ApiDetailQueryForm form);
}
