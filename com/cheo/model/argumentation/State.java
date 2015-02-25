package com.cheo.model.argumentation;

public class State{
	private boolean oneRuleExecuted = false;
	private boolean twoRulesExecuted = false;
	
	public void nextState(){
		if(!oneRuleExecuted){
			oneRuleExecuted=true;
		}else{
			twoRulesExecuted=true;
		}		
	}
	public boolean isOneRuleExecuted() {
		return oneRuleExecuted;
	}
	public void setOneRuleExecuted(boolean oneRuleExecuted) {
		this.oneRuleExecuted = oneRuleExecuted;
	}
	public boolean isTwoRulesExecuted() {
		return twoRulesExecuted;
	}
	public void setTwoRulesExecuted(boolean twoRulesExecuted) {
		this.twoRulesExecuted = twoRulesExecuted;
	}
}
