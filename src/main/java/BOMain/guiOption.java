package BOMain;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;

public class guiOption extends Frame implements ActionListener {

	public static JProgressBar jprogressbar;
	static String endMessage = "Not done";

	Label inputBOServerLabel;
	Label usernameLabel;
	Label passwordLabel;
	Label inputReportIdLabel;
	Label csvFileSaveLabel;
//	Label KPI;
//	Label updateReportLabel;
//	Label tabUpdatingElementLabel;
//	Label saveElementsLabel;
//	Label uploadUpdatedElementLabel;

	TextField inputBOServertxt;
	TextField usernametxt;
	TextField passwordtxt;
	TextField inputReportIdtxt;
	TextField csvFileSavetxt;
//	TextField KPItxt;
//	Choice selectTab;
	
//	TextField saveElementstxt;
//	TextField uploadUpdatedElementtxt;
    
	Button csvFileSave;
	Button create;
	Button cancel;
	Button elementsDataSave;
	Button uploadUpdatedElement;
	
//		Button update;
//		Button cancelUpdate;
//	
//    ButtonGroup yesNoButtonGroup;
//    JRadioButton yesButton, noButton;
    
	LinkedHashMap<String, Integer> tabNameIdMap = new LinkedHashMap<>();

	public guiOption() {

		addWindowListener(new fdmyWindowAdapter());
		setBackground(Color.lightGray);
		setForeground(Color.black);
		setLayout(null);
		jprogressbar = new JProgressBar();
		
//		yesNoButtonGroup = new ButtonGroup();
//      yesButton = new JRadioButton("Yes");
//      noButton = new JRadioButton("No", true);

		inputBOServerLabel = new Label("Enter BO Server Link", Label.LEFT);
		usernameLabel = new Label("Enter Username", Label.LEFT);
		passwordLabel = new Label("Enter Password", Label.LEFT);
		inputReportIdLabel = new Label("Enter Report ID", Label.LEFT);
		csvFileSaveLabel = new Label("Save CSV Files at Path", Label.LEFT);
//		updateReportLabel = new Label("Do you want to update Report?", Label.LEFT);
//		tabUpdatingElementLabel = new Label("Select Tab for updating Reports", Label.LEFT);
//		saveElementsLabel = new Label("Save Elements Data Excel at Path", Label.LEFT);
//		uploadUpdatedElementLabel = new Label("Upload the Updated Element Data Excel from Path", Label.LEFT);
		
		inputBOServertxt = new TextField();
		usernametxt = new TextField();
		passwordtxt = new TextField();
		inputReportIdtxt = new TextField();
		csvFileSavetxt = new TextField();
		
//		selectTab = new Choice();  
//		
//		saveElementstxt = new TextField();
//		uploadUpdatedElementtxt = new TextField();

		csvFileSave = new Button("..");
		create = new Button("Create");
		cancel = new Button("Cancel");
//		elementsDataSave = new Button("...");
//		uploadUpdatedElement = new Button("....");
//		update = new Button("Update");
//		cancelUpdate = new Button("Cancel");
//		
//		  add(yesButton);
//        add(noButton);
//        yesNoButtonGroup.add(yesButton);
//        yesNoButtonGroup.add(noButton);

		add(inputBOServerLabel);
		add(usernameLabel);
		add(passwordLabel);
		add(inputReportIdLabel);
		add(csvFileSaveLabel);
//		add(updateReportLabel);
//		add(tabUpdatingElementLabel);
//		add(saveElementsLabel);
//		add(uploadUpdatedElementLabel);
		
		add(inputBOServertxt);
		add(usernametxt);
		add(passwordtxt);
		add(inputReportIdtxt);
		add(csvFileSavetxt);
		
//		add(selectTab);
//		
//		add(saveElementstxt);
//		add(uploadUpdatedElementtxt);

		add(csvFileSave);
		add(create);
		add(cancel);
//	    add(elementsDataSave);
//		add(uploadUpdatedElement);
//		add(update);
//		add(cancelUpdate);
//		
//		yesButton.setBounds(420, 325, 60, 30);
//      noButton.setBounds(490, 325, 70, 30);

		inputBOServerLabel.setBounds(25, 85, 300, 30);
		usernameLabel.setBounds(25, 125, 300, 30);
		passwordLabel.setBounds(25, 165, 300, 30);
		inputReportIdLabel.setBounds(25, 205, 300, 30);
		csvFileSaveLabel.setBounds(25, 245, 300, 30);
//		updateReportLabel.setBounds(25, 325, 300, 30);
//		tabUpdatingElementLabel.setBounds(25, 365, 300, 30);
//		saveElementsLabel.setBounds(25, 405, 300, 30);
//		uploadUpdatedElementLabel.setBounds(25, 445, 300, 30);
		
		inputBOServertxt.setBounds(325, 85, 400, 30);
		usernametxt.setBounds(325, 125, 400, 30);
		passwordtxt.setBounds(325, 165, 400, 30);
		inputReportIdtxt.setBounds(325, 205, 400, 30);
		csvFileSavetxt.setBounds(325, 245, 400, 30);
		
//		selectTab.setBounds(325, 365, 400, 30);
//		
//		saveElementstxt.setBounds(325, 405, 400, 30);
//		uploadUpdatedElementtxt.setBounds(325, 445, 400, 30);
		
		csvFileSave.setBounds(750, 245, 50, 30);
		create.setBounds(420, 285, 60, 30);
		cancel.setBounds(490, 285, 70, 30);
//   	elementsDataSave.setBounds(750, 405, 50, 30);
//	    uploadUpdatedElement.setBounds(750, 445, 50, 30);
//		update.setBounds(420, 485, 60, 30);
//		cancelUpdate.setBounds(490, 485, 70, 30);

		csvFileSavetxt.setText("C:\\Users\\user\\Documents");
//		saveElementstxt.setText("C:\\Users\\user\\Documents");
//		uploadUpdatedElementtxt.setText("C:\\Users\\user\\Documents");
//		
//		yesButton.addActionListener(this);
//      yesButton.setActionCommand("EnableFields");
//
//        noButton.addActionListener(this);
//        noButton.setActionCommand("DisableFields");
//        noButton.setSelected(true);

		csvFileSave.addActionListener(this);
		create.addActionListener(this);
		cancel.addActionListener(this);
//	    elementsDataSave.addActionListener(this);
//	    uploadUpdatedElement.addActionListener(this);
//		update.addActionListener(this);
//		cancelUpdate.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) {
		
//		 if (ae.getActionCommand().equals("DisableFields")) {
//			 selectTab.setEnabled(false);
//			 saveElementstxt.setEnabled(false);
//			 uploadUpdatedElementtxt.setEnabled(false);
//	        }
//
//	        if (ae.getActionCommand().equals("EnableFields")) {
//	        	selectTab.setEnabled(true);
//	        	saveElementstxt.setEnabled(true);
//	        	uploadUpdatedElementtxt.setEnabled(true);
//	        }

		if (ae.getActionCommand().equals("..")) {
			System.out.println("Enter the csv output directory");
			saveCSVFile();
		}
		
		if (ae.getActionCommand().equals("Create")) {
			disableFields();
			validationReqInputs();
			try {
				initiateProcess1();
			} catch (Exception e) {
				e.printStackTrace();
			}
			enableFields();
		}
	}
//		if (yesButton.isSelected()) {
//			disableFields();
//			validationReqYesMethod();
//			try {
//				initiateProcess2();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			enableFields();
//		}

//		if (ae.getActionCommand().equals("...")) {
//			System.out.println("Select the Element Data Excel File directory");
//			saveExcelFile();
//			disableFields();
//			validationReqExcelOutputMethod();
//			String excelOutputPath = saveElementstxt.getText();
//			String selectedItem = selectTab.getSelectedItem();
//			if(!selectedItem.equals("Select Tab")) {
//				int tabId = tabNameIdMap.get(selectedItem);
//				try {
//					Program.getElementDataInExcelFile(excelOutputPath, selectedItem, tabId);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			enableFields();
//		}
		
//		if (ae.getActionCommand().equals("....")) {
//			System.out.println("Select the Updated Element Data Excel File to be Uploaded");
//			uploadUpdatedExcelFile();
//		}
		
//		if (ae.getActionCommand().equals("Update")) {
//			disableFields();
//			validationReqInputsUpdateMethod();
//			String uploadUpdatedExcelFile = uploadUpdatedElementtxt.getText();
//			String selectedItem = selectTab.getSelectedItem();
//			if(!selectedItem.equals("Select Tab")) {
//				int tabId = tabNameIdMap.get(selectedItem);
//				try {
//					Program.putUpdatedElementDataInFile(uploadUpdatedExcelFile, selectedItem, tabId);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			enableFields();
//		}
//		
//		if (ae.getActionCommand().equals("Cancel")) {
//			System.exit(0);
//		}
//		
//	}

	public void saveCSVFile() {
		final JFileChooser jfc = new JFileChooser();
		File selectedFile = jfc.getSelectedFile();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setDialogTitle("Save As");
		jfc.showSaveDialog(null);
		csvFileSavetxt.setText(jfc.getSelectedFile().toString());
	}
	
//	public void saveExcelFile() {
//		final JFileChooser jfc = new JFileChooser();
//		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		jfc.setDialogTitle("Save As");
//		jfc.showSaveDialog(null);
//		saveElementstxt.setText(jfc.getSelectedFile().toString());
//	}
//	
//	public void uploadUpdatedExcelFile() {
//		final JFileChooser jfc = new JFileChooser();
//		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//		jfc.setDialogTitle("Upload");
//		jfc.showSaveDialog(null);
//		uploadUpdatedElementtxt.setText(jfc.getSelectedFile().toString());
//	}

	public void disableFields() {

		inputBOServerLabel.setEnabled(false);
		usernameLabel.setEnabled(false);
		passwordLabel.setEnabled(false);
		inputReportIdLabel.setEnabled(false);
		csvFileSaveLabel.setEnabled(false);
//		updateReportLabel.setEnabled(false);
//		tabUpdatingElementLabel.setEnabled(false);
//		saveElementsLabel.setEnabled(false);
//		uploadUpdatedElementLabel.setEnabled(false);

		inputBOServertxt.setEnabled(false);
		usernametxt.setEnabled(false);
		passwordtxt.setEnabled(false);
		inputReportIdtxt.setEnabled(false);
		csvFileSavetxt.setEnabled(false);
		
//		yesButton.setEnabled(false);
//		noButton.setEnabled(false);
//		
//		selectTab.setEnabled(false);
//		
//		saveElementstxt.setEnabled(false);
//		uploadUpdatedElementtxt.setEnabled(false);

		csvFileSave.setEnabled(false);
		create.setEnabled(false);
//		elementsDataSave.setEnabled(false);
//		uploadUpdatedElement.setEnabled(false);
//		update.setEnabled(false);

	}

	public void enableFields() {

		inputBOServerLabel.setEnabled(true);
		usernameLabel.setEnabled(true);
		passwordLabel.setEnabled(true);
		inputReportIdLabel.setEnabled(true);
		csvFileSaveLabel.setEnabled(true);
//		updateReportLabel.setEnabled(true);
//		tabUpdatingElementLabel.setEnabled(true);
//		saveElementsLabel.setEnabled(true);
//		uploadUpdatedElementLabel.setEnabled(true);
		
		inputBOServertxt.setEnabled(true);
		usernametxt.setEnabled(true);
		passwordtxt.setEnabled(true);
		inputReportIdtxt.setEnabled(true);
		csvFileSavetxt.setEnabled(true);
		
//		yesButton.setEnabled(true);
//		noButton.setEnabled(true);
//		
//		selectTab.setEnabled(true);
//		
//		saveElementstxt.setEnabled(true);
//		uploadUpdatedElementtxt.setEnabled(true);
		
		csvFileSave.setEnabled(true);
		create.setEnabled(true);
//		elementsDataSave.setEnabled(true);
//		uploadUpdatedElement.setEnabled(true);
//		update.setEnabled(true);
		
	}

	public void validationReqInputs() {

		if (inputBOServertxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter the BO Server Link");
			System.out.println("Enter the BO Server Link");
		}

		if (usernametxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter Username");
			System.out.println("Enter Username");
		}

		if (passwordtxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter Password");
			System.out.println("Enter Password");
		}

		if (inputReportIdtxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter the Report ID");
			System.out.println("Enter the Report ID");
		}

		if (csvFileSavetxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Select path to save CSV file.");
			System.out.println("Select path to save CSV file.");
		}

	}
	
	public void validationReqYesMethod() {

		if (inputBOServertxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter the BO Server Link");
			System.out.println("Enter the BO Server Link");
		}

		if (usernametxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter Username");
			System.out.println("Enter Username");
		}

		if (passwordtxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter Password");
			System.out.println("Enter Password");
		}

		if (inputReportIdtxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter the Report ID");
			System.out.println("Enter the Report ID");
		}
	}
	
	public void validationReqExcelOutputMethod() {

		if (inputBOServertxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter the BO Server Link");
			System.out.println("Enter the BO Server Link");
		}

		if (usernametxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter Username");
			System.out.println("Enter Username");
		}

		if (passwordtxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter Password");
			System.out.println("Enter Password");
		}

		if (inputReportIdtxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter the Report ID");
			System.out.println("Enter the Report ID");
		}
		
//		if (noButton.isSelected()) {
//			JOptionPane.showMessageDialog(null, "Select Yes Button if you want to Update Report");
//			System.out.println("Select Yes Button if you want to Update Report");
//		}
//		
//		if (selectTab.getSelectedItem().equals("Select Tab")) {
//			JOptionPane.showMessageDialog(null, "Select the particular Tab of Report to be Updated");
//			System.out.println("Select the particular Tab of Report to be Updated");
//		}
	}
	
	
	
	public void validationReqInputsUpdateMethod() {

		if (inputBOServertxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter the BO Server Link");
			System.out.println("Enter the BO Server Link");
		}

		if (usernametxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter Username");
			System.out.println("Enter Username");
		}

		if (passwordtxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter Password");
			System.out.println("Enter Password");
		}

		if (inputReportIdtxt.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Enter the Report ID");
			System.out.println("Enter the Report ID");
		}
		
//		if (noButton.isSelected()) {
//			JOptionPane.showMessageDialog(null, "Select Yes Button if you want to Update Report");
//			System.out.println("Select Yes Button if you want to Update Report");
//		}
//		
//		if (selectTab.getSelectedItem().equals("Select Tab")) {
//			JOptionPane.showMessageDialog(null, "Select the particular tab of Report to be Updated");
//			System.out.println("Select the particular Tab of Report to be Updated");
//		}
//		
//		if (saveElementstxt.getText().equals("")) {
//			JOptionPane.showMessageDialog(null, "Enter the Element Data Excel File Output directory");
//			System.out.println("Enter the Element Data Excel File Output directory");
//		}
//		
//		if (uploadUpdatedElementtxt.getText().equals("")) {
//			JOptionPane.showMessageDialog(null, "Enter the Updated Element Data Excel File to be Uploaded");
//			System.out.println("Enter the Updated Element Data Excel File to be Uploaded");
//		}

	}

	public void initiateProcess1() throws Exception {

		String serverURI = inputBOServertxt.getText();
		String userName = usernametxt.getText();
		String passWord = passwordtxt.getText();
		String reportId = inputReportIdtxt.getText();
		String outputPath = csvFileSavetxt.getText();

		final Program program = new Program(serverURI, userName, passWord, reportId, outputPath);
		program.processQuerySpecification();
		program.processDocuments();
		program.processReports();
		program.closeAllFiles();
		showEndMsg1(outputPath);
		endMessage = "Done";

	}
	
	public void initiateProcess2() throws Exception {

		String serverURI = inputBOServertxt.getText();
		String userName = usernametxt.getText();
		String passWord = passwordtxt.getText();
		String reportId = inputReportIdtxt.getText();

		final Program program = new Program(serverURI, userName, passWord, reportId, null);
		tabNameIdMap = program.getTabNamesAndId();
//		selectTab.add("Select Tab");
//		
//		for(String s : tabNameIdMap.keySet()) {
//			selectTab.add(s);
//		}
	}
	
	public void showEndMsg1(String opath) {
		JOptionPane.showMessageDialog(null, "The output files available at : " + opath, "BO Reports Tool",
				JOptionPane.PLAIN_MESSAGE);
	}

}



