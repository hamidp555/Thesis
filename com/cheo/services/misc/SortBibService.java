package com.cheo.services.misc;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class SortBibService {

	
	private void removeOldFile(String location) throws IOException{
		String folderPath = StringUtils.substringBeforeLast(location, "/");
		File folder = new File(folderPath);
		File[] files = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".txt") && fileName.equalsIgnoreCase("bibThesis_sorted.txt");
		    }
		});
		//Remove the file if it exists
		Assert.isTrue(files.length < 2);
		if(files.length == 1 )
			FileUtils.forceDelete(files[0]);
	}
	
	public void sortBib(String location) throws IOException{
		@SuppressWarnings("unchecked")
		List<String> authours = FileUtils.readLines(new File(location));
		Iterator<String> iter = authours.iterator();
		while(iter.hasNext()){
			if(StringUtils.isBlank(iter.next())){
				iter.remove();
			}
		}
		List<String> authorsNewLine = new LinkedList<String>();
		Collections.sort(authours); 
		for(String author: authours){
			authorsNewLine.add(author);
			authorsNewLine.add(StringUtils.EMPTY);
		}
		String location_sorted = StringUtils.substringBefore(location, ".") + "_sorted.txt";
		File file = new File(location_sorted);
		removeOldFile(location);
		FileUtils.writeLines(file, authorsNewLine, "\n");
	}
}
