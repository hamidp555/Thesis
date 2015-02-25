package com.cheo.preprocessing;

import com.cheo.base.TextUnitWrapper;
import com.cheo.base.annotations.Comment;
import com.cheo.base.annotations.EDU;
import com.cheo.base.annotations.Order;
import com.cheo.services.SpellChekerService;

@Order(order = 3)
@Comment
@EDU
public class SpellCheckingStrategy implements PreprocessStrategy {

	private SpellChekerService spellChecker;
	
	public void setSpellChecker(SpellChekerService spellChecker) {
		this.spellChecker = spellChecker;
	}
	
	@Override
	public void apply(TextUnitWrapper textUnitWrapper) throws Exception{
		String toprocess = textUnitWrapper.getCleaned();
		String cleaned = spellChecker.spellCheckComment(toprocess);
		textUnitWrapper.setCleaned(cleaned);
	}

}
