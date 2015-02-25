package com.cheo.services;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cheo.base.CommentWrapper;
import com.cheo.base.TextUnitWrapper;
import com.cheo.base.annotations.Comment;
import com.cheo.base.annotations.Order;
import com.cheo.preprocessing.PreprocessStrategy;

public class PreprocessorServiceComments implements PreprocessorService, ApplicationContextAware {


	private ApplicationContext context; 

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;

	}
	
	@Override
	public TextUnitWrapper preprocess(String text) throws Exception {

		Map<Integer, PreprocessStrategy> orderedBeans = new TreeMap<Integer, PreprocessStrategy>();
		Map<String,PreprocessStrategy> beans = context.getBeansOfType(PreprocessStrategy.class);

		for(Entry<String,PreprocessStrategy> entry : beans.entrySet()){
			PreprocessStrategy bean = entry.getValue();
	
			Class<? extends PreprocessStrategy> clazz = bean.getClass();
			if(clazz.isAnnotationPresent(Order.class) &&
					clazz.isAnnotationPresent(Comment.class) ){
				Annotation annot = clazz.getAnnotation(Order.class);
				Order order = (Order)annot;
				orderedBeans.put(order.order(), bean);
			}
		}

		TextUnitWrapper textUnitWrapper = new CommentWrapper();
		textUnitWrapper.setTextUnit(text);
		for(Entry<Integer, PreprocessStrategy> entry : orderedBeans.entrySet()){	
			entry.getValue().apply(textUnitWrapper);	
		}
		return textUnitWrapper;
	}


}
