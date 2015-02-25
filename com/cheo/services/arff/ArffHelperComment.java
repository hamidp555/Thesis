
package com.cheo.services.arff;

import org.springframework.beans.factory.InitializingBean;

import com.cheo.base.enums.ClassLabel;
import com.cheo.model.Comment;

public class ArffHelperComment extends AbstractArffHelper implements InitializingBean{

	private ArffConfigReader reader;

	public void setReader(ArffConfigReader reader) {
		this.reader = reader;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		config = reader.read();
	}
	
	public String getClazz(Comment comment) throws Exception{
		
		if(hasRelIrrelClasses()){
			return comment.isRelevant() ? 
					ClassLabel.RELEVANT.getValue() : 
						ClassLabel.IRRELEVANT.getValue();
		}else{
			return comment.getClassLabel().getValue();
		}

	}
}


