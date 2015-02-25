package com.cheo.junit.preprocessing;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cheo.base.TextUnitWrapper;
import com.cheo.preprocessing.ElongatedWordStrategy;
import com.cheo.preprocessing.LowerCaseStrategy;

public class ElongatedTWordTest extends AbstractPreprocessingTest {

	private String actual = "I'm going to seeeeee ask about siiiimple looool breathing treatments youuuu also woow.";
	
	private String expected = "I'm going to see ask about simple lol breathing treatments you also wow.";
	
	@Test
	public void test() throws Exception{
		ElongatedWordStrategy elongStrategy = (ElongatedWordStrategy)context.getBean("elong.strategy");
		LowerCaseStrategy lowercaseStrategy = (LowerCaseStrategy)context.getBean("lowercase.strategy");
		TextUnitWrapper cw = new TextUnitWrapper();
		cw.setTextUnit(actual);
		lowercaseStrategy.apply(cw);
		elongStrategy.apply(cw);
		assertEquals(expected, cw.getCleaned());
	}

}
