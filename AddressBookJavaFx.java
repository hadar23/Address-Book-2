import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Stack;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class AddressBookJavaFx extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		for (int i = 0; i <= AddressBookPane.MAX_PANES; i++) {
			AddressBookPane p = AddressBookPane.getInstance();
			if (p != null) {
				Pane pane = p.getPane();
				Scene scene = new Scene(pane);
				scene.getStylesheets().add("styles.css");
				primaryStage.setTitle("AddressBook");
				primaryStage.setScene(scene);
				primaryStage.show();
				primaryStage.setAlwaysOnTop(true);
				primaryStage = new Stage();
			} else
				System.out.println("Singelton violation. Only 3 panes were created");
		}
	}
}

class AddressBookPane extends GridPane {
	private Stack<CommandButton.Memento> stack = new Stack<CommandButton.Memento>();
	private TextField jtfName = new TextField();
	private TextField jtfStreet = new TextField();
	private TextField jtfCity = new TextField();
	private TextField jtfState = new TextField();
	private TextField jtfZip = new TextField();
	private FlowPane jpButton = new FlowPane();
	private FirstButton jbtFirst;
	private NextButton jbtNext;
	private PreviousButton jbtPrevious;
	private LastButton jbtLast;
	private AddButton jbtAdd;
	private UndoButton jbtUndo;
	private RedoButton jbtRedo;
	private ArrayList<CommandButton> commadArray = new ArrayList<CommandButton>();
	private static int numOfPanes = 0;
	public static final int MAX_PANES = 3;
	public static final int UPDATE_FILE_PANES = 1;
	private RandomAccessFile raf;
	private EventHandler<ActionEvent> ae = e -> ((Command) e.getSource()).Execute();

	public static AddressBookPane getInstance() {
		AddressBookPane ap = new AddressBookPane();
		numOfPanes++;
		if (numOfPanes <= UPDATE_FILE_PANES) {
			Decorator.decorator(ap.jpButton, true, ap.commadArray);
			return ap;
		} else if (numOfPanes <= MAX_PANES) {
			Decorator.decorator(ap.jpButton, false, ap.commadArray);
			return ap;
		} else
			return null;
	}

	private AddressBookPane() {
		try {
			raf = new RandomAccessFile("address.dat", "rw");
		} catch (IOException ex) {
			System.out.print("Error: " + ex);
			System.exit(0);
		}
		jtfState.setAlignment(Pos.CENTER_LEFT);
		jtfState.setPrefWidth(100);
		jtfZip.setPrefWidth(70);
		// Adding the buttons to the ArrayList
		commadArray.add(jbtFirst = new FirstButton(false, "First", raf, getPane()));
		commadArray.add(jbtNext = new NextButton(false, "Next", raf, getPane()));
		commadArray.add(jbtPrevious = new PreviousButton(false, "Previous", raf, getPane()));
		commadArray.add(jbtLast = new LastButton(false, "Last", raf, getPane()));
		commadArray.add(jbtAdd = new AddButton(true, "Add", raf, getPane()));
		commadArray.add(jbtUndo = new UndoButton(true, "Undo", raf, getPane(), stack));
		commadArray.add(jbtRedo = new RedoButton(true, "Redo", raf, getPane(), stack));
		// Execute of command
		jbtAdd.setOnAction(ae);
		jbtFirst.setOnAction(ae);
		jbtNext.setOnAction(ae);
		jbtPrevious.setOnAction(ae);
		jbtLast.setOnAction(ae);
		jbtRedo.setOnAction(ae);
		jbtUndo.setOnAction(ae);
		// Labels
		Label state = new Label("State");
		Label zp = new Label("Zip");
		Label name = new Label("Name");
		Label street = new Label("Street");
		Label city = new Label("City");
		// Set the Name, Street and City Grid Pane
		GridPane nameStreetCityGrid = new GridPane();
		nameStreetCityGrid.add(name, 0, 0);
		nameStreetCityGrid.add(street, 0, 1);
		nameStreetCityGrid.add(city, 0, 2);
		nameStreetCityGrid.setAlignment(Pos.CENTER_LEFT);
		nameStreetCityGrid.setVgap(8);
		nameStreetCityGrid.setPadding(new Insets(0, 2, 0, 2));
		GridPane.setVgrow(name, Priority.ALWAYS);
		GridPane.setVgrow(street, Priority.ALWAYS);
		GridPane.setVgrow(city, Priority.ALWAYS);
		// Set the City, State and Zip Grid Pane
		GridPane cityStateZipGrid = new GridPane();
		cityStateZipGrid.setHgap(8);
		cityStateZipGrid.add(jtfCity, 0, 0);
		cityStateZipGrid.add(state, 1, 0);
		cityStateZipGrid.add(jtfState, 2, 0);
		cityStateZipGrid.add(zp, 3, 0);
		cityStateZipGrid.add(jtfZip, 4, 0);
		cityStateZipGrid.setAlignment(Pos.CENTER_LEFT);
		GridPane.setHgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfState, Priority.ALWAYS);
		GridPane.setVgrow(jtfZip, Priority.ALWAYS);
		GridPane.setVgrow(state, Priority.ALWAYS);
		GridPane.setVgrow(zp, Priority.ALWAYS);
		GridPane p4 = new GridPane();
		p4.add(jtfName, 0, 0);
		p4.add(jtfStreet, 0, 1);
		p4.add(cityStateZipGrid, 0, 2);
		p4.setVgap(1);
		GridPane.setHgrow(jtfName, Priority.ALWAYS);
		GridPane.setHgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setHgrow(cityStateZipGrid, Priority.ALWAYS);
		GridPane.setVgrow(jtfName, Priority.ALWAYS);
		GridPane.setVgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setVgrow(cityStateZipGrid, Priority.ALWAYS);
		// Main Show Grid Pane
		GridPane MainShowGrid = new GridPane();
		MainShowGrid.add(nameStreetCityGrid, 0, 0);
		MainShowGrid.add(p4, 1, 0);
		GridPane.setHgrow(nameStreetCityGrid, Priority.NEVER);
		GridPane.setHgrow(p4, Priority.ALWAYS);
		GridPane.setVgrow(nameStreetCityGrid, Priority.ALWAYS);
		GridPane.setVgrow(p4, Priority.ALWAYS);
		MainShowGrid
				.setStyle("-fx-border-color: grey;" + " -fx-border-width: 1;" + " -fx-border-style: solid outside ;");
		jpButton.setHgap(5);
		jpButton.setAlignment(Pos.CENTER);
		GridPane.setVgrow(jpButton, Priority.NEVER);
		GridPane.setVgrow(MainShowGrid, Priority.ALWAYS);
		GridPane.setHgrow(jpButton, Priority.ALWAYS);
		GridPane.setHgrow(MainShowGrid, Priority.ALWAYS);
		this.setVgap(5);
		this.add(MainShowGrid, 0, 0);
		this.add(jpButton, 0, 1);
		jbtFirst.Execute();
	}

	public void actionHandled(ActionEvent e) {
		((Command) e.getSource()).Execute();
	}

	public void SetName(String text) {
		jtfName.setText(text);
	}

	public void SetStreet(String text) {
		jtfStreet.setText(text);
	}

	public void SetCity(String text) {
		jtfCity.setText(text);
	}

	public void SetState(String text) {
		jtfState.setText(text);
	}

	public void SetZip(String text) {
		jtfZip.setText(text);
	}

	public String GetName() {
		return jtfName.getText();
	}

	public String GetStreet() {
		return jtfStreet.getText();
	}

	public String GetCity() {
		return jtfCity.getText();
	}

	public String GetState() {
		return jtfState.getText();
	}

	public String GetZip() {
		return jtfZip.getText();
	}

	public AddressBookPane getPane() {
		return this;
	}
}

interface Command {
	public void Execute();
}

class CommandButton extends Button implements Command {
	public final static int NAME_SIZE = 32;
	public final static int STREET_SIZE = 32;
	public final static int CITY_SIZE = 20;
	public final static int STATE_SIZE = 10;
	public final static int ZIP_SIZE = 5;
	public final static int RECORD_SIZE = (NAME_SIZE + STREET_SIZE + CITY_SIZE + STATE_SIZE + ZIP_SIZE);
	protected AddressBookPane p;
	protected RandomAccessFile raf;
	protected boolean update;

	public CommandButton(boolean update, String text, RandomAccessFile raf, AddressBookPane newPane) {
		super(text);
		setRandomAccessFile(raf);
		setPane(newPane);
		this.update = update;
	}

	public void setPane(AddressBookPane pane) {
		p = pane;
	}

	public void setRandomAccessFile(RandomAccessFile r) {
		raf = r;
	}

	public void Execute() {
	}

	public void writeAddress(long position, Address a) {
		try {
			raf.seek(position);
			FixedLengthStringIO.writeFixedLengthString(a.getName(), NAME_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(a.getStreet(), STREET_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(a.getCity(), CITY_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(a.getState(), STATE_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(a.getZip(), ZIP_SIZE, raf);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void writeAddressFromMemento(long position, Memento m) {
		writeAddress(position, m.getAddress());
	}

	public Address readAddress(long position) throws IOException {
		raf.seek(position);
		String name = FixedLengthStringIO.readFixedLengthString(NAME_SIZE, raf);
		String street = FixedLengthStringIO.readFixedLengthString(STREET_SIZE, raf);
		String city = FixedLengthStringIO.readFixedLengthString(CITY_SIZE, raf);
		String state = FixedLengthStringIO.readFixedLengthString(STATE_SIZE, raf);
		String zip = FixedLengthStringIO.readFixedLengthString(ZIP_SIZE, raf);
		return new Address(name, street, city, state, zip);
	}

	public void setAddress(Address add) {
		p.SetName(add.getName());
		p.SetStreet(add.getStreet());
		p.SetCity(add.getCity());
		p.SetState(add.getState());
		p.SetZip(add.getZip());
	}

	public Address getAddress() {
		return new Address(p.GetName(), p.GetStreet(), p.GetCity(), p.GetState(), p.GetZip());
	}

	public static class Memento {
		private Address a;

		protected Memento(Address a) {
			this.a = a;
		}

		private Address getAddress() {
			return a;
		}
	}
}

class AddButton extends CommandButton {
	public AddButton(boolean update, String text, RandomAccessFile raf, AddressBookPane p) {
		super(update, text, raf, p);
	}

	@Override
	public void Execute() {
		try {
			writeAddress(raf.length(), getAddress());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class NextButton extends CommandButton {
	public NextButton(boolean update, String text, RandomAccessFile raf, AddressBookPane p) {
		super(update, text, raf, p);
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() == 0) {
				setAddress(new Address("", "", "", "", ""));
				return;
			}
			long currentPosition = raf.getFilePointer();
			if (currentPosition < raf.length())
				setAddress(readAddress(currentPosition));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class PreviousButton extends CommandButton {
	public PreviousButton(boolean update, String text, RandomAccessFile raf, AddressBookPane p) {
		super(update, text, raf, p);
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() == 0) {
				setAddress(new Address("", "", "", "", ""));
				return;
			}
			long currentPosition = raf.getFilePointer();
			if (currentPosition > raf.length())
				currentPosition = raf.length();
			if (currentPosition - 2 * 2 * RECORD_SIZE >= 0)
				setAddress(readAddress(currentPosition - 2 * 2 * RECORD_SIZE));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class LastButton extends CommandButton {
	public LastButton(boolean update, String text, RandomAccessFile raf, AddressBookPane p) {
		super(update, text, raf, p);
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() == 0) {
				setAddress(new Address("", "", "", "", ""));
				return;
			}
			long lastPosition = raf.length();
			if (lastPosition > 0)
				setAddress(readAddress(lastPosition - 2 * RECORD_SIZE));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class FirstButton extends CommandButton {
	public FirstButton(boolean update, String text, RandomAccessFile raf, AddressBookPane p) {
		super(update, text, raf, p);
	}

	@Override
	public void Execute() {
		try {
			if (raf.length() == 0) {
				setAddress(new Address("", "", "", "", ""));
				return;
			}
			if (raf.length() > 0)
				setAddress(readAddress(0));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class UndoButton extends CommandButton {
	private Stack<Memento> stack;

	public UndoButton(boolean update, String text, RandomAccessFile raf, AddressBookPane p, Stack<Memento> stack) {
		super(update, text, raf, p);
		this.stack = stack;
	}

	@Override
	public void Execute() {
		try {
			long lastPosition = raf.length();
			if (lastPosition > 0) {
				Address a = readAddress(lastPosition - 2 * RECORD_SIZE);
				stack.push(new Memento(a));
				raf.setLength(lastPosition - 2 * RECORD_SIZE);
				if (raf.length() > 0)
					setAddress(readAddress(0));
				else
					setAddress(new Address("", "", "", "", ""));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class RedoButton extends CommandButton {
	private Stack<Memento> stack;

	public RedoButton(boolean update, String text, RandomAccessFile raf, AddressBookPane p, Stack<Memento> stack) {
		super(update, text, raf, p);
		this.stack = stack;
	}

	@Override
	public void Execute() {
		try {
			if (!stack.isEmpty()) {
				writeAddressFromMemento(raf.length(), stack.pop());
				long lastPosition = raf.length();
				if (lastPosition > 0)
					setAddress(readAddress(lastPosition - 2 * RECORD_SIZE));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
