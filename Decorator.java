import java.util.ArrayList;
import javafx.scene.layout.FlowPane;

public class Decorator {
	public static void decorator(FlowPane buttonPane, boolean update, ArrayList<CommandButton> commadArray) {
		if (update)
			for (int j = 0; j < commadArray.size(); j++)
				buttonPane.getChildren().add(commadArray.get(j));
		else
			for (int j = 0; j < commadArray.size(); j++)
				if (!commadArray.get(j).update)
					buttonPane.getChildren().add(commadArray.get(j));

	}
}
