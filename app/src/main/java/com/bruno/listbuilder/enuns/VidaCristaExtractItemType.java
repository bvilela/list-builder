package com.bruno.listbuilder.enuns;

import lombok.Getter;

public enum VidaCristaExtractItemType {

	READ_OF_WEEK(false),
	PRESIDENT(true),
	LABEL(false),
	NO_PARTICIPANTS(false),
	WITH_PARTICIPANTS(true);
	
	@Getter
	private final boolean hasParticipants;
	
	VidaCristaExtractItemType(boolean hasParticipants) {
		this.hasParticipants = hasParticipants;
	}
	
}
