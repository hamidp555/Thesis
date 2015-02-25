package com.cheo.junit.preprocessing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.cheo.base.CommentWrapper;
import com.cheo.preprocessing.LowerCaseStrategy;
import com.cheo.preprocessing.POSTagStrategy;
import com.cheo.preprocessing.SplitTokenStrategy;

public class SplitTokenTest extends AbstractPreprocessingTest {

	private String actual = "I'm going to see ask about??? simple lol breathing treatments!!! you also@@ wow.";
	
	private String expected = "I'm going to see ask about ??? simple lol breathing treatments !!! you also @@ wow.";
	
	@Test
	public void test() throws Exception{
		SplitTokenStrategy splitTokenStrategy = (SplitTokenStrategy)context.getBean("splitToken.strategy");
		POSTagStrategy posTagStrategy = (POSTagStrategy)context.getBean("posTag.strategy");
		LowerCaseStrategy lowercaseStrategy = (LowerCaseStrategy)context.getBean("lowercase.strategy");
		
		CommentWrapper cw = new CommentWrapper();
		cw.setTextUnit(actual);
		
		lowercaseStrategy.apply(cw);
		posTagStrategy.apply(cw);
		splitTokenStrategy.apply(cw);
		
		String comment = cw.getFilteredCommentFromWords();
		assertEquals(comment, expected);
	}
	
}
