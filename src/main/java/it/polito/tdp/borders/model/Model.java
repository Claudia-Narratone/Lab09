package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> grafo;
	private BordersDAO dao;
	private CountryIdMap countryIdMap;
	private List<Country> countries;

	public Model() {
		dao=new BordersDAO();
	}

	public void creaGrafo(int anno) {
		this.grafo=new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
		
		countryIdMap = new CountryIdMap();
		countries = dao.loadAllCountries(countryIdMap);
		
		List<Border> confini=dao.getCountryPairs(countryIdMap, anno);
		
		if(confini.isEmpty()) {
			throw new RuntimeException("No country pairs for specified year");
		}
		
		for(Border b:confini) {
			grafo.addVertex(b.getC1());
			grafo.addVertex(b.getC2());
			grafo.addEdge(b.getC1(), b.getC2());
		}
		
		System.out.println("Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi\n");
		
		countries = new ArrayList<>(grafo.vertexSet());
		Collections.sort(countries);
	}
	
	public List<Country> getCountries(){
		if(countries==null) {
			return new ArrayList<Country>();
		}
		return countries;
	}
	
	public Map<Country, Integer> getCountryCounts(){
		
		Map<Country, Integer> map=new HashMap<Country, Integer>();
		
		for(Country c: grafo.vertexSet()) {
			map.put(c, grafo.degreeOf(c));
		}
		
		return map;
	}
	
	public int getNumberOfConnectedComponents(){
		
		if (grafo == null) {
			throw new RuntimeException("Grafo non esistente");
		}
		
		ConnectivityInspector<Country, DefaultEdge> inspector= new ConnectivityInspector<Country, DefaultEdge>(grafo);
		return inspector.connectedSets().size();
		
	}
	
	public List<Country> getAllNeighboursJGraphT(Country selectedCountry){
		
		List<Country> reachableCountries=new ArrayList<Country>();
		GraphIterator<Country, DefaultEdge> iterator=new DepthFirstIterator<Country, DefaultEdge>(grafo, selectedCountry);
		
		while(iterator.hasNext()) {
			reachableCountries.add(iterator.next());
		}
		return reachableCountries;
	}
	
	public List<Country> getAllNeighboursRecursive(Country selectedCountry){
		List<Country> visitedCountries=new LinkedList<Country>();
		recursiveVisit(selectedCountry, visitedCountries);
		return visitedCountries;
	}

	private void recursiveVisit(Country n, List<Country> visitedCountries) {
		
		visitedCountries.add(n);
		
		for(Country c: Graphs.neighborListOf(grafo, n)) {
			if(!visitedCountries.contains(c))
				recursiveVisit(c, visitedCountries);
		}
	}
}
