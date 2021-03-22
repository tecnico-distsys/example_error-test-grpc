package pt.tecnico.example.server.domain;

import pt.tecnico.example.server.domain.exception.InvalidNameException;

/** Domain root, where state and behavior of the server are implemented. */
public class Example {

	private String name;

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) throws InvalidNameException {
		if (name.isBlank())
			throw new InvalidNameException();
		this.name = name;
	}

}
