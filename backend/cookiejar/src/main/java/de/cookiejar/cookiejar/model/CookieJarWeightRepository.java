package de.cookiejar.cookiejar.model;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CookieJarWeightRepository extends PagingAndSortingRepository<CookieJarWeight, Long> {
	public List<CookieJarWeight> findTop3ByOrderByIdDesc();
	public List<CookieJarWeight> findTop2ByOrderByIdDesc();
}
