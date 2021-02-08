package warehouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import warehouse.storage.DaoFactory;
import warehouse.storage.Sklad;
import warehouse.storage.SkladDao;
import warehouse.storage.Tovar;
import warehouse.storage.TovarDao;
import warehouse.storage.Umiestnenie;
import warehouse.storage.UmiestnenieDao;

public class NaskladnenieObrazovkaController {

	@FXML
	private TableView<Umiestnenie> umiestneniaTableView;

	@FXML
	private TableColumn<Umiestnenie, String> druhTovaruTableColumn;

	@FXML
	private TableColumn<Umiestnenie, Integer> mnozstvoTableColumn;

	@FXML
	private TableColumn<Umiestnenie, String> skladTableColumn;

	@FXML
	private TableColumn<Umiestnenie, Integer> regalTableColumn;

	@FXML
	private TableColumn<Umiestnenie, Integer> policaTableColumn;

	@FXML
	private Button pridatButton;

	@FXML
	private Button odobratButton;

	@FXML
	private ComboBox<Sklad> skladComboBox;

	@FXML
	private ComboBox<Tovar> druhTovaruComboBox;

	@FXML
	private TextField mnozstvoTextField;

	@FXML
	private Button naskladnitButton;

	@FXML
	private Label jednotkaLabel;

	@FXML
	private Button pridatTovarButton;

	private TovarDao tovarDao = DaoFactory.INSTANCE.getTovarDao();
	private SkladDao skladDao = DaoFactory.INSTANCE.getSkladDao();
	private UmiestnenieDao umiestnenieDao = DaoFactory.INSTANCE.getUmiestnenieDao();
	private List<Umiestnenie> umiestnenia = new ArrayList<Umiestnenie>();
	Stack<Umiestnenie> volne = new Stack<Umiestnenie>();
	private List<Umiestnenie> obsadene = new ArrayList<Umiestnenie>();
	private List<Long> obsadeneId = new ArrayList<Long>();
	private Sklad s = skladDao.getById(1L);

	@FXML
	void initialize() {
		
		// sklad
		obsadene = umiestnenieDao.vypisSklad(s);
		for (Umiestnenie u : obsadene) {
			obsadeneId.add(u.getId());
		}
		volne = umiestnenieDao.volneUmiestnenia(s);

		List<Sklad> sklady = skladDao.getAll();
		skladComboBox.setItems(FXCollections.observableArrayList(sklady));
		skladComboBox.getSelectionModel().select(sklady.get(0));
		skladComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sklad>() {

			@Override
			public void changed(ObservableValue<? extends Sklad> observable, Sklad oldValue, Sklad newValue) {
				
				s = newValue;
				obsadene = umiestnenieDao.vypisSklad(s);
				for (Umiestnenie u : obsadene) {
					obsadeneId.add(u.getId());
				}
				volne = umiestnenieDao.volneUmiestnenia(s);
				
				umiestnenia = new ArrayList<Umiestnenie>();
				umiestneniaTableView.setItems(FXCollections.observableList(umiestnenia));
			}

		});
		

		umiestneniaTableView.setItems(FXCollections.observableList(umiestnenia));
		umiestneniaTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		druhTovaruTableColumn
				.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDruh().getNazov()));
		mnozstvoTableColumn.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("mnozstvo"));
		skladTableColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSklad().getNazov()));
		regalTableColumn.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("regal"));
		policaTableColumn.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("policka"));

		List<Tovar> tovary = tovarDao.getAll();
		druhTovaruComboBox.setItems(FXCollections.observableArrayList(tovary));
		druhTovaruComboBox.getSelectionModel().select(tovary.get(0));
		druhTovaruComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tovar>() {

			@Override
			public void changed(ObservableValue<? extends Tovar> observable, Tovar oldValue, Tovar newValue) {
				if (newValue != null)
					jednotkaLabel.setText(newValue.getMernaJednotka().getOznacenie());
			}

		});

		pridatButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String error = "";
				if (mnozstvoTextField.getText() == null || mnozstvoTextField.getText().isBlank())
					error = "MusÌte zadaù mnoûstvo tovaru na naskladnenie!";

				if (mnozstvoTextField.getText() == null || mnozstvoTextField.getText().isBlank())
					error = "MusÌte zadaù druh tovaru, ktor˝ chcete naskladniù!";

				try {
					if (Integer.parseInt(mnozstvoTextField.textProperty().getValue()) < 1)
						error = "\nMnoûstvo tovaru musÌ byù v‰Ëöie ako 1!";
				} catch (NumberFormatException e) {
					error = error + "\nMusÌte zadaù ËÌslo!";
				}

				if (!error.isBlank()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText(error);
					alert.show();
				} else {
					rozdelAPridaj();
					umiestneniaTableView.setItems(FXCollections.observableList(umiestnenia));
				}

			}
		});

		odobratButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				List<Umiestnenie> odstranit = new ArrayList<Umiestnenie>(
						umiestneniaTableView.getSelectionModel().getSelectedItems());

				for (Umiestnenie u : odstranit) {
					if (!obsadeneId.contains(u.getId()))
						volne.push(u);
					else
						obsadene.add(umiestnenieDao.getById(u.getId()));
				}
				umiestnenia.removeAll(odstranit);

				umiestneniaTableView.setItems(FXCollections.observableList(umiestnenia));
			}
		});
	}

	@FXML
	void naskladnitButtonClick(ActionEvent event) {
		String error = "";
		if (umiestnenia.isEmpty())
			error = "MusÌte zadaù tovar na naskladnenie!";

		if (!error.isBlank()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(error);
			alert.show();
		} else {

			List<Umiestnenie> naskladnene = new ArrayList<Umiestnenie>();
			for (Umiestnenie u : umiestnenia) {
				if (!obsadeneId.contains(u.getId()))
					u.setId(null);
				naskladnene.add(umiestnenieDao.naskladnenie(u, s));
			}

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ZoznamTovaruObrazovka.fxml"));
				ZoznamTovaruObrazovkaController controller = new ZoznamTovaruObrazovkaController(
						"Tovar na naskladnenie", naskladnene);
				loader.setController(controller);
				Parent parent = loader.load();
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.setTitle("Zoznam naskladnenÈho tovaru!");
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			obsadene = umiestnenieDao.vypisSklad(s);
			volne = umiestnenieDao.volneUmiestnenia(s);
			umiestnenia = new ArrayList<Umiestnenie>();
			umiestneniaTableView.setItems(FXCollections.observableList(umiestnenia));
		}

	}

	@FXML
	void pridatTovarButtonClick(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TovarObrazovka.fxml"));
			TovarObrazovkaController controller = new TovarObrazovkaController();
			loader.setController(controller);
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Pridanie a ˙prava druhu tovaru");
			stage.showAndWait();
			druhTovaruComboBox.setItems(FXCollections.observableArrayList(tovarDao.getAll()));
			Tovar ulozeny = controller.getPoslednyUlozeny();
			if (ulozeny != null)
				druhTovaruComboBox.getSelectionModel().select(ulozeny);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rozdelAPridaj() {
		int mnozstvo = Integer.parseInt(mnozstvoTextField.getText());
		int obmedzenie = druhTovaruComboBox.getValue().getObmedzenie();
		Tovar druh = druhTovaruComboBox.getValue();

		// zisùujeme, Ëi uû nie s˙ miesta, kde sa d· tovar eöte doplniù
		List<Umiestnenie> naOdstranenie = new ArrayList<Umiestnenie>();
		for (Umiestnenie o : obsadene) {
			if (o.getDruh().equals(druh) && o.getMnozstvo() < obmedzenie) {
				naOdstranenie.add(o);
			}
		}

		obsadene.removeAll(naOdstranenie);
		umiestnenia.addAll(0, naOdstranenie); // prid· ich na zaËiatok, t.j. od indexu 0

		// ak je eöte potrebnÈ nieËo naskladniù - pokraËujeme v kontrole pridan˝ch
		// umiestnenÌ
		for (Umiestnenie u : umiestnenia) {
			if (u.getDruh().equals(druh) && u.getMnozstvo() < obmedzenie) {
				int pricitat = obmedzenie - u.getMnozstvo();
				if (pricitat <= mnozstvo) {
					u.setMnozstvo(u.getMnozstvo() + pricitat);
					mnozstvo = mnozstvo - pricitat;
					umiestneniaTableView.refresh();
				}

				else {
					u.setMnozstvo(u.getMnozstvo() + mnozstvo);
					umiestneniaTableView.refresh();
					mnozstvo = 0;
					break;
				}
			}
		}

		// doplnenie tovaru do voæn˝ch umiestnenÌ (rozdelenie podæa obmedzenia)
		int pocet = (int) mnozstvo / obmedzenie;
		if (mnozstvo % obmedzenie != 0)
			pocet++;

		for (int i = 0; i < pocet; i++) {
			if (!volne.isEmpty()) {
				Umiestnenie u = volne.pop();
				u.setDruh(druh);
				if (mnozstvo < obmedzenie)
					u.setMnozstvo(mnozstvo);
				else
					u.setMnozstvo(obmedzenie);
				umiestnenia.add(u);
				mnozstvo = mnozstvo - obmedzenie;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Sklad je pln˝");
				alert.show();
				break;
			}
		}

	}

}
