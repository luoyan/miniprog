///////////////////////////////////////////
//  * File:    XMMap.cpp
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#include "XMMap.h"

#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

namespace b2bmlr
{


XMMap::XMMap(const char* _path)
{			
	m_pData = NULL;
	m_nPos = 0;
	m_nLength = 0;
	m_hFile = 0;
	Open(_path);
}

XMMap::~XMMap(void)
{
	Close();
}

bool XMMap::Open(const char* _path)
{
	Close();

	m_hFile = open(_path, O_RDONLY);

	if (m_hFile < 0)
		return false;
	else 
	{
		struct stat st;
		if(fstat(m_hFile, &st) == 0)
			m_nLength =  st.st_size;

		void* address = mmap(0, m_nLength, PROT_READ, MAP_SHARED, m_hFile, 0);
		if (address == MAP_FAILED)
			return false;
		else
			m_pData = (char*)address;
	}
	m_filename = _path;
	return true;
}

void XMMap::Close()
{
	if ( m_pData != NULL )
		munmap(m_pData, m_nLength);
	if ( m_hFile > 0 )
		close(m_hFile);
	m_hFile = 0;
	m_pData = NULL;
	m_nPos = 0;
}


		
}

