import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rooms")

public class Room implements Serializable {

	private static final long serialVersionUID = -3009157732242241606L;
	
	@Id
	@Column(name = "id")
	private long id;

	@Column(name = "number")
	private String number;

	@Column(name = "name")
	private String name;

	protected Room() {
	}

	public Room(String number, String name) {
		this.number = number;
		this.name = name;
	}

}
