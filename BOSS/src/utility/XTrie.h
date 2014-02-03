///////////////////////////////////////////
//  * File:    XTrie.h
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#ifndef XTRIE_H_
#define XTRIE_H_

#include <string>
#include <vector>
#include <map>

namespace b2bmlr{
class Dict;
}

namespace b2bmlr {
class XMMap;

class XTrie{
public:
	XTrie();
	~XTrie();
	bool Load(const char *pDictName);
	bool Save(const char *pDictName);
	void Release();
	
	void Add(const char *pKey, const void *pValue, int nLen);
	bool Lookup(const char *pKey, void* &pValue, int &nLen);
	void Add(const void *pKey, int nKeyLen, const void *pValue, int nLen);
	bool Lookup(const void *pKey, int nKeyLen, void* &pValue, int &nLen);

	int LookupAll(const void *pKey, int nMaxKeyLen, int *pKeyLens,
			void **pValues, int *pValueLens, int nMaxMatches);
	
	static void Exchange(XTrie &trie1, XTrie &trie2);
	
private:
	b2bmlr::Dict *m_pTrie;
	XMMap *m_pMMap, *m_pMMapIndex;
	char *m_pData;
	int m_nDateSize;
	int m_nBufSize;
	
	int m_nBlockSize;
};

}

#endif
