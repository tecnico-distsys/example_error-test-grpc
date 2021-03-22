package pt.tecnico.example.server;

import java.io.IOException;
import java.util.Scanner;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/** Application main code that launches the gRPC server. */
public class ExampleApp {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(ExampleApp.class.getSimpleName());

		// Receive and print arguments.
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

		// Check arguments.
		if (args.length < 1) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s port%n", ExampleApp.class.getName());
			return;
		}

		final int port = Integer.parseInt(args[0]);
		final BindableService impl = new ExampleServiceImpl();

		// Create a new server to listen on port.
		Server server = ServerBuilder.forPort(port).addService(impl).build();

		// Start the server.
		server.start();

		// Server threads are running in the background.
		System.out.println("Server started");

		// Create new thread where we wait for the user input.
		new Thread(() -> {
			System.out.println("<Press enter to shutdown>");
			new Scanner(System.in).nextLine();

			server.shutdown();
		}).start();

		// Do not exit the main thread. Wait until server is terminated.
		server.awaitTermination();
	}

}
