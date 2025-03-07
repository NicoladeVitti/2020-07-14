/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Team;
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

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnClassifica"
    private Button btnClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbSquadra"
    private ComboBox<Team> cmbSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doClassifica(ActionEvent event) {

    	Team t = this.cmbSquadra.getValue();
    	List<Team> t1 = model.getSquadreSopra(t);
    	
    	txtResult.appendText("SQUADRE BATTUTE:\n");

    	for(Team s : t1) {
    		txtResult.appendText(s.getName()+"\n");
    	}
    	
    	List<Team> t2 = model.getSquadreSotto(t);
    	
    	txtResult.appendText("SQUADRE CHE HA BATTUTO:\n");

    	for(Team s : t2) {
    		txtResult.appendText(s.getName()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	model.creaGrafo();
    	txtResult.appendText("#ARCHI: "+model.getNumArchi());
    	txtResult.appendText("#VERTICI: "+model.getNumVertici()+"\n");
    	
    	this.cmbSquadra.getItems().clear();
    	this.cmbSquadra.getItems().addAll(model.getSquadre());
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	String num = this.txtN.getText();
    	String c = this.txtX.getText();
    	Integer X;
    	Integer N;
    	
    	try {
    		N = Integer.parseInt(num);
    		X = Integer.parseInt(c);
    	}
    	catch(NumberFormatException e) {
    		txtResult.appendText("ERRORE: FORMATO DEL NUMERO DI REPORTER O DELLA SOGLIA NON CORRETTO");
    		return; 
    	}
    	
    	model.init(N,X);
    	model.simula();
    	
		txtResult.appendText("NUMERO MEDIO DI REPORT PER OGNI PARTITA: "+model.getMedia()+"\n");
		txtResult.appendText("NUMERO TOTALE DI PARTITE PER CUI IL NUMERO TOTALE DI REPORTER E' SOTTO LA SOGLIA: "+model.getSottoSoglia()+"\n");

    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnClassifica != null : "fx:id=\"btnClassifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbSquadra != null : "fx:id=\"cmbSquadra\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
