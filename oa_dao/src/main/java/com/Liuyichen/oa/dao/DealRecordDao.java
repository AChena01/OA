package com.Liuyichen.oa.dao;

import com.Liuyichen.oa.entity.DealRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("dealRecordDao")
public interface DealRecordDao {
    void insert(DealRecord dealRecord);

    List<DealRecord> selectByClaimVoucher(int cvid);
}
