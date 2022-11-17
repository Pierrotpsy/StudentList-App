package hellofx;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.validator.routines.EmailValidator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StudentController implements Initializable {

    @FXML
    private ComboBox<String> box_gender;

    @FXML
    private Button bttn_add;

    @FXML
    private Button bttn_cancel;

    @FXML
    private Button bttn_delete;

    @FXML
    private Button bttn_save;

    @FXML
    private Button bttn_update;

    @FXML
    private Label lbl_average;

    @FXML
    private Label lbl_comments;

    @FXML
    private Label lbl_date;

    @FXML
    private Label lbl_email;

    @FXML
    private Label lbl_gender;

    @FXML
    private Label lbl_list;

    @FXML
    private Label lbl_mark;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_photo;

    @FXML
    private Label lbl_student;
    
    @FXML
    private Label lbl_errors;

    @FXML
    private DatePicker picker_date;

    @FXML
    private ListView<Student> students_list;

    @FXML
    private TextArea txt_average;

    @FXML
    private TextArea txt_comments;

    @FXML
    private TextArea txt_email;

    @FXML
    private TextArea txt_mark;

    @FXML
    private TextArea txt_name;

    @FXML
    private TextField txt_path;

    @FXML
    private ImageView view_image;

    private DBManager manager = new DBManager();
    private EmailValidator validator = EmailValidator.getInstance();
    
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		List<String> gvalues = new ArrayList<String>();
		 gvalues.add("Male");
		 gvalues.add("Female");
		 ObservableList<String> gender = FXCollections.observableArrayList(gvalues);
		 box_gender.setItems(gender);

		 picker_date.setValue(LocalDate.now());
		 txt_mark.setText("0");

		 fetchStudents();
		
		 students_list.getSelectionModel().selectedItemProperty().addListener(e->displayStudentDetails(students_list.getSelectionModel().getSelectedItem()));

	}
	private void displayStudentDetails(Student selectedStudent) {
		if(selectedStudent!=null){
			txt_name.setText(selectedStudent.getName());
			box_gender.setValue(selectedStudent.getGender());
			txt_email.setText(selectedStudent.getEmail());
			picker_date.setValue(selectedStudent.getBirthdate());
			txt_path.setText(selectedStudent.getPhoto());
			txt_mark.setText(String.valueOf(selectedStudent.getMark()));
			txt_comments.setText(selectedStudent.getComments());
			Image image = new Image(selectedStudent.getPhoto());
			view_image.setImage(image);
		}
	}
	
	public void fetchStudents() {
		List<Student> listStudents=manager.loadStudents();
		if (listStudents!=null) {
			ObservableList<Student> students;
			students= FXCollections.observableArrayList(listStudents);
			students_list.setItems(students);
		}
		txt_average.setText(String.format("%.3f", manager.meanMarks()));
	}
	
	public void onNew(){
		students_list.getSelectionModel().clearSelection();
		 this.txt_name.setText(null);
		 this.box_gender.setValue(null);
		 txt_email.setText(null);
		 picker_date.setValue(LocalDate.now());
		 txt_path.setText(null);
		 txt_mark.setText("0");
		 view_image.setImage(null);
		 txt_comments.setText(null);
	}
	
	public void onCancel(){
		students_list.getSelectionModel().selectFirst();
		displayStudentDetails(students_list.getSelectionModel().getSelectedItem());
		
	}
	
	public void onSave() {
		if(txt_name.getText() == null || txt_name.getText() == "") {
			lbl_errors.setText("Please enter a name");
			return;
		} else if(box_gender.getValue() == null) {
			lbl_errors.setText("Please choose a gender");
			return;
		} else if(validator.isValid(txt_email.getText())) {
			lbl_errors.setText("Invalid email");
			return;
		} else if((picker_date.getValue().getYear() < 1980 || picker_date.getValue().getYear() > 2000)) {
			lbl_errors.setText("Birth year must be between 1980 and 2000");
			return;
		} else if(Double.valueOf(txt_mark.getText()) < 0 || Double.valueOf(txt_mark.getText()) > 20) {
			lbl_errors.setText("Mark must be between 0 and 20");
			return;
		}
		 Student s= new Student((int)Math.floor(Math.random()*(100000)+1), txt_name.getText(),box_gender.getValue(), txt_email.getText(), picker_date.getValue(), txt_path.getText(), Double.valueOf(txt_mark.getText()), txt_comments.getText());
		 manager.addStudent(s);
		 fetchStudents();
	}
	
	public void onEdit() {
		if(txt_name.getText() == null || txt_name.getText() == "") {
			lbl_errors.setText("Please enter a name");
			return;
		} else if(box_gender.getValue() == null) {
			lbl_errors.setText("Please choose a gender");
			return;
		} else if(validator.isValid(txt_email.getText())) {
			lbl_errors.setText("Invalid email");
			return;
		} else if((picker_date.getValue().getYear() < 1980 || picker_date.getValue().getYear() > 2000)) {
			lbl_errors.setText("Birth year must be between 1980 and 2000");
			return;
		} else if(Double.valueOf(txt_mark.getText()) < 0 || Double.valueOf(txt_mark.getText()) > 20) {
			lbl_errors.setText("Mark must be between 0 and 20");
			return;
		}
		int id = students_list.getSelectionModel().getSelectedItem().getId();
		Student s= new Student(id, txt_name.getText(),box_gender.getValue(), txt_email.getText(), picker_date.getValue(), txt_path.getText(), Double.valueOf(txt_mark.getText()), txt_comments.getText());
		manager.updateStudent(s);
		fetchStudents();
	}
	
	public void onDelete() {
		int id = students_list.getSelectionModel().getSelectedItem().getId();
		manager.deleteStudent(id);
		fetchStudents();
		students_list.getSelectionModel().selectFirst();
		displayStudentDetails(students_list.getSelectionModel().getSelectedItem());
	}
	





}