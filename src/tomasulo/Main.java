package tomasulo;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
    private TomasuloCore core;
    private Analyzer analyzer;
    private Stage pStage;
    
    // 0: not running, 1: running, 2: finish
    private int tomsulo_state = 0;
    
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
    private TableView<Instruction> instTable;
    
    public void init() {

    }

    @Override
    public void start(Stage primaryStage) {
        try {
            pStage = primaryStage;
            core = new TomasuloCore();
            analyzer = new Analyzer(core);

            AnchorPane root = FXMLLoader.load(getClass().getClassLoader().getResource("TomasuloUI.fxml"));
            
            init();
            
            Scene scene = new Scene(root, 640, 600);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void inputInst() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("输入指令");
        dialog.setHeaderText("请输入单条指令：");

        dialog.showAndWait().ifPresent(response -> {
            if (analyzer.addInst(response) == false) {
                Alert alert = new Alert(AlertType.ERROR, "指令格式不正确！");
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void loadInsts() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
        File file = fileChooser.showOpenDialog(pStage);
        if (file != null) {
            System.out.println("Your name: " + file.getName());
        }
    }

    public static void main(String[] args) {
        // launch(args);
        testAnalyzer();
    }

    public static void testAnalyzer() {
        TomasuloCore core = new TomasuloCore();
        Analyzer analyzer = new Analyzer(core);
        
    	analyzer.tomasulo.clear();
    	analyzer.addInst("LD R6 34 R2");
    	analyzer.addInst("LD R2 45 R3");
    	analyzer.addInst("MULD R0 R2 R4");
    	analyzer.addInst("SUBD R8 R6 R2");
    	analyzer.addInst("DIVD R10 R0 R6");
    	analyzer.addInst("ADDD R6 R8 R2");
    	analyzer.tomasulo.start();
    	while (!analyzer.tomasulo.next()) {
    		analyzer.tomasulo.print();
    	}
    	analyzer.tomasulo.print();
    }

    public static void test()
    {
    	TomasuloCore tomasulo=new TomasuloCore();
    	tomasulo.clear();
    	int CASE=1;
    	switch (CASE)
    	{
    	case 1:	//ppt样例
    		tomasulo.addInst(InstType.LD,6,34,2);
        	tomasulo.addInst(InstType.LD,2,45,3);
        	tomasulo.addInst(InstType.MULTD,0,2,4);
        	tomasulo.addInst(InstType.SUBD,8,6,2);
        	tomasulo.addInst(InstType.DIVD,10,0,6);
        	tomasulo.addInst(InstType.ADDD,6,8,2);
        	break;
    	case 2:	//浮点乘除法器流水线，IF(1),ID(1),EX,MEM(1),WB(1)，乘法EX(6)，除法EX(6*6)
    		tomasulo.resource.freg[1].value=10;
        	tomasulo.resource.freg[2].value=4;
        	tomasulo.addInst(InstType.MULTD,0,1,2);
        	tomasulo.addInst(InstType.DIVD,3,4,5);
        	break;
    	case 3:	//LD ST同一位置
    		tomasulo.resource.freg[1].value=10;
        	tomasulo.resource.freg[2].value=4;
        	tomasulo.addInst(InstType.MULTD,0,1,2);
        	tomasulo.addInst(InstType.ST,0,5,1);
    		tomasulo.addInst(InstType.LD,4,5,2);
    	}
    	tomasulo.start();
    	while (!tomasulo.next())
    		tomasulo.print();
    	tomasulo.print();
    }

}
