package tomasulo;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class UIController {
    
    @FXML
    private Button inputButton;
    
    @FXML
    private Button loadButton;
    
    @FXML
    private Button nextButton;
    
    @FXML
    private Button nextNButton;
    
    @FXML
    private Button clearButton;
    
    // InstTable
    @FXML
    private TableView<InstructionTableItem> instTable;
    @FXML
    private TableColumn<InstructionTableItem, String> instTableNameCol;
    @FXML
    private TableColumn<InstructionTableItem, String> instTableDestCol;
    @FXML
    private TableColumn<InstructionTableItem, String> instTableSjCol;
    @FXML
    private TableColumn<InstructionTableItem, String> instTableSkCol;
    
    private Main mainApp;

    @FXML
    public void initialize() {
        // init instTable
        instTableNameCol.setCellValueFactory(cellData -> cellData.getValue().typeName);
        instTableDestCol.setCellValueFactory(cellData -> cellData.getValue().op1);
        instTableSjCol.setCellValueFactory(cellData -> cellData.getValue().op2);
        instTableSkCol.setCellValueFactory(cellData -> cellData.getValue().op3);
    }
    
    @FXML
    public void inputInst() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("输入指令");
        dialog.setHeaderText("请输入单条指令：");

        dialog.showAndWait().ifPresent(response -> {
            System.out.println(response);
            if (mainApp.analyzer.addInst(response) == false) {
                Alert alert = new Alert(AlertType.ERROR, "指令格式不正确！");
                alert.showAndWait();
                return;
            }
        });
        
        if (mainApp.getInstData().size() < mainApp.core.num) {
            for (int i = mainApp.getInstData().size(); i < mainApp.core.num; ++i) {
                mainApp.getInstData().add(new InstructionTableItem(mainApp.core.insts[i]));
            }
        }
    }

    @FXML
    public void loadInsts() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
        File file = fileChooser.showOpenDialog(mainApp.pStage);
        if (file != null) {
            System.out.println("Your name: " + file.getName());
        }
    }
    
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        instTable.setItems(mainApp.getInstData());
    }
}
