package com.cheo.preprocessing;

import org.apache.commons.lang3.StringUtils;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.annotations.Comment;
import com.cheo.base.annotations.EDU;
import com.cheo.base.annotations.Order;


@Order(order = 0)
@Comment
@EDU
public class LowerCaseStrategy implements PreprocessStrategy {

	@Override
	public void apply(TextUnitWrapper textUnitWrapper) throws Exception {
		String toprocess = textUnitWrapper.getTextUnit();
		String cleaned =StringUtils.lowerCase(toprocess);
		textUnitWrapper.setCleaned(cleaned);
	}

}
