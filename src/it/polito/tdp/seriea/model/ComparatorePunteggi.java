package it.polito.tdp.seriea.model;

import java.util.Comparator;

public class ComparatorePunteggi implements Comparator<ModelStat> {

	

	@Override
	public int compare(ModelStat o1, ModelStat o2) {
	
		return -(o1.getPunteggio()-o2.getPunteggio());
	}

}
