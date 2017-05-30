package tomasulo;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class Main /*extends Application*/ {
/*    private TomasuloCore core;

    @Override
    public void start(Stage primaryStage) {
        try {
            core = new TomasuloCore();

            Button btn = new Button();
            btn.setText(core.bProperty().toString());
            core.bProperty().addListener(new ChangeListener<Object>(){
                @Override
                public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                    btn.setText(core.bProperty().toString());
                }
            });

            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    core.action();
                }
            });

            StackPane root = new StackPane();
            root.getChildren().add(btn);
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) {
    //    launch(args);
    	test();
    }
    
    public static void test()
    {
    	TomasuloCore tomasulo=new TomasuloCore();
    	tomasulo.clear();
    	int CASE=3;
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
        	tomasulo.addInst(InstType.DIVD,0,1,2);
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
