package com.cheo.services.arff;

import java.util.List;

import com.cheo.base.enums.ClassLabel;

public interface ArffHelper {

	boolean hasRelIrrelClasses();

	List<ClassLabel> getClassLabels();

	boolean containsClazz(ClassLabel clazz);

	boolean isNormilized() ;

	ArffConfig getConfig();

}
