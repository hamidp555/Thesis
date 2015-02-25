package com.cheo.model.argumentation;

import java.util.LinkedList;
import java.util.List;

import com.cheo.model.Argument;
import com.cheo.model.EDU;

public class RuleEvaluator {

	public static Argument evaluate(List<EDU> edus, int index) throws Exception{

		List<IRule> rules = new LinkedList<IRule>();
		
		rules.add(new RuleSizeOne(edus, index));
		rules.add(new RuleSizeTwoIndexOne(edus, index));
		rules.add(new RuleSizeTwoIndexZero(edus, index));
		rules.add(new RuleSizeThree(edus, index));
		rules.add(new RuleSizeThreePlusIndexLast(edus, index));
		rules.add(new RuleSizeThreePlusIndexOne(edus, index));
		rules.add(new RuleSizeThreePlusIndexOneToLast(edus, index));
		rules.add(new RuleSizeThreePlusIndexZero(edus, index));
		rules.add(new RuleSizeThreePlus(edus, index));


		//To ensure rules are mutually exclusive
		State state = new State();
		Argument arg = null;
		for(IRule rule : rules){
			if(rule.isMatch()){
				arg =rule.execute();
				state.nextState();
			}
			if(state.isTwoRulesExecuted()){
				throw new Exception("Rules are mutually exclusive");
			}
		}
		return arg;
	}
	
}
