package com.example.chickeninvaders;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class Client {

	private String serverAddress;
	private final int serverport = Server.port;
	public Socket socket;

	public Client(Context c) {
		join(c);
	}

	private void join(Context c) {
		WifiManager mang = (WifiManager) c
				.getSystemService(Context.WIFI_SERVICE);
		// open wifi if it isn't opened
		if (!mang.isWifiEnabled())
			if (mang.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
				mang.setWifiEnabled(true);

		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\"" + Menu.networkName + "\"";
		conf.preSharedKey = "\"" + Menu.networkPass + "\"";

		mang.addNetwork(conf);
		List<WifiConfiguration> list = mang.getConfiguredNetworks();
		for (WifiConfiguration i : list) {
			if (i.SSID != null && i.SSID.equals("\"" + Menu.networkName + "\"")) {
				try {
					mang.disconnect();
					mang.enableNetwork(i.networkId, true);
					boolean b = mang.reconnect();
					Log.d("connect", "waiting to connect to server");
//					Thread.sleep(5000);
					ConnectivityManager connManager = (ConnectivityManager) c
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					boolean isWifi = false;
					NetworkInfo current;
					while (!isWifi) {
						current = connManager.getActiveNetworkInfo();
						isWifi = (current != null)
								&& (current.getType() == ConnectivityManager.TYPE_WIFI);

					}
					// serverIp();
					// get ip of the server
					WifiManager wifii = (WifiManager) c
							.getSystemService(Context.WIFI_SERVICE);
					DhcpInfo d = wifii.getDhcpInfo();
					Log.d("connect", " connectected to server");
					int address = d.serverAddress;
					serverAddress = ((address & 0xFF) + "."
							+ ((address >> 8) & 0xFF) + "."
							+ ((address >> 16) & 0xFF) + "." + ((address >> 24) & 0xFF));
					break;
				} catch (Exception e) {
					// TODO: handle exception
					Log.d("client mada", "omkkkkkk");
				}

			}
		}// end loop
			// now open socket to the other device
		try {
			// object represent the ip address
			InetAddress ipaddrs = InetAddress.getByName(serverAddress);
			Log.d("join", ipaddrs.getHostName());
			Log.d("join", ipaddrs.getHostAddress());
			Log.d("join Method", "searching...");
			// create new socket
			socket = new Socket(serverAddress, serverport);
			Log.d("join method", "got the other device");
			// get input and output streams of the socket
			// to receive and send data to the client
			// y3ni bft7 reader w writer 3la el socket

			// mn awl hna el code eli gay hinfsal fe 7ta tnia
//			InputStream sin = socket.getInputStream();
//			OutputStream sout = socket.getOutputStream();
//
//			// converting to another stream 3"lasa kda
//			DataInputStream in = new DataInputStream(sin);
//			DataOutputStream out = new DataOutputStream(sout);
//
//			// hnna b2a code el send wl recieve
//			while (true) {
//				// sending
//				out.writeUTF("");
//				out.flush();
//				// receiving
//				in.readUTF();
//			}
		} catch (Exception e) {
			// TODO: handle exception
//			Log.d("join method", "error");
//			e.printStackTrace();
			MainActivity.view.finish();
			//MainActivity.view.startActivity(new Intent(MainActivity.view,MainActivity.class));
		}
	}

	public void ClientMethod() {

		String adress = "localhost";// ip of the server
		// String adress="localhost";
		try {
			// object represent the ip address
			InetAddress ipaddrs = InetAddress.getByName(adress);
			// System.out.println(ipaddrs.getHostName());
			// System.out.println(ipaddrs.getHostAddress());
			//

			System.out.println("Any of you heard of a socket with IP address "
					+ adress + " and port " + serverport + "?");

			// create new socket
			Socket socket = new Socket(ipaddrs, serverport);
			System.out.println("yes ! i just got hold of the program");

			// get input and output streams of the socket
			// to receive and send data to the client
			// y3ni bft7 reader w writer 3la el socket
			InputStream sin = socket.getInputStream();
			OutputStream sout = socket.getOutputStream();

			// converting to another stream 3"lasa kda
			DataInputStream in = new DataInputStream(sin);
			DataOutputStream out = new DataOutputStream(sout);

			// creating stream to read data from user
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			String line = null;
			System.out
					.println("Type in something and press enter. Will send it to the server and tell ya what it thinks.");
			System.out.println();
			while (true) {
				// wait for the user to type in something
				// and press enter.
				line = br.readLine();
				System.out.println("sending to server...");
				out.writeUTF(line);// write to server
				out.flush();
				// now i want to read from the server its reply
				line = in.readUTF();// read the reply of server
				System.out
						.println("The server was very polite. It sent me this : "
								+ line);
				System.out
						.println("Looks like the server is pleased with us. Go ahead and enter more lines.");
				System.out.println();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// Client client = new C/*lient();
		// client.ClientMethod();*/
	}

}
