///////////////////////////////////////////
//  * File:    XTrie.cpp
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#include "XTrie.h"
#include "XMMap.h"
#include <sstream>
#include <iostream>
#include "mlr_trie.hh"

using namespace std;

namespace b2bmlr {

XTrie::XTrie()
{
	m_pTrie = new b2bmlr::DoubleTrie();
	m_pMMapIndex = NULL;
	m_pMMap = NULL;
	m_nBlockSize = 65536;
	m_nDateSize = 0;
	m_nBufSize = 0;
	m_pData = NULL;
}

XTrie::~XTrie()
{
	Release();
}

void XTrie::Release()
{
	if(m_pTrie)
		delete m_pTrie;
	m_pTrie = NULL;
	if(m_pMMapIndex)
	{
		delete m_pMMapIndex;
	}
	if(m_pMMap)
	{
		delete m_pMMap;
	}
	else if(m_pData)
	{
		delete m_pData;
		m_pData = NULL;
	}
	m_pMMapIndex = NULL;
	m_pMMap = NULL;
}

bool XTrie::Load(const char *pDictName)
{
	string strName;

	strName = pDictName;
	strName += ".index";
/*
	if(!m_pTrie->load(strName.c_str()))
		return false;
*/
	m_pMMapIndex = new XMMap();
	if (!m_pMMapIndex->Open(strName.c_str()))
		return false;
	if (!m_pTrie->load((char *)m_pMMapIndex->Data(), m_pMMapIndex->Length()))
		return false;

	strName = pDictName;
	strName += ".value";
	m_pMMap = new XMMap();
	if (!m_pMMap->Open(strName.c_str()))
		return false;
	m_pData = m_pMMap->Data();
	m_nDateSize = m_pMMap->Length();
	m_nBufSize = m_pMMap->Length();
	return true;
}

bool XTrie::Save(const char *pDictName)
{
	string strName;
	
	strName = pDictName;
	strName += ".index";
	m_pTrie->write(strName.c_str());
	
	strName = pDictName;
	strName += ".value";
	FILE *fp;
	fp = fopen(strName.c_str(),"w");
	if(!fp)
		return false;
	int nWrite = fwrite(m_pData, 1, m_nDateSize, fp);
	if(nWrite == 0)
		return false;
	fclose(fp);
	return true;
}

void XTrie::Add(const char *pKey, const void *pValue, int nLen)
{
	Add(pKey, strlen(pKey), pValue, nLen);
}

bool XTrie::Lookup(const char *pKey, void* &pValue, int &nLen)
{
	return Lookup(pKey, strlen(pKey), pValue, nLen);
}

void XTrie::Add(const void *pKey, int nKeyLen, const void *pValue, int nLen)
{
	if(m_pMMap)
	{
		char *p = new char[m_nDateSize];
		memcpy(p, m_pData, m_nDateSize);
		delete m_pMMap;
		m_pMMap = NULL;
		m_pData = p;
		m_nBufSize = m_nDateSize;
	}
	int nTotalLen = nLen + 4;
	if(!m_pData)
	{
		m_pData = new char[m_nBlockSize];
		m_nBufSize = m_nBlockSize;
	}
	else if(m_nDateSize + nTotalLen > m_nBufSize)
	{
		char *p = new char[m_nBufSize + m_nBlockSize];
		memcpy(p, m_pData, m_nDateSize);
		delete [] m_pData;
		m_pData = p;
		m_nBufSize += m_nBlockSize;
	}
	memcpy(m_pData + m_nDateSize, &nLen, 4);
	if(nLen > 0)
		memcpy(m_pData + m_nDateSize + 4, pValue, nLen);
	m_pTrie->add((b2bmlr::DictKeyT*)pKey, nKeyLen, m_nDateSize + 1);
	m_nDateSize += nTotalLen;
}

bool XTrie::Lookup(const void *pKey, int nKeyLen, void* &pValue, int &nLen)
{
	int nOffset = m_pTrie->match((b2bmlr::DictKeyT*)pKey, nKeyLen);
	if(nOffset == 0)
		return false;
	nLen = *((int *)(m_pData + nOffset - 1));
	pValue = m_pData + nOffset + 3;
	return true;
}

int XTrie::LookupAll(const void *pKey, int nMaxKeyLen, int *pKeyLens,
		void **pValues, int *pValueLens, int nMaxMatches)
{
	b2bmlr::Dict::MatchInfo matchInfos[nMaxMatches];
	int found = m_pTrie->matchAll((b2bmlr::DictKeyT *)pKey,
			nMaxKeyLen, matchInfos, nMaxMatches);
	for (int i = 0; i < found; i ++)
	{
		pKeyLens[i] = matchInfos[i].pos + 1;
		pValues[i] = m_pData + matchInfos[i].state + 3;
		pValueLens[i] = *((int *)(m_pData + matchInfos[i].state - 1));
	}
	
	return found;
}

void XTrie::Exchange(XTrie &trie1, XTrie &trie2)
{	
        b2bmlr::Dict *pTrie = trie1.m_pTrie;
	trie1.m_pTrie = trie2.m_pTrie;
	trie2.m_pTrie = pTrie;
	
	XMMap *pMMap = trie1.m_pMMap;
	trie1.m_pMMap = trie2.m_pMMap;
	trie2.m_pMMap = pMMap;
	
	char *pData = trie1.m_pData;
	trie1.m_pData = trie2.m_pData;
	trie2.m_pData = pData;
	
	int nDateSize = trie1.m_nDateSize;
	trie1.m_nDateSize = trie2.m_nDateSize;
	trie2.m_nDateSize = nDateSize;
	
	int nBufSize = trie1.m_nBufSize;
	trie1.m_nBufSize = trie2.m_nBufSize;
	trie2.m_nBufSize = nBufSize;
}

}

