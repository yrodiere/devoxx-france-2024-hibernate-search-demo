package org.hibernate.demos.quarkus;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.hibernate.demos.quarkus.ai.EmbeddingModelBridge;
import org.hibernate.demos.quarkus.domain.Dialogue;
import org.hibernate.demos.quarkus.dto.DialogueDto;
import org.hibernate.demos.quarkus.dto.DialogueMapper;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;
import java.util.stream.Collectors;

@Path("/")
@Transactional
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DialoguesKnnSearchResource {

	@Inject
	DialogueMapper mapper;

	@Inject
	SearchSession searchSession;

	@Inject
	EmbeddingModelBridge bridge;

	@GET
	@Path("search-knn")
	public List<DialogueDto> searchKnn(@QueryParam("term") String term) {
		if (term == null || term.isEmpty()) {
			return List.of();
		}

		List<Dialogue> result = searchSession.search( Dialogue.class )
				.where( f -> f.knn( 5 ).field( "embedding" )
						.matching( bridge.toEmbedding(term) )
						.boost( 1.0f ))
				.fetchHits( 5 );
		return result.stream().map( mapper::toDto ).collect( Collectors.toList() );
	}
}
