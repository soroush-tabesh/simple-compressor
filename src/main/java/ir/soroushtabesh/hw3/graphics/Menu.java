package ir.soroushtabesh.hw3.graphics;

import ir.soroushtabesh.hw3.compressor.Compressor;
import ir.soroushtabesh.hw3.compressor.LZ77;
import ir.soroushtabesh.hw3.compressor.entity.FileEntry;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class Menu {
    public ListView<FileEntry> filesListView;
    public TextField inDecText;
    public TextField outDecText;
    public TextField inCompText;
    public TextField outCompText;
    public TextField passDec;
    public TextField passComp;

    public void selectInDec(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        var file = fileChooser.showOpenDialog(null);
        if (file == null)
            return;
        inDecText.setText(file.getAbsolutePath());
        var descriptor = Compressor.readDescriptor(file.getAbsolutePath());
        filesListView.getItems().clear();
        if (descriptor != null)
            filesListView.getItems().addAll(descriptor.getFiles());
        else
            new Alert(Alert.AlertType.ERROR, "Incorrect File").showAndWait();

    }

    public void selectOutDec(ActionEvent actionEvent) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        var file = fileChooser.showDialog(null);
        if (file == null)
            return;
        outDecText.setText(file.getAbsolutePath());
    }

    public void decompress(ActionEvent actionEvent) {
        try {
            Compressor.decompress(inDecText.getText(), outDecText.getText(), passDec.getText());
            new Alert(Alert.AlertType.INFORMATION, "Done").showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error").showAndWait();
        }
    }

    public void selectInComp(ActionEvent actionEvent) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        var file = fileChooser.showDialog(null);
        if (file == null)
            return;
        inCompText.setText(file.getAbsolutePath());
    }

    public void selectOutComp(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        var file = fileChooser.showSaveDialog(null);
        if (file == null)
            return;
        outCompText.setText(file.getAbsolutePath());
    }

    public void compress(ActionEvent actionEvent) {
        try {
            Compressor.compress(inCompText.getText(), outCompText.getText()
                    , new LZ77(16, 7), passComp.getText());
            new Alert(Alert.AlertType.INFORMATION, "Done").showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error").showAndWait();
        }
    }
}
