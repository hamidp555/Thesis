package com.cheo.model.argumentation;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cheo.base.enums.Stance;
import com.cheo.model.Argument;
import com.cheo.model.EDU;
import com.cheo.model.Topic;

public class RuleSizeThreePlusIndexOne implements IRule{

	private List<EDU> edus;
	private int index;

	public RuleSizeThreePlusIndexOne(List<EDU> edus, int index){
		this.edus=edus;
		this.index = index;
	}

	@Override
	public boolean isMatch() {
		if(edus.size() > 3 && index == 1){
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
		EDU oneAfter = edus.get(index+1);
		EDU twoAfter = edus.get(index+2);
		
		boolean oneBeforeHasStance = !oneBefore.getStance().equals(Stance.NONE);
		boolean oneAfterHasStance = !oneAfter.getStance().equals(Stance.NONE);
		boolean twoAfterHasStance = !twoAfter.getStance().equals(Stance.NONE);

		Argument arg = new Argument();
		arg.setTopic(tpc);
		//Before
		if(oneBeforeHasStance){
			//do nothing
		}
		else{
			arg.getBefor().add(oneBefore);
		}
		//After
		if(oneAfterHasStance){
			//do nothing
		} 
		else if(twoAfterHasStance){
			arg.getAfter().add(oneAfter);
		}
		else{
			arg.getAfter().add(oneAfter);
			arg.getAfter().add(twoAfter);
		}
		return arg;
	}
}
