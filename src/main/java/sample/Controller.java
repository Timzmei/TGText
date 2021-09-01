package sample;

/**
 * Created by timurg
 * on 31.08.2021
 * project name TGText
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

public class Controller {

    private final static FileChooser FILE_CHOOSER = new FileChooser();

    private TextFile currentTextFile;

    private final EditorModel model;

    public Controller(EditorModel model) {
        this.model = model;
        this.currentTextFile = null;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Menu file_menu;

    @FXML
    private MenuItem open_item;

    @FXML
    private MenuItem newFile_item;

    @FXML
    private MenuItem save_item;

    @FXML
    private MenuItem save_as_item;

    @FXML
    private MenuItem close_item;

    @FXML
    private Menu edit_menu;

    @FXML
    private MenuItem copy_item;

    @FXML
    private MenuItem paste_item;

    @FXML
    private MenuItem cut_item;

    @FXML
    private MenuItem delete_item;

    @FXML
    private Menu help_menu;

    @FXML
    private MenuItem about_item;

    @FXML
    private FontAwesomeIconView save_button;

    @FXML
    private FontAwesomeIconView open_button;

    @FXML
    private FontAwesomeIconView new_button;

    @FXML
    private MaterialDesignIconView markdown_button;

    @FXML
    private TextFlow text_flow;

    @FXML
    private TextArea text_area;

    @FXML
    void onAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("About");
        alert.setContentText("This is a simple text editor EditorApp");
        alert.show();

    }

    @FXML
    void onClose(ActionEvent event) {
        model.close();
    }

    @FXML
    void onCopy(ActionEvent event) {
        text_area.copy();
    }

    @FXML
    void onCut(ActionEvent event) {
        text_area.cut();
    }

    @FXML
    void onDelete(ActionEvent event) {
        IndexRange indexRange = text_area.getSelection();
        text_area.deleteText(indexRange);
    }

    @FXML
    void onNewFile(ActionEvent event) {

    }

    @FXML
    void onOpen(ActionEvent event) {
        openFile();
    }


    @FXML
    void onPaste(ActionEvent event) {
        text_area.paste();
    }

    @FXML
    void onSave(ActionEvent event) throws IOException {
        saveFile();
    }


    @FXML
    void onSaveAs(ActionEvent event) {
        saveNewFile();
    }

    @FXML
    public void onSaveMouse(MouseEvent mouseEvent) throws IOException {
        saveFile();
    }

    @FXML
    public void onNewFileMouse(MouseEvent mouseEvent) {
    }

    @FXML
    public void onOpenMouse(MouseEvent mouseEvent) {
        openFile();
    }

    @FXML
    public void onMouseClickedMarkdown(MouseEvent mouseEvent) {
        boolean visibleMDText = text_flow.isVisible();
        text_flow.setVisible(visibleMDText);
        text_flow.setDisable(!visibleMDText);
    }


    @FXML
    void initialize() {
        assert open_button != null : "fx:id=\"open_button\" was not injected: check your FXML file 'sample.fxml'.";
        assert save_button != null : "fx:id=\"save_button\" was not injected: check your FXML file 'sample.fxml'.";
        assert text_area != null : "fx:id=\"text_area\" was not injected: check your FXML file 'sample.fxml'.";
        assert file_menu != null : "fx:id=\"file_menu\" was not injected: check your FXML file 'sample.fxml'.";
        assert open_item != null : "fx:id=\"open_item\" was not injected: check your FXML file 'sample.fxml'.";
        assert newFile_item != null : "fx:id=\"newFile_item\" was not injected: check your FXML file 'sample.fxml'.";
        assert save_item != null : "fx:id=\"save_item\" was not injected: check your FXML file 'sample.fxml'.";
        assert save_as_item != null : "fx:id=\"save_as_item\" was not injected: check your FXML file 'sample.fxml'.";
        assert close_item != null : "fx:id=\"close_item\" was not injected: check your FXML file 'sample.fxml'.";
        assert edit_menu != null : "fx:id=\"edit_menu\" was not injected: check your FXML file 'sample.fxml'.";
        assert copy_item != null : "fx:id=\"copy_item\" was not injected: check your FXML file 'sample.fxml'.";
        assert paste_item != null : "fx:id=\"paste_item\" was not injected: check your FXML file 'sample.fxml'.";
        assert cut_item != null : "fx:id=\"cut_item\" was not injected: check your FXML file 'sample.fxml'.";
        assert delete_item != null : "fx:id=\"delete_item\" was not injected: check your FXML file 'sample.fxml'.";
        assert help_menu != null : "fx:id=\"help_menu\" was not injected: check your FXML file 'sample.fxml'.";
        assert about_item != null : "fx:id=\"about_item\" was not injected: check your FXML file 'sample.fxml'.";

    }


    private void saveFile() throws IOException {
        if (this.currentTextFile != null) {
            Files.delete(currentTextFile.getFile());
            this.currentTextFile = new TextFile(currentTextFile.getFile(), Arrays.asList(text_area.getText().split("\n")));
            model.save(this.currentTextFile);
        } else {
            saveNewFile();
        }
    }

    private void saveNewFile() {
        final File file = FILE_CHOOSER.showSaveDialog(null);
        if (file == null) {
            return;
        }
        final String absolutePath = file.getAbsolutePath(), fileName = file.getName();
        this.currentTextFile = new TextFile(file.toPath(), Arrays.asList(text_area.getText().split("\n")));
        model.save(this.currentTextFile);
    }


    private void openFile() {
        FILE_CHOOSER.setInitialDirectory(new File("./"));
        File file = FILE_CHOOSER.showOpenDialog(null);
        if (file != null) {
            IOResult<TextFile> io = model.load(file.toPath());

            if (io.isOk() && io.hasData()) {
                this.currentTextFile = io.getData();

                text_area.clear();
                this.currentTextFile.getContent().forEach(line -> text_area.appendText(line + "\n"));
            } else {
                System.out.println("Failed");
            }
        }
    }


}