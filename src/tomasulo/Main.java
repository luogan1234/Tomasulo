package tomasulo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
    public TomasuloCore core;
    public Analyzer analyzer;
    public Stage pStage;

    private ObservableList<InstructionTableItem> instTableList = FXCollections.observableArrayList();
    public ObservableList<InstructionTableItem> getInstData() {
        return instTableList;
    }

    private ObservableList<BufferTableItem> loadTableList = FXCollections.observableArrayList();
    public ObservableList<BufferTableItem> getLoadData() {
        return loadTableList;
    }
    
    private ObservableList<BufferTableItem> storeTableList = FXCollections.observableArrayList();
    public ObservableList<BufferTableItem> getStoreData() {
        return storeTableList;
    }
    
    private ObservableList<BufferTableItem> reserveTableList = FXCollections.observableArrayList();
    public ObservableList<BufferTableItem> getReserveData() {
        return reserveTableList;
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            pStage = primaryStage;
            analyzer = new Analyzer();
            core = analyzer.tomasulo;
            core.clear();
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("TomasuloUI.fxml"));
            AnchorPane root =loader.load();
            
            for (int i = 0; i < core.resource.ldBuffer.length; ++i)
                loadTableList.add(new BufferTableItem(core.resource.ldBuffer[i]));
            
            for (int i = 0; i < core.resource.stBuffer.length; ++i)
                storeTableList.add(new BufferTableItem(core.resource.stBuffer[i]));
            
            for (int i = 0; i < core.resource.addBuffer.length; ++i)
                reserveTableList.add(new BufferTableItem(core.resource.addBuffer[i]));
            for (int i = 0; i < core.resource.multBuffer.length; ++i)
                reserveTableList.add(new BufferTableItem(core.resource.multBuffer[i]));
            
            UIController controller = loader.getController();
            controller.setMainApp(this);
            
            Scene scene = new Scene(root, 640, 700);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Tomasulo模拟器 by 罗干 高童 王笑晗");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void testAnalyzer() {
        Analyzer analyzer = new Analyzer();
        
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
