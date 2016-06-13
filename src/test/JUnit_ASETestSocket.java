package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnit_ASETestSocket {

	public static ServerSocket socket;
	public static Socket client;
	public static BufferedReader reader;
	public static PrintWriter writer;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void a_canStartServer(){
		try {
			socket = new ServerSocket(8000);
		} catch (IOException e) {
			fail("Could not start server!");
		}
	}

	@Test
	public void b_canReceiveASE() {
		try {
			client = socket.accept();
		} catch (IOException e) {
			fail("Could not receive client!");
		}
	}
	
	@Test
	public void c_canCreateWriterReader(){
		try {
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			fail("Could not create reader!");
		}
		try {
			writer = new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			fail("Could not create writer!");
		}
		
	}
	
	@Test
	public void d_canReceiveFromWeight(){
		try {
			String str = reader.readLine();
			System.out.println("Message received: "+str);
		} catch (IOException e) {
			fail("Error receving!");
		}
	}
	
	@Test
	public void e_canSendToServer(){
		writer.println("RM20 B");
		writer.println("RM20 A");
		//past procedure 2
	}

}
