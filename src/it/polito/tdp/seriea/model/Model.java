package it.polito.tdp.seriea.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private List<Season> stagioni;
	private Map<String,Team> mapSquadre=new HashMap<String, Team>();
	private List<Team> squadre;
	private SimpleDirectedWeightedGraph<Team,DefaultWeightedEdge> graph;
	private List<Match> partite;
	

	public Model() {
		super();
		dao=new SerieADAO();
	}
	
	
	public List<Season >getSeasons(){
		if(stagioni==null){
			stagioni=dao.listSeasons();
		
		}
		
		return stagioni;
	}
	
	
	
	public void creaGrafo(Season stagione) {
		graph=new SimpleDirectedWeightedGraph<Team,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.partite=null;
		
		//aggiungi vertici
		Graphs.addAllVertices(graph,this.getAllTeams());
		
		
		
		//aggiungi archi
		for(Match mtemp: this.getAllPartite(stagione)){
			Team home=mtemp.getHomeTeam();
			Team away=mtemp.getAwayTeam();
			String ris=mtemp.getFtr();
			int peso;
			if(ris.compareTo("H")==0){
				peso=1;
			}
			else{
				if(ris.compareTo("A")==0)
					peso=-1;
				else
					peso=0;
			}
			
			
			DefaultWeightedEdge e = graph.addEdge(home,away);
			graph.setEdgeWeight(e, peso);
			
			
			
		}
		
		Set<Team> elimina=new HashSet<Team>();
		
		for(Team ttemp: graph.vertexSet()){
			if(Graphs.neighborListOf(graph, ttemp).size()==0)
				elimina.add(ttemp);
		}
		
		graph.removeAllVertices(elimina);
		
		}


	private List<Team>  getAllTeams() {
		if(squadre==null){
			squadre=dao.listTeams();
			for(Team ttemp: squadre){
				mapSquadre.put(ttemp.getTeam(),ttemp);
			}
				
		}
		
		return squadre;
	}
	
	private List<Match> getAllPartite(Season stagione){
		if(partite==null)
			partite= dao.getPartiteBySeason(stagione,mapSquadre);
		
		return partite;
	}
	
	
	public Map<String,ModelStat> getClassifica(Season stagione){
		Map<String,ModelStat> stats=new TreeMap<String,ModelStat>();
		this.creaGrafo(stagione);
		for(Team ttemp: graph.vertexSet()){
			for(DefaultWeightedEdge e:graph.outgoingEdgesOf(ttemp)){
				int punteggio;
				int risultato= (int) graph.getEdgeWeight(e);
				
				if(risultato==1){
					punteggio=3;
				}else{
					if(risultato==0)
						punteggio=1;
					else
						punteggio=0;
				}
				
				if(stats.get(ttemp.getTeam())==null){
					
				stats.put(ttemp.getTeam(),new ModelStat(ttemp,punteggio));
				}
				else{
					ModelStat old=stats.get(ttemp.getTeam());
					int oldP=old.getPunteggio();
					ModelStat nuovo =new ModelStat(ttemp,punteggio+oldP);
				
					stats.replace(ttemp.getTeam(), old, nuovo);
				
				}
				
			}
			
			
			for(DefaultWeightedEdge e:graph.incomingEdgesOf(ttemp)){
				int punteggio;
				int risultato= (int) graph.getEdgeWeight(e);
				
				if(risultato==1){
					punteggio=0;
				}else{
					if(risultato==0)
						punteggio=1;
					else
						punteggio=3;
				}
				
				if(stats.get(ttemp.getTeam())==null){
					
				stats.put(ttemp.getTeam(),new ModelStat(ttemp,punteggio));
				}
				else{
					ModelStat old=stats.get(ttemp.getTeam());
					int oldP=old.getPunteggio();
					ModelStat nuovo =new ModelStat(ttemp,punteggio+oldP);
				
					stats.replace(ttemp.getTeam(), old, nuovo);
				
				}
				
			}
		}
		
		return stats;
	}
	
	
	public List<DefaultWeightedEdge> trovaSequenza(){
		List<DefaultWeightedEdge> best=new ArrayList<DefaultWeightedEdge> ();
		
		for( Team squadra: graph.vertexSet()){
		List<DefaultWeightedEdge> parziale=new ArrayList<DefaultWeightedEdge>();
		
		
	
		
		scegli(parziale,0,best,squadra);
		
		
		
		}
		return best;
	}
	
	

	private void scegli(List<DefaultWeightedEdge> parziale, int livello, List<DefaultWeightedEdge> best, Team squadra) {
		
		if(parziale.size()<best.size()){
				best.clear();
				best.addAll(parziale);
		}else{
				for(DefaultWeightedEdge e: graph.outgoingEdgesOf(squadra)){
				if(graph.getEdgeWeight(e)==1){
					if(!parziale.contains(e)){
						parziale.add(e);
						
						
						scegli(parziale,livello+1,best,graph.getEdgeTarget(e));
						
						parziale.remove(e);
					}
				
				}
				}

		}
	}


}

			
