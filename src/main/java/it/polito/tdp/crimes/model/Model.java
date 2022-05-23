package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private Graph<String, DefaultWeightedEdge> grafo;

	private EventsDao dao;

	private List<String> best;

	public Model() {
		dao = new EventsDao();
	}

	public void creaGrafo(String categoria, int mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		Graphs.addAllVertices(grafo, dao.getVertici(categoria, mese));
		for (Adiacenza a : dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(grafo, a.getV1(), a.getV2(), a.getPeso());
		}
//		System.out.println("n vert: " + grafo.vertexSet().size());
//		System.out.println("n archi: " + grafo.edgeSet().size());
	}

	public List<Adiacenza> getArchiMaggiorPesoMedio() {
		double pesoTot = 0;
		List<Adiacenza> result = new ArrayList<>();
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			pesoTot += grafo.getEdgeWeight(e);
		}
		double avg = pesoTot / grafo.edgeSet().size();
		System.out.println(avg);
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (grafo.getEdgeWeight(e) > avg) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e),
						(int) this.grafo.getEdgeWeight(e)));
			}
		}
		return result;
	}

	public List<String> calcolaPercorso(String sorgente, String destinazione) {
		best = new LinkedList<String>();
		List<String> parziale = new LinkedList<>();
		parziale.add(sorgente);
		cerca(parziale, destinazione);
		return best;
	}

	private void cerca(List<String> parziale, String destinazione) {
		if (parziale.get(parziale.size() - 1).equals(destinazione)) {
			if (parziale.size() > best.size()) {
				best = new LinkedList<>(parziale);
			}
			return;
		}

		for (String v : Graphs.neighborListOf(grafo, parziale.get(parziale.size() - 1))) {
			if (!parziale.contains(v)) {
				parziale.add(v);
				cerca(parziale, destinazione);
				parziale.remove(parziale.size() - 1);
			}
		}
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public List<Adiacenza> getArchi() {
		List<Adiacenza> archi = new ArrayList<Adiacenza>();
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			archi.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e),
					(int) this.grafo.getEdgeWeight(e)));
		}
		return archi;
	}

	public List<String> getCategorie() {
		return this.dao.getCategorie();
	}
}
