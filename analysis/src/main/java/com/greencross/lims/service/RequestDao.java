package com.greencross.lims.service;

import com.greencross.lims.dao.AbstractJpaDAO;
import com.greencross.lims.dao.GroupableJpa;
import com.greencross.lims.dto.QueryServerside;
import com.greencross.lims.entity.readonly.Request;
import com.greencross.lims.entity.readonly.Sample;
import com.greencross.lims.mapper.LocalDateToEpoch;
import com.greencross.lims.test.avoid.TestInfo;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class RequestDao extends AbstractJpaDAO<Request> implements GroupableJpa<Request> {
	private final LocalDateToEpoch localDateToEpoch;
	public RequestDao(LocalDateToEpoch localDateToEpoch) {
		this.localDateToEpoch = localDateToEpoch;
	}

	@Override
	public Predicate map(CriteriaBuilder cb, Root<Request> c, QueryServerside.Filter filter) {
		return map(cb, c, filter.key(), filter.value());
	}
	private Predicate map(CriteriaBuilder cb, Root<Request> c, String key, String value) {
		if(key == null || key.trim().isEmpty()) {
			Predicate[] predicates = Stream.of(
					map(cb, c, "sample", value),
					map(cb, c, "patient", value)
			).filter(Objects::nonNull).toArray(Predicate[]::new);
			return cb.or(predicates);
		} else if("sample".equalsIgnoreCase(key)) {
			if(value == null) return null;
			if(value.contains("-")) value = value.replace("-", "");
			if(value.matches("\\d+")) return cb.equal(c.get("sample"), em().getReference(Sample.class, Long.parseLong(value)));
			else return null;
		} else if("patient".equalsIgnoreCase(key)) {
			return cb.equal(parse(c,"sample.patient.name"), value);
		} else if("service".equalsIgnoreCase(key)) {
			var predicate = cb.in(parse(c, "service.id"));
			for(String s: value.split(",")) predicate.value(s.trim());
			return predicate;
		} else if("from".equalsIgnoreCase(key)) return cb.greaterThanOrEqualTo(c.get("dateRequest"), localDateToEpoch.map(Long.parseLong(value)));
		else if("to".equalsIgnoreCase(key)) return cb.lessThanOrEqualTo(c.get("dateRequest"), localDateToEpoch.map(Long.parseLong(value)));
		else if("analyzed".equalsIgnoreCase(key) && "true".equalsIgnoreCase(value)) return cb.equal(parse(c, "analyzed"), true);
		else if("not_complete".equalsIgnoreCase(key) && "true".equalsIgnoreCase(value)) return cb.in(parse(c, "state")).value("검사의뢰").value("분석완료").value("결과지생성완료");
		return null;
	}
	@Override
	public Page<Request> search(QueryServerside query) {
		assert query != null;
		Pageable pageable;
		if(query.sortBy()!=null) {
			boolean isAsc = query.asc();
			pageable = PageRequest.of(query.page(), query.limit(), Sort.by(isAsc? Sort.Direction.ASC: Sort.Direction.DESC, query.sortBy()));
		} else pageable = PageRequest.of(query.page(), query.limit());
		if(query.filters()==null) query.filters(new LinkedList<>());
		List<QueryServerside.Filter> filters = new LinkedList<>(query.filters());
		filters.add(new QueryServerside.Filter().key("service").value(Arrays.stream(TestInfo.TESTS).map(TestInfo::code).collect(Collectors.joining(","))));
		query.filters(filters);
		return new PageImpl<>(list(query).collect(Collectors.toList()), pageable, count(query));
	}
}
