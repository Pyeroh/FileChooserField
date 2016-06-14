package nc.dev.pyeroh.jfx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class FileChooserField extends AnchorPane implements Initializable {

	private StringProperty title;

	private ObjectProperty<File> initialDirectory;

	private ListProperty<ExtensionFilter> extensionFilters;

	private ObjectProperty<Window> chooserOwnerWindow;

	private ObjectProperty<Kind> kind;

	private ListProperty<File> selectedFiles;

	/**
	 * Represents all selected files of this component
	 */
	@FXML
	private TextField filesPaths;

	/**
	 * On press on this button, opens the FileChooser, with set parameters
	 */
	@FXML
	private Button openFileChooser;

	@FXML
	private ImageView openIcon;

	public static enum Kind {
		SINGLE,
		MULTIPLE,
		SAVE;
	}

	public FileChooserField() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FileChooserField.fxml"));

		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		initialize(null, null);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title = new SimpleStringProperty("");
		initialDirectory = new SimpleObjectProperty<>(new File(System.getProperty("user.dir")));
		extensionFilters = new SimpleListProperty<>(FXCollections.observableArrayList());
		chooserOwnerWindow = new SimpleObjectProperty<>(null);
		kind = new SimpleObjectProperty<>(Kind.SINGLE);
		selectedFiles = new SimpleListProperty<>(FXCollections.observableArrayList());

		selectedFiles.addListener(new ListChangeListener<File>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends File> c) {
				filesPaths.setText(getSelectedFiles().parallelStream().map(File::toString).collect(Collectors.joining("|")));
			}
		});
	}

	@FXML
	private void displayFileChooser() {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle(getTitle());
		fileChooser.setInitialDirectory(getInitialDirectory());
		fileChooser.getExtensionFilters().addAll(getExtensionFilters());

		List<File> files = new ArrayList<>();
		switch (getKind()) {
		case SINGLE:
			files.add(fileChooser.showOpenDialog(getChooserOwnerWindow()));
			break;
		case MULTIPLE:
			files.addAll(fileChooser.showOpenMultipleDialog(getChooserOwnerWindow()));
			break;
		case SAVE:
			files.add(fileChooser.showSaveDialog(getChooserOwnerWindow()));
			break;
		default:
			break;
		}
		files.removeIf(f -> f == null);

		if (!files.isEmpty()) {
			getSelectedFiles().clear();
			getSelectedFiles().addAll(files);
		}
	}

	public StringProperty titleProperty() {
		return this.title;
	}

	public String getTitle() {
		return this.titleProperty().get();
	}

	public void setTitle(final String title) {
		this.titleProperty().set(title);
	}

	public ObjectProperty<File> initialDirectoryProperty() {
		return this.initialDirectory;
	}

	public File getInitialDirectory() {
		return this.initialDirectoryProperty().get();
	}

	public void setInitialDirectory(final File initialDirectory) {
		this.initialDirectoryProperty().set(initialDirectory);
	}

	public ListProperty<ExtensionFilter> extensionFiltersProperty() {
		return this.extensionFilters;
	}

	public ObservableList<ExtensionFilter> getExtensionFilters() {
		return this.extensionFiltersProperty().get();
	}

	public void setExtensionFilters(final ObservableList<ExtensionFilter> extensionFilters) {
		this.extensionFiltersProperty().set(extensionFilters);
	}

	public ObjectProperty<Window> chooserOwnerWindowProperty() {
		return this.chooserOwnerWindow;
	}

	public Window getChooserOwnerWindow() {
		return this.chooserOwnerWindowProperty().get();
	}

	public void setChooserOwnerWindow(final Window chooserOwnerWindow) {
		this.chooserOwnerWindowProperty().set(chooserOwnerWindow);
	}

	public ObjectProperty<Kind> kindProperty() {
		return this.kind;
	}

	public Kind getKind() {
		return this.kindProperty().get();
	}

	public void setKind(final Kind kind) {
		this.kindProperty().set(kind);
	}

	public ListProperty<File> selectedFilesProperty() {
		return this.selectedFiles;
	}

	public ObservableList<File> getSelectedFiles() {
		return this.selectedFilesProperty().get();
	}

	public void setSelectedFiles(final ObservableList<File> selectedFiles) {
		this.selectedFilesProperty().set(selectedFiles);
	}

}
