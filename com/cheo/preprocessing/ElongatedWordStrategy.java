package com.cheo.preprocessing;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.annotations.Comment;
import com.cheo.base.annotations.EDU;
import com.cheo.base.annotations.Order;
import com.cheo.services.feature.ElongatedWordService;


@Order(order = 2)
@Comment
@EDU
public class ElongatedWordStrategy implements PreprocessStrategy {

	private ElongatedWordService service;

	public void setService(ElongatedWordService service) {
		this.service = service;
	}

	@Override
	public void apply(TextUnitWrapper textUnitWrapper) throws Exception{

		String toProcess = textUnitWrapper.getCleaned();
		String cleaned = service.cleanElongatedWords(toProcess);
		textUnitWrapper.setCleaned(cleaned);

	}

}
