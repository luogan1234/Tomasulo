package tomasulo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;

public class UIController {
    // 0: not running, 1: running, 2: finish
    private IntegerProperty tomasulo_state = new SimpleIntegerProperty(0);
    public IntegerProperty state() { return tomasulo_state; }
    
    private int step = 5; 
    
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
    @FXML
    private Button settingButton;
    @FXML
    private Label timeLabel;
    
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
    
    // InstStatusTable
    @FXML
    private TableView<InstructionTableItem> instStatusTable;
    @FXML
    private TableColumn<InstructionTableItem, String> instStatusTableIssueCol;
    @FXML
    private TableColumn<InstructionTableItem, String> instStatusTableCompCol;
    @FXML
    private TableColumn<InstructionTableItem, String> instStatusTableWriteCol;
    
    // LoadTable
    @FXML
    private TableView<BufferTableItem> loadTable;
    @FXML
    private TableColumn<BufferTableItem, String> loadTableTimeCol;
    @FXML
    private TableColumn<BufferTableItem, String> loadTableNameCol;
    @FXML
    private TableColumn<BufferTableItem, String> loadTableBusyCol;
    @FXML
    private TableColumn<BufferTableItem, String> loadTableAddrCol;
    
    // StoreTable
    @FXML
    private TableView<BufferTableItem> storeTable;
    @FXML
    private TableColumn<BufferTableItem, String> storeTableTimeCol;
    @FXML
    private TableColumn<BufferTableItem, String> storeTableNameCol;
    @FXML
    private TableColumn<BufferTableItem, String> storeTableBusyCol;
    @FXML
    private TableColumn<BufferTableItem, String> storeTableAddrCol;
    @FXML
    private TableColumn<BufferTableItem, String> storeTableQjCol;
    
    // ReserveTable
    @FXML
    private TableView<BufferTableItem> reserveTable;
    @FXML
    private TableColumn<BufferTableItem, String> reserveTableTimeCol;
    @FXML
    private TableColumn<BufferTableItem, String> reserveTableNameCol;
    @FXML
    private TableColumn<BufferTableItem, String> reserveTableBusyCol;
    @FXML
    private TableColumn<BufferTableItem, String> reserveTableOpCol;
    @FXML
    private TableColumn<BufferTableItem, String> reserveTableVjCol;
    @FXML
    private TableColumn<BufferTableItem, String> reserveTableVkCol;
    @FXML
    private TableColumn<BufferTableItem, String> reserveTableQjCol;
    @FXML
    private TableColumn<BufferTableItem, String> reserveTableQkCol;
    
    // FloutRegTable
    @FXML
    private TableView<StringProperty[]> floatRegTable;
    
    // regTable
    @FXML
    private TableView<StringProperty[]> regTable;
    
    // ReserveTable
    @FXML
    private TableView<MemoryTableItem> memoryTable;
    @FXML
    private TableColumn<MemoryTableItem, Number> memoryTableAddrCol;
    @FXML
    private TableColumn<MemoryTableItem, Number> memoryTableValueCol;
    
    private Main mainApp;

    @FXML
    public void initialize() {
        // inst state
        state().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                switch (newValue.intValue()) {
                case 0:
                    inputButton.setDisable(false);
                    loadButton.setDisable(false);
                    nextButton.setDisable(true);
                    nextNButton.setDisable(true);
                    break;
                case 1:
                    inputButton.setDisable(true);
                    loadButton.setDisable(true);
                    nextButton.setDisable(false);
                    nextNButton.setDisable(false);
                    break;
                case 2:
                    inputButton.setDisable(true);
                    loadButton.setDisable(true);
                    nextButton.setDisable(true);
                    nextNButton.setDisable(true);
                    break;
                default:
                    break;
                }
            }
        });
        
        // init instTable
        instTableNameCol.setCellValueFactory(cellData -> cellData.getValue().typeName);
        instTableDestCol.setCellValueFactory(cellData -> cellData.getValue().op1);
        instTableSjCol.setCellValueFactory(cellData -> cellData.getValue().op2);
        instTableSkCol.setCellValueFactory(cellData -> cellData.getValue().op3);
        
        // init instStatusTable
        instStatusTableIssueCol.setCellValueFactory(cellData -> cellData.getValue().issue);
        instStatusTableCompCol.setCellValueFactory(cellData -> cellData.getValue().execComp);
        instStatusTableWriteCol.setCellValueFactory(cellData -> cellData.getValue().writeResult);
        
        // init loadTable
        loadTableTimeCol.setCellValueFactory(cellData -> cellData.getValue().time);
        loadTableNameCol.setCellValueFactory(cellData -> cellData.getValue().name);
        loadTableBusyCol.setCellValueFactory(cellData -> cellData.getValue().busy);
        loadTableAddrCol.setCellValueFactory(cellData -> cellData.getValue().address);
        
        // init storeTable
        storeTableTimeCol.setCellValueFactory(cellData -> cellData.getValue().time);
        storeTableNameCol.setCellValueFactory(cellData -> cellData.getValue().name);
        storeTableBusyCol.setCellValueFactory(cellData -> cellData.getValue().busy);
        storeTableAddrCol.setCellValueFactory(cellData -> cellData.getValue().address);
        storeTableQjCol.setCellValueFactory(cellData -> cellData.getValue().Qj);
        
        // init reserveTable
        reserveTableTimeCol.setCellValueFactory(cellData -> cellData.getValue().time);
        reserveTableNameCol.setCellValueFactory(cellData -> cellData.getValue().name);
        reserveTableBusyCol.setCellValueFactory(cellData -> cellData.getValue().busy);
        reserveTableOpCol.setCellValueFactory(cellData -> cellData.getValue().op);
        reserveTableVjCol.setCellValueFactory(cellData -> cellData.getValue().Vj);
        reserveTableVkCol.setCellValueFactory(cellData -> cellData.getValue().Vk);
        reserveTableQjCol.setCellValueFactory(cellData -> cellData.getValue().Qj);
        reserveTableQkCol.setCellValueFactory(cellData -> cellData.getValue().Qk);
        
        // init memoryTable
        memoryTableAddrCol.setCellValueFactory(cellData -> cellData.getValue().address);
        memoryTableValueCol.setCellValueFactory(cellData -> cellData.getValue().value);
        
        // make memory Editable
        memoryTableValueCol.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        memoryTableValueCol.setOnEditCommit(
                new EventHandler<CellEditEvent<MemoryTableItem, Number>>() {
                    @Override
                    public void handle(CellEditEvent<MemoryTableItem, Number> t) {
                        ((MemoryTableItem) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).updateFromUI(t.getNewValue());
                    }
                }
            );
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
        
        addInstData();
    }

    @FXML
    public void loadInsts() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showOpenDialog(mainApp.pStage);
        if (file != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 
                    System.out.println(tempString);
                    if (mainApp.analyzer.addInst(tempString) == false) {
                        Alert alert = new Alert(AlertType.ERROR, "指令格式不正确！");
                        alert.showAndWait();
                        break;
                    }
                    addInstData();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
    }
    
    @FXML
    public void clear() {
        System.out.println("Clear called!");
        state().set(0);
        mainApp.core.clear();
        mainApp.getInstData().clear();
        updateAll();
    }
    
    @FXML
    public void setN() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(step));
        dialog.setTitle("设置多步执行步数");
        dialog.setHeaderText("多步时，每次执行次数：");

        dialog.showAndWait().ifPresent(response -> {
            System.out.println(response);
            Integer i = Integer.valueOf(response);
            if (i.intValue() < 2) {
                Alert alert = new Alert(AlertType.ERROR, "步数格式不正确！");
                alert.showAndWait();
                return;
            }
            step = i.intValue();
        });
        
    }
    
    @FXML
    public int next() {
        if (state().get() == 0) {
            state().set(1);
            mainApp.core.start();
        }
        if (!mainApp.core.next()) {
            updateAll();
        } else {
            updateAll();
            state().set(2);
            return -1;
        }
        return 0;
    }
    
    @FXML
    public void nextN() {
        for (int i = 0; i < step; ++i)
            if (next() != 0)
                return;
    }
    
    public void addInstData() {
        if (mainApp.getInstData().size() < mainApp.core.num) {
            for (int i = mainApp.getInstData().size(); i < mainApp.core.num; ++i) {
                mainApp.getInstData().add(new InstructionTableItem(mainApp.core.insts[i]));
            }
            nextButton.setDisable(false);
        }
    }
    
    public void updateAll() {
        updateInstr(mainApp.getInstData());
        updateBuffer(mainApp.getLoadData());
        updateBuffer(mainApp.getStoreData());
        updateBuffer(mainApp.getReserveData());
        mainApp.getFloatRegTableContent().update();
        mainApp.getRegTableContent().update();
        timeLabel.setText(String.valueOf(mainApp.core.round));
    }
    
    public void updateBuffer(ObservableList<BufferTableItem> list) {
        for (int i = 0; i < list.size(); ++i)
            list.get(i).update();
    }
    
    public void updateInstr(ObservableList<InstructionTableItem> list) {
        for (int i = 0; i < list.size(); ++i)
            list.get(i).update();
    }
    
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        // init floatRegTable
        TableColumn<StringProperty[], String> column;
        for (int i = 0; i < mainApp.core.resource.freg.length; ++i) {
            column = new TableColumn<StringProperty[], String>("F"+String.valueOf(i));
            column.setPrefWidth(floatRegTable.getPrefWidth()/(mainApp.core.resource.freg.length+1));
            final int ii = i;
            column.setCellValueFactory(cellData -> cellData.getValue()[ii]);
            floatRegTable.getColumns().add(column);
        }
        
        // init regTable
        for (int i = 0; i < mainApp.core.resource.reg.length; ++i) {
            column = new TableColumn<StringProperty[], String>("R"+String.valueOf(i));
            column.setPrefWidth(regTable.getPrefWidth()/(mainApp.core.resource.reg.length+1));
            final int ii = i;
            column.setCellValueFactory(cellData -> cellData.getValue()[ii]);
            regTable.getColumns().add(column);
        }

        // Add observable list data to the table
        instTable.setItems(mainApp.getInstData());
        instStatusTable.setItems(mainApp.getInstData());
        loadTable.setItems(mainApp.getLoadData());
        storeTable.setItems(mainApp.getStoreData());
        reserveTable.setItems(mainApp.getReserveData());
        floatRegTable.setItems(mainApp.getFloatRegTableData());
        regTable.setItems(mainApp.getRegTableData());
        memoryTable.setItems(mainApp.getMemoryData());
        
    }
}
