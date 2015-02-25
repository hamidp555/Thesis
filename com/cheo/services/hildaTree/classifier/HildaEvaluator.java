package com.cheo.services.hildaTree.classifier;

import java.util.LinkedList;
import java.util.List;

import com.cheo.base.enums.ClassLabel;
import com.cheo.model.argumentation.State;
import com.cheo.services.hildaTree.RSTRelation;

public class HildaEvaluator {
	
	public static ClassLabel evaluate(RSTRelation operator, ClassLabel left, ClassLabel right) throws Exception{
		
		List<IHildaRule> rules = new LinkedList<IHildaRule>();
		
		rules.add(new HildaContrastRule());
		
		ClassLabel result = null;
		State state = new State();
		for(IHildaRule rule : rules){
			if(rule.isMatch(operator)){
				result = rule.apply(left, right);
				state.nextState();
			}
			if(state.isTwoRulesExecuted()){
				throw new Exception("Rules are mutually exclusive");
			}
			
		}
		
		return result;
	}
	
	
	

}
