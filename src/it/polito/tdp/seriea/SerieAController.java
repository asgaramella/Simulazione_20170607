/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.seriea.model.ComparatorePunteggi;
import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.ModelStat;
import it.polito.tdp.seriea.model.Season;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {
	
	Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSeason"
    private ChoiceBox<Season> boxSeason; // Value injected by FXMLLoader

    @FXML // fx:id="boxTeam"
    private ChoiceBox<?> boxTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void handleCarica(ActionEvent event) {
    	txtResult.clear();
    	Season stagione= this.boxSeason.getValue();
    	List<ModelStat> ltemp= new ArrayList(  model.getClassifica(stagione).values());
    	Collections.sort(ltemp, new ComparatorePunteggi());
    	
    	
    	for(ModelStat mstemp: ltemp){
    		
    		txtResult.appendText(mstemp.getTeam().toString()+ " " +Integer.toString(mstemp.getPunteggio())+"\n");
    		
    		
    	}
    	
    }

    @FXML
    void handleDomino(ActionEvent event) {
    	txtResult.clear();
    	
    	for(DefaultWeightedEdge e : model.trovaSequenza()){
    		txtResult.appendText(e.toString()+" ");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSeason != null : "fx:id=\"boxSeason\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxTeam != null : "fx:id=\"boxTeam\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";
    }

	public void setModel(Model model) {
		this.model=model;
		this.boxSeason.getItems().addAll(model.getSeasons());
		this.boxSeason.setValue(this.boxSeason.getItems().get(0));

		
		
	}
}
