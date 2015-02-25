package com.cheo.base;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class TextUnitWrapper {

	private String textUnit;

	private String cleaned;

	private Map<Integer, List<TokenWrapper>> posMap = new TreeMap<Integer, List<TokenWrapper>>();

	private List<String> posEmoticons  = new LinkedList<String>();

	private List<String> negEmoticons = new LinkedList<String>();

	private Statistics statistics = new Statistics();

	public Statistics getStatistics() {
		return statistics;
	}

	public List<String> getPosEmoticons() {
		return posEmoticons;
	}

	public void setPosEmoticons(List<String> posEmoticons) {
		this.posEmoticons = posEmoticons;
	}

	public List<String> getNegEmoticons() {
		return negEmoticons;
	}

	public void setNegEmoticons(List<String> negEmoticons) {
		this.negEmoticons = negEmoticons;
	}

	public String getTextUnit() {
		return textUnit;
	}

	public void setTextUnit(String textUnit) {
		this.textUnit = textUnit;
	}

	public String getCleaned() {
		return cleaned;
	}

	public void setCleaned(String cleaned) {
		this.cleaned = cleaned;
	}

	public Map<Integer, List<TokenWrapper>> getPosMap() {
		return posMap;
	}

	public void setPosMap(Map<Integer, List<TokenWrapper>> posMap) {
		this.posMap = posMap;
	}


	public class Statistics {
		
		private int totalNumTerms;

		private int numPositiveSWN;
		private int numPositiveGI;
		private int numPositivePL;
		private int numPositiveDepmode;
		private int numPositiveNRC;

		private int numPositiveSLANG;
		private int numPositiveEMOTICON;

		private int numNegativeSWN;
		private int numNegativeGI;
		private int numNegativePL;
		private int numNegativeDepmode;
		private int numNegativeNRC;

		private int numNegativeSLANG;
		private int numNegativeEMOTICON;

		private int numWeakSubjPL;
		private int numStrongSubjPL;

		private int numPunctuation;
		private int numElongatedWords;
		private int numDSLWords;

		public int getNumPositiveEMOTICON() {
			return numPositiveEMOTICON;
		}

		public void incrementNumPositiveEMOTICON() {
			synchronized (this) {
				this.numPositiveEMOTICON++;
			}
		}

		public void setNumPositiveEMOTICON(int count) {
			synchronized (this) {
				this.numPositiveEMOTICON=count;
			}
		}

		public int getNumNegativeEMOTICON() {
			return numNegativeEMOTICON;
		}

		public void incrementNumNegativeEMOTICON() {
			synchronized (this) {
				this.numNegativeEMOTICON++;
			}
		}

		public void setNumNegativeEMOTICON(int count) {
			synchronized (this) {
				this.numNegativeEMOTICON=count;
			}
		}

		public int getNumWeakSubjPL() {
			return numWeakSubjPL;
		}

		public void incrementNumWeakSubjPL() {
			synchronized (this) {
				this.numWeakSubjPL++;
			}
		}

		public int getNumStrongSubjPL() {
			return numStrongSubjPL;
		}

		public void incrementNumStrongSubjPL() {
			synchronized (this) {
				this.numStrongSubjPL++;
			}
		}

		public int getNumPositiveSLANG() {
			return numPositiveSLANG;
		}

		public void incrementNumPositiveSLANG() {
			synchronized (this) {
				this.numPositiveSLANG++;
			}
		}

		public int getNumNegativeSLANG() {
			return numNegativeSLANG;
		}

		public void incrementNumNegativeSLANG() {
			synchronized (this) {
				this.numNegativeSLANG++;
			}
		}

		public int getNumPositiveSWN() {
			return numPositiveSWN;
		}

		public int getNumPositiveGI() {
			return numPositiveGI;
		}

		public int getNumPositivePL() {
			return numPositivePL;
		}

		public int getNumNegativeSWN() {
			return numNegativeSWN;
		}

		public int getNumNegativeGI() {
			return numNegativeGI;
		}

		public int getNumNegativePL() {
			return numNegativePL;
		}

		public int getNumPunctuation() {
			return numPunctuation;
		}

		public int getNumElongatedWords() {
			return numElongatedWords;
		}

		public void incrementNumPositiveSWN() {
			synchronized (this) {
				this.numPositiveSWN++;
			}
		}

		public void incrementNumPositiveGI() {
			synchronized (this) {
				this.numPositiveGI++;
			}
		}

		public void incrementNumPositivePL() {
			synchronized (this) {
				this.numPositivePL++;
			}
		}

		public void incrementNumNegativeSWN() {
			synchronized (this) {
				this.numNegativeSWN++;
			}
		}

		public void incrementNumNegativeGI() {
			synchronized (this) {
				this.numNegativeGI++;
			}
		}

		public void incrementNumNegativePL() {
			synchronized (this) {
				this.numNegativePL++;
			}
		}

		public void incrementNumPunctuation() {
			synchronized (this) {
				this.numPunctuation++;
			}
		}

		public void incrementNumElongatedWords() {
			synchronized (this) {
				this.numElongatedWords++;
			}
		}

		public void setNumElongatedWords(int count) {
			synchronized (this) {
				this.numElongatedWords=count;
			}
		}

		public int getNumDSLWords() {
			return numDSLWords;
		}

		public void incrementNumDSLWords() {
			synchronized (this) {
				this.numDSLWords++;
			}
		}

		public int getNumPositiveDepmode() {
			return numPositiveDepmode;
		}

		public void incrementNumPositiveDepmode() {
			synchronized (this) {
				this.numPositiveDepmode++;
			}
		}

		public int getNumNegativeDepmode() {
			return numNegativeDepmode;
		}

		public void incrementNumNegativeDepmode() {
			synchronized (this) {
				this.numNegativeDepmode++;
			}
		}

		public int getNumPositiveNRC() {
			return numPositiveNRC;
		}

		public int getNumNegativeNRC() {
			return numNegativeNRC;
		}

		public void incrementNumNegativeNRC() {
			synchronized (this) {
				this.numNegativeNRC++;
			}
		}

		public void incrementNumPositiveNRC() {
			synchronized (this) {
				this.numPositiveNRC++;
			}
		}

		//Num terms for normilization later
		public int getTotalNumTerms() {
			return totalNumTerms;
		}

		public void setTotalNumTerms(int totalNumTerms) {
			this.totalNumTerms = totalNumTerms;
		}
		
	}


}
