package com.testtask.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String ssoid;
    private Integer ts;
    private String grp;
    private String type;
    private String subtype;
    private String url;
    private String orgid;
    private String formid;
    private String ltpa;
    private String sudirresponse;
    private Date ymdh;

    public Form() {
    }

    public Form(String ssoid, Integer ts, String grp, String type, String subtype, String url, String orgid, String formid, String ltpa, String sudirresponse, Date ymdh) {
        this.ssoid = ssoid;
        this.ts = ts;
        this.grp = grp;
        this.type = type;
        this.subtype = subtype;
        this.url = url;
        this.orgid = orgid;
        this.formid = formid;
        this.ltpa = ltpa;
        this.sudirresponse = sudirresponse;
        this.ymdh = ymdh;
    }

    public Integer getId() {
        return id;
    }

    public String getSsoid() {
        return ssoid;
    }

    public Integer getTs() {
        return ts;
    }

    public String getGrp() {
        return grp;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getUrl() {
        return url;
    }

    public String getOrgid() {
        return orgid;
    }

    public String getFormid() {
        return formid;
    }

    public String getLtpa() {
        return ltpa;
    }

    public String getSudirresponse() {
        return sudirresponse;
    }

    public Date getYmdh() {
        return ymdh;
    }
}
