package com.cheo.junit.preprocessing;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cheo.base.TextUnitWrapper;
import com.cheo.preprocessing.ApostropheStrategy;
import com.cheo.preprocessing.LowerCaseStrategy;

public class ApostropheTest extends AbstractPreprocessingTest{

	private String actual = "I'm going to ask about breathing treatments also. It can be Amin's book. I mean really it couldn't hurt";
	
	private String expected = "i am going to ask about breathing treatments also. it can be amin's book. i mean really it could not hurt";
	
	@Test
	public void test() throws Exception {
		
		ApostropheStrategy apostStrategy = (ApostropheStrategy)context.getBean("apost.strategy");
		LowerCaseStrategy lowercaseStrategy = (LowerCaseStrategy)context.getBean("lowercase.strategy");
		TextUnitWrapper cw = new TextUnitWrapper();
		cw.setTextUnit(actual);
		lowercaseStrategy.apply(cw);
		apostStrategy.apply(cw);
		assertEquals(expected, cw.getCleaned());
	}

}
