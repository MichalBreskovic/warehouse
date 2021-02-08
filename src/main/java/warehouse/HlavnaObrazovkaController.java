package warehouse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import warehouse.storage.DaoFactory;
import warehouse.storage.Umiestnenie;
import warehouse.storage.UmiestnenieDao;

public class HlavnaObrazovkaController {

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
	private TextField druhTovaruTextField;

	@FXML
	private Button naskladnitTovarButton;

	@FXML
	private Button vyskladnitTovarButton;

	@FXML
	private Button druhTovaruButton;

	@FXML
	private Button prehladSkladuButton;

	@FXML
	private TextField regalTextField;

	@FXML
	private TextField policaTextField;

	private UmiestnenieDao umiestnenieDao = DaoFactory.INSTANCE.getUmiestnenieDao();
	List<Umiestnenie> umiestnenia;

	@FXML
	void initialize() {
		umiestnenia = umiestnenieDao.getAll();
		umiestneniaTableView.setItems(FXCollections.observableList(umiestnenia));

		druhTovaruTableColumn
				.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDruh().getNazov()));
		mnozstvoTableColumn.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("mnozstvo"));
		skladTableColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSklad().getNazov()));
		regalTableColumn.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("regal"));
		policaTableColumn.setCellValueFactory(new PropertyValueFactory<Umiestnenie, Integer>("policka"));

		druhTovaruTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				filter();
			}

		});

		regalTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				filter();
			}

		});

		policaTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				filter();
			}

		});

	} // konèí inicialize()

	@FXML
	void druhTovaruButtonClick(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TovarObrazovka.fxml"));
			TovarObrazovkaController controller = new TovarObrazovkaController();
			loader.setController(controller);
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Pridanie a úprava druhu tovaru");
			stage.showAndWait();
			umiestnenia = umiestnenieDao.getAll();
			umiestneniaTableView.getSelectionModel().clearSelection();
			umiestneniaTableView.refresh();
			filter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void naskladnitTovarButtonClick(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NaskladnenieObrazovka.fxml"));
			NaskladnenieObrazovkaController controller = new NaskladnenieObrazovkaController();
			loader.setController(controller);
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Naskladnenie tovaru");
			stage.showAndWait();
			umiestnenia = umiestnenieDao.getAll();
			umiestneniaTableView.getSelectionModel().clearSelection();
			filter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void vyskladnitTovarButtonClick(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("VyskladnenieObrazovka.fxml"));
			VyskladnenieObrazovkaController controller = new VyskladnenieObrazovkaController();
			loader.setController(controller);
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Vyskladnenie tovaru");
			stage.showAndWait();
			umiestnenia = umiestnenieDao.getAll();
			umiestneniaTableView.getSelectionModel().clearSelection();
			filter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void prehladSkladuButtonClick(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SkladoveZasobyObrazovka.fxml"));
			SkladoveZasobyObrazovkaController controller = new SkladoveZasobyObrazovkaController();
			loader.setController(controller);
			Parent parent = loader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Preh¾ad skladových zásob");
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void filter() {

		String d = druhTovaruTextField.textProperty().getValue();
		int r = 0;
		int p = 0;

		if (!regalTextField.textProperty().getValue().isBlank()) {
			try {
				r = Integer.parseInt(regalTextField.textProperty().getValue());
				regalTextField.setStyle("-fx-background-color: white");
			} catch (NumberFormatException e) {
				regalTextField.setStyle("-fx-background-color: lightcoral");
			}
		} else {
			regalTextField.setStyle("-fx-background-color: white");
		}

		if (!policaTextField.textProperty().getValue().isBlank()) {
			try {
				p = Integer.parseInt(policaTextField.textProperty().getValue());
				policaTextField.setStyle("-fx-background-color: white");
			} catch (NumberFormatException e) {
				policaTextField.setStyle("-fx-background-color: lightcoral");
			}
		} else {
			policaTextField.setStyle("-fx-background-color: white");
		}

		umiestneniaTableView.setItems(FXCollections.observableList(hladajUmiestnenie(d, r, p)));
	}

	public List<Umiestnenie> hladajUmiestnenie(String substring, int regal, int polica) {

		// filter pod¾a: https://www.baeldung.com/java-stream-filter-lambda
		List<Umiestnenie> najdene = umiestnenia.stream()
				.filter(u -> u.getDruh().getNazov().matches("(.*)" + substring + "(.*)")
						&& ((regal == 0) ? true : ((regal == u.getRegal()) ? true : false))
						&& ((polica == 0) ? true : ((polica == u.getPolicka()) ? true : false)))
				.collect(Collectors.toList());
		return najdene;
	}

}
