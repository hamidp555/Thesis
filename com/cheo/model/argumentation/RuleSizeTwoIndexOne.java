package com.cheo.model.argumentation;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cheo.base.enums.Stance;
import com.cheo.model.Argument;
import com.cheo.model.EDU;
import com.cheo.model.Topic;

public class RuleSizeTwoIndexOne implements IRule {

	private List<EDU> edus;
	private int index;
	
	public RuleSizeTwoIndexOne(List<EDU> edus, int index){
		this.edus=edus;
		this.index = index;
	}

	@Override
	public boolean isMatch() {
		if(edus.size() == 2 && index == 1){
			EDU edu = edus.get(index);
			Stance stance = edu.getStance();
			String topic = edu.getTopic();
			boolean withTopicWithoutStace = !StringUtils.isBlank(topic) && stance.equals(Stance.NONE);
			boolean withTopicWithStance = !StringUtils.isBlank(topic) && !stance.equals(Stance.NONE);
			return withTopicWithoutStace || withTopicWithStance;
		}
		return false;
	}

	@Override
	public Argument execute() throws Exception {
		if(!isMatch()){
			throw new Exception(getClass().getName() + " is not a match and cannot be executed!");
		}
		Stance stance = edus.get(index).getStance();
		String topic = edus.get(index).getTopic();

		Topic tpc = new Topic();
		tpc.setStance(stance);
		tpc.setTopic(topic);
		tpc.setEdu(edus.get(index));
		
		EDU oneBefore = edus.get(index-1);
		
		Argument arg = new Argument();
		arg.setTopic(tpc);
		arg.getBefor().add(oneBefore);
		
		return arg;
	}

}
