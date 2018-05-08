package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import configure.ConfigureVO;

public class Controller {
	Board board;
	ConfigureVO configure;

	void init() {

		// ConfigureVO vo = new ConfigureVO();
		// vo.setGoverment_legitimacy(0.62);
		// vo.setInitial_agent_density(0.40);
		// vo.setInitial_cop_density(0.02);
		// vo.setMax_jail_term(10);
		// vo.setMovement(false);
		// vo.setVision(7.9);

		Gson gson = new Gson();
		FileReader reader = null;
		try {
			reader = new FileReader(new File("configure.json"));
			configure = gson.fromJson(reader, ConfigureVO.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		board = new Board(configure);

	}

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.init();
		controller.start();
	}

	void start() {
	}
}
