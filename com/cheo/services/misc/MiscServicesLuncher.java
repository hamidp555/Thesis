package com.cheo.services.misc;

public class MiscServicesLuncher {
	
	public static void main(String[] args) throws Exception {
		String location = System.getProperty("bib.location");
		SortBibService sortBib = new SortBibService();
		sortBib.sortBib(location);
	}

}
