package org.hibernate.demos.quarkus.dto;

import org.hibernate.demos.quarkus.domain.Dialogue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface DialogueMapper {
	DialogueDto toDto(Dialogue client);
}
