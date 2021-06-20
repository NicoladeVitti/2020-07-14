package it.polito.tdp.PremierLeague.model;

public class MatchWithResult {

	private Team teamCasa;
	private Team teamOspite;
	private Integer result;
	
	public MatchWithResult(Team teamCasa, Team teamOspite, Integer result) {
		super();
		this.teamCasa = teamCasa;
		this.teamOspite = teamOspite;
		this.result = result;
	}

	public Team getTeamCasa() {
		return teamCasa;
	}

	public void setTeamCasa(Team teamCasa) {
		this.teamCasa = teamCasa;
	}

	public Team getTeamOspite() {
		return teamOspite;
	}

	public void setTeamOspite(Team teamOspite) {
		this.teamOspite = teamOspite;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}
	
}
