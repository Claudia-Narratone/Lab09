
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    private ComboBox<Country> cmbBoxStati;

    @FXML
    private Button btnCalcolaVicini;

    @FXML
    private TextArea txtResultVicini;


    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	
    	txtResult.clear();

		int anno;

		try {
			anno = Integer.parseInt(txtAnno.getText());
			if ((anno < 1816) || (anno > 2016)) {
				txtResult.setText("Inserire un anno nell'intervallo 1816 - 2016");
				return;
			}
			
		} catch (NumberFormatException e) {
			txtResult.setText("Inserire un anno nell'intervallo 1816 - 2016");
			return;
		}

		try {
			model.creaGrafo(anno);
			List<Country> countries = model.getCountries();
			
			cmbBoxStati.getItems().addAll(countries);
			
			txtResult.appendText(String.format("Numero componenti connesse: %d\n", model.getNumberOfConnectedComponents()));

			Map<Country, Integer> stats = model.getCountryCounts();
			for (Country country : stats.keySet())
				txtResult.appendText(String.format("%s %d\n", country, stats.get(country)));

		} catch (RuntimeException e) {
			txtResult.setText("Errore: " + e.getMessage() + "\n");
			return;
		}
    	
    }
    
    @FXML
    void doCalcolaVicini(ActionEvent event) {
    	txtResultVicini.clear();
    	
    	if (cmbBoxStati.getItems().isEmpty()) {
			txtResultVicini.setText("Graph is empty. Create a graph, or select another year.");
		}
    	
    	Country selectedCountry = cmbBoxStati.getSelectionModel().getSelectedItem();
		if (selectedCountry == null) {
			txtResultVicini.setText("Select a country first.");
		}
		try {
			List<Country> reachableCountries = model.getAllNeighboursJGraphT(selectedCountry);
			for (Country country : reachableCountries) {
				txtResultVicini.appendText(String.format("%s\n", country));
			}
		} catch (RuntimeException e) {
			// If the countries are inserted in the ComboBox when the graph is created,
			// this should never happen.
			txtResultVicini.setText("Selected country is not in the graph.");
		}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbBoxStati != null : "fx:id=\"cmbBoxStati\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaVicini != null : "fx:id=\"btnCalcolaVicini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResultVicini != null : "fx:id=\"txtResultVicini\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
