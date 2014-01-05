package com.example.chickeninvaders;

import java.lang.reflect.Method;
import java.net.*;
import java.io.*;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class Server {

	public static final int port = 4888;
	 ServerSocket ss;
	public Socket socket;

	public Server(Context c) {
		createNetwork(c);
	}

	/*
	 * Method that create access point to be the network hold the game for
	 * multipalyer option
	 */
	public void createNetwork(Context c) {
		WifiManager wifiManager;
		wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
		Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();
		boolean methodFound = false;
		for (Method method : wmMethods) {
			if (method.getName().equals("setWifiApEnabled")) {
				methodFound = true;
				WifiConfiguration netConfig = new WifiConfiguration();
				netConfig.SSID = Menu.networkName;
				netConfig.preSharedKey = Menu.networkPass;
				netConfig.allowedKeyManagement
						.set(WifiConfiguration.KeyMgmt.WPA_PSK);
				try {
					boolean apstatus = (Boolean) method.invoke(wifiManager,
							netConfig, true);
					for (Method isWifiApEnabledmethod : wmMethods) {
						if (isWifiApEnabledmethod.getName().equals(
								"isWifiApEnabled")) {
							while (!(Boolean) isWifiApEnabledmethod
									.invoke(wifiManager)) {
							}

							for (Method method1 : wmMethods) {
								if (method1.getName().equals("getWifiApState")) {
									int apstate;
									apstate = (Integer) method1
											.invoke(wifiManager);
								}
							}
						}
					}
					if (apstatus) {
						Log.d("Splash Activity", "Access Point created");
					} else {
						Log.d("Splash Activity", "Access Point creation failed");
					}

				} catch (Exception e) {
					Log.d("CreateNetork", "error occurred");
				}
			}
		}
		if (!methodFound) {
			Log.d("Splash Activity", "cannot configure an access point");
		}

		try {
			// open socket waiting for clients
			// create a server socket and bind it to the above port number.
			ss = new ServerSocket(port);
			Log.d("server", "waiting for a client..");
			// make the server listen for a connection, and let you know when it
			// gets one.
			socket = ss.accept();
			Log.d("connected", "got the other player");

			// hna el mfrod abd2 el game

			// Get the input and output streams of the socket, so that you can
			// receive and send data to the client.
			// el code mn awl hna mesh hitkb hna
			// InputStream sin = socket.getInputStream();
			// OutputStream sout = socket.getOutputStream();
			//
			// // Just converting them to different streams, so that string
			// // handling becomes easier.
			// DataInputStream in = new DataInputStream(sin);
			// DataOutputStream out = new DataOutputStream(sout);
			// //
			// -----------------------------------------------------------------------------------------------
			// String line = null;// mfrod b2a di e data eli hb3tha badl el
			// listen
			// // to in stream
			//
			// BufferedReader br = new BufferedReader(new InputStreamReader(
			// System.in));
			// //
			// -----------------------------------------------------------------------
			// // el stran eli fo2 dol hit3"iro
			//
			// // fadl b2a code el sending wl recieve eli gwa el inf loop
			// while (true) {
			// // get data
			// in.read();
			// // send data
			// out.writeUTF("");
			// out.flush();
			// }
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("socket server code", "error");
		}
	}

	public static void main(String[] ar) {

		int port = 5050; // just a random port. make sure you enter something
							// between 1025 and 65535.

		try {
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Waiting for a client...");

			Socket socket = ss.accept();

			System.out
					.println("Got a client :) ... Finally, someone saw me through all the cover!");
			System.out.println();

			// Get the input and output streams of the socket, so that you can
			// receive and send data to the client.
			InputStream sin = socket.getInputStream();
			OutputStream sout = socket.getOutputStream();

			// Just converting them to different streams, so that string
			// handling becomes easier.
			DataInputStream in = new DataInputStream(sin);
			DataOutputStream out = new DataOutputStream(sout);

			String line = null;

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			while (true) {

				line = in.readUTF(); // wait for the client to send a line of
										// text.

				System.out.println("The dumb client just sent me this line : "
						+ line);
				System.out.println("now type your reply to client...");

				// get reply of the server
				String reply = br.readLine();
				// write on socket the reply (i mean write it so that client can
				// receive)
				out.writeUTF(reply); // send the same line back to the client.
				out.flush(); // flush the stream to ensure that the data reaches
								// the other end.

				System.out.println("Waiting for the next line...");
				System.out.println();
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}