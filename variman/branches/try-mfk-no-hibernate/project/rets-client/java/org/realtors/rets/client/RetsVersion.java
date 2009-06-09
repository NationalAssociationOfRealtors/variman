package org.realtors.rets.client;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class RetsVersion implements Serializable {
	public static final String RETS_VERSION_HEADER = "RETS-Version";

	public static final RetsVersion RETS_1_0 = new RetsVersion(1, 0, 0);
	public static final RetsVersion RETS_1_5 = new RetsVersion(1, 5, 0);
	public static final RetsVersion RETS_1_6 = new RetsVersion(1, 6, 0);
	public static final RetsVersion RETS_1_7 = new RetsVersion(1, 7, 0);
	public static final RetsVersion RETS_1_7_2 = new RetsVersion(1, 7, 2);
	public static final RetsVersion DEFAULT = RETS_1_5;

	private int mMajor;
	private int mMinor;
	private int mDraft;

	public RetsVersion(int major, int minor) {
		this(major,minor,0);
	}
	public RetsVersion(int major, int minor, int draft) {
		this.mMajor = major;
		this.mMinor = minor;
		this.mDraft = draft;
	}

	public int getMajor() {
		return this.mMajor;
	}

	public int getMinor() {
		return this.mMinor;
	}

	public int getDraft() {
		return this.mDraft;
	}

	@Override
	public String toString() {
		if (this.mDraft == 0) {
			return "RETS/" + this.mMajor + "." + this.mMinor;
		}
		return "RETS/" + this.mMajor + "." + this.mMinor + "." + this.mDraft;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof RetsVersion) {
			RetsVersion v = (RetsVersion) o;
			if ((v.getMajor() == this.mMajor) && (v.getMinor() == this.mMinor) && (v.getDraft() == this.mDraft)) {
				return true;
			}
		}
		return false;
	}

	public boolean notEquals(Object o) {
		if (o instanceof RetsVersion) {
			RetsVersion v = (RetsVersion) o;
			if ((v.getMajor() == this.mMajor) && (v.getMinor() == this.mMinor) && (v.getDraft() == this.mDraft)) {
				return false;
			}
		}
		return true;
	}
	
	public static RetsVersion parse(String ver) {
		if( StringUtils.isEmpty(ver) ) return null;
		String[] split = StringUtils.trimToEmpty(ver).split("\\.");
		int ma = NumberUtils.toInt(split[0],1);
		int mn = split.length > 1 ? NumberUtils.toInt(split[1],0) : 0; 
		int dr = split.length > 2 ? NumberUtils.toInt(split[2],0) : 0;
		return new RetsVersion(ma,mn,dr);
	}

}
