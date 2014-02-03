// FastTrie 2.3.2 2012-01-04

#ifndef __MMAP_H__
#define __MMAP_H__

#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <pthread.h>

#include <string>
#include <map>

namespace ft2 {

template <class T>
class MMap
{
public:
	MMap(const char *filename = "");
	MMap(const MMap &from);
	MMap & operator =(const MMap &from);
	~MMap();

	std::pair<const void *, size_t> mmap() const;

private:
	struct Usage
	{
		Usage() : fd(-1), address(0), length(0), used(0) {}

		int fd;
		const void *address;
		size_t length;
		size_t used;
	};

	std::string m_filename;

	static pthread_mutex_t m_mutex;
	static std::map<std::string, Usage> *m_mmaps;
};

template <class T>
MMap<T>::MMap(const char *filename) : m_filename(filename)
{
	if (m_filename.empty()) return;

	Usage usage;

	pthread_mutex_lock(&m_mutex);
	if (m_mmaps == 0) m_mmaps = new std::map<std::string, Usage>();
	typename std::map<std::string, Usage>::iterator it = m_mmaps->find(m_filename);
	if (it == m_mmaps->end())
	{
		usage.fd = open(m_filename.c_str(), O_RDONLY);
		if (usage.fd < 0) goto error;
		struct stat st;
		if (fstat(usage.fd, &st) < 0 || st.st_size == 0) goto error;
		usage.address = ::mmap(0, st.st_size, PROT_READ, MAP_SHARED, usage.fd, 0);
		if (usage.address == MAP_FAILED) goto error;
		usage.length = st.st_size;
		it = m_mmaps->insert(std::make_pair(m_filename, usage)).first;
	}
	it->second.used ++;
	pthread_mutex_unlock(&m_mutex);
	return;

error:
	if (usage.fd >= 0) close(usage.fd);
	if (m_mmaps && m_mmaps->empty()) { delete m_mmaps; m_mmaps = 0; }
	pthread_mutex_unlock(&m_mutex);
	throw int(-1);
}

template <class T>
MMap<T>::MMap(const MMap<T> &from) : m_filename(from.m_filename)
{
	if (m_filename.empty()) return;

	pthread_mutex_lock(&m_mutex);
	typename std::map<std::string, Usage>::iterator it = m_mmaps->find(m_filename);
	it->second.used ++;
	pthread_mutex_unlock(&m_mutex);
}

template <class T>
MMap<T> & MMap<T>::operator =(const MMap<T> &from)
{
	if (!m_filename.empty())
	{
		pthread_mutex_lock(&m_mutex);
		typename std::map<std::string, Usage>::iterator it = m_mmaps->find(m_filename);
		it->second.used --;
		if (it->second.used == 0)
		{
			munmap((void *)it->second.address, it->second.length);
			close(it->second.fd);
			m_mmaps->erase(it);
		}
		if (m_mmaps && m_mmaps->empty()) { delete m_mmaps; m_mmaps = 0; }
		pthread_mutex_unlock(&m_mutex);
	}

	m_filename = from.m_filename;

	if (!m_filename.empty())
	{
		pthread_mutex_lock(&m_mutex);
		typename std::map<std::string, Usage>::iterator it = m_mmaps->find(m_filename);
		it->second.used ++;
		pthread_mutex_unlock(&m_mutex);
	}

	return *this;
}

template <class T>
MMap<T>::~MMap()
{
	if (m_filename.empty()) return;

	pthread_mutex_lock(&m_mutex);
	typename std::map<std::string, Usage>::iterator it = m_mmaps->find(m_filename);
	it->second.used --;
	if (it->second.used == 0)
	{
		munmap((void *)it->second.address, it->second.length);
		close(it->second.fd);
		m_mmaps->erase(it);
	}
	if (m_mmaps && m_mmaps->empty()) { delete m_mmaps; m_mmaps = 0; }
	pthread_mutex_unlock(&m_mutex);
}

template <class T>
std::pair<const void *, size_t> MMap<T>::mmap() const
{
	if (m_filename.empty()) return std::pair<const void *, size_t>();

	pthread_mutex_lock(&m_mutex);
	typename std::map<std::string, Usage>::iterator it = m_mmaps->find(m_filename);
	std::pair<const void *, size_t> result(it->second.address, it->second.length);
	pthread_mutex_unlock(&m_mutex);

	return result;
}

template <class T>
pthread_mutex_t MMap<T>::m_mutex = PTHREAD_MUTEX_INITIALIZER;
template <class T>
std::map<std::string, typename MMap<T>::Usage> *MMap<T>::m_mmaps = 0;

} // namespace ft2

#endif // __MMAP_H__
