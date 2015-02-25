package com.cheo.junit.preprocessing;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cheo.base.TextUnitWrapper;
import com.cheo.preprocessing.LowerCaseStrategy;
import com.cheo.preprocessing.SpellCheckingStrategy;

public class SpelCheckingTest extends AbstractPreprocessingTest {

	private String expected = "it simply wouldn't be reasonable to expect all parents or guardians to be fully versed on each and every one of these conditions to enable them to give informed consent, therefore some tests should be carried out as standard and procedures put in place to anonymize personally identifiable data so it can be used for research purposes.";
	private String actual = "It simply wouldn't be reaonable to expedct all parents or guardians to be fully versed on each and every one of these conditions to enable them to give infrmed consent, therfore some tests should be carried out as standard and procedures put in place to anonymize personally identifiable data so it can be used for research purposes.";
	
	@Test
	public void test() throws Exception{
		SpellCheckingStrategy spellCheckStrategy = (SpellCheckingStrategy)context.getBean("spellCheck.strategy");
		LowerCaseStrategy lowercaseStrategy = (LowerCaseStrategy)context.getBean("lowercase.strategy");
		TextUnitWrapper cw = new TextUnitWrapper();
		cw.setTextUnit(actual);
		lowercaseStrategy.apply(cw);
		spellCheckStrategy.apply(cw);
		assertEquals(expected, cw.getCleaned());
	}

}
