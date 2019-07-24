package pt.lsts.aiscaster;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

import com.google.common.eventbus.Subscribe;

import dk.tbsalling.ais.tracker.AISTrack;
import dk.tbsalling.ais.tracker.AISTracker;
import dk.tbsalling.ais.tracker.events.AisTrackDynamicsUpdatedEvent;

public class AisCaster {

	private AISTracker tracker;
	
	LinkedHashMap<Long, Long> mmsiToTimestamps = new LinkedHashMap<>();

	public AisCaster(String host, int port) throws UnknownHostException, IOException {
		
		Socket socket = new Socket();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {				
				if (socket != null)
					try {
						socket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
		
		socket.connect(new InetSocketAddress(host, port));		
		System.out.println("Connected to "+host+":"+port);
		tracker = new AISTracker();
		tracker.registerSubscriber(this);
		tracker.update(socket.getInputStream());
	}

	@Subscribe
	public void handleEvent(AisTrackDynamicsUpdatedEvent event) {
		sendData(event.getAisTrack());
	}

	private synchronized void sendData(AISTrack track) {
		if (track.getShipName() == null)
			return;

		AisShip ship = new AisShip(track.getMmsi(), track.getTimeOfLastUpdate().toEpochMilli(),
				track.getLatitude().doubleValue(), track.getLongitude().doubleValue(),
				track.getCourseOverGround().doubleValue(), track.getSpeedOverGround().doubleValue(),
				track.getTrueHeading().doubleValue(), track.getShipName(), track.getShipType().toString());		
	
		System.out.println(ship);
	}

	

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: java -jar AisCaster.jar <HOST>:<PORT>");
			System.exit(1);
		}
		
		String host = args[0].split(":")[0];
		int port = Integer.parseInt(args[0].split(":")[1]);
		
		new AisCaster(host, port);		
	}

}
