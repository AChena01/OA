package com.Liuyichen.oa.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class WeiBaoDealRecord {
    private Integer id;

    private Integer weiBaoId;

    private String dealSn;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date dealTime;

    private String dealWay;

    private String dealResult;

    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWeiBaoId() {
        return weiBaoId;
    }

    public void setWeiBaoId(Integer weiBaoId) {
        this.weiBaoId = weiBaoId;
    }

    public String getDealSn() {
        return dealSn;
    }

    public void setDealSn(String dealSn) {
        this.dealSn = dealSn;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealWay() {
        return dealWay;
    }

    public void setDealWay(String dealWay) {
        this.dealWay = dealWay;
    }

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private Employee dealer;

    public Employee getDealer() {
        return dealer;
    }

    public void setDealer(Employee dealer) {
        this.dealer = dealer;
    }
}
