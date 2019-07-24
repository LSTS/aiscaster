package pt.lsts.aiscaster;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AisShip {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss Z\"");
	
	public final long mmsi, tstamp;
	public final double latitude, longitude, cog, sog, heading;
	public final String name, type;		

	public AisShip(long mmsi, long tstamp, double latitude, double longitude, double cog, double sog, double heading, String name, String type) {
		this.mmsi = mmsi;
		this.tstamp = tstamp;
		this.latitude = latitude;
		this.longitude = longitude;
		this.cog = cog;
		this.sog = sog;
		this.heading = heading;
		this.name = name.replaceAll("\"", "").replaceAll("\\.","").replaceAll("^\\-", "");
		
		this.type = type;
		
	}
	public AisShip(String csvString) throws ParseException {
		String[] parts = csvString.split(",");
		mmsi = Long.valueOf(parts[0]);
		tstamp = sdf.parse(parts[1]).getTime();
		latitude = Double.valueOf(parts[2]);
		longitude = Double.valueOf(parts[3]);
		cog = Double.valueOf(parts[4]);
		sog = Double.valueOf(parts[5]);
		heading = Double.valueOf(parts[6]);
		name = parts[9].replaceAll("\"", "").replaceAll("\\.","");
		type = parts[11];
	}
	
	@Override
	public String toString() {
		return name+" at "+latitude+" / "+longitude+", "+sog;
	}
}

