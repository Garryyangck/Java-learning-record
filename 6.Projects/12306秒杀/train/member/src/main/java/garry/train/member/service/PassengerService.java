package garry.train.member.service;

import garry.train.member.form.PassengerSaveForm;

/**
 * @author Garry
 * 2024-09-13 19:14
 */
public interface PassengerService {
    /**
     * 存储新乘客
     */
    void save(PassengerSaveForm form);
}
