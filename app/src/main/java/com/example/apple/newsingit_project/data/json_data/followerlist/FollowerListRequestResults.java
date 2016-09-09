package com.example.apple.newsingit_project.data.json_data.followerlist;

public class FollowerListRequestResults {
    private String name;
    private long id;
    private String pf_url;
    private String aboutme;
    private boolean flag;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPf_url() {
        return this.pf_url;
    }

    public void setPf_url(String pf_url) {
        this.pf_url = pf_url;
    }

    public String getAboutme() {
        return this.aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }
}
