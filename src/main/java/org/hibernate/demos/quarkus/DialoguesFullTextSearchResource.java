package org.hibernate.demos.quarkus;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
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
public class DialoguesFullTextSearchResource {

	@Inject
	DialogueMapper mapper;

	@Inject
	SearchSession searchSession;

	@GET
	@Path("search-full-text")
	public List<DialogueDto> search(@QueryParam("term") String term) {
		List<Dialogue> result = searchSession.search( Dialogue.class )
				.where( f ->
						f.match()
								.fields( "text", "character.name" )
								.matching( term )
				)
				.fetchHits( 5 );
		return result.stream().map( mapper::toDto ).collect( Collectors.toList() );
	}
}
//http://localhost:8080/search-full-text?term=Something%20to%20say%20hello
//http://localhost:8080/search-knn?term=Something%20to%20say%20hello
//http://localhost:8080/search-full-text?term=dialogue%20that%20looks%20like%20saying%20hi
//http://localhost:8080/search-knn?term=dialogue%20that%20looks%20like%20saying%20hi