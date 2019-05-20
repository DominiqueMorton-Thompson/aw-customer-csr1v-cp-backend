package com.amwater.csr1v.data.resolver;

import org.springframework.stereotype.Component;

import com.amwater.csr1v.data.context.Context;

@Component
public interface BaseResolver {

	  void getData(Context context,String url);
}
