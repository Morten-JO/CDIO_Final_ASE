package input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import communication.WeightingProcedure;
import dao.DatabaseHandler;

public class SocketHandler extends Observable {

	private boolean running;
	private Socket socket;
	private String[] ips;
	private int port;
	private DatabaseHandler handler;
	private WeightingProcedure[] coms;
	private long timePerStart = 3600000;
	
	private Timer timer;

	public SocketHandler(String[] ips, int port) {
		running = true;
		this.ips = ips;
		this.port = port;
		handler = new DatabaseHandler();
		coms = new WeightingProcedure[ips.length];
	}

	public void start() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				connectToWeights();
			}
		}, 0, timePerStart);
		
	}

	public void connectToWeights() {
		System.out.println("Started Connecting Weights");
		for (int i = 0; i < ips.length; i++) {
			boolean shouldStart = false;
			if (coms[i] == null) {
				shouldStart = true;
			} else {
				if (!coms[i].isRunning()) {
					shouldStart = true;
				}
			}
			if (shouldStart) {
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress(ips[i], port), 3000);
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
					WeightingProcedure comm = new WeightingProcedure(socket, reader, writer, handler);
					comm.start();
					coms[i] = comm;
				} catch (IOException e) {
					coms[i] = null;
				}
			}
		}
		System.out.println("Finished Connecting weights");
		System.err.println("--------------------");
		setChanged();
		notifyObservers("update");
	}

	public Socket getsocket() {
		return socket;
	}

	public boolean isRunning() {
		return running;
	}

	public WeightingProcedure[] getWeights() {
		return coms;
	}

	public String[] getIps() {
		return ips;
	}

	public void shutdownASE() {
		for (int i = 0; i < coms.length; i++) {
			if (coms[i] != null) {
				if (coms[i].isRunning()) {
					coms[i].setRunning(false);
					try {
						coms[i].getSocket().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					coms[i] = null;
				}
			}
		}
		running = false;
	}
}