package com.cheo.preprocessing;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.RegexUtils;
import com.cheo.base.annotations.Comment;
import com.cheo.base.annotations.EDU;
import com.cheo.base.annotations.Order;


@Order(order = 1)
@Comment
@EDU
public class ApostropheStrategy implements PreprocessStrategy{

	@Override
	public void apply(TextUnitWrapper textUnitWrapper) throws Exception{
		String toprocess = textUnitWrapper.getCleaned();
		String cleaned = RegexUtils.cleanApostrofe(toprocess);
		textUnitWrapper.setCleaned(cleaned);
	}

}
