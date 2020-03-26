package pt.tecnico.distsys.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.distsys.example.ExampleServiceGrpc;
import pt.tecnico.distsys.example.GetNameRequest;
import pt.tecnico.distsys.example.NameResponse;
import pt.tecnico.distsys.example.SetNameRequest;

/**
 * Encapsulates gRPC channel and stub for remote service. All remote calls from
 * client should use this object.
 */
public class ExampleFrontend implements AutoCloseable {
	private final ManagedChannel channel;
	private final ExampleServiceGrpc.ExampleServiceBlockingStub stub;

	public ExampleFrontend(String host, int port) {
		// Channel is the abstraction to connect to a service endpoint.
		// Let us use plaintext communication because we do not have certificates.
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		// Create a blocking stub.
		stub = ExampleServiceGrpc.newBlockingStub(channel);
	}

	public NameResponse setName(SetNameRequest request) {
		return stub.setName(request);
	}

	public NameResponse getName(GetNameRequest request) {
		return stub.getName(request);
	}

	@Override
	public final void close() {
		channel.shutdown();
	}
}
