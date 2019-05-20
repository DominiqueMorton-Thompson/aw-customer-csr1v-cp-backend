package com.amwater.csr1v.data.criteria;

import com.apporchid.foundation.criteria.ICriteria;

public abstract class Criteria {
	public abstract String getUrl();
	
	public abstract ICriteria<?> getCriteria();
}
