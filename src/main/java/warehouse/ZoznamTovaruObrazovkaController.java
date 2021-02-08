package warehouse;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import warehouse.storage.Umiestnenie;

public class ZoznamTovaruObrazovkaController {
	
	@FXML
	private Label nadpisLabel;

	@FXML
	private ListView<String> umiestneniaListView;

	@FXML
	private Button tlacitZoznamButton;

	@FXML
	private Button zrusitButton;

	private String nadpis;
	private List<Umiestnenie> umiestnenia;

	public ZoznamTovaruObrazovkaController(String nadpis, List<Umiestnenie> umiestnenia) {
		this.nadpis = nadpis;
		this.umiestnenia = umiestnenia;
	}

	@FXML
	void initialize() {
		nadpisLabel.setText(nadpis);
		nadpisLabel.setStyle("-fx-text-alignment: center");

		List<String> text = new ArrayList<String>();
		for (Umiestnenie u : umiestnenia) {
			text.add("#" + u.getId() + " " + u.getDruh().getNazov() + " " + u.getMnozstvo() + " "
					+ u.getDruh().getMernaJednotka().getOznacenie() + " "
					+ u.getSklad().getNazov() + " " + u.getRegal() + " " + u.getPolicka());
		}
		umiestneniaListView.setItems(FXCollections.observableArrayList(text));
	}

	@FXML
	void tlacitZoznamButtonClick(ActionEvent event) {
		//zatia¾ neiplementované
	}

	@FXML
	void zrusitButtonClick(ActionEvent event) {
		nadpisLabel.getScene().getWindow().hide();
	}
}