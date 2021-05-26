package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	
	//MODELLO -> stato del sistema a ogni passo
	private Graph<Country, DefaultEdge> grafo;
	
	//Tipi di evento -> coda prioritaria
	private PriorityQueue<Evento> queue;
	
	//Parametri simulazione
	private int N_MIGRANTI = 1000;
	private Country partenza;
	
	//Valori in output
	private int T = -1;
	private Map<Country, Integer> stanziali;
	
	public void init(Country country, Graph<Country, DefaultEdge> graph) {
		this.partenza = country;
		this.grafo = graph;
		
		this.T = 1;
		this.stanziali = new HashMap<>();
		for(Country c: this.grafo.vertexSet()) {
			stanziali.put(c, 0);
			
		this.queue = new PriorityQueue<Evento>();
		
		this.queue.add(new Evento(T, partenza, this.N_MIGRANTI));
		}
	}
	
	public void run() {
		Evento e;
		
		while((e = this.queue.poll()) != null) {
			this.T = e.getT();
			int nPersone = e.getN();
			Country stato = e.getCountry();
			
			List<Country> vicini = Graphs.neighborListOf(grafo, stato);
			int migrantiPerStato = (nPersone/2)/vicini.size();// troncamento per difetto
			
			if(migrantiPerStato > 0) {
				for(Country confinante: vicini) {
					this.queue.add(new Evento(e.getT()+1, confinante, migrantiPerStato));
				}
			}else {
				//il n° di persone < n° di stati
			}
			
			int stanziali = nPersone - migrantiPerStato*vicini.size();
			this.stanziali.put(stato, this.stanziali.get(stato) + stanziali); //perchè le persone possono ritornare sullo stesso stato
		}
	}
	
	public Map<Country, Integer> getStanziali(){
		return this.stanziali;
	}
	
	public Integer getT() {
		return this.T;
	}
}
