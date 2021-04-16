package org.notabarista.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemStatus {
	ACTIVE("Active"), INACTIVE("Inactive"), DRAFT("Draft"), ARCHIVED("Archived");

	private String statusName;

}
