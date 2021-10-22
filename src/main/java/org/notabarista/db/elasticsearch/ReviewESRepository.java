package org.notabarista.db.elasticsearch;

import org.notabarista.domain.elasticsearch.ReviewES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReviewESRepository extends ElasticsearchRepository<ReviewES, String> {

}
