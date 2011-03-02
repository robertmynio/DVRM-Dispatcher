package vdrm.base.util;

import java.util.UUID;

public class UniqueIdGenerator implements IUniqueIdGenerator {

	@Override
	public UUID getUniqueId() {
		return UUID.randomUUID();
	}
	
	public static UUID getUID(){
		return UUID.randomUUID();
	}

}
