package warehouse;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import warehouse.business.Polozka;
import warehouse.business.PolozkaManager;
import warehouse.business.PolozkaManagerImplementation;
import warehouse.storage.DaoFactory;
import warehouse.storage.Sklad;
import warehouse.storage.SkladDao;
import warehouse.storage.UmiestnenieDao;

public class SkladoveZasobyObrazovkaController {

	@FXML
	private TableView<Polozka> skladoveZasobyTableView;

	@FXML
	private TableColumn<Polozka, String> nazovTableColumn;

	@FXML
	private TableColumn<Polozka, Integer> mnozstvoTableColumn;

	@FXML
	private TableColumn<Polozka, String> mernaJednotkaTableColumn;

	@FXML
	private TableColumn<Polozka, Double> celkovaCenaTableColumn;

	@FXML
	private Label infoTextLabel;

	@FXML
	private ComboBox<Sklad> skladComboBox;

	private SkladDao skladDao = DaoFactory.INSTANCE.getSkladDao();
	private UmiestnenieDao umiestnenieDao = DaoFactory.INSTANCE.getUmiestnenieDao();
	private PolozkaManager polozkaManager = new PolozkaManagerImplementation();
	private Sklad sklad = skladDao.getById(1);

	@FXML
	void initialize() {

		nazovTableColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDruh().getNazov()));
		mnozstvoTableColumn.setCellValueFactory(new PropertyValueFactory<>("pocet"));
		mernaJednotkaTableColumn.setCellValueFactory(
				cell -> new SimpleStringProperty(cell.getValue().getDruh().getMernaJednotka().getOznacenie()));
		celkovaCenaTableColumn.setCellValueFactory(new PropertyValueFactory<>("celkovaCena"));

		aktualizuj();

		List<Sklad> sklady = skladDao.getAll();
		skladComboBox.setItems(FXCollections.observableArrayList(sklady));
		skladComboBox.getSelectionModel().select(sklady.get(0));
		skladComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sklad>() {

			@Override
			public void changed(ObservableValue<? extends Sklad> observable, Sklad oldValue, Sklad newValue) {

				sklad = newValue;
				aktualizuj();
			}

		});

	}

	public void aktualizuj() {
		skladoveZasobyTableView.setItems(FXCollections.observableArrayList(polozkaManager.getPolozky(sklad)));

		double percentoObsadenosti = ((double) umiestnenieDao.vypisSklad(sklad).size()
				/ (double) (sklad.getPocetPoliciek() * sklad.getPocetRegalov())) * 100.00;
		String text = sklad.getNazov() + "\n" + sklad.getAdresa() + "\n" + umiestnenieDao.vypisSklad(sklad).size()
				+ " (" + sklad.getPocetPoliciek() * sklad.getPocetRegalov() + ")\n" + percentoObsadenosti + " %";
		infoTextLabel.setText(text);
	}

}
