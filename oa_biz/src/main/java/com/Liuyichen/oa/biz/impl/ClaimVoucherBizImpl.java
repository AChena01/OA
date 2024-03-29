package com.Liuyichen.oa.biz.impl;

import com.Liuyichen.oa.biz.ClaimVoucherBiz;
import com.Liuyichen.oa.dao.*;
import com.Liuyichen.oa.entity.ClaimVoucher;
import com.Liuyichen.oa.entity.ClaimVoucherItem;
import com.Liuyichen.oa.entity.DealRecord;
import com.Liuyichen.oa.entity.Employee;
import com.Liuyichen.oa.global.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("claimVoucherBiz")
public class ClaimVoucherBizImpl implements ClaimVoucherBiz {

    @Qualifier("claimVoucherDao")
    @Autowired
    private ClaimVoucherDao claimVoucherDao;
    @Qualifier("claimVoucherItemDao")
    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;
    @Qualifier("dealRecordDao")
    @Autowired
    private DealRecordDao dealRecordDao;
    @Qualifier("employeeDao")
    @Autowired
    private EmployeeDao employeeDao;
    @Qualifier("imageDao")
    @Autowired
    private ImageDao imageDao;

    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setCreateTime(new Date());
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED);
        claimVoucherDao.insert(claimVoucher);

        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            claimVoucherItemDao.insert(item);
        }
    }


    public ClaimVoucher get(int id) {
        return claimVoucherDao.select(id);
    }

    public void delete(int id) {
        claimVoucherDao.delete(id);
    }

    public List<ClaimVoucherItem> getItems(int cvid) {
        return claimVoucherItemDao.selectByClaimVoucher(cvid);
    }

    public List<DealRecord> getRecords(int cvid) {
        return dealRecordDao.selectByClaimVoucher(cvid);
    }

    public List<ClaimVoucher> getForSelf(String sn) {
        return claimVoucherDao.selectByCreateSn(sn);
    }

    public List<ClaimVoucher> getForDeal(String sn) {
        return claimVoucherDao.selectByNextDealSn(sn);
    }

    public void update(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED);
        claimVoucherDao.update(claimVoucher);

        List<ClaimVoucherItem> olds = claimVoucherItemDao.selectByClaimVoucher(claimVoucher.getId());
        for (ClaimVoucherItem old : olds) {
            boolean isHave = false;
            for (ClaimVoucherItem item : items) {
                if (item.getId() == old.getId()) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                claimVoucherItemDao.delete(old.getId());
            }
        }
        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            if (item.getId() > 0) {
                claimVoucherItemDao.update(item);
            } else {
                claimVoucherItemDao.insert(item);
            }
        }

    }

    public void submit(int id) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(id);
        Employee employee = employeeDao.select(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_SUBMIT);
        if (employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(), Contant.POST_FM) != null) {
            claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(), Contant.POST_FM).get(0).getSn());

        }

        claimVoucherDao.update(claimVoucher);

        DealRecord dealRecord = new DealRecord();
        dealRecord.setDealWay(Contant.DEAL_SUBMIT);
        dealRecord.setDealSn(employee.getSn());
        dealRecord.setClaimVoucherId(id);
        dealRecord.setDealResult(Contant.CLAIMVOUCHER_SUBMIT);
        dealRecord.setDealTime(new Date());
        dealRecord.setComment("无");

        dealRecordDao.insert(dealRecord);
    }


    public void deal(DealRecord dealRecord) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(dealRecord.getClaimVoucherId());
        Employee employee = employeeDao.select(dealRecord.getDealSn());
        if (dealRecord.getDealWay().equals(Contant.DEAL_PASS)) {
            if (claimVoucher.getTotalAmount() <= Contant.LIMIT_CHECK && employee.getPost().equals(Contant.POST_CM)) {
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Contant.POST_GM).get(0).getSn());

                dealRecord.setDealTime(new Date());
                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            } else if (employee.getPost().equals(Contant.POST_GM)) {
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Contant.POST_CM2).get(0).getSn());

                dealRecord.setDealTime(new Date());
                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            } else if (employee.getPost().equals(Contant.POST_CM2)) {
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Contant.POST_SHENJI).get(0).getSn());
                dealRecord.setDealTime(new Date());
                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            } else if (employee.getPost().equals(Contant.POST_SHENJI)) {
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Contant.POST_SHENJI2).get(0).getSn());
                dealRecord.setDealTime(new Date());
                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            } else if (employee.getPost().equals(Contant.POST_SHENJI2)) {
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Contant.POST_CASHIER).get(0).getSn());
                dealRecord.setDealTime(new Date());
                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            } else {
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Contant.POST_CM).get(0).getSn());
                dealRecord.setDealTime(new Date());
                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            }
        } else if (dealRecord.getDealWay().equals(Contant.DEAL_BACK)) {
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_BACK);
            claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
            dealRecord.setDealTime(new Date());
            dealRecord.setDealResult(Contant.CLAIMVOUCHER_BACK);

        } else if (dealRecord.getDealWay().equals(Contant.DEAL_REJECT)) {
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_TERMINATED);
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealTime(new Date());
            dealRecord.setDealResult(Contant.CLAIMVOUCHER_TERMINATED);
        } else if (dealRecord.getDealWay().equals(Contant.DEAL_PAID)) {
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_PAID);
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealTime(new Date());
            dealRecord.setDealResult(Contant.CLAIMVOUCHER_PAID);
        }
        claimVoucherDao.update(claimVoucher);
        dealRecordDao.insert(dealRecord);
    }
}
