package com.vupt.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MyController {
    @FXML
    TextField tfFileRoot ;
    @FXML
    TextField tfFolder;
    @FXML
    TextField tfFilePath;
    private List<GiayNghiBHXHDetail> giayNghiBHXHDetails;
    @Autowired
    ModelMapper modelMapper;
    public static void loadView(Stage stage) throws IOException {
        FXMLLoader loader= new FXMLLoader(MyController.class.getResource("/sample.fxml"));
        loader.setControllerFactory(Main.getApplicationContext()::getBean);
        Parent view = loader.load();
        stage.setTitle("Xuất báo cáo giấy nghỉ BHXH");
        stage.setScene(new Scene(view));
        stage.show();
    }
    public void importValue() throws IOException {
        if(!tfFileRoot.getText().trim().isEmpty()){
            this.giayNghiBHXHDetails=ReadExcelExample.readExcel(tfFileRoot.getText());
            sortGiayNghiBHXHDetails();
            CustomAlert.AlertBuilder.builder(Alert.AlertType.INFORMATION)
                    .setTitle("Nhập dữ liệu")
                    .setHeaderText(null)
                    .setContentText("Nhập thành công")
                    .build().show();
        }


    }
    public void exportValue() throws IOException {
        if(!tfFilePath.getText().trim().isEmpty()){
            List<GiayNghiBHXHDTO> giayNghiBHXHDTOS= this.giayNghiBHXHDetails.stream()
                    .map(detail-> modelMapper.map(detail,GiayNghiBHXHDTO.class))
                    .collect(Collectors.toList());
            WriteExcelExample.writeExcel(giayNghiBHXHDTOS,tfFilePath.getText());
            CustomAlert.AlertBuilder.builder(Alert.AlertType.INFORMATION)
                    .setTitle("Xuất dữ liệu")
                    .setHeaderText(null)
                    .setContentText("Xuất thành công")
                    .build().show();
        }

    }
    @FXML
    public void openRootFile(){
        FileChooser fileChooser=new FileChooser();
        File file= fileChooser.showOpenDialog(tfFilePath.getScene().getWindow());
        if(file!=null){
            tfFileRoot.setText(file.getAbsolutePath());
        }
        else
            System.out.println("No directory selected.");
    }

    public void sortGiayNghiBHXHDetails(){
        // Sắp xếp ArrayList theo thuộc tính tên
       Collections.sort(giayNghiBHXHDetails, new Comparator<GiayNghiBHXHDetail>() {
           @Override
           public int compare(GiayNghiBHXHDetail o1, GiayNghiBHXHDetail o2) {
               String hoten1=o1.getHO_TEN();
               String hoten2=o2.getHO_TEN();
               String name1=hoten1.substring(hoten1.lastIndexOf(" "));
               String name2=hoten2.substring(hoten2.lastIndexOf(" "));
               int compareResult=name1.compareTo(name2);
               if(compareResult==0){
                   String last1=hoten1.substring(0,hoten1.indexOf(" "));
                   String last2=hoten2.substring(0,hoten2.indexOf(" "));
                   return  last1.compareTo(last2);
               }
               else
                   return  compareResult;
           }
       });

        for(int i=0;i<giayNghiBHXHDetails.size();i++){
            giayNghiBHXHDetails.get(i).setSTT(String.valueOf(i+1));
        }
    }
    @FXML
    public void openFolder(){
        DirectoryChooser directoryChooser=new DirectoryChooser();
        directoryChooser.setTitle("Select a Directory");

        // Hiển thị hộp thoại chọn thư mục khi nút được nhấn
        File selectedDirectory = directoryChooser.showDialog((Stage)tfFolder.getScene().getWindow());

        if (selectedDirectory != null) {
            System.out.println("Selected directory: " + selectedDirectory.getAbsolutePath());
            tfFolder.setText(selectedDirectory.getAbsolutePath());
            tfFilePath.setText(tfFolder.getText()+"\\"+ "GiayNghiBHXH.xls");
        } else {
            System.out.println("No directory selected.");
        }
    }
}
