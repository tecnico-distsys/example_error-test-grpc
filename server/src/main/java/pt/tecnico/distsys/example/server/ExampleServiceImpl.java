package pt.tecnico.distsys.example.server;

import static io.grpc.Status.INVALID_ARGUMENT;

import java.util.logging.Logger;

import io.grpc.stub.StreamObserver;
import pt.tecnico.distsys.example.ExampleServiceGrpc;
import pt.tecnico.distsys.example.GetNameRequest;
import pt.tecnico.distsys.example.NameResponse;
import pt.tecnico.distsys.example.SetNameRequest;
import pt.tecnico.distsys.example.server.domain.Example;
import pt.tecnico.distsys.example.server.domain.exception.InvalidNameException;

/** gRPC server operations implementation relying on domain objects. */
public final class ExampleServiceImpl extends ExampleServiceGrpc.ExampleServiceImplBase {
	private static final Logger LOGGER = Logger.getLogger(ExampleServiceImpl.class.getName());
	/** Domain root. */
	private final Example shop = new Example();

	@Override
	public void getName(GetNameRequest request, StreamObserver<NameResponse> responseObserver) {
		LOGGER.info("getName()...");

		// Send the name back to the client.
		String name = shop.getName();
		responseObserver.onNext(NameResponse.newBuilder().setName(name).build());
		responseObserver.onCompleted();
	}

	@Override
	public void setName(SetNameRequest request, StreamObserver<NameResponse> responseObserver) {
		String name = request.getProposedName();
		LOGGER.info("setName(" + name + ")...");
		try {
			shop.setName(name);
			LOGGER.info("Shop name set to '" + name + "'.");

			// Send the name back to the client.
			name = shop.getName();
			responseObserver.onNext(NameResponse.newBuilder().setName(name).build());
			responseObserver.onCompleted();

		} catch (InvalidNameException e) {
			LOGGER.info(e.getMessage());
			responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
		}
	}

}
