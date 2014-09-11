/* $Id$
 * $URL:
 * https://svn.gfi-info.com/codeAll4Green/trunk/All4Green/AgentPlatform/src
 * /main/java/eu/a4g/agent/impl/DCAgentImpl.java $
 * 
 * Part of the EU project All4Green, see http://www.all4green-project.eu/
 * 
 * @license
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Copyright © 2010-2013 Almende B.V.
 */
package io.coala.optimize;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * {@link WeightedSkipListMap}
 * 
 * @date $Date: 2014-07-07 16:12:23 +0200 (Mon, 07 Jul 2014) $
 * @version $Revision: 323 $
 * @author <a href="mailto:rick@almende.org">Rick van Krevelen</a>
 * 
 * @param <C>
 * @param <K>
 */
public class WeightedSkipListMap<C extends WeightedCriterion, K extends WeightedComparable<C>, V extends Serializable>
		implements WeightedNavigableMap<C, K, V>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param weights
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <C extends WeightedCriterion> WeightedNavigableMap<C, WeightedComparable<C>, Serializable> forWeights(
			final Map<C, Number> weights)
	{
		return forWeights(weights, WeightedComparable.class, Serializable.class);
	}

	/**
	 * @param weights
	 * @return
	 */
	public static <C extends WeightedCriterion, K extends WeightedComparable<C>, V extends Serializable> WeightedNavigableMap<C, K, V> forWeights(
			final Map<C, Number> weights, final Class<K> alternativeType,
			final Class<V> valueType)
	{
		return new WeightedSkipListMap<C, K, V>(weights,
				new WeightedComparator.Factory<C>()
				{
					@Override
					public WeightedComparator<C> create(
							final Map<C, Number> weights)
					{
						return new WeightedProductComparator<C>(weights);
					}
				});
	}

	/** */
	private NavigableMap<K, V> map;

	private final WeightedComparator.Factory<C> comparatorFactory;

	public WeightedSkipListMap(final Map<C, Number> weights,
			final WeightedComparator.Factory<C> comparatorFactory)
	{
		this(new HashMap<K, V>(), weights, comparatorFactory);
	}

	public WeightedSkipListMap(final Map<K, V> map,
			final Map<C, Number> weights,
			final WeightedComparator.Factory<C> comparatorFactory)
	{
		this.comparatorFactory = comparatorFactory;
		setMap(map, comparatorFactory.create(weights));
	}

	/** @return the wrapped map */
	protected synchronized NavigableMap<K, V> getMap()
	{
		return this.map;
	}

	/** re-instantiate the wrapped map with specified values and weights */
	protected synchronized void setMap(final Map<K, V> map,
			final WeightedComparator<C> comparator)
	{
		final NavigableMap<K, V> sortedMap = new ConcurrentSkipListMap<K, V>(
				comparator);
		sortedMap.putAll(map);
		this.map = sortedMap;
	}

	@Override
	public synchronized Number putWeight(final C criterion, final Number weight)
	{
		final Map<C, Number> weights = new HashMap<C, Number>(comparator()
				.getWeights());
		final Number result = weights.put(criterion, weight);
		setMap(getMap(), this.comparatorFactory.create(weights));
		return result;
	}

	@Override
	public synchronized Number removeWeight(C criterion)
	{
		final Map<C, Number> weights = new HashMap<C, Number>(comparator()
				.getWeights());
		final Number result = weights.remove(criterion);
		setMap(getMap(), this.comparatorFactory.create(weights));
		return result;
	}

	@Override
	public synchronized Set<C> getCriteria()
	{
		return new HashSet<C>(comparator().getWeights().keySet());
	}

	@Override
	public synchronized Number getWeight(final C criterion)
	{
		return comparator().getWeights().get(criterion);
	}

	/*************************** WRAPPED METHODS *****************************/

	@Override
	public String toString()
	{
		return getMap().toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized WeightedComparator<C> comparator()
	{
		return (WeightedComparator<C>) getMap().comparator();
	}

	@Override
	public synchronized SortedMap<K, V> subMap(final K fromKey, final K toKey)
	{
		return getMap().subMap(fromKey, toKey);
	}

	@Override
	public synchronized SortedMap<K, V> headMap(final K toKey)
	{
		return getMap().headMap(toKey);
	}

	@Override
	public synchronized SortedMap<K, V> tailMap(final K fromKey)
	{
		return getMap().tailMap(fromKey);
	}

	@Override
	public synchronized K firstKey()
	{
		return getMap().firstKey();
	}

	@Override
	public synchronized K lastKey()
	{
		return getMap().lastKey();
	}

	@Override
	public synchronized Set<K> keySet()
	{
		return getMap().keySet();
	}

	@Override
	public synchronized Collection<V> values()
	{
		return getMap().values();
	}

	@Override
	public synchronized Set<Entry<K, V>> entrySet()
	{
		return getMap().entrySet();
	}

	@Override
	public synchronized int size()
	{
		return getMap().size();
	}

	@Override
	public synchronized boolean isEmpty()
	{
		return getMap().isEmpty();
	}

	@Override
	public synchronized boolean containsKey(final Object key)
	{
		return getMap().containsKey(key);
	}

	@Override
	public synchronized boolean containsValue(final Object value)
	{
		return getMap().containsValue(value);
	}

	@Override
	public synchronized V get(final Object key)
	{
		return getMap().get(key);
	}

	@Override
	public synchronized V put(final K key, final V value)
	{
		return getMap().put(key, value);
	}

	@Override
	public synchronized V remove(final Object key)
	{
		return getMap().remove(key);
	}

	@Override
	public synchronized void putAll(final Map<? extends K, ? extends V> m)
	{
		getMap().putAll(m);
	}

	@Override
	public synchronized void clear()
	{
		getMap().clear();
	}

	@Override
	public synchronized Entry<K, V> lowerEntry(final K key)
	{
		return getMap().lowerEntry(key);
	}

	@Override
	public synchronized K lowerKey(final K key)
	{
		return getMap().lowerKey(key);
	}

	@Override
	public synchronized Entry<K, V> floorEntry(final K key)
	{
		return getMap().floorEntry(key);
	}

	@Override
	public synchronized K floorKey(final K key)
	{
		return getMap().floorKey(key);
	}

	@Override
	public synchronized Entry<K, V> ceilingEntry(final K key)
	{
		return getMap().ceilingEntry(key);
	}

	@Override
	public synchronized K ceilingKey(final K key)
	{
		return getMap().ceilingKey(key);
	}

	@Override
	public synchronized Entry<K, V> higherEntry(final K key)
	{
		return getMap().higherEntry(key);
	}

	@Override
	public synchronized K higherKey(K key)
	{
		return getMap().higherKey(key);
	}

	@Override
	public synchronized Entry<K, V> firstEntry()
	{
		return getMap().firstEntry();
	}

	@Override
	public synchronized Entry<K, V> lastEntry()
	{
		return getMap().lastEntry();
	}

	@Override
	public synchronized Entry<K, V> pollFirstEntry()
	{
		return getMap().pollFirstEntry();
	}

	@Override
	public synchronized Entry<K, V> pollLastEntry()
	{
		return getMap().pollLastEntry();
	}

	@Override
	public synchronized NavigableMap<K, V> descendingMap()
	{
		return getMap().descendingMap();
	}

	@Override
	public synchronized NavigableSet<K> navigableKeySet()
	{
		return getMap().navigableKeySet();
	}

	@Override
	public synchronized NavigableSet<K> descendingKeySet()
	{
		return getMap().descendingKeySet();
	}

	@Override
	public synchronized NavigableMap<K, V> subMap(final K fromKey,
			final boolean fromInclusive, final K toKey,
			final boolean toInclusive)
	{
		return getMap().subMap(fromKey, fromInclusive, toKey, toInclusive);
	}

	@Override
	public synchronized NavigableMap<K, V> headMap(final K toKey,
			final boolean inclusive)
	{
		return getMap().headMap(toKey, inclusive);
	}

	@Override
	public synchronized NavigableMap<K, V> tailMap(final K fromKey,
			final boolean inclusive)
	{
		return getMap().tailMap(fromKey, inclusive);
	}

}
