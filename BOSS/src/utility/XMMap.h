///////////////////////////////////////////
//  * File:    XMMap.cpp
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#ifndef _XMMAP_H
#define _XMMAP_H

#include <string>
#include "stdlib.h"
#include "string.h"
#include "stdint.h"

namespace b2bmlr
{

class XMMap
{
public:			
	XMMap(const char* _path);
	XMMap()	: m_pData(NULL), m_nPos(0), m_nLength(0), m_hFile(0) {}		

	virtual ~XMMap(void);
public:			
	bool Open(const char* _path);
	void Close();
	char* Data(){return m_pData;}
	int64_t Length(){return m_nLength;}
protected:			
	std::string m_filename;
	char* m_pData;
	int64_t m_nPos;
	int64_t m_nLength;
	int m_hFile;
};


}

#endif
