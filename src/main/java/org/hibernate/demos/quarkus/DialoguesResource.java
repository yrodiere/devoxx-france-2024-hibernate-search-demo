package org.hibernate.demos.quarkus;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.hibernate.demos.quarkus.domain.Character;
import org.hibernate.demos.quarkus.domain.Dialogue;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.mapper.orm.mapping.SearchMapping;
import org.hibernate.search.mapper.orm.session.SearchSession;

@Path("/")
@Transactional
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DialoguesResource {

	@Inject
	SearchMapping searchMapping;

	@Inject
	SearchSession searchSession;

	@GET
	@Path("create")
	@Transactional
	public String dialogue(@QueryParam("id") Long characterId, @QueryParam("content") String content) {
		if (characterId == null || content == null) {
			return "id and content query parameters";
		}

		Dialogue dialogue = new Dialogue();
		dialogue.character = Character.findById(characterId);
		dialogue.text = content;
		dialogue.persist();
		return "created";
	}

	@GET
	@Path("reindex")
	@Transactional(TxType.NEVER)
	public void reindex() throws InterruptedException {
		searchMapping.scope( Dialogue.class )
				.massIndexer()
				.startAndWait();
	}

	@Transactional(TxType.NEVER)
	void reindexOnStart(@Observes StartupEvent event) throws InterruptedException {
		if ( "dev".equals( ProfileManager.getActiveProfile() ) ) {
			Long indexSize = QuarkusTransaction.requiringNew()
					.call(searchSession.search(Dialogue.class)
							.where(SearchPredicateFactory::matchAll)::fetchTotalHitCount);

			if (indexSize == null || indexSize == 0) {
				reindex();
			}
		}
	}
}