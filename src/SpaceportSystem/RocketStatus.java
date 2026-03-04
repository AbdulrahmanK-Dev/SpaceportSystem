package SpaceportSystem;
import java.io.Serializable;

public enum RocketStatus implements Serializable{
	READY,
	IN_MAINTENANCE,
	AVAILABLE,
	IN_FLIGHT;
}
