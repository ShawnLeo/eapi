package com.meimeitech.eapi.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class JpaSpecUtils {

	/**
	 * 合并多个查询条件为数组，删除所有null值
	 * 
	 * @param restrictions
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Predicate[] merge(Object... restrictions) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (Object r : restrictions) {
			if (r == null) {
				continue;
			}

			if (r instanceof Predicate) {
				predicates.add((Predicate) r);
				continue;
			}

			if (!(r instanceof Collection)) {
				continue;
			}

			for (Object obj : (List) r) {
				if (obj instanceof Predicate) {
					predicates.add((Predicate) obj);
				}
			}
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	/**
	 * like '%:value%'
	 * 
	 * @param cb
	 * @param path
	 * @param value
	 * @return
	 */
	public static Predicate like(CriteriaBuilder cb, Path<String> path, String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		return cb.like(path, "%" + value + "%");
	}

	/**
	 * 等于
	 * 
	 * @param cb
	 * @param path
	 * @param value
	 * @return
	 */
	public static <T> Predicate eq(CriteriaBuilder cb, Path<T> path, T value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		return cb.equal(path, value);
	}

	/**
	 * 大于
	 * 
	 * @param cb
	 * @param path
	 * @param value
	 * @return
	 */
	public static <T extends Comparable<? super T>> Predicate gt(CriteriaBuilder cb, Path<T> path, T value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		return cb.greaterThan(path, value);
	}

	/**
	 * 大于等于
	 * 
	 * @param cb
	 * @param path
	 * @param value
	 * @return
	 */
	public static <T extends Comparable<? super T>> Predicate ge(CriteriaBuilder cb, Path<T> path, T value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		return cb.greaterThanOrEqualTo(path, value);
	}

	/**
	 * 小于
	 * 
	 * @param cb
	 * @param path
	 * @param value
	 * @return
	 */
	public static <T extends Comparable<? super T>> Predicate lt(CriteriaBuilder cb, Path<T> path, T value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		return cb.lessThan(path, value);
	}

	/**
	 * 小于等于
	 * 
	 * @param cb
	 * @param path
	 * @param value
	 * @return
	 */
	public static <T extends Comparable<? super T>> Predicate le(CriteriaBuilder cb, Path<T> path, T value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		return cb.lessThanOrEqualTo(path, value);
	}

	/***
	 * 包含集合
	 * 
	 * @param cb
	 * @param path
	 * @param values
	 * @return
	 */
	public static <T> List<Predicate> contains(CriteriaBuilder cb, Path<T> path, Collection<T> values) {
		if (CollectionUtils.isEmpty(values)) {
			return null;
		}

		List<Predicate> predicates = new ArrayList<Predicate>();
		for (T value : values) {
			predicates.add(cb.equal(path, value));
		}
		return predicates;
	}

	/**
	 * in集合
	 * 
	 * @param cb
	 * @param path
	 * @param values
	 * @return
	 */
	public static <T> Predicate in(CriteriaBuilder cb, Path<T> path, Collection<T> values) {
		if (CollectionUtils.isEmpty(values)) {
			return null;
		}

		return cb.isTrue(path.in(values));
	}

}
