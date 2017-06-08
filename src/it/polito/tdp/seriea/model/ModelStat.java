package it.polito.tdp.seriea.model;

public class ModelStat implements Comparable<ModelStat>{
	
	private Team team;
	private int punteggio;
	
	
	
	
	
	
	public ModelStat(Team team, int punteggio) {
		super();
		this.team = team;
		this.punteggio = punteggio;
	}






	public Team getTeam() {
		return team;
	}






	public void setTeam(Team team) {
		this.team = team;
	}






	public int getPunteggio() {
		return punteggio;
	}






	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}






	@Override
	public int compareTo(ModelStat other) {
		// TODO Auto-generated method stub
		return -(this.getPunteggio()-other.getPunteggio());
	}
	
	

}
