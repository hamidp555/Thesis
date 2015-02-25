package com.cheo.junit.feature;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	PLTest.class, 
	SWNTest.class, 
	DepechemodeTest.class })
public class TestSuite {

}
