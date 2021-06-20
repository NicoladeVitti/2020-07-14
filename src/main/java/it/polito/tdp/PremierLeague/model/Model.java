package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Map<Integer, Team> idMap;
	private Graph<Team, DefaultWeightedEdge> grafo;
	private Map<Team, Integer> puntiSquadra;
	
	//SIMULAZIONE
	private PriorityQueue<Match> queue;
	private Integer N;
	private Integer X;
	private Map<Team, Integer> reportPerTeam;
	private Integer somma;
	private Integer media;
	private Integer sottoSoglia;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
	}
	
	public void creaGrafo() {
		grafo = new SimpleDirectedWeightedGraph(DefaultWeightedEdge.class);
		dao.listAllTeams(idMap);
		
		//AGGIUNGO VERTICI 
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//AGGIUNGO GLI ARCHI 
		List<MatchWithResult> l = dao.getMatchWithResult(idMap);
		puntiSquadra = new HashMap<>();
		for(Team t : idMap.values()) {
			puntiSquadra.put(t, 0);
		}
		
		for(MatchWithResult m : l) {
			
				if(m.getResult() == 1) {
					puntiSquadra.put(m.getTeamCasa(), puntiSquadra.get(m.getTeamCasa()) +3);
				}
				else if(m.getResult() == (-1)) {
					puntiSquadra.put(m.getTeamOspite(), puntiSquadra.get(m.getTeamOspite()) +3);
				}
				else if(m.getResult() == 0) {
					puntiSquadra.put(m.getTeamCasa(), puntiSquadra.get(m.getTeamCasa()) +1);
					puntiSquadra.put(m.getTeamOspite(), puntiSquadra.get(m.getTeamOspite()) +1);

				}
			
			
		}
		
		
		
		for(Team t1 : grafo.vertexSet()) {
			for(Team t2 : grafo.vertexSet()) {
				if(!t1.equals(t2)) {
					
					if(puntiSquadra.get(t1) > puntiSquadra.get(t2)) {
						Graphs.addEdgeWithVertices(grafo, t1, t2, (puntiSquadra.get(t1)-puntiSquadra.get(t2)));
					}
					
				}
			}
		}
	}
	
	public Integer getNumVertici(){
		return grafo.vertexSet().size();
	}
	
	public Integer getNumArchi(){
		return grafo.edgeSet().size();
	}

	public Set<Team> getSquadre() {
		
		return grafo.vertexSet();
	}
	
	public List<Team> getSquadreSotto(Team t){
		List<Team> result = new LinkedList<>();
		
		for(Team team : this.puntiSquadra.keySet()) {
			if(this.puntiSquadra.get(t) > this.puntiSquadra.get(team)) {
				result.add(team);
			}
		}
		
		class Comparatore implements Comparator<Team>{

			@Override
			public int compare(Team o1, Team o2) {
				return -(puntiSquadra.get(o1) - puntiSquadra.get(o2));
			}
			
		}
		
		Collections.sort(result, new Comparatore());
		return result;
	}
	
	public List<Team> getSquadreSopra(Team t){
		
		List<Team> result = new LinkedList<>();
		
		for(Team team : this.puntiSquadra.keySet()) {
			if(this.puntiSquadra.get(t) < this.puntiSquadra.get(team)) {
				result.add(team);
			}
		}
		
		class Comparatore implements Comparator<Team>{

			@Override
			public int compare(Team o1, Team o2) {
				return puntiSquadra.get(o1) - puntiSquadra.get(o2);
			}
			
		}
		
		Collections.sort(result, new Comparatore());
		return result;
	}

	public void init(Integer n, Integer x) {
		
		this.queue = new PriorityQueue<>();
		this.reportPerTeam = new HashMap<>();
		this.N = n;
		this.X = x;
		somma = 0;
		media = 0;
		sottoSoglia = 0;
		
		for(Team t : this.idMap.values()) {
			this.reportPerTeam.put(t, N);
		}
		
		for(Match m : dao.listAllMatches()) {
			this.queue.add(m);
		}
		
	}

	public void simula() {
		
		while(!queue.isEmpty()) {
			Match m = queue.poll();
			processEvent(m);
		}
		
	}

	private void processEvent(Match m) {
		
		somma += (this.reportPerTeam.get(idMap.get(m.getTeamHomeID()))) +(this.reportPerTeam.get(idMap.get(m.getTeamAwayID())));
		if((this.reportPerTeam.get(idMap.get(m.getTeamHomeID()))) +(this.reportPerTeam.get(idMap.get(m.getTeamAwayID()))) < X) {
			this.sottoSoglia++;
		}
		
		if(m.resultOfTeamHome == 0) {
			return;
		}
		else {
			Team squadraVincente = null;
			Team squadraPerdente = null;
			
			if(m.resultOfTeamHome == 1) {
				squadraVincente = this.idMap.get(m.teamHomeID);
				squadraPerdente = this.idMap.get(m.getTeamAwayID());
			}
			else if(m.resultOfTeamHome == (-1)) {
				squadraPerdente = this.idMap.get(m.teamHomeID);
				squadraVincente = this.idMap.get(m.getTeamAwayID());
			}
			
			
			Double p = Math.random();
			
			if(p<=0.5) {
				if(this.reportPerTeam.get(squadraVincente) != 0 && this.getSquadreSopra(squadraVincente).size() != 0) {
					
					this.reportPerTeam.put(squadraVincente, this.reportPerTeam.get(squadraVincente)-1);
					
					Integer casuale = new Random().nextInt(this.getSquadreSopra(squadraVincente).size());
					this.reportPerTeam.put(this.getSquadreSopra(squadraVincente).get(casuale), this.reportPerTeam.get(this.getSquadreSopra(squadraVincente).get(casuale))+1);
				}
			}
			
			
			Double p2 = Math.random();
			
			if(p2 <= 0.2) {
				if(this.reportPerTeam.get(squadraPerdente) != 0 && this.getSquadreSotto(squadraPerdente).size() != 0) {
					
					int reporterTrasferiti = (int) (Math.random() * (this.reportPerTeam.get(squadraPerdente))-1) +1;
					
					this.reportPerTeam.put(squadraPerdente, this.reportPerTeam.get(squadraPerdente)-reporterTrasferiti);
					
					Integer casuale = new Random().nextInt(this.getSquadreSotto(squadraPerdente).size());
					this.reportPerTeam.put(this.getSquadreSotto(squadraPerdente).get(casuale), this.reportPerTeam.get(this.getSquadreSotto(squadraPerdente).get(casuale))+reporterTrasferiti);
	
				}

			}
		}
	}
	
	public Integer getMedia() {
		return somma/(dao.listAllMatches().size());
	}
	
	public Integer getSottoSoglia() {
		return this.sottoSoglia;
	}
	
	
}
