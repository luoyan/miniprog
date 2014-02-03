// FastTrie 2.3.2 2012-01-04

#ifndef __FAST_TRIE_H__
#define __FAST_TRIE_H__

#include <string.h>
#include <stdint.h>
#include <unistd.h>

#include <vector>
#include <list>
#include <algorithm>

#include "MMap.h"

namespace ft2 {

template <class ValueT> class Container;
template <class ValueT> class Iterator;

template <class ValueT, class SizeT>                          class Vector;
template <class ValueT, int option, class CharT, class SizeT> class Trie;
template <class KeyT, class ValueT, class HashT, class SizeT> class HashMap;
template <class ValueT1, class ValueT2>                       class Pair;

enum
{
	FT_TAIL       = 1,
	FT_PATH       = 2,
	FT_QUICKBUILD = 4,
};

template <class T>
struct Range /// range of objects
{
	const T *begin; ///< begin of the range
	const T *end;   ///< end of the range

	Range()                       : begin(0), end(0) {}
	Range(const T *b, const T *e) : begin(b), end(e) {}

	/// number of objects in the range
	size_t size() const { return end - begin; }
};

/* defines range of various types, facilitates MulAddHash, Trie, etc. */

template <class T>
inline Range<T> range(const T &key)
{
	return Range<T>(&key, &key + 1);
}
template <class _CharT, class _Traits, class _Alloc>
inline Range<_CharT> range(const std::basic_string<_CharT, _Traits, _Alloc> &key)
{
	return Range<_CharT>(&*key.begin(), &*key.end());
}
template <class _Tp, class _Alloc>
inline Range<_Tp> range(const std::vector<_Tp, _Alloc> &key)
{
	return Range<_Tp>(&*key.begin(), &*key.end());
}
template <class ValueT, class SizeT>
inline Range<ValueT> range(const Vector<ValueT, SizeT> &key)
{
	return Range<ValueT>(&*key.begin(), &*key.end());
}
inline Range<char> range(const char *key)
{
	return Range<char>(key, key + strlen(key));
}
inline Range<char> range(char *key)
{
	return Range<char>(key, key + strlen(key));
}
template <class T>
inline Range<T> range(T *key)
{
	T *end = key;
	while (*end) end = std::find(end, end + 1048576, T());
	return Range<T>(key, end);
}

class MulAddHash /// hash various types into uint32_t
{
public:
	template <class T>
	uint32_t operator ()(const T &key) const { return hash(range(key)); }

private:
	template <class T>
	uint32_t hash(Range<T> r) const
	{
		static const uint32_t primes[16] =
		{
			0x01EE5DB9, 0x491408C3, 0x0465FB69, 0x421F0141,
			0x2E7D036B, 0x2D41C7B9, 0x58C0EF0D, 0x7B15A53B,
			0x7C9D3761, 0x5ABB9B0B, 0x24109367, 0x5A5B741F,
			0x6B9F12E9, 0x71BA7809, 0x081F69CD, 0x4D9B740B,
		};

		uint32_t sum = 0, i = 0;

		if      (sizeof(T) % 4 == 0)
			for (const uint32_t *p = (uint32_t *)r.begin; p != (uint32_t *)r.end; ++ p)
				sum += primes[i ++ & 15] * (uint32_t)*p;
		else if (sizeof(T) % 4 == 2)
			for (const uint16_t *p = (uint16_t *)r.begin; p != (uint16_t *)r.end; ++ p)
				sum += primes[i ++ & 15] * (uint32_t)*p;
		else
			for (const uint8_t  *p = (uint8_t  *)r.begin; p != (uint8_t  *)r.end; ++ p)
				sum += primes[i ++ & 15] * (uint32_t)*p;

		return sum;
	}
};

/** @brief container which actually hold data
 *
 * Use this class for loading or building FastTrie.
 *
 * @tparam ValueT value type of the container, may be any of following:
 *                -# C primitive types except pointers, e.g.: char, double
 *                -# composition of type 1, e.g.: std::pair<int, float>
 *                -# Vector<ValueT> for a variable-length value, e.g.:
 *                   Vector<char> for a string
 *                -# Trie<ValueT> or HashMap<KeyT, ValueT>
 *                   for an associative array, e.g.:
 *                   HashMap<int, Vector<char> > is an int to string mapping
 *                -# Pair<ValueT1, ValueT2> for any composition, e.g.:
 *                   Pair<Trie<int>, Vector<char> > is a trie and a string
 */
template <class ValueT>
class Container : private MMap<Container<ValueT> >
{
public:
	/**
	 * std_value_type maps a Container's ValueT to an appropriate STL type.
	 * Basically, it maps Vector to vector, Trie or HashMap to map, Pair to pair,
	 * and others to themselves. Just a template programming facility.
	 */
	typedef ValueT std_value_type;

	typedef       ValueT   value_type;
	typedef const ValueT & const_reference;
	typedef const ValueT * const_iterator;

	/** @brief init Container with no elements */
	Container() : m_numValues(0), m_values(0) {}
	/** @brief load Container from filename
	 *
	 * @param[in]  filename filename to be loaded \n
	 *                      User must keep the file available during the lifetime.
	 * @throw      int      failed (file not exists, unreadable, etc.)
	 */
	Container(const char *filename);
	/** @brief load Container from memory address
	 *
	 * @param[in]  begin    memory address to be loaded \n
	 *                      User must keep the memory valid during the lifetime.
	 * @param[in]  end      memory ending address to be checked \n
	 *                      If end is 0, won't check.
	 * @throw      int      check failed
	 */
	Container(const void *begin, const void *end = 0);

	/** @brief return the begin iterator of the container */
	const_iterator begin() const { return m_values; }
	/** @brief return the end iterator of the container */
	const_iterator end()   const { return m_values + m_numValues; }

	/** @brief return the size of the container */
	size_t size() const { return m_numValues; }
	/** @brief return the i'th element in the container */
	const_reference operator [](size_t i) const { return m_values[i]; }

	/** @brief build a container
	 *
	 * @tparam OutIteratorT output iterator type, which should be char type
	 * @tparam IteratorT    input iterator type, which should:
	 *                      - for C primitive types or their compositions as
	 *                        ValueT, point to ValueT
	 *                      - for Vector as ValueT, point to std::vector
	 *                      - for Trie or HashMap as ValueT, point to std::map or
	 *                        sorted uniqued std::list<std::pair<key, value> >
	 *                      - for Pair as ValueT, point to std::pair
	 *
	 * @param[out] out      output iterator where data will be written to \n
	 *                      Use ostreambuf_iterator<char>(cout) to write stdout,
	 *                      or something alike to write a file or a string.
	 * @param[in]  begin    begin iterator of values \n
	 *                      NOTICE: For building a Trie (std::map x), begin and
	 *                      end should point to &x and &x + 1, not x.begin() and
	 *                      x.end(). Same is building a Vector from std::vector.
	 * @param[in]  end      end iterator of values
	 * @param[in]  skipLast skip the last block of container (internal use)
	 */
	template <class OutIteratorT, class IteratorT>
	static OutIteratorT build(OutIteratorT out, IteratorT begin, IteratorT end,
			void *skipLast = 0);

	template <class OutIteratorT>
	static OutIteratorT build(OutIteratorT out, const ValueT *begin, const ValueT *end,
			void *skipLast = 0);
	template <class OutIteratorT>
	static OutIteratorT build(OutIteratorT out, ValueT *begin, ValueT *end,
			void *skipLast = 0)
	{
		return build(out, (const ValueT *)begin, (const ValueT *)end, skipLast);
	}

private:
	const uint8_t * initPointers(const uint8_t *begin, const uint8_t *end = 0);

	size_t m_numValues;
	const ValueT *m_values;

	template <class V>                            friend class Container;
	template <class V, class S>                   friend class Vector;
	template <class V, int   o, class C, class S> friend class Trie;
	template <class K, class V, class H, class S> friend class HashMap;
	template <class V1, class V2>                 friend class Pair;
};

template <class ValueT, class SizeT>
class Container<Vector<ValueT, SizeT> >
		: private MMap<Container<Vector<ValueT, SizeT> > >
{
public:
	typedef std::vector<
			typename Container<ValueT>::std_value_type> std_value_type;

	typedef          Vector<ValueT, SizeT>   value_type;
	typedef          Vector<ValueT, SizeT>   const_reference;
	typedef Iterator<Vector<ValueT, SizeT> > const_iterator;

	Container();
	Container(const char *filename);
	Container(const void *begin, const void *end = 0);

	const_iterator begin() const { return const_iterator(this, 0); }
	const_iterator end()   const { return const_iterator(this, size()); }

	size_t size() const { return m_entries.size() - 1; }
	const_reference operator [](size_t i) const { return const_reference(this, i); }

	template <class OutIteratorT, class IteratorT>
	static OutIteratorT build(OutIteratorT out, IteratorT begin, IteratorT end,
			void *skipLast = 0);

private:
	const uint8_t * initPointers(const uint8_t *begin, const uint8_t *end = 0);

	Container<SizeT>  m_entries;
	Container<ValueT> m_values;

	static const SizeT m_defaultEntries[1];

	template <class V>                            friend class Container;
	template <class V, class S>                   friend class Vector;
	template <class V, int   o, class C, class S> friend class Trie;
	template <class K, class V, class H, class S> friend class HashMap;
	template <class V1, class V2>                 friend class Pair;
};

template <class ValueT, int option, class CharT, class SizeT>
class Container<Trie<ValueT, option, CharT, SizeT> >
		: private MMap<Container<Trie<ValueT, option, CharT, SizeT> > >
{
public:
	typedef std::map<std::vector<CharT>,
			typename Container<ValueT>::std_value_type> std_value_type;

	typedef          Trie<ValueT, option, CharT, SizeT>   value_type;
	typedef          Trie<ValueT, option, CharT, SizeT>   const_reference;
	typedef Iterator<Trie<ValueT, option, CharT, SizeT> > const_iterator;

	Container();
	Container(const char *filename);
	Container(const void *begin, const void *end = 0);

	const_iterator begin() const { return const_iterator(this, 0); }
	const_iterator end()   const { return const_iterator(this, size()); }

	size_t size() const { return m_nodes[0].children; }
	const_reference operator [](size_t i) const { return const_reference(this, i); }

	template <class OutIteratorT, class IteratorT>
	static OutIteratorT build(OutIteratorT out, IteratorT begin, IteratorT end,
			void *skipLast = 0);

private:
	struct Node
	{
		Node()                 : parent(0), children(0) {}
		Node(SizeT p, SizeT c) : parent(p), children(c) {}

		SizeT parent;
		SizeT children;
	};

	template <class IteratorT>
	struct OpenNode
	{
		IteratorT begin;
		IteratorT end;
		uint32_t  level;
		uint32_t  count;
		int32_t   character;
		SizeT     entry;
	};

	enum { FT_MARGIN = (1 << sizeof(CharT) * 8) + 1 };
	enum { FT_MASK = (SizeT)(1) << (sizeof(SizeT) * 8 - 1) };
	enum { CHAR_TERMINATOR = -1 };

	const uint8_t * initPointers(const uint8_t *begin, const uint8_t *end = 0);

	Container<Node>                  m_nodes;
	Container<SizeT>                 m_paths;
	Container<Vector<CharT, SizeT> > m_tails;
	Container<ValueT>                m_values;

	static const Node m_defaultNodes[1];

	/* If FT_PATH specified in option on querying but not on building, try to
	 * build the path container and save it in m_pathString. */
	std::string m_pathString;

	template <class V>                            friend class Container;
	template <class V, class S>                   friend class Vector;
	template <class V, int   o, class C, class S> friend class Trie;
	template <class K, class V, class H, class S> friend class HashMap;
	template <class V1, class V2>                 friend class Pair;
};

template <class KeyT, class ValueT, class HashT, class SizeT>
class Container<HashMap<KeyT, ValueT, HashT, SizeT> >
		: private MMap<Container<HashMap<KeyT, ValueT, HashT, SizeT> > >
{
public:
	typedef std::map<
			typename Container<KeyT  >::std_value_type,
			typename Container<ValueT>::std_value_type> std_value_type;

	typedef          HashMap<KeyT, ValueT, HashT, SizeT>   value_type;
	typedef          HashMap<KeyT, ValueT, HashT, SizeT>   const_reference;
	typedef Iterator<HashMap<KeyT, ValueT, HashT, SizeT> > const_iterator;

	Container() {}
	Container(const char *filename);
	Container(const void *begin, const void *end = 0);

	const_iterator begin() const { return const_iterator(this, 0); }
	const_iterator end()   const { return const_iterator(this, size()); }

	size_t size() const { return m_maps.size(); }
	const_reference operator [](size_t i) const { return const_reference(this, i); }

	template <class OutIteratorT, class IteratorT>
	static OutIteratorT build(OutIteratorT out, IteratorT begin, IteratorT end,
			void *skipLast = 0);

private:
	template <class T> struct NonConst          { typedef T type; };
	template <class T> struct NonConst<const T> { typedef T type; };

	const uint8_t * initPointers(const uint8_t *begin, const uint8_t *end = 0);

	Container<Vector<Vector<Pair<KeyT, ValueT>, SizeT>, SizeT> > m_maps;

	template <class V>                            friend class Container;
	template <class V, class S>                   friend class Vector;
	template <class V, int   o, class C, class S> friend class Trie;
	template <class K, class V, class H, class S> friend class HashMap;
	template <class V1, class V2>                 friend class Pair;
};

template <class ValueT1, class ValueT2>
class Container<Pair<ValueT1, ValueT2> >
		: private MMap<Container<Pair<ValueT1, ValueT2> > >
{
public:
	typedef std::pair<
			typename Container<ValueT1>::std_value_type,
			typename Container<ValueT2>::std_value_type> std_value_type;

	typedef          Pair<ValueT1, ValueT2>   value_type;
	typedef          Pair<ValueT1, ValueT2>   const_reference;
	typedef Iterator<Pair<ValueT1, ValueT2> > const_iterator;

	Container() {}
	Container(const char *filename);
	Container(const void *begin, const void *end = 0);

	const_iterator begin() const { return const_iterator(this, 0); }
	const_iterator end()   const { return const_iterator(this, size()); }

	size_t size() const { return m_values1.size(); }
	const_reference operator [](size_t i) const { return const_reference(this, i); }

	template <class OutIteratorT, class IteratorT>
	static OutIteratorT build(OutIteratorT out, IteratorT begin, IteratorT end,
			void *skipLast = 0);

private:
	const uint8_t * initPointers(const uint8_t *begin, const uint8_t *end = 0);

	Container<ValueT1> m_values1;
	Container<ValueT2> m_values2;

	template <class V>                            friend class Container;
	template <class V, class S>                   friend class Vector;
	template <class V, int   o, class C, class S> friend class Trie;
	template <class K, class V, class H, class S> friend class HashMap;
	template <class V1, class V2>                 friend class Pair;
};

/** @brief Vector interface to Container
 *
 * @tparam ValueT value type, may be any of following:
 *                -# C primitive types except pointers, e.g.: char, double
 *                -# composition of type 1, e.g.: std::pair<int, float>
 *                -# Vector<ValueT> for a variable-length value, e.g.:
 *                   Vector<char> for a string
 *                -# Trie<ValueT> or HashMap<KeyT, ValueT>
 *                   for an associative array, e.g.:
 *                   HashMap<int, Vector<char> > is an int to string mapping
 *                -# Pair<ValueT1, ValueT2> for any composition, e.g.:
 *                   Pair<Trie<int>, Vector<char> > is a trie and a string
 * @tparam SizeT  size type, may be uint32_t or uint64_t, corresponding to
 *                32-bits or 64-bits addressing
 */
template <class ValueT, class SizeT = uint32_t>
class Vector
{
public:
	typedef typename Container<ValueT>::value_type      value_type;
	typedef typename Container<ValueT>::const_reference const_reference;
	typedef typename Container<ValueT>::const_iterator  const_iterator;

	Vector() : m_container(&m_defaultContainer), m_i(0) {}

	/** @brief return the begin iterator of the vector */
	const_iterator begin() const
	{
		return m_container->m_values.begin() + m_container->m_entries[m_i];
	}
	/** @brief return the end iterator of the vector */
	const_iterator end() const
	{
		return m_container->m_values.begin() + m_container->m_entries[m_i + 1];
	}

	/** @brief return the size of the vector */
	size_t size() const
	{
		return m_container->m_entries[m_i + 1] - m_container->m_entries[m_i];
	}
	/** @brief return the i'th element in the vector */
	const_reference operator [](size_t i) const
	{
		return m_container->m_values[m_container->m_entries[m_i] + i];
	}

	const Vector * operator ->() const { return this; }

	friend bool operator ==(const Vector &a, const Vector &b)
	{ return a.size() == b.size() &&  std::equal(a.begin(), a.end(), b.begin()); }
	friend bool operator !=(const Vector &a, const Vector &b)
	{ return a.size() != b.size() || !std::equal(a.begin(), a.end(), b.begin()); }
	friend bool operator < (const Vector &a, const Vector &b)
	{ return  lexicographical_compare(a.begin(), a.end(), b.begin(), b.end()); }
	friend bool operator > (const Vector &a, const Vector &b)
	{ return  lexicographical_compare(b.begin(), b.end(), a.begin(), a.end()); }
	friend bool operator <=(const Vector &a, const Vector &b)
	{ return !lexicographical_compare(b.begin(), b.end(), a.begin(), a.end()); }
	friend bool operator >=(const Vector &a, const Vector &b)
	{ return !lexicographical_compare(a.begin(), a.end(), b.begin(), b.end()); }

private:
	Vector(const Container<Vector<ValueT, SizeT> > *container, size_t i)
			: m_container(container), m_i(i) {}

	static const Container<Vector<ValueT, SizeT> > defaultContainer();

	/* Vector is actually a pointer to the m_i'th element in m_container. */
	const Container<Vector<ValueT, SizeT> > *m_container;
	size_t m_i;

	/* The default value Vector() points to m_defaultContainer. */
	static const Container<Vector<ValueT, SizeT> > m_defaultContainer;

	friend class Container<Vector<ValueT, SizeT> >;
	friend class Iterator <Vector<ValueT, SizeT> >;
};

/** @brief Trie interface to Container
 *
 * @tparam ValueT value type, may be any of following:
 *                -# C primitive types except pointers, e.g.: char, double
 *                -# composition of type 1, e.g.: std::pair<int, float>
 *                -# Vector<ValueT> for a variable-length value, e.g.:
 *                   Vector<char> for a string
 *                -# Trie<ValueT> or HashMap<KeyT, ValueT>
 *                   for an associative array, e.g.:
 *                   HashMap<int, Vector<char> > is an int to string mapping
 *                -# Pair<ValueT1, ValueT2> for any composition, e.g.:
 *                   Pair<Trie<int>, Vector<char> > is a trie and a string
 * @tparam option structure option, may be bitwise-or'd of following:
 *                - FT_TAIL: saves trie tails isolatedly, yields better
 *                  performance in most cases
 *                - FT_PATH: saves or loads trie paths, for extracting the key
 *                  from an iterator, cost more time on loading or more space
 *                - FT_QUICKBUILD: quick build at the cost of a larger output
 * @tparam CharT  character type, may be uint8_t or uint16_t, corresponding to
 *                256-branches or 65536-branches trie
 * @tparam SizeT  size type, may be uint32_t or uint64_t, corresponding to
 *                32-bits or 64-bits addressing
 */
template <class ValueT, int option = FT_TAIL,
		class CharT = uint8_t, class SizeT = uint32_t>
class Trie
{
public:
	typedef typename Container<ValueT>::value_type      value_type;
	typedef typename Container<ValueT>::const_reference const_reference;
	typedef typename Container<ValueT>::const_iterator  const_iterator;

	Trie() : m_container(&m_defaultContainer), m_i(0) {}

	/** @brief return the begin iterator of the trie */
	const_iterator begin() const
	{
		return m_container->m_values.begin()
				+ (m_container->m_nodes[m_i + 1].parent & ~FT_MASK);
	}
	/** @brief return the end iterator of the trie */
	const_iterator end() const
	{
		return m_container->m_values.begin()
				+ (m_container->m_nodes[m_i + 2].parent & ~FT_MASK);
	}

	/** @brief return the size of the trie */
	size_t size() const
	{
		return m_container->m_nodes[m_i + 2].parent
				-  m_container->m_nodes[m_i + 1].parent;
	}
	/** @brief get the value of a key in trie, same as operator () */
	template <class KeyT>
	const_reference operator [](const KeyT &key) const
	{
		return operator ()(key);
	}

	const Trie * operator ->() const { return this; }

	/** @brief match a key in trie
	 *
	 * @param[in]  keyBegin begin of the key
	 * @param[in]  keyEnd   end of the key
	 * @param[out] value    value of the key \n
	 *                      If value is 0 or no match, no value will be written.
	 * @return              number of matches
	 */
	uint32_t match(const CharT *keyBegin, const CharT *keyEnd,
			ValueT *value = 0) const;
	/** @brief match all keys in trie, fixed beginning, variable ending
	 *
	 * @param[in]  keyBegin begin of possible keys
	 * @param[in]  keyEnd   maximum end of possible keys
	 * @param[in]  maxMatches maximum number of matches returned
	 * @param[out] ranges   ranges of matched keys \n
	 *                      If ranges is 0 or no match, no range will be written.
	 * @param[out] values   values of matched keys \n
	 *                      If values is 0 or no match, no value will be written.
	 * @return              number of matches
	 */
	uint32_t matchBeginning(const CharT *keyBegin, const CharT *keyEnd,
			uint32_t maxMatches = (uint32_t)(-1),
			Range<CharT> *ranges = 0, value_type *values = 0) const;
	/** @brief match all keys in trie, variable beginning, variable ending
	 *
	 * @param[in]  keyBegin minimum begin of possible keys
	 * @param[in]  keyEnd   maximum end of possible keys
	 * @param[in]  maxMatches maximum number of matches returned
	 * @param[out] ranges   ranges of matched keys \n
	 *                      If ranges is 0 or no match, no range will be written.
	 * @param[out] values   values of matched keys \n
	 *                      If values is 0 or no match, no value will be written.
	 * @param[in]  step     iterating step of the beginning of possible keys \n
	 *                      E.g. key = "abcdefg" and step = 2, possible keys are
	 *                      "a...", "c...", "e..." and "g...".
	 * @return              number of matches
	 */
	uint32_t matchAll(const CharT *keyBegin, const CharT *keyEnd,
			uint32_t maxMatches = (uint32_t)(-1),
			Range<CharT> *ranges = 0, value_type *values = 0, uint32_t step = 1) const;

	/** @brief locate the value of a key in trie
	 *
	 * @param[in]  keyBegin begin of the key
	 * @param[in]  keyEnd   end of the key
	 * @return              iterator to the value of the key \n
	 *                      If no match, end() will be returned.
	 *
	 * Similar with match(keyBegin, keyEnd, value) const, but return the iterator
	 * instead of number of matches.
	 */
	const_iterator find(const CharT *keyBegin, const CharT *keyEnd) const;

	/** @brief locate the value of a key in trie
	 *
	 * @tparam KeyT   key type, may be any of following:
	 *                -# C primitive types except pointers, e.g.: char, double
	 *                -# composition of type 1, e.g.: std::pair<int, float>
	 *                -# for a variable-length key, please use std::string or
	 *                   std::vector<T> with type 1 or 2 as T, e.g.:
	 *                   std::vector<int>
	 *
	 * @param[in]  key      the key
	 * @return              iterator to the value of the key \n
	 *                      If no match, end() will be returned.
	 *
	 * These are overloaded versions of find(keyBegin, keyEnd) const:
	 * - If KeyT is std::string or std::vector, find(key.begin(), key.end())
	 *   will be returned.
	 * - If a pointer is given, find(key, end) will be returned, where
	 *   *end == KeyT().
	 * - Otherwise, find(&key, &key + sizeof(KeyT)) will be returned.
	 */
	template <class KeyT>
	const_iterator find(const KeyT &key) const
	{
		return find((CharT *)range(key).begin, (CharT *)range(key).end);
	}

	/** @brief get the value of a key in trie
	 *
	 * @param[in]  keyBegin begin of the key
	 * @param[in]  keyEnd   end of the key
	 * @return              value of the key \n
	 *                      If no match, ValueT() will be returned.
	 *
	 * Similar with match(keyBegin, keyEnd, value) const, but return the value
	 * instead of number of matches.
	 */
	const_reference operator ()(const CharT *keyBegin, const CharT *keyEnd) const;

	/** @brief get the value of a key in trie
	 *
	 * @tparam KeyT   key type, may be any of following:
	 *                -# C primitive types except pointers, e.g.: char, double
	 *                -# composition of type 1, e.g.: std::pair<int, float>
	 *                -# for a variable-length key, please use std::string or
	 *                   std::vector<T> with type 1 or 2 as T, e.g.:
	 *                   std::vector<int>
	 *
	 * @param[in]  key      the key
	 * @return              value of the key \n
	 *                      If no match, ValueT() will be returned.
	 *
	 * These are overloaded versions of operator ()(keyBegin, keyEnd) const:
	 * - If KeyT is std::string or std::vector, operator ()(key.begin(), key.end())
	 *   will be returned.
	 * - If a pointer is given, operator ()(key, end) will be returned, where
	 *   *end == KeyT().
	 * - Otherwise, operator ()(&key, &key + sizeof(KeyT)) will be returned.
	 */
	template <class KeyT>
	const_reference operator ()(const KeyT &key) const
	{
		return operator ()((CharT *)range(key).begin, (CharT *)range(key).end);
	}

	/** @brief get the key of an iterator
	 *
	 * @tparam KeyT   key type, same as in operator ()(key) const
	 *
	 * @param[in]  it       the iterator
	 * @return              key of the iterator \n
	 *                      If FT_PATH not specified in option, neither during
	 *                      building or querying, key will be unextractable.
	 *                      Under this circumstance, KeyT() will be returned.
	 */
	template <class KeyT>
	const KeyT key(const_iterator it) const;

private:
	/* an partial specialized template for fetching variable key type */
	template <class T>
	struct Memory
	{
		static bool resize(T &x, size_t size) { return sizeof(T) == size; }
		static void *begin(T &x) { return &x; }
		static void *end  (T &x) { return &x + 1; }
	};

	template <class _CharT, class _Traits, class _Alloc>
	struct Memory<std::basic_string<_CharT, _Traits, _Alloc> >
	{
		typedef std::basic_string<_CharT, _Traits, _Alloc> T;

		static bool resize(T &x, size_t size)
		{
			if (size % sizeof(_CharT)) return false;
			x.resize(size / sizeof(_CharT)); return true;
		}
		static void *begin(T &x) { return &*x.begin(); }
		static void *end  (T &x) { return &*x.end(); }
	};

	template <class _Tp, class _Alloc>
	struct Memory<std::vector<_Tp, _Alloc> >
	{
		typedef std::vector<_Tp, _Alloc> T;

		static bool resize(T &x, size_t size)
		{
			if (size % sizeof(_Tp)) return false;
			x.resize(size / sizeof(_Tp)); return true;
		}
		static void *begin(T &x) { return &*x.begin(); }
		static void *end  (T &x) { return &*x.end(); }
	};

	typedef typename Container<Trie<ValueT, option, CharT, SizeT> >::Node Node;

	enum { FT_MASK = Container<Trie<ValueT, option, CharT, SizeT> >::FT_MASK };
	enum { CHAR_TERMINATOR = -1 };

	Trie(const Container<Trie<ValueT, option, CharT, SizeT> > *container, size_t i)
			: m_container(container), m_i(i) {}

	static const Container<Trie<ValueT, option, CharT, SizeT> > defaultContainer();

	/* Trie is actually a pointer to the m_i'th element in m_container. */
	const Container<Trie<ValueT, option, CharT, SizeT> > *m_container;
	size_t m_i;

	/* The default value Trie() points to m_defaultContainer. */
	static const Container<Trie<ValueT, option, CharT, SizeT> > m_defaultContainer;

	/* If no match, m_zero = ValueT() will be return. */
	static const ValueT m_zero;

	friend class Container<Trie<ValueT, option, CharT, SizeT> >;
	friend class Iterator <Trie<ValueT, option, CharT, SizeT> >;
};

/** @brief HashMap interface to Container
 *
 * @tparam KeyT   key type, may be any of following:
 *                -# C primitive types except pointers, e.g.: char, double
 *                -# composition of type 1, e.g.: std::pair<int, float>
 *                -# for a variable-length key, please use std::string or
 *                   std::vector<T> with type 1 or 2 as T, e.g.:
 *                   std::vector<int>
 * @tparam ValueT value type, may be any of following:
 *                -# C primitive types except pointers, e.g.: char, double
 *                -# composition of type 1, e.g.: std::pair<int, float>
 *                -# Vector<ValueT> for a variable-length value, e.g.:
 *                   Vector<char> for a string
 *                -# Trie<ValueT> or HashMap<KeyT, ValueT>
 *                   for an associative array, e.g.:
 *                   HashMap<int, Vector<char> > is an int to string mapping
 *                -# Pair<ValueT1, ValueT2> for any composition, e.g.:
 *                   Pair<Trie<int>, Vector<char> > is a trie and a string
 * @tparam HashT  hash function type
 * @tparam SizeT  size type, may be uint32_t or uint64_t, corresponding to
 *                32-bits or 64-bits addressing
 */
template <class KeyT, class ValueT,
		class HashT = MulAddHash, class SizeT = uint32_t>
class HashMap
{
public:
	typedef typename Container<           ValueT  >::value_type      value_type;
	typedef typename Container<           ValueT  >::const_reference const_reference;
	typedef typename Container<Pair<KeyT, ValueT> >::const_iterator  const_iterator;

	HashMap() : m_container(&m_defaultContainer), m_i(0) {}

	/** @brief return the begin iterator of the hash map */
	const_iterator begin() const
	{
		return m_container->m_maps[m_i].begin()->begin();
	}
	/** @brief return the end iterator of the hash map */
	const_iterator end() const
	{
		return m_container->m_maps[m_i].end()->begin();
	}

	/** @brief return the size of the hash map */
	size_t size() const
	{
		return end() - begin();
	}
	/** @brief get the value of a key in hash map, same as operator () */
	template <class AnyKeyT>
	const_reference operator [](const AnyKeyT &key) const
	{
		return operator ()(key);
	}

	const HashMap * operator ->() const { return this; }

	/** @brief locate a key in hash map
	 *
	 * @param[in]  key      the key
	 * @return              iterator to the key-value Pair \n
	 *                      If no match, end() will be returned.
	 */
	inline const_iterator find(const KeyT &key) const;

	/* for Vector as KeyT: string, vector or * are acceptable */
	template <class _CharT, class _Traits, class _Alloc>
	const_iterator find(const std::basic_string<_CharT, _Traits, _Alloc> &key) const
	{ return find(key, range(key)); }
	template <class _Tp, class _Alloc>
	const_iterator find(const std::vector<_Tp, _Alloc> &key) const
	{ return find(key, range(key)); }
	template <class V, class S>
	const_iterator find(const Vector<V, S> &key) const
	{ return find(key, range(key)); }
	const_iterator find(const char *key) const
	{ return find(key, range(key)); }
	const_iterator find(char *key) const
	{ return find(key, range(key)); }
	template <class T>
	const_iterator find(T *key) const
	{ return find(key, range(key)); }

	/** @brief get the value of a key in hash map
	 *
	 * @param[in]  key      the key
	 * @return              value of the key \n
	 *                      If no match, ValueT() will be returned.
	 */
	inline const_reference operator ()(const KeyT &key) const;

	/* for Vector as KeyT: string, vector or * are acceptable */
	template <class _CharT, class _Traits, class _Alloc>
	const_reference operator ()(const std::basic_string<_CharT, _Traits, _Alloc> &key) const
	{ return operator ()(key, range(key)); }
	template <class _Tp, class _Alloc>
	const_reference operator ()(const std::vector<_Tp, _Alloc> &key) const
	{ return operator ()(key, range(key)); }
	template <class V, class S>
	const_reference operator ()(const Vector<V, S> &key) const
	{ return operator ()(key, range(key)); }
	const_reference operator ()(const char *key) const
	{ return operator ()(key, range(key)); }
	const_reference operator ()(char *key) const
	{ return operator ()(key, range(key)); }
	template <class T>
	const_reference operator ()(T *key) const
	{ return operator ()(key, range(key)); }

private:
	HashMap(const Container<HashMap<KeyT, ValueT, HashT, SizeT> > *container, size_t i)
			: m_container(container), m_i(i) {}

	template <class StdKeyT, class T>
	inline const_iterator find(const StdKeyT &key, Range<T> r) const;

	template <class StdKeyT, class T>
	inline const_reference operator ()(const StdKeyT &key, Range<T> r) const;

	static const Container<HashMap<KeyT, ValueT, HashT, SizeT> > defaultContainer();

	/* HashMap is actually a pointer to the m_i'th element in m_container. */
	const Container<HashMap<KeyT, ValueT, HashT, SizeT> > *m_container;
	size_t m_i;

	/* The default value HashMap() points to m_defaultContainer. */
	static const Container<HashMap<KeyT, ValueT, HashT, SizeT> > m_defaultContainer;

	/* If no match, m_zero = ValueT() will be return. */
	static const ValueT m_zero;

	friend class Container<HashMap<KeyT, ValueT, HashT, SizeT> >;
	friend class Iterator <HashMap<KeyT, ValueT, HashT, SizeT> >;
};

/** @brief Pair interface to Container
 *
 * @tparam ValueT1 first value type, may be any of following:
 *                -# C primitive types except pointers, e.g.: char, double
 *                -# composition of type 1, e.g.: std::pair<int, float>
 *                -# Vector<ValueT> for a variable-length value, e.g.:
 *                   Vector<char> for a string
 *                -# Trie<ValueT> or HashMap<KeyT, ValueT>
 *                   for an associative array, e.g.:
 *                   HashMap<int, Vector<char> > is an int to string mapping
 *                -# Pair<ValueT1, ValueT2> for any composition, e.g.:
 *                   Pair<Trie<int>, Vector<char> > is a trie and a string
 * @tparam ValueT2 second value type
 */
template <class ValueT1, class ValueT2>
class Pair
{
public:
	typedef ValueT1 first_type;
	typedef ValueT2 second_type;

	ValueT1 first;
	ValueT2 second;

	Pair() : first(), second() {}

	const Pair * operator ->() const { return this; }

	friend bool operator ==(const Pair &a, const Pair &b)
	{ return a.first == b.first && a.second == b.second; }
	friend bool operator !=(const Pair &a, const Pair &b)
	{ return a.first != b.first || a.second != b.second; }
	friend bool operator < (const Pair &a, const Pair &b)
	{ return a.first <  b.first || a.first == b.first && a.second <  b.second; }
	friend bool operator > (const Pair &a, const Pair &b)
	{ return a.first >  b.first || a.first == b.first && a.second >  b.second; }
	friend bool operator <=(const Pair &a, const Pair &b)
	{ return a.first <= b.first || a.first == b.first && a.second <= b.second; }
	friend bool operator >=(const Pair &a, const Pair &b)
	{ return a.first >= b.first || a.first == b.first && a.second >= b.second; }

private:
	Pair(const Container<Pair<ValueT1, ValueT2> > *container, size_t i)
			: first(container->m_values1[i]), second(container->m_values2[i]) {}

	friend class Container<Pair<ValueT1, ValueT2> >;
	friend class Iterator <Pair<ValueT1, ValueT2> >;
};

template <class ValueT>
class Iterator
{
public:
	typedef std::random_access_iterator_tag iterator_category;
	typedef ValueT value_type;
	typedef size_t difference_type;
	typedef ValueT pointer;
	typedef ValueT reference;

	Iterator() : m_container(0), m_i(0) {}

	const Iterator & base() const { return *this; }

	ValueT operator * ()         const { return ValueT(m_container, m_i); }
	ValueT operator ->()         const { return ValueT(m_container, m_i); }
	ValueT operator [](size_t i) const { return ValueT(m_container, m_i + i); }

	Iterator & operator ++()    { ++ m_i; return *this; }
	Iterator   operator ++(int) { return Iterator(m_container, m_i ++); }
	Iterator & operator --()    { -- m_i; return *this; }
	Iterator   operator --(int) { return Iterator(m_container, m_i --); }
	Iterator & operator +=(size_t i)       { m_i += i; return *this; }
	Iterator   operator + (size_t i) const { return Iterator(m_container, m_i + i); }
	Iterator & operator -=(size_t i)       { m_i -= i; return *this; }
	Iterator   operator - (size_t i) const { return Iterator(m_container, m_i - i); }

	friend bool   operator ==(const Iterator &a, const Iterator &b)
	{ return a.m_i == b.m_i && a.m_container == b.m_container; }
	friend bool   operator !=(const Iterator &a, const Iterator &b)
	{ return a.m_i != b.m_i || a.m_container != b.m_container; }
	friend bool   operator < (const Iterator &a, const Iterator &b) { return a.m_i <  b.m_i; }
	friend bool   operator > (const Iterator &a, const Iterator &b) { return a.m_i >  b.m_i; }
	friend bool   operator <=(const Iterator &a, const Iterator &b) { return a.m_i <= b.m_i; }
	friend bool   operator >=(const Iterator &a, const Iterator &b) { return a.m_i >= b.m_i; }
	friend size_t operator - (const Iterator &a, const Iterator &b) { return a.m_i -  b.m_i; }
	friend Iterator operator + (size_t i, const Iterator &b) { return b + i; }

private:
	Iterator(const Container<ValueT> *container, size_t i)
			: m_container(container), m_i(i) {}

	/* Iterator is a pointer to the m_i'th element in m_container. */
	const Container<ValueT> *m_container;
	size_t m_i;

	friend class Container<ValueT>;

	/* HashMap need return conference to value */
	template <class K, class V, class H, class S> friend class HashMap;
};

template <class ValueT>
Container<ValueT>::Container(const char *filename)
		: MMap<Container<ValueT> >(filename)
{
	if (filename[0] == 0) throw int(-1);

	const uint8_t *begin = (uint8_t *)this->mmap().first;
	const uint8_t *end = begin + this->mmap().second;

	if (initPointers(begin, end) != end) throw int(-1);
}

template <class ValueT>
Container<ValueT>::Container(const void *begin, const void *end)
{
	if (initPointers((uint8_t *)begin, (uint8_t *)end)
			!= (uint8_t *)end && end) throw int(-1);
}

template <class ValueT>
const uint8_t * Container<ValueT>::initPointers(
		const uint8_t *begin, const uint8_t *end)
{
	const size_t align = 16;

	if (end && end < begin + align) return 0;

	m_numValues = ((size_t *)begin)[0];
	m_values = (ValueT *)(begin + align);

	size_t bytes = sizeof(ValueT) * m_numValues;
	bytes = (bytes + align - 1) / align * align;

	if (end && end < begin + align + bytes) return 0;

	return begin + align + bytes;
}

template <class ValueT, class SizeT>
Container<Vector<ValueT, SizeT> >::Container()
{
	m_entries.m_numValues = 1;
	m_entries.m_values = m_defaultEntries;
}

template <class ValueT, class SizeT>
Container<Vector<ValueT, SizeT> >::Container(const char *filename)
		: MMap<Container<Vector<ValueT, SizeT> > >(filename)
{
	if (filename[0] == 0) throw int(-1);

	const uint8_t *begin = (uint8_t *)this->mmap().first;
	const uint8_t *end = begin + this->mmap().second;

	if (initPointers(begin, end) != end) throw int(-1);
}

template <class ValueT, class SizeT>
Container<Vector<ValueT, SizeT> >::Container(
		const void *begin, const void *end)
{
	if (initPointers((uint8_t *)begin, (uint8_t *)end)
			!= (uint8_t *)end && end) throw int(-1);
}

template <class ValueT, class SizeT>
const uint8_t * Container<Vector<ValueT, SizeT> >::initPointers(
		const uint8_t *begin, const uint8_t *end)
{
	begin = m_entries.initPointers(begin, end); if (!begin) return 0;
	begin = m_values .initPointers(begin, end); if (!begin) return 0;

	return begin;
}

template <class ValueT, int option, class CharT, class SizeT>
Container<Trie<ValueT, option, CharT, SizeT> >::Container()
{
	m_nodes.m_numValues = 1;
	m_nodes.m_values = m_defaultNodes;
}

template <class ValueT, int option, class CharT, class SizeT>
Container<Trie<ValueT, option, CharT, SizeT> >::Container(const char *filename)
		: MMap<Container<Trie<ValueT, option, CharT, SizeT> > >(filename)
{
	if (filename[0] == 0) throw int(-1);

	const uint8_t *begin = (uint8_t *)this->mmap().first;
	const uint8_t *end = begin + this->mmap().second;

	if (initPointers(begin, end) != end) throw int(-1);
}

template <class ValueT, int option, class CharT, class SizeT>
Container<Trie<ValueT, option, CharT, SizeT> >::Container(
		const void *begin, const void *end)
{
	if (initPointers((uint8_t *)begin, (uint8_t *)end)
			!= (uint8_t *)end && end) throw int(-1);
}

template <class ValueT, int option, class CharT, class SizeT>
const uint8_t * Container<Trie<ValueT, option, CharT, SizeT> >::initPointers(
		const uint8_t *begin, const uint8_t *end)
{
	begin = m_nodes .initPointers(begin, end); if (!begin) return 0;
	begin = m_paths .initPointers(begin, end); if (!begin) return 0;
	begin = m_tails .initPointers(begin, end); if (!begin) return 0;
	begin = m_values.initPointers(begin, end); if (!begin) return 0;

	if ((option & FT_PATH) && m_paths.size() < m_values.size())
	{
		m_pathString.clear();

		std::vector<SizeT> paths(m_values.size());

		for (size_t i = size() + 2; i != m_nodes.size(); i ++)
		{
			if (m_nodes[i].parent
					&& m_nodes[i].parent < m_nodes.size()
					&& m_nodes[i].children < paths.size()
					&& m_nodes[m_nodes[i].parent].children + CHAR_TERMINATOR == i)
				paths[m_nodes[i].children] = m_nodes[i].parent;
			if ((m_nodes[i].children & FT_MASK)
					&& (m_nodes[i].children & ~FT_MASK) < paths.size())
				paths[m_nodes[i].children & ~FT_MASK] = i;
		}

		Container<SizeT>::build(
				back_inserter(m_pathString), &*paths.begin(), &*paths.end());
	}

	return begin;
}

template <class ValueT, int option, class CharT, class SizeT>
uint32_t Trie<ValueT, option, CharT, SizeT>::match(
		const CharT *keyBegin, const CharT *keyEnd, ValueT *value) const
{
	const Node *nodes = m_container->m_nodes.m_values;

	SizeT node = 1 + m_i;
	SizeT children = nodes[node].children;

	for (const CharT *key = keyBegin; ; key ++)
	{
		if ((option & FT_TAIL) && (children & FT_MASK))
		{
			children &= ~FT_MASK;

			if ((size_t)(keyEnd - key)     != m_container->m_tails[children].size()
					|| !std::equal(key, keyEnd, &*m_container->m_tails[children].begin()))
				return 0;

			if (value) *value = m_container->m_values[children];

			return 1;
		}

		if (key == keyEnd) break;

		if (nodes[children + *key].parent != node) return 0;
		node = children + *key;
		children = nodes[children + *key].children;
	}

	SizeT term = children + CHAR_TERMINATOR;

	if (nodes[term].parent != node) return 0;
	if (value) *value = m_container->m_values[nodes[term].children];

	return 1;
}

template <class ValueT, int option, class CharT, class SizeT>
uint32_t Trie<ValueT, option, CharT, SizeT>::matchBeginning(
		const CharT *keyBegin, const CharT *keyEnd,
		uint32_t maxMatches, Range<CharT> *ranges,
		Trie<ValueT, option, CharT, SizeT>::value_type *values) const
{
	uint32_t numMatches = 0;

	if (!maxMatches) return numMatches;

	const Node *nodes = m_container->m_nodes.m_values;

	SizeT node = 1 + m_i;
	SizeT children = nodes[node].children;

	for (const CharT *key = keyBegin; ; key ++)
	{
		if ((option & FT_TAIL) && (children & FT_MASK))
		{
			children &= ~FT_MASK;

			size_t size = m_container->m_tails[children].size();

			if ((size_t)(keyEnd - key) < size
					|| !std::equal(key, key + size, &*m_container->m_tails[children].begin()))
				return numMatches;

			if (ranges)
			{
				ranges[numMatches].begin = keyBegin;
				ranges[numMatches].end = key + size;
			}
			if (values) values[numMatches] = m_container->m_values[children];

			return ++ numMatches;
		}

		SizeT term = children + CHAR_TERMINATOR;

		if (nodes[term].parent == node)
		{
			if (ranges)
			{
				ranges[numMatches].begin = keyBegin;
				ranges[numMatches].end = key;
			}
			if (values) values[numMatches] = m_container->m_values[nodes[term].children];

			if (++ numMatches >= maxMatches) return numMatches;
		}

		if (key == keyEnd) break;

		if (nodes[children + *key].parent != node) return numMatches;
		node = children + *key;
		children = nodes[children + *key].children;
	}

	return numMatches;
}

template <class ValueT, int option, class CharT, class SizeT>
uint32_t Trie<ValueT, option, CharT, SizeT>::matchAll(
		const CharT *keyBegin, const CharT *keyEnd,
		uint32_t maxMatches, Range<CharT> *ranges,
		Trie<ValueT, option, CharT, SizeT>::value_type *values, uint32_t step) const
{
	uint32_t numMatches = 0;

	for (const CharT *key = keyBegin; ; key += step)
	{
		numMatches += matchBeginning(key, keyEnd, maxMatches - numMatches,
				(ranges ? ranges + numMatches : 0),
				(values ? values + numMatches : 0));
		if ((size_t)(keyEnd - key) < step || numMatches == maxMatches) break;
	}

	return numMatches;
}

template <class ValueT, int option, class CharT, class SizeT>
typename Trie<ValueT, option, CharT, SizeT>::const_iterator
Trie<ValueT, option, CharT, SizeT>::find(
		const CharT *keyBegin, const CharT *keyEnd) const
{
	const Node *nodes = m_container->m_nodes.m_values;

	SizeT node = 1 + m_i;
	SizeT children = nodes[node].children;

	for (const CharT *key = keyBegin; ; key ++)
	{
		if ((option & FT_TAIL) && (children & FT_MASK))
		{
			children &= ~FT_MASK;

			if ((size_t)(keyEnd - key)     != m_container->m_tails[children].size()
					|| !std::equal(key, keyEnd, &*m_container->m_tails[children].begin()))
				return end();

			return m_container->m_values.begin() + children;
		}

		if (key == keyEnd) break;

		if (nodes[children + *key].parent != node) return end();
		node = children + *key;
		children = nodes[children + *key].children;
	}

	SizeT term = children + CHAR_TERMINATOR;

	if (nodes[term].parent != node) return end();
	return m_container->m_values.begin() + nodes[term].children;
}

template <class ValueT, int option, class CharT, class SizeT>
typename Trie<ValueT, option, CharT, SizeT>::const_reference
Trie<ValueT, option, CharT, SizeT>::operator ()(
		const CharT *keyBegin, const CharT *keyEnd) const
{
	const Node *nodes = m_container->m_nodes.m_values;

	SizeT node = 1 + m_i;
	SizeT children = nodes[node].children;

	for (const CharT *key = keyBegin; ; key ++)
	{
		if ((option & FT_TAIL) && (children & FT_MASK))
		{
			children &= ~FT_MASK;

			if ((size_t)(keyEnd - key)     != m_container->m_tails[children].size()
					|| !std::equal(key, keyEnd, &*m_container->m_tails[children].begin()))
				return m_zero;

			return m_container->m_values[children];
		}

		if (key == keyEnd) break;

		if (nodes[children + *key].parent != node) return m_zero;
		node = children + *key;
		children = nodes[children + *key].children;
	}

	SizeT term = children + CHAR_TERMINATOR;

	if (nodes[term].parent != node) return m_zero;
	return m_container->m_values[nodes[term].children];
}

template <class ValueT, int option, class CharT, class SizeT>
template <class KeyT>
const KeyT Trie<ValueT, option, CharT, SizeT>::key(const_iterator it) const
{
	const Node  *nodes = m_container->m_nodes.m_values;
	const SizeT *paths = m_container->m_paths.m_values;

	if ((option & FT_PATH)
			&& m_container->m_paths.size() < m_container->m_values.size())
		paths = Container<SizeT>((void *)m_container->m_pathString.c_str()).m_values;
	else if (!(option & FT_PATH))
		return KeyT();

	size_t i = it - m_container->m_values.begin();

	size_t numChars = 0;
	if (option & FT_TAIL) numChars += m_container->m_tails[i].size();
	for (SizeT node = paths[i]; !(nodes[node].parent & FT_MASK);
			node = nodes[node].parent) numChars ++;

	KeyT result;

	if (!Memory<KeyT>::resize(result, numChars * sizeof(CharT))) return result;

	CharT *p = (CharT *)Memory<KeyT>::end(result);
	if (option & FT_TAIL)
	{
		p -= m_container->m_tails[i].size();
		std::copy(m_container->m_tails[i].begin(), m_container->m_tails[i].end(), p);
	}
	for (SizeT node = paths[i]; !(nodes[node].parent & FT_MASK);
			node = nodes[node].parent)
		*(-- p) = (CharT)(node - nodes[nodes[node].parent].children);

	return result;
}

template <class KeyT, class ValueT, class HashT, class SizeT>
Container<HashMap<KeyT, ValueT, HashT, SizeT> >::Container(const char *filename)
		: MMap<Container<HashMap<KeyT, ValueT, HashT, SizeT> > >(filename)
{
	if (filename[0] == 0) throw int(-1);

	const uint8_t *begin = (uint8_t *)this->mmap().first;
	const uint8_t *end = begin + this->mmap().second;

	if (initPointers(begin, end) != end) throw int(-1);
}

template <class KeyT, class ValueT, class HashT, class SizeT>
Container<HashMap<KeyT, ValueT, HashT, SizeT> >::Container(
		const void *begin, const void *end)
{
	if (initPointers((uint8_t *)begin, (uint8_t *)end)
			!= (uint8_t *)end && end) throw int(-1);
}

template <class KeyT, class ValueT, class HashT, class SizeT>
const uint8_t * Container<HashMap<KeyT, ValueT, HashT, SizeT> >::initPointers(
		const uint8_t *begin, const uint8_t *end)
{
	begin = m_maps.initPointers(begin, end); if (!begin) return 0;

	return begin;
}

template <class KeyT, class ValueT, class HashT, class SizeT>
typename HashMap<KeyT, ValueT, HashT, SizeT>::const_iterator
HashMap<KeyT, ValueT, HashT, SizeT>::find(const KeyT &key) const
{
	Vector<Pair<KeyT, ValueT>, SizeT> items =
			m_container->m_maps[m_i][HashT()(key) % m_container->m_maps[m_i].size()];

	for (typename Vector<Pair<KeyT, ValueT>, SizeT>::const_iterator
			it = items.begin(); it != items.end(); ++ it)
		if (key == it.m_container->m_values1[it.m_i]) return it;

	return end();
}

template <class KeyT, class ValueT, class HashT, class SizeT>
typename HashMap<KeyT, ValueT, HashT, SizeT>::const_reference
HashMap<KeyT, ValueT, HashT, SizeT>::operator ()(const KeyT &key) const
{
	Vector<Pair<KeyT, ValueT>, SizeT> items =
			m_container->m_maps[m_i][HashT()(key) % m_container->m_maps[m_i].size()];

	for (typename Vector<Pair<KeyT, ValueT>, SizeT>::const_iterator
			it = items.begin(); it != items.end(); ++ it)
		if (key == it.m_container->m_values1[it.m_i])
			return it.m_container->m_values2[it.m_i];

	return m_zero;
}

template <class KeyT, class ValueT, class HashT, class SizeT>
template <class StdKeyT, class T>
typename HashMap<KeyT, ValueT, HashT, SizeT>::const_iterator
HashMap<KeyT, ValueT, HashT, SizeT>::find(const StdKeyT &key, Range<T> r) const
{
	Vector<Pair<KeyT, ValueT>, SizeT> items =
			m_container->m_maps[m_i][HashT()(key) % m_container->m_maps[m_i].size()];

	for (typename Vector<Pair<KeyT, ValueT>, SizeT>::const_iterator
			it = items.begin(); it != items.end(); ++ it)
		if ((size_t)(r.end - r.begin)  == it.m_container->m_values1[it.m_i].size()
				&& std::equal(r.begin, r.end, it.m_container->m_values1[it.m_i].begin()))
			return it;

	return end();
}

template <class KeyT, class ValueT, class HashT, class SizeT>
template <class StdKeyT, class T>
typename HashMap<KeyT, ValueT, HashT, SizeT>::const_reference
HashMap<KeyT, ValueT, HashT, SizeT>::operator ()(const StdKeyT &key, Range<T> r) const
{
	Vector<Pair<KeyT, ValueT>, SizeT> items =
			m_container->m_maps[m_i][HashT()(key) % m_container->m_maps[m_i].size()];

	for (typename Vector<Pair<KeyT, ValueT>, SizeT>::const_iterator
			it = items.begin(); it != items.end(); ++ it)
		if ((size_t)(r.end - r.begin)  == it.m_container->m_values1[it.m_i].size()
				&& std::equal(r.begin, r.end, it.m_container->m_values1[it.m_i].begin()))
			return it.m_container->m_values2[it.m_i];

	return m_zero;
}

template <class ValueT1, class ValueT2>
Container<Pair<ValueT1, ValueT2> >::Container(const char *filename)
		: MMap<Container<Pair<ValueT1, ValueT2> > >(filename)
{
	if (filename[0] == 0) throw int(-1);

	const uint8_t *begin = (uint8_t *)this->mmap().first;
	const uint8_t *end = begin + this->mmap().second;

	if (initPointers(begin, end) != end) throw int(-1);
}

template <class ValueT1, class ValueT2>
Container<Pair<ValueT1, ValueT2> >::Container(
		const void *begin, const void *end)
{
	if (initPointers((uint8_t *)begin, (uint8_t *)end)
			!= (uint8_t *)end && end) throw int(-1);
}

template <class ValueT1, class ValueT2>
const uint8_t * Container<Pair<ValueT1, ValueT2> >::initPointers(
		const uint8_t *begin, const uint8_t *end)
{
	begin = m_values1.initPointers(begin, end); if (!begin) return 0;
	begin = m_values2.initPointers(begin, end); if (!begin) return 0;

	return m_values1.size() == m_values2.size() ? begin : 0;
}

template <class ValueT>
template <class OutIteratorT, class IteratorT>
OutIteratorT Container<ValueT>::build(
		OutIteratorT out, IteratorT begin, IteratorT end, void *skipLast)
{
	if (skipLast) // this is the last block of container
	{
		if (skipLast != (void *)(-1))
			std::copy(begin, end, (ValueT *)skipLast);

		return out;
	}

	const size_t align = 16;

	size_t numValues = distance(begin, end);
	size_t bytes = sizeof(ValueT) * numValues;
	bytes = (bytes + align - 1) / align * align;

	std::string result(align + bytes, 0);

	((size_t *)&result[0])[0] = numValues;
	std::copy(begin, end, (ValueT *)(&result[align]));

	out = std::copy(&*result.begin(), &*result.end(), out);

	return out;
}

template <class ValueT>
template <class OutIteratorT>
OutIteratorT Container<ValueT>::build(
		OutIteratorT out, const ValueT *begin, const ValueT *end, void *skipLast)
{
	if (skipLast) // this is the last block of container
	{
		if (skipLast != (void *)(-1))
			std::copy(begin, end, (ValueT *)skipLast);

		return out;
	}

	const size_t align = 16;

	size_t numValues = distance(begin, end);
	size_t bytes = sizeof(ValueT) * numValues;
	bytes = (bytes + align - 1) / align * align;

	char zeros[align] = { 0 };

	out = std::copy((char *)&numValues, (char *)(&numValues + 1), out);
	out = std::copy(zeros, zeros + align - sizeof(numValues), out);
	out = std::copy((char *)begin, (char *)end, out);
	out = std::copy(zeros, zeros + bytes - sizeof(ValueT) * numValues, out);

	return out;
}

template <class ValueT, class SizeT>
template <class OutIteratorT, class IteratorT>
OutIteratorT Container<Vector<ValueT, SizeT> >::build(
		OutIteratorT out, IteratorT begin, IteratorT end, void *skipLast)
{
	typedef typename std::iterator_traits<IteratorT>
			::value_type::iterator SubIterator;
	typedef typename std::iterator_traits<SubIterator>
			::value_type ValueType;

	std::vector<SizeT> entries(1, 0);
	entries.reserve(distance(begin, end) + 1);

	for (IteratorT it = begin; it != end; ++ it)
		entries.push_back(entries.back() + it->size());

	std::vector<ValueType> values;
	values.reserve(entries.back());

	for (IteratorT it = begin; it != end; ++ it)
	{
		values.insert(values.end(), it->begin(), it->end());

		it->clear();
	}

	Container<SizeT>::build(out, &*entries.begin(), &*entries.end()); entries.clear();
	Container<ValueT>::build(out, values.begin(), values.end(), skipLast); values.clear();

	return out;
}

template <class ValueT, int option, class CharT, class SizeT>
template <class OutIteratorT, class IteratorT>
OutIteratorT Container<Trie<ValueT, option, CharT, SizeT> >::build(
		OutIteratorT out, IteratorT begin, IteratorT end, void *skipLast)
{
	typedef typename std::iterator_traits<IteratorT>
			::value_type::iterator SubIterator;
	typedef typename std::iterator_traits<SubIterator>
			::value_type::second_type ValueType;

	std::vector<Node>                nodes;
	std::vector<SizeT>               paths;
	std::vector<std::vector<CharT> > tails;
	std::vector<ValueType>           values;

	size_t numTries  = distance(begin, end);
	size_t numValues = 0;

	for (IteratorT it = begin; it != end; ++ it)
		numValues += it->size();

	nodes.reserve(numTries + 2 + FT_MARGIN);
	paths.reserve(numValues);
	tails.reserve(numValues);
	values.reserve(numValues);

	nodes.resize(numTries + 2,             Node( 0, 1));
	nodes.resize(numTries + 2 + FT_MARGIN, Node(-1, 1));
	nodes[0].children          =   numTries + 2;
	nodes[numTries + 2].parent = - numTries - 2;

	size_t entry = 1;

	for (IteratorT it = begin; it != end; ++ it, ++ entry)
	{
		nodes[entry].parent = values.size() | FT_MASK;

		OpenNode<SubIterator> open;

		open.begin = it->begin();
		open.end   = it->end();
		open.level = 0;
		open.count = 0;
		open.character = 0;
		open.entry = entry;

		std::list<OpenNode<SubIterator> > openNodes;

		if (!it->empty()) openNodes.push_back(open);

		while (!openNodes.empty())
		{
			OpenNode<SubIterator> head = openNodes.back();
			openNodes.pop_back();

			if (option & FT_TAIL)
			{
				if (head.count == 1 && head.begin->first.size() > head.level)
				{
					nodes[head.entry].children = values.size() | FT_MASK;
					paths.push_back(head.entry);
					tails.push_back(std::vector<CharT>(
							head.begin->first.begin() + head.level,
							head.begin->first.end()));
					values.push_back(head.begin->second);

					it->erase(head.begin);

					continue;
				}
			}

			int32_t minCharacter = (1U << 31) - 1; // max int32_t

			std::list<OpenNode<SubIterator> > subOpenNodes;

			for (SubIterator itSub = head.begin; itSub != head.end; ++ itSub)
			{
				if (subOpenNodes.empty()
						|| itSub->first.size() <= head.level
						|| subOpenNodes.front().begin->first.size() <= head.level
						|| itSub->first[head.level]
						!= subOpenNodes.front().begin->first[head.level])
				{
					if (!subOpenNodes.empty())
						subOpenNodes.front().end = itSub;

					open.begin = itSub;
					open.end   = itSub;
					open.level = head.level + 1;
					open.count = 0;
					open.character = (itSub->first.size() <= head.level)
							? CHAR_TERMINATOR : (int32_t)(CharT)itSub->first[head.level];
					open.entry = 0;

					subOpenNodes.push_front(open);

					if (open.character < minCharacter)
						minCharacter = open.character;
				}

				subOpenNodes.front().count ++;
			}
			if (!subOpenNodes.empty())
				subOpenNodes.front().end = head.end;

			SizeT children = nodes[0].children;
			while (1 + minCharacter > (int32_t)children)
				children += nodes[children].children;
			children -= minCharacter;

			typename std::list<OpenNode<SubIterator> >::iterator itEnt;

			for (itEnt = subOpenNodes.begin(); itEnt != subOpenNodes.end(); )
			{
				if (!(nodes[children + itEnt->character].parent & FT_MASK))
				{
					children += nodes[children + minCharacter].children;
					itEnt = subOpenNodes.begin();
				}
				else ++ itEnt;
			}

			if (nodes.size() < children + FT_MARGIN * 2)
				nodes.resize(children + FT_MARGIN * 2, Node(-1, 1));

			nodes[head.entry].children = children;

			for (itEnt = subOpenNodes.begin(); itEnt != subOpenNodes.end(); )
			{
				itEnt->entry = children + itEnt->character;

				nodes[itEnt->entry + nodes[itEnt->entry].parent].children =
						nodes[itEnt->entry].children - nodes[itEnt->entry].parent;
				nodes[itEnt->entry + nodes[itEnt->entry].children].parent =
						nodes[itEnt->entry].parent - nodes[itEnt->entry].children;
				nodes[itEnt->entry].parent = head.entry;

				if (itEnt->character == CHAR_TERMINATOR)
				{
					nodes[itEnt->entry].children = values.size();
					paths.push_back(head.entry);
					tails.push_back(std::vector<CharT>());
					values.push_back(itEnt->begin->second);

					it->erase(itEnt->begin);

					itEnt = subOpenNodes.erase(itEnt);
				}
				else ++ itEnt;
			}

			if (option & FT_QUICKBUILD)
			{
				while (nodes[0].children + FT_MARGIN * 8 < nodes.size())
					nodes[0].children += nodes[nodes[0].children].children;
				nodes[nodes[0].children].parent = - nodes[0].children;
			}

			openNodes.splice(openNodes.end(), subOpenNodes);
		}
	}

	while (nodes[nodes.size() - FT_MARGIN].parent & FT_MASK)
		nodes.resize(nodes.size() - 1);

	nodes[0           ].children = numTries;
	nodes[1 + numTries].parent   = values.size() | FT_MASK;

	for (size_t i = numTries + 2; i != nodes.size(); i ++)
		if (nodes[i].parent & FT_MASK)
			nodes[i].parent = nodes[i].children = 0;

	if (!(option & FT_PATH)) paths.clear();
	if (!(option & FT_TAIL)) tails.clear();

	Container<Node> ::build(out, &*nodes.begin(), &*nodes.end()); nodes.clear();
	Container<SizeT>::build(out, &*paths.begin(), &*paths.end()); paths.clear();
	Container<Vector<CharT, SizeT> >::build(out, tails.begin(), tails.end()); tails.clear();
	Container<ValueT>::build(out, values.begin(), values.end(), skipLast); values.clear();

	return out;
}

template <class KeyT, class ValueT, class HashT, class SizeT>
template <class OutIteratorT, class IteratorT>
OutIteratorT Container<HashMap<KeyT, ValueT, HashT, SizeT> >::build(
		OutIteratorT out, IteratorT begin, IteratorT end, void *skipLast)
{
	typedef typename std::iterator_traits<IteratorT>
			::value_type::iterator SubIterator;
	typedef typename NonConst<typename std::iterator_traits<SubIterator>
			::value_type::first_type>::type KeyType;
	typedef typename std::iterator_traits<SubIterator>
			::value_type::second_type ValueType;
	typedef std::pair<KeyType, ValueType> ItemType;

	std::vector<std::vector<std::vector<ItemType> > > items;

	items.reserve(distance(begin, end));
	for (IteratorT it = begin; it != end; ++ it)
	{
		size_t bucket_size = it->size() ? (it->size() | 1) : 0;

		std::vector<SizeT> counts(bucket_size);
		for (SubIterator itSub = it->begin(); itSub != it->end(); ++ itSub)
			counts[HashT()(itSub->first) % bucket_size] ++;

		items.push_back(std::vector<std::vector<ItemType> >());
		items.back().reserve(bucket_size);
		for (size_t i = 0; i != bucket_size; i ++)
		{
			items.back().push_back(std::vector<ItemType>());
			items.back().back().reserve(counts[i]);
		}

		for (SubIterator itSub = it->begin(); itSub != it->end(); ++ itSub)
			items.back()[HashT()(itSub->first) % bucket_size].push_back(
					ItemType(itSub->first, itSub->second));
	}

	Container<Vector<Vector<Pair<KeyT, ValueT>, SizeT>, SizeT> >::build(
			out, items.begin(), items.end(), skipLast); items.clear();

	return out;
}

template <class ValueT1, class ValueT2>
template <class OutIteratorT, class IteratorT>
OutIteratorT Container<Pair<ValueT1, ValueT2> >::build(
		OutIteratorT out, IteratorT begin, IteratorT end, void *skipLast)
{
	typedef typename std::iterator_traits<IteratorT>
			::value_type::first_type  ValueType1;
	typedef typename std::iterator_traits<IteratorT>
			::value_type::second_type ValueType2;

	std::vector<ValueType1> values1;
	std::vector<ValueType2> values2;

	values1.reserve(distance(begin, end));
	for (IteratorT it = begin; it != end; ++ it)
		values1.push_back(it->first);

	Container<ValueT1>::build(out, values1.begin(), values1.end()); values1.clear();

	values2.reserve(distance(begin, end));
	for (IteratorT it = begin; it != end; ++ it)
		values2.push_back(it->second);

	Container<ValueT2>::build(out, values2.begin(), values2.end(), skipLast); values2.clear();

	return out;
}

template <class ValueT, class SizeT>
const Container<Vector<ValueT, SizeT> >
Vector<ValueT, SizeT>::defaultContainer()
{
	static std::string data;

	if (data.empty())
	{
		typename Container<Vector<ValueT, SizeT> >::std_value_type null;
		Container<Vector<ValueT, SizeT> >::build(
				back_inserter(data), &null, &null + 1);
	}

	return Container<Vector<ValueT, SizeT> >((void *)data.c_str());
}

template <class ValueT, int option, class CharT, class SizeT>
const Container<Trie<ValueT, option, CharT, SizeT> >
Trie<ValueT, option, CharT, SizeT>::defaultContainer()
{
	static std::string data;

	if (data.empty())
	{
		typename Container<Trie<ValueT, option, CharT, SizeT> >::std_value_type null;
		Container<Trie<ValueT, option, CharT, SizeT> >::build(
				back_inserter(data), &null, &null + 1);
	}

	return Container<Trie<ValueT, option, CharT, SizeT> >((void *)data.c_str());
}

template <class KeyT, class ValueT, class HashT, class SizeT>
const Container<HashMap<KeyT, ValueT, HashT, SizeT> >
HashMap<KeyT, ValueT, HashT, SizeT>::defaultContainer()
{
	static std::string data;

	if (data.empty())
	{
		typename Container<HashMap<KeyT, ValueT, HashT, SizeT> >::std_value_type null;
		Container<HashMap<KeyT, ValueT, HashT, SizeT> >::build(
				back_inserter(data), &null, &null + 1);
	}

	return Container<HashMap<KeyT, ValueT, HashT, SizeT> >((void *)data.c_str());
}

template <class ValueT, class SizeT>
const SizeT Container<Vector<ValueT, SizeT> >::m_defaultEntries[1] = { 0 };

template <class ValueT, int option, class CharT, class SizeT>
const typename Container<Trie<ValueT, option, CharT, SizeT> >::Node
		Container<Trie<ValueT, option, CharT, SizeT> >::m_defaultNodes[1];

template <class ValueT, class SizeT>
const Container<Vector<ValueT, SizeT> >
Vector<ValueT, SizeT>::m_defaultContainer =
		Vector<ValueT, SizeT>::defaultContainer();

template <class ValueT, int option, class CharT, class SizeT>
const Container<Trie<ValueT, option, CharT, SizeT> >
Trie<ValueT, option, CharT, SizeT>::m_defaultContainer =
		Trie<ValueT, option, CharT, SizeT>::defaultContainer();

template <class KeyT, class ValueT, class HashT, class SizeT>
const Container<HashMap<KeyT, ValueT, HashT, SizeT> >
HashMap<KeyT, ValueT, HashT, SizeT>::m_defaultContainer =
		HashMap<KeyT, ValueT, HashT, SizeT>::defaultContainer();

template <class ValueT, int option, class CharT, class SizeT>
const ValueT Trie<ValueT, option, CharT, SizeT>::m_zero = ValueT();

template <class KeyT, class ValueT, class HashT, class SizeT>
const ValueT HashMap<KeyT, ValueT, HashT, SizeT>::m_zero = ValueT();

} // namespace ft2

#endif // __FAST_TRIE_H__
