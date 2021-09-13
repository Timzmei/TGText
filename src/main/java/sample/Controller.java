package sample;

/**
 * Created by timurg
 * on 31.08.2021
 * project name TGText
 */

import com.cathive.fonts.fontawesome.FontAwesomeIconView;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Controller {

    private final static FileChooser FILE_CHOOSER = new FileChooser();

    private TextFile currentTextFile;

    private final EditorModel model;
    private WebView browser;
    private WebEngine webEngine;

    public Controller(EditorModel model) {
        this.model = model;
        this.currentTextFile = null;
        this.browser = new WebView();
        this.webEngine = this.browser.getEngine();
    }

    @FXML
    private BorderPane border_pane;

    @FXML
    private TextArea text_area;

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
    private FontAwesomeIconView open_button;

    @FXML
    private FontAwesomeIconView save_button;

    @FXML
    private Button markdown_button;

    @FXML
    private HBox fieldMd;

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
    public void saveChange(Event event) {
        setMDtext(this.webEngine);
    }

    @FXML
    public void onMouseClickedMarkdown(Event event) {

        if(border_pane.getRight() !=null) {
            border_pane.setRight(null);
        } else {
            border_pane.setRight(browser);
            setMDtext(this.webEngine);
        }

    }

    private void setMDtext(WebEngine webEngine) {

        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(text_area.getText());
        String html = renderer.render(document);
        webEngine.loadContent(html);
    }


    @FXML
    void initialize() {



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