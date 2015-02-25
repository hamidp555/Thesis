package com.cheo.junit.preprocessing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.cheo.preprocessing.ApostropheStrategy;
import com.cheo.preprocessing.ElongatedWordStrategy;
import com.cheo.preprocessing.LowerCaseStrategy;
import com.cheo.preprocessing.POSTagStrategy;
import com.cheo.preprocessing.RemoveStrategy;
import com.cheo.preprocessing.SpellCheckingStrategy;
import com.cheo.preprocessing.SplitTokenStrategy;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	LowerCaseStrategy.class,
	ApostropheStrategy.class,
	ElongatedWordStrategy.class,
	SpellCheckingStrategy.class,
	POSTagStrategy.class,
	SplitTokenStrategy.class,
	RemoveStrategy.class
})
public class PreprocessingStategySuite {

}
