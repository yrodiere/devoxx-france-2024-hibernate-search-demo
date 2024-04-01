package org.hibernate.demos.quarkus;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.hibernate.demos.quarkus.ai.EmbeddingModelBridge;
import org.hibernate.demos.quarkus.domain.Character;
import org.hibernate.demos.quarkus.domain.Dialogue;
import org.hibernate.demos.quarkus.dto.DialogueDto;
import org.hibernate.demos.quarkus.dto.DialogueMapper;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;
import java.util.stream.Collectors;

@Path("/")
@Transactional
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DialoguesResource {

	@Inject
	DialogueMapper mapper;

	@Inject
	SearchMapping searchMapping;

	@Inject
	SearchSession searchSession;

	@Inject
	EmbeddingModelBridge bridge;

	@GET
	@Path("/character/{id}")
	public Character character(@PathParam("id") Long id) {
		return Character.findById(id);
	}

	@GET
	@Path("/dialogue/{id}")
	public Dialogue dialogue(@PathParam("id") Long id) {
		return Dialogue.findById(id);
	}

	@GET
	@Path("/reindex")
	@Transactional(TxType.NEVER)
	public void reindex() throws InterruptedException {
		searchMapping.scope( Dialogue.class )
				.massIndexer()
				.startAndWait();
	}

	@Transactional(TxType.NEVER)
	void reindexOnStart(@Observes StartupEvent event) throws InterruptedException {
		if ( "dev".equals( ProfileManager.getActiveProfile() ) ) {
			reindex();
		}
	}

	@GET
	@Path("/search-full-text")
	public List<DialogueDto> search(@QueryParam("term") String term) {
		List<Dialogue> result = searchSession.search( Dialogue.class )
				.where( f ->
						f.match()
								.field( "text" )
								.matching( term )
				)
				.fetchHits( 5 );
		return result.stream().map( mapper::toDto ).collect( Collectors.toList() );
	}

	@GET
	@Path("/search-hybrid")
	public List<DialogueDto> searchHybrid(@QueryParam("term") String term) {
		List<Dialogue> result = searchSession.search( Dialogue.class )
				.where( f -> f.or(
						f.match()
								.field( "text" )
								.matching( term ),
						f.knn( 10 ).field( "embedding" )
								.matching( bridge.toEmbedding(term) )
								.boost( 1.0f )
					)
				)
				.fetchHits( 5 );
		return result.stream().map( mapper::toDto ).collect( Collectors.toList() );
	}

	@GET
	@Path("/search-knn")
	public List<DialogueDto> searchKnn(@QueryParam("term") String term) {
		List<Dialogue> result = searchSession.search( Dialogue.class )
				.where( f -> f.knn( 10 ).field( "embedding" )
								.matching( bridge.toEmbedding(term) )
								.boost( 1.0f )
				)
				.fetchHits( 5 );
		return result.stream().map( mapper::toDto ).collect( Collectors.toList() );
	}

}