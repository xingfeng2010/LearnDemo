package com.administrator.learndemo.map;

/**
 * Created by Administrator on 2018/1/5.
 */

public class StrategyBean {
    private boolean mCongestion;
    private boolean mCost;
    private boolean mHightspeed;
    private boolean mAvoidhightspeed;

    public StrategyBean(boolean congestion,boolean cost,boolean highspeed,boolean avoidhighspeed){
        setCongestion(congestion);
        setCost(cost);
        setHightspeed(highspeed);
        setAvoidhightspeed(avoidhighspeed);
    }

    public StrategyBean() {

    }

    public boolean isCongestion() {
        return mCongestion;
    }

    public void setCongestion(boolean mCongestion) {
        this.mCongestion = mCongestion;
    }

    public boolean isCost() {
        return mCost;
    }

    public void setCost(boolean mCost) {
        this.mCost = mCost;
    }

    public boolean isHightspeed() {
        return mHightspeed;
    }

    public void setHightspeed(boolean mHightspeed) {
        this.mHightspeed = mHightspeed;
    }

    public boolean isAvoidhightspeed() {
        return mAvoidhightspeed;
    }

    public void setAvoidhightspeed(boolean mAvoidhightspeed) {
        this.mAvoidhightspeed = mAvoidhightspeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StrategyBean that = (StrategyBean) o;

        if (mCongestion != that.mCongestion) return false;
        if (mCost != that.mCost) return false;
        if (mHightspeed != that.mHightspeed) return false;
        return mAvoidhightspeed == that.mAvoidhightspeed;

    }

    @Override
    public int hashCode() {
        int result = (mCongestion ? 1 : 0);
        result = 31 * result + (mCost ? 1 : 0);
        result = 31 * result + (mHightspeed ? 1 : 0);
        result = 31 * result + (mAvoidhightspeed ? 1 : 0);
        return result;
    }
}
