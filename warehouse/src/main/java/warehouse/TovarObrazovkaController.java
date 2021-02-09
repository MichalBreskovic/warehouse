package warehouse;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import warehouse.storage.DaoFactory;
import warehouse.storage.Jednotka;
import warehouse.storage.JednotkaDao;
import warehouse.storage.Tovar;
import warehouse.storage.TovarDao;
import warehouse.storage.UmiestnenieDao;

public class TovarObrazovkaController {

	@FXML
	private ComboBox<Tovar> tovarComboBox;

	@FXML
	private TextField nazovTextField;

	@FXML
	private TextField jednotkovaCenaTextField;

	@FXML
	private TextField obmedzenieTextField;

	@FXML
	private TextArea popisTextArea;

	@FXML
	private ComboBox<Jednotka> jednotkaComboBox;

	@FXML
	private Button ulozitButton;

	@FXML
	private Button zrusitButton;

	@FXML
	private Button vlozitNovy;

	@FXML
	private Button vymazatButton;

	private TovarDao tovarDao = DaoFactory.INSTANCE.getTovarDao();
	private JednotkaDao jednotkaDao = DaoFactory.INSTANCE.getJednotkaDao();
	private UmiestnenieDao umiestnenieDao = DaoFactory.INSTANCE.getUmiestnenieDao();
	private List<Tovar> tovary;
	private List<Jednotka> jednotky;
	private TovarFxModel tovarModel;
	private Tovar ulozeny;

	public TovarObrazovkaController() {
		tovarModel = new TovarFxModel();
	}

	public TovarObrazovkaController(Tovar tovar) {
		tovarModel = new TovarFxModel(tovar);
	}

	@FXML
	void initialize() {
		tovary = tovarDao.getAll();
		tovarComboBox.setItems(FXCollections.observableArrayList(tovary));

		jednotky = jednotkaDao.getAll();
		jednotkaComboBox.setItems(FXCollections.observableArrayList(jednotky));

		tovarComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tovar>() {

			@Override
			public void changed(ObservableValue<? extends Tovar> observable, Tovar oldValue, Tovar newValue) {

				tovarModel = new TovarFxModel();
				if (newValue != null)
					tovarModel = new TovarFxModel(newValue);
				refresh();
			}

		});

		refresh();

	}

	@FXML
	void ulozitButtonClick(ActionEvent event) {

		String error = tovarModel.getError();
		if (error == null) {
			ulozeny = tovarDao.save(tovarModel.getTovar());
			boolean bol = false;
			List<Tovar> tovary = tovarComboBox.getItems();
			List<Jednotka> jednotky = jednotkaComboBox.getItems();

			int i = 0;
			for (Tovar t : tovary) {
				if (t.equals(ulozeny)) {
					tovarComboBox.getItems().remove(t);
					tovarComboBox.getItems().add(i,ulozeny);
					tovarComboBox.getSelectionModel().select(ulozeny);
					bol = true;
					break;
				}
				i++;
			}
			if (bol == false) {
				tovarComboBox.getItems().add(ulozeny);
				tovarComboBox.getSelectionModel().select(ulozeny);
			}
			
			for (Jednotka j : jednotky) {
				Jednotka ulozena = ulozeny.getMernaJednotka();
				if (j.equals(ulozena)) {
					jednotkaComboBox.getItems().remove(j);
					jednotkaComboBox.getItems().add(ulozena);
					jednotkaComboBox.getSelectionModel().select(ulozena);
					break;
				}
			}

			refresh();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(error);
			alert.show();
		}

	}

	@FXML
	void zrusitButtonClick(ActionEvent event) {
		tovarComboBox.getScene().getWindow().hide();
	}

	@FXML
	void vlozitNovyButtonClick(ActionEvent event) {
		tovarComboBox.getSelectionModel().clearSelection();
	}

	@FXML
	void vymazatButtonClick(ActionEvent event) {
		String error = "";

		if (tovarModel.getId() == null)
			error = "MusÌte zvoliù druh tovaru, ktor˝ chcete vymazaù!";

		if (tovarModel.getId() != null && umiestnenieDao.hladaj(tovarModel.getTovar()).size() > 0)
			error = "Druh tovaru sa pouûÌva. \nJe ho moûnÈ vymazaù aû po vyskladnenÌ vöetkÈho tovaru tohto druhu!";

		if (!error.isBlank()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(error);
			alert.show();
		} else {
			Tovar vymazany = tovarDao.delete(tovarModel.getId());
			List<Tovar> tovary = tovarComboBox.getItems();

			for (Tovar t : tovary) {
				if (t.equals(vymazany)) {
					tovarComboBox.getItems().remove(t);
					tovarComboBox.getSelectionModel().select(null);
					break;
				}
			}
			refresh();
		}
	}

	public void refresh() {
		jednotkaComboBox.setItems(FXCollections.observableArrayList(jednotky));

		nazovTextField.textProperty().bindBidirectional(tovarModel.nazovProperty());
		jednotkovaCenaTextField.textProperty().bindBidirectional(tovarModel.jednotkovaCenaProperty());
		obmedzenieTextField.textProperty().bindBidirectional(tovarModel.obmedzenieProperty());
		popisTextArea.textProperty().bindBidirectional(tovarModel.popisProperty());
		jednotkaComboBox.valueProperty().bindBidirectional(tovarModel.mernaJednotkaProperty());
	}

	public Tovar getPoslednyUlozeny() {
		return ulozeny;
	}

}
