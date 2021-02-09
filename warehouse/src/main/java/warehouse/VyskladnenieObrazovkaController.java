package warehouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import warehouse.storage.DaoFactory;
import warehouse.storage.Sklad;
import warehouse.storage.SkladDao;
import warehouse.storage.Umiestnenie;
import warehouse.storage.UmiestnenieDao;

public class VyskladnenieObrazovkaController {

	@FXML
	private ComboBox<Sklad> skladComboBox;

	@FXML
	private TableView<Umiestnenie> skladoveZasobyTableView;

	@FXML
	private TableColumn<Umiestnenie, String> druhTovaruNaSklade;

	@FXML
	private TableColumn<Umiestnenie, Long> idNaSklade;

	@FXML
	private TableColumn<Umiestnenie, Integer> mnozstvoNaSklade;

	@FXML
	private TableColumn<Umiestnenie, Integer> regalNaSklade;

	@FXML
	private TableColumn<Umiestnenie, Integer> policaNaSklade;

	@FXML
	private Button pridatButton;

	@FXML
	private Button odobratButton;

	@FXML
	private Button vyskladnitButton;

	@FXML
	private TableView<Umiestnenie> naVyskladnenieTableView;

	@FXML
	private TableColumn<Umiestnenie, String> druhTovaruVyskladnit;

	@FXML
	private TableColumn<Umiestnenie, Long> idVyskladnit;

	@FXML
	private TableColumn<Umiestnenie, Integer> mnozstvoVyskladnit;

	@FXML
	private TableColumn<Umiestnenie, Integer> regalVyskladnit;

	@FXML
	private TableColumn<Umiestnenie, Integer> policaVyskladnit;

	private SkladDao skladDao = DaoFactory.INSTANCE.getSkladDao();
	private UmiestnenieDao umiestnenieDao = DaoFactory.INSTANCE.getUmiestnenieDao();
	private ObservableList<Umiestnenie> umiestnenia;
	private ObservableList<Umiestnenie> umiestneniaNaVyskladnenie = FXCollections
			.observableList(new ArrayList<Umiestnenie>());
	private Sklad s = skladDao.getById(1L);

	@FXML
	void initialize() {

		// zatiaæ len pre jeden sklad
		umiestnenia = FXCollections.observableList(umiestnenieDao.vypisSklad(s));

		// naskladnenie tabuæka
		skladoveZasobyTableView.setItems(umiestnenia);
		skladoveZasobyTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		idNaSklade.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Long>("id"));
		druhTovaruNaSklade.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDruh().getNazov()));
		mnozstvoNaSklade.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("mnozstvo"));
		regalNaSklade.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("regal"));
		policaNaSklade.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("policka"));

		// vyskladnenie tabuæka
		naVyskladnenieTableView.setItems(umiestneniaNaVyskladnenie);
		naVyskladnenieTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		idVyskladnit.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Long>("id"));
		druhTovaruVyskladnit
				.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDruh().getNazov()));
		mnozstvoVyskladnit.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("mnozstvo"));
		regalVyskladnit.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("regal"));
		policaVyskladnit.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("policka"));

		List<Sklad> sklady = skladDao.getAll();
		skladComboBox.setItems(FXCollections.observableArrayList(sklady));
		skladComboBox.getSelectionModel().select(sklady.get(0));
		skladComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sklad>() {

			@Override
			public void changed(ObservableValue<? extends Sklad> observable, Sklad oldValue, Sklad newValue) {

				s = newValue;
				skladoveZasobyTableView.getSelectionModel().clearSelection();
				umiestneniaNaVyskladnenie.clear();
				umiestnenia = FXCollections.observableList(umiestnenieDao.vypisSklad(s));
				skladoveZasobyTableView.setItems(umiestnenia);
			}

		});
	}

	@FXML
	void odobratButtonClick(ActionEvent event) {
		List<Umiestnenie> naVratenie = new ArrayList<Umiestnenie>(
				naVyskladnenieTableView.getSelectionModel().getSelectedItems());
		umiestneniaNaVyskladnenie.removeAll(naVratenie);
		umiestnenia.addAll(naVratenie);
		naVyskladnenieTableView.getSelectionModel().clearSelection();
	}

	@FXML
	void pridatButtonClick(ActionEvent event) {
		List<Umiestnenie> naVyskladnenie = new ArrayList<Umiestnenie>(
				skladoveZasobyTableView.getSelectionModel().getSelectedItems());
		umiestnenia.removeAll(naVyskladnenie);
		umiestneniaNaVyskladnenie.addAll(naVyskladnenie);
		skladoveZasobyTableView.getSelectionModel().clearSelection();
	}

	@FXML
	void vyskladnitButtonClick(ActionEvent event) {
		String error = "";
		if (umiestneniaNaVyskladnenie.isEmpty())
			error = "MusÌte zadaù tovar na vyskladnenie!";

		if (!error.isBlank()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(error);
			alert.show();
		} else {

			List<Umiestnenie> vyskladnene = new ArrayList<Umiestnenie>();
			for (Umiestnenie u : umiestneniaNaVyskladnenie) {
				vyskladnene.add(umiestnenieDao.vyskladnenie(u.getId()));
			}

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ZoznamTovaruObrazovka.fxml"));
				ZoznamTovaruObrazovkaController controller = new ZoznamTovaruObrazovkaController(
						"Tovar na vyskladnenie", vyskladnene);
				loader.setController(controller);
				Parent parent = loader.load();
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setTitle("Zoznam vyskladnenÈho tovaru!");
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			umiestneniaNaVyskladnenie.clear();
		}

	}
}
