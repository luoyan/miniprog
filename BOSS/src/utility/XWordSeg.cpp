///////////////////////////////////////////
//  * File:    XWordSeg.cpp
//  * Author:  XingFei
//  * Contact: fei.xing@alibaba-inc.com
///////////////////////////////////////////


#include "XWordSeg.h"
#include "strnormalize.h"
#include "XConfig.h"

#include <iostream>
#include <fstream>
#include <pthread.h>
#include "type.h"

using namespace std;
using namespace ws;
using namespace kee;

static pthread_mutex_t mutex_yws = PTHREAD_MUTEX_INITIALIZER;

namespace b2bmlr {

#define YWS_MAX_WORD_LENGTH 128

#define KB_PP 1
#define KB_LX 2
#define KB_XSC 3
#define KB_BL 0.08

XWordSeg::XWordSeg() : m_pYWS(NULL)
{
    m_cdGbkToUtf8 = iconv_open("UTF8", "GBK");
    m_cdUtf8ToGbk = iconv_open("GBK", "UTF8");
}

XWordSeg::~XWordSeg()
{
	iconv_close(m_cdGbkToUtf8);
	iconv_close(m_cdUtf8ToGbk);
        if (m_pYWS != NULL)
	    	delete m_pYWS;
	if (m_pKEE != NULL)
		delete m_pKEE;
}

bool XWordSeg::Init(const char *ywsConfFilePath, ws::WsSegmenter *ws)
{
        XConfig conf;
        string strPath;
        if(!conf.Load(ywsConfFilePath))
        {
            cerr << "Error: cannot open configure file" << endl;
            return false;
        }
        if (ws == NULL)
        {
	    m_pYWS = new WsSegmenter();
	    if (!m_pYWS->init(ywsConfFilePath))
            {
                    cerr << "Error: offline init bws!"<< endl;
	            return false;
            }
        }
        if(!conf.GetItem("Files", "KEE_CONFIGURE_FILE", strPath))
        {
            cerr << "Error: cannot open kee configure file" << endl;
            return false;
        }
        m_pKEE = new KeyEntityExtractor(strPath.c_str());
        if(NULL == m_pKEE)
        {
            cerr << "Error: init KEE " << strPath << endl;
            return false;
        }
	if(!conf.GetItem("Files", "BWS_WORDTAG_FILE", strPath))
        {       
                cerr << "Error: cannot open bwswordtag configure file" << endl;
		return false;
        }
	if (!LoadBwsWordtags(strPath, ws))
	{
		cerr << "Error: load bws wordtags " << strPath << endl;
		return false;
	}

	return true;
}

int XWordSeg::ChooseSX(vector<ws::WordType> &wordtp, int nClass)
{
        int typeID = 0;
        int tag = 0;
        float weight = 0.0;
        for(vector<ws::WordType>::iterator i = wordtp.begin(); i != wordtp.end(); ++ i)
        {
                weight += (*i).weight;
        }
        if(nClass != 2)
        {

            for(vector<ws::WordType>::iterator j = wordtp.begin(); j != wordtp.end(); ++ j )
            {     
                    typeID = (*j).typeID;
                    if(typeID == CP_PP)
                    {
                        tag = KB_PP;
                        if ((*j).weight/weight < KB_BL) tag = 0;
                        break;
                    }
                    else if (typeID == CPLX || typeID == CPLX_JD || typeID == CPLX_FH || typeID == CPLX_TC)
                    {
                        tag = KB_LX;
                        if ((*j).weight/weight < KB_BL) tag = 0;
                        break;
                    }
                
            }
        }
        else
        {
            for(vector<ws::WordType>::iterator k = wordtp.begin();k != wordtp.end(); ++ k)
            {
                    typeID = (*k).typeID;
                    if(typeID == CPLX_XSC)
                    {
                        tag = KB_XSC;
//                        if ((*k).weight/weight < KB_BL) tag = 0;
                        break;
                    }
            }
        }

        return tag;
}

int XWordSeg::FindIndex(std::vector<vector<ws::WORD_T> > &vec2Words, int nKeyWordIndex)
{
        int max = 0;
        int min = vec2Words.size() - 1;
        int subkeynum = 0;
        int keyindex = 0;
        int wordid = 0;
        
        subkeynum = vec2Words[max][nKeyWordIndex].subWordNum;
        if(subkeynum > 1)
        {
            keyindex = vec2Words[max][nKeyWordIndex].subWordIndex + subkeynum -1;
            subkeynum = vec2Words[max + 1][keyindex].subWordNum;
            if(subkeynum > 1)
            {
                return vec2Words[max + 1][keyindex].subWordIndex + subkeynum -1;
            }
            else
            {
                wordid = vec2Words[max + 1][keyindex].wordId;
                for(size_t i = keyindex; i != vec2Words[min].size(); ++ i)
                {
                    if(vec2Words[min][i].wordId == wordid)
                        return i;
                }
            }
            
        }
        else
        {
            wordid = vec2Words[max][nKeyWordIndex].wordId;
            for(size_t j = nKeyWordIndex; j != vec2Words[min].size(); ++ j)
            {
                if(vec2Words[min][j].wordId == wordid)
                    return j;
            }
        }

        return -1;

}


bool XWordSeg::Segment(const char *pUTF8Text, int nTextLen, vector<string> &vecWords, vector<int> &vecTags, ws::WsSegmenter *ws)
{
	if (!ws) ws = m_pYWS;

	vecWords.resize(0);
        vecTags.resize(0);

	uint16_t utf16Buffer[YWS_MAX_WORD_LENGTH + 1];
	uint16_t utf16BufferSize = YWS_MAX_WORD_LENGTH;

	vector<ws::WORD_T> words;
	int size = ws->utf8ToUtf16(
			pUTF8Text, nTextLen, utf16Buffer, utf16BufferSize);
	pthread_mutex_lock(&mutex_yws);
	bool success = ws->segment(utf16Buffer, size, words);
	if (!success)
	{
		pthread_mutex_unlock(&mutex_yws);
		return false;
	}
	char szWordUtf8[YWS_MAX_WORD_LENGTH * 3 + 1];
	for (size_t i = 0; i < words.size(); i ++)
	{
		int size = ws->utf16ToUtf8(
				words[i].pBuf, words[i].length, szWordUtf8, YWS_MAX_WORD_LENGTH * 3);
		szWordUtf8[size] = 0;
		string tmp = szWordUtf8;
		if (m_setRemoveWords.find(tmp) == m_setRemoveWords.end())
                {
                        vecWords.push_back(tmp);
                        int tag = ChooseSX(words[i].wordTypes, 1);
                        vecTags.push_back(tag);
                }
	}
	pthread_mutex_unlock(&mutex_yws);
	return true;
}

// return a negative hash value as the word id
static int hash_string(const char *word)
{
	static const uint32_t primes[16] = {
		0x01EE5DB9, 0x491408C3, 0x0465FB69, 0x421F0141,
		0x2E7D036B, 0x2D41C7B9, 0x58C0EF0D, 0x7B15A53B,
		0x7C9D3761, 0x5ABB9B0B, 0x24109367, 0x5A5B741F,
		0x6B9F12E9, 0x71BA7809, 0x081F69CD, 0x4D9B740B,
	};
	uint32_t sum = 0;
	for (int i = 0; word[i]; i ++)
		sum += primes[i & 15] * (unsigned char)word[i];
	return sum | 0x80000000;
}

// return a negative hash value as the word id
static int hash_alphanum(const char *word)
{
	static const uint32_t primes[16] = {
		0x01EE5DB9, 0x491408C3, 0x0465FB69, 0x421F0141,
		0x2E7D036B, 0x2D41C7B9, 0x58C0EF0D, 0x7B15A53B,
		0x7C9D3761, 0x5ABB9B0B, 0x24109367, 0x5A5B741F,
		0x6B9F12E9, 0x71BA7809, 0x081F69CD, 0x4D9B740B,
	};
	uint32_t sum = 0;
	for (int i = 0; word[i]; i ++)
		sum += primes[i & 15] * (unsigned char)word[i];
	return sum & 0x7FFFFFFF | 0x40000000;
}

bool XWordSeg::SegmentRM(const char *pUTF8Text, int nTextLen, std::vector<int> &vecWordIDs , std::vector<int> &vecWordTags)
{
	vecWordIDs.resize(0);
        vecWordTags.resize(0);
	uint16_t utf16Buffer[YWS_MAX_WORD_LENGTH + 1];
	uint16_t utf16BufferSize = YWS_MAX_WORD_LENGTH;

	vector<ws::WORD_T> words;
	int size = m_pYWS->utf8ToUtf16(
			pUTF8Text, nTextLen, utf16Buffer, utf16BufferSize);
	pthread_mutex_lock(&mutex_yws);
	bool success = m_pYWS->segment(utf16Buffer, size, words);
        if (!success)
	{
		pthread_mutex_unlock(&mutex_yws);
		return false;
	}
	char szWordUtf8[YWS_MAX_WORD_LENGTH * 3 + 1];
	for (size_t i = 0; i < words.size(); i ++)
	{
		int word_id = words[i].wordId;        
		if (word_id < 0)
		{
			int size = m_pYWS->utf16ToUtf8(
					words[i].pBuf, words[i].length, szWordUtf8, YWS_MAX_WORD_LENGTH * 3);
			szWordUtf8[size] = 0;
			if (size == 0)
				word_id = 0;
			else
			{
				for (size = 0; isalnum(szWordUtf8[size]); size ++) ;
				if (szWordUtf8[size])
					word_id = hash_string(szWordUtf8);
				else
					word_id = hash_alphanum(szWordUtf8);
			}
		}
		if (word_id
				&& m_setRemoveWordIDs.find(word_id) == m_setRemoveWordIDs.end())
                {
			vecWordIDs.push_back(word_id);
                        int tag = ChooseSX(words[i].wordTypes, 1);
                        vecWordTags.push_back(tag);

                }
	}
	pthread_mutex_unlock(&mutex_yws);

	return true;

        
}
bool XWordSeg::SegmentRM(const char *pUTF8Text, int nTextLen, std::vector<int> &vecWordIDs , std::vector<int> &vecWordTags, WsSegmenter *ws)
{
	vecWordIDs.resize(0);
        vecWordTags.resize(0);
	uint16_t utf16Buffer[YWS_MAX_WORD_LENGTH + 1];
	uint16_t utf16BufferSize = YWS_MAX_WORD_LENGTH;

        if (ws == NULL)
        {
            cerr<<"bws pointer is NULL!"<<endl;
            return false;
        }

	vector<ws::WORD_T> words;
	int size = ws->utf8ToUtf16(
			pUTF8Text, nTextLen, utf16Buffer, utf16BufferSize);
	bool success = ws->segment(utf16Buffer, size, words);
        if (!success)
	{
		return false;
	}
	char szWordUtf8[YWS_MAX_WORD_LENGTH * 3 + 1];
	for (size_t i = 0; i < words.size(); i ++)
	{
		int word_id = words[i].wordId;        
		if (word_id < 0)
		{
			int size = ws->utf16ToUtf8(
					words[i].pBuf, words[i].length, szWordUtf8, YWS_MAX_WORD_LENGTH * 3);
			szWordUtf8[size] = 0;
			if (size == 0)
				word_id = 0;
			else
			{
				for (size = 0; isalnum(szWordUtf8[size]); size ++) ;
				if (szWordUtf8[size])
					word_id = hash_string(szWordUtf8);
				else
					word_id = hash_alphanum(szWordUtf8);
			}
		}
		if (word_id
				&& m_setRemoveWordIDs.find(word_id) == m_setRemoveWordIDs.end())
                {
			vecWordIDs.push_back(word_id);
                        int tag = ChooseSX(words[i].wordTypes, 1);
                        vecWordTags.push_back(tag);

                }
	}

	return true;

        
}


bool XWordSeg::Segment(const char *pUTF8Text, int nTextLen, vector<int> &vecWordIDs, vector<int> &vecTags)
{
	vecWordIDs.resize(0);
        vecTags.resize(0);

	uint16_t utf16Buffer[YWS_MAX_WORD_LENGTH + 1];
	uint16_t utf16BufferSize = YWS_MAX_WORD_LENGTH;

	int size = m_pYWS->utf8ToUtf16(
			pUTF8Text, nTextLen, utf16Buffer, utf16BufferSize);
	pthread_mutex_lock(&mutex_yws);

        vector<vector<ws::WORD_T> > vecwords;
	bool success = m_pYWS->segment(utf16Buffer, size, vecwords);
        if (!success)
	{
		pthread_mutex_unlock(&mutex_yws);
		return false;
	}
        int max = 0;
        int min = vecwords.size() - 1;
        vector<int16_t> keindexs;

        success = m_pKEE->extract(vecwords[max], m_pYWS,keindexs);
        if (!success)
	{
		pthread_mutex_unlock(&mutex_yws);
		return false;
	}

        int iRemove = 0;
        int iNumLX = 0;
        char szWordUtf8[YWS_MAX_WORD_LENGTH * 3];
        for(size_t i = 0; i != vecwords[min].size(); ++ i)
        {
                vecwords[min][i].tagId = 0;
		int word_id = vecwords[min][i].wordId;
                if (word_id < 0)
		{
			int size = m_pYWS->utf16ToUtf8(
					vecwords[min][i].pBuf, vecwords[min][i].length, szWordUtf8, YWS_MAX_WORD_LENGTH * 3);
			szWordUtf8[size] = 0;
			if (size == 0)
				word_id = 0;
			else
			{
				for (size = 0; isalnum(szWordUtf8[size]); size ++) ;
				if (szWordUtf8[size])
					word_id = hash_string(szWordUtf8);
				else
					word_id = hash_alphanum(szWordUtf8);
			}
		}
		if (word_id)
                {
			vecWordIDs.push_back(word_id);
                        int tag = 0;
                        map<int, int>::const_iterator iter = m_mapBwsWordtags.find(word_id);
                        if (iter != m_mapBwsWordtags.end())
                                tag = iter->second;
                        else
                                tag = ChooseSX(vecwords[min][i].wordTypes, 1);
                        if (tag == KB_LX)
                                iNumLX ++;
                        vecTags.push_back(tag);
                        if(m_setRemoveWordIDs.find(word_id) != m_setRemoveWordIDs.end())
                                iRemove ++;
                }
        }
        
        string kuohao = ")";
        int kuohaoID = hash_string(kuohao.c_str());
        int allLen = vecwords[min].size();
        int allMaxLen = vecwords[max].size() - iRemove;
        if (kuohaoID == vecwords[min][allLen - 1].wordId)
            iRemove -= 2;

        if (!((iRemove < 1 && iNumLX > 2) && (allMaxLen > 3)))
        {
            for(size_t i=0; i < keindexs.size(); ++ i )
            {
                int key = keindexs[i];
                int keyindex = FindIndex(vecwords, key); 
                if (keyindex == -1) return false;
                vecwords[min][keyindex].tagId = 1;
                if (vecwords[max][key].subWordNum > 1)
	        {	
		    int tag = vecTags[keyindex];
		    if(tag == KB_LX)
                    {
                        vecTags[keyindex - 1] = ChooseSX(vecwords[min][keyindex - 1].wordTypes, 2);
                        vecwords[min][keyindex - 1].tagId = 2;
                    }
	        }
            }

            for(size_t j = 0; j != vecTags.size(); ++ j)
            {
                if(vecTags[j] == KB_LX && vecwords[min][j].tagId == 0 )
                    vecTags[j] = 0;
            }
        }
        vector<int>::iterator itWord,itTag;
        itTag = vecTags.begin();
        for(itWord = vecWordIDs.begin(); itWord != vecWordIDs.end(); ++ itWord)
        {
            if (m_setRemoveWordIDs.find(*itWord) != m_setRemoveWordIDs.end())
            {
                vecWordIDs.erase(itWord);
                itWord --;
                vecTags.erase(itTag);
                itTag --;
            }
            itTag ++;
        }
	pthread_mutex_unlock(&mutex_yws);

	return true;
}


bool XWordSeg::Segment(StringWS &strws)
{
	strws.m_vecWordIDs.reserve(30);
	strws.m_vecWordIDs.resize(0);
	strws.m_vecWordTags.reserve(30);
	strws.m_vecWordTags.resize(0);

	uint16_t utf16Buffer[YWS_MAX_WORD_LENGTH];
	uint16_t utf16BufferSize = YWS_MAX_WORD_LENGTH;
	vector<vector<ws::WORD_T> > vecwords;
	int size = m_pYWS->utf8ToUtf16(
			strws.m_str.c_str(), strws.m_str.length(), utf16Buffer, utf16BufferSize);
	pthread_mutex_lock(&mutex_yws);
	bool success = m_pYWS->segment(utf16Buffer, size, vecwords);
	if (!success)
	{
		pthread_mutex_unlock(&mutex_yws);
		return false;
	}
        int max = 0;
        int min = vecwords.size() - 1;
        vector<int16_t> keindexs;
        success = m_pKEE->extract(vecwords[max], m_pYWS, keindexs);
	if (!success)
	{
	    pthread_mutex_unlock(&mutex_yws);
	    return false;
	}
  
        int iRemove = 0;
        int iNumLX = 0;
        char szWordUtf8[YWS_MAX_WORD_LENGTH * 3];
        for(size_t i = 0; i != vecwords[min].size(); ++ i)
        {
                vecwords[min][i].tagId = 0;
		int word_id = vecwords[min][i].wordId;
                if (word_id < 0)
		{
			int size = m_pYWS->utf16ToUtf8(
					vecwords[min][i].pBuf, vecwords[min][i].length, szWordUtf8, YWS_MAX_WORD_LENGTH * 3);
			szWordUtf8[size] = 0;
			if (size == 0)
				word_id = 0;
			else
			{
				for (size = 0; isalnum(szWordUtf8[size]); size ++) ;
				if (szWordUtf8[size])
					word_id = hash_string(szWordUtf8);
				else
					word_id = hash_alphanum(szWordUtf8);
			}
		}
		if (word_id)
                {
			strws.m_vecWordIDs.push_back(word_id);
                        int tag = 0;
                        map<int, int>::const_iterator iter = m_mapBwsWordtags.find(word_id);
                        if (iter != m_mapBwsWordtags.end())
                                tag = iter->second;
                        else
                                tag = ChooseSX(vecwords[min][i].wordTypes, 1);
                        if (tag == KB_LX)
                                iNumLX ++;
                        strws.m_vecWordTags.push_back(tag);
                        if(m_setRemoveWordIDs.find(word_id) != m_setRemoveWordIDs.end())
                                iRemove ++;
                }
        }
        
        string kuohao = ")";
        int kuohaoID = hash_string(kuohao.c_str());
        int allLen = vecwords[min].size();
        int allMaxLen = vecwords[max].size() - iRemove;
        if (kuohaoID == vecwords[min][allLen - 1].wordId)
            iRemove -= 2;

        if (!((iRemove < 1 && iNumLX > 2) && (allMaxLen > 3)))
        {
            for(size_t i=0; i < keindexs.size(); ++ i )
            {
                int key = keindexs[i];
                int keyindex = FindIndex(vecwords, key); 
                if (keyindex == -1) return false;
                vecwords[min][keyindex].tagId = 1;
                if (vecwords[max][key].subWordNum > 1)
	        {	
		    int tag = strws.m_vecWordTags[keyindex];
		    if(tag == KB_LX)
                    {
                        strws.m_vecWordTags[keyindex - 1] = ChooseSX(vecwords[min][keyindex - 1].wordTypes, 2);
                        vecwords[min][keyindex - 1].tagId = 2;
                    }
	        }
            }

            for(size_t j = 0; j != strws.m_vecWordTags.size(); ++ j)
            {
                if(strws.m_vecWordTags[j] == KB_LX && vecwords[min][j].tagId == 0 )
                    strws.m_vecWordTags[j] = 0;
            }
        }
//        cout<<"iRemove:"<<iRemove<< " "<<"iNumLX:"<<iNumLX<<" "<<"allMaxLen:"<<allMaxLen<<endl;

        vector<int>::iterator itWord,itTag;
        itTag = strws.m_vecWordTags.begin();
        for(itWord = strws.m_vecWordIDs.begin(); itWord != strws.m_vecWordIDs.end(); ++ itWord)
        {
            if (m_setRemoveWordIDs.find(*itWord) != m_setRemoveWordIDs.end())
            {
                strws.m_vecWordIDs.erase(itWord);
                itWord --;
                strws.m_vecWordTags.erase(itTag);
                itTag --;
            }
            itTag ++;
        }

	pthread_mutex_unlock(&mutex_yws);
	return true;

}

bool XWordSeg::Segment(StringWS &strws, WsSegmenter *ws)
{
	strws.m_vecWordIDs.reserve(30);
	strws.m_vecWordIDs.resize(0);
	strws.m_vecWordTags.reserve(30);
	strws.m_vecWordTags.resize(0);

	uint16_t utf16Buffer[YWS_MAX_WORD_LENGTH];
	uint16_t utf16BufferSize = YWS_MAX_WORD_LENGTH;

        if (ws == NULL)
        {
            cerr<<"bws pointer is NULL!"<<endl;
            return false;
        }


	vector<vector<ws::WORD_T> > vecwords;
	int size = ws->utf8ToUtf16(
			strws.m_str.c_str(), strws.m_str.length(), utf16Buffer, utf16BufferSize);
	bool success = ws->segment(utf16Buffer, size, vecwords);
	if (!success)
	{
		return false;
	}
        int max = 0;
        int min = vecwords.size() - 1;
        vector<int16_t> keindexs;
  //      pthread_mutex_lock(&mutex_yws);
        success = m_pKEE->extract(vecwords[max], ws,keindexs);
  //      pthread_mutex_unlock(&mutex_yws);

	if (!success)
	{
		return false;
	}
        
        int iRemove = 0;
        int iNumLX = 0;
        char szWordUtf8[YWS_MAX_WORD_LENGTH * 3];
        for(size_t i = 0; i != vecwords[min].size(); ++ i)
        {
                vecwords[min][i].tagId = 0;
		int word_id = vecwords[min][i].wordId;
                if (word_id < 0)
		{
			int size = ws->utf16ToUtf8(
					vecwords[min][i].pBuf, vecwords[min][i].length, szWordUtf8, YWS_MAX_WORD_LENGTH * 3);
			szWordUtf8[size] = 0;
			if (size == 0)
				word_id = 0;
			else
			{
				for (size = 0; isalnum(szWordUtf8[size]); size ++) ;
				if (szWordUtf8[size])
					word_id = hash_string(szWordUtf8);
				else
					word_id = hash_alphanum(szWordUtf8);
			}
		}
		if (word_id)
                {
			strws.m_vecWordIDs.push_back(word_id);
                        int tag = 0;
                        map<int, int>::const_iterator iter = m_mapBwsWordtags.find(word_id);
                        if (iter != m_mapBwsWordtags.end())
                                tag = iter->second;
                        else
                                tag = ChooseSX(vecwords[min][i].wordTypes, 1);
                        if (tag == KB_LX)
                                iNumLX ++;
                        strws.m_vecWordTags.push_back(tag);
                        if(m_setRemoveWordIDs.find(word_id) != m_setRemoveWordIDs.end())
                                iRemove ++;
                }
        }
        
        string kuohao = ")";
        int kuohaoID = hash_string(kuohao.c_str());
        int allLen = vecwords[min].size();
        int allMaxLen = vecwords[max].size() - iRemove;
        if (kuohaoID == vecwords[min][allLen - 1].wordId)
            iRemove -= 2;

        if (!((iRemove < 1 && iNumLX > 2) && (allMaxLen > 3)))
        {
            for(size_t i=0; i < keindexs.size(); ++ i )
            {
                int key = keindexs[i];
                int keyindex = FindIndex(vecwords, key); 
                if (keyindex == -1) return false;
                vecwords[min][keyindex].tagId = 1;
                if (vecwords[max][key].subWordNum > 1)
	        {	
		    int tag = strws.m_vecWordTags[keyindex];
		    if(tag == KB_LX)
                    {
                        strws.m_vecWordTags[keyindex - 1] = ChooseSX(vecwords[min][keyindex - 1].wordTypes, 2);
                        vecwords[min][keyindex - 1].tagId = 2;
                    }
	        }
            }

            for(size_t j = 0; j != strws.m_vecWordTags.size(); ++ j)
            {
                if(strws.m_vecWordTags[j] == KB_LX && vecwords[min][j].tagId == 0 )
                    strws.m_vecWordTags[j] = 0;
            }
        }
//        cout<<"iRemove:"<<iRemove<< " "<<"iNumLX:"<<iNumLX<<" "<<"allMaxLen:"<<allMaxLen<<endl;
        vector<int>::iterator itWord,itTag;
        itTag = strws.m_vecWordTags.begin();
        for(itWord = strws.m_vecWordIDs.begin(); itWord != strws.m_vecWordIDs.end(); ++ itWord)
        {
            if (m_setRemoveWordIDs.find(*itWord) != m_setRemoveWordIDs.end())
            {
                strws.m_vecWordIDs.erase(itWord);
                itWord --;
                strws.m_vecWordTags.erase(itTag);
                itTag --;
            }
            itTag ++;
        }
	return true;

}

bool XWordSeg::Segment(const string &strUTF8Text, vector<string> &vecWords, vector<int> &vecTags, WsSegmenter *ws)
{
	return Segment(strUTF8Text.c_str(), strUTF8Text.length(), vecWords, vecTags, ws);
}

bool XWordSeg::Segment(const string &strUTF8Text, vector<int> &vecWordIDs, vector<int> &vecTags)
{
	return Segment(strUTF8Text.c_str(), strUTF8Text.length(), vecWordIDs, vecTags);
}
bool XWordSeg::SegmentRM(const std::string &strUTF8Text, std::vector<int> &vecWordIDs, std::vector<int> &vecWordTags)
{
        return SegmentRM(strUTF8Text.c_str(), strUTF8Text.length(), vecWordIDs, vecWordTags);
}

bool XWordSeg::SegmentRM(const std::string &strUTF8Text, std::vector<int> &vecWordIDs, std::vector<int> &vecWordTags, WsSegmenter *ws)
{
        return SegmentRM(strUTF8Text.c_str(), strUTF8Text.length(), vecWordIDs, vecWordTags, ws);
}

const string XWordSeg::Utf8ToGbk(const string &strUtf8Text)
{
	return utf8_to_gbk(strUtf8Text);
}

const string XWordSeg::GbkToUtf8(const string &strGBKText)
{
	return gbk_to_utf8(strGBKText);
}

bool XWordSeg::SetRemoveWordList(const vector<string> &vecWords)
{
	m_setRemoveWords.clear();
	m_setRemoveWordIDs.clear();

	for (size_t i = 0; i < vecWords.size(); i ++)
	{
		m_setRemoveWords.insert(vecWords[i]);
		m_setRemoveWordIDs.insert(hash_string(vecWords[i].c_str()));
	}

	return true;
}

bool XWordSeg::LoadBwsWordtags(const string &config, ws::WsSegmenter *ws)
{
	string line;

	ifstream ifBwsWordtags(config.c_str());
        if (!ifBwsWordtags)
                return false;
	while (getline(ifBwsWordtags, line))
	{
		size_t t = line.find('\t');
		if (t == string::npos) continue;
                int nWordId = 0;
                string word = line.substr(0, t);
                if (ws == NULL)
                        nWordId = m_pYWS->getWordId(word.c_str(), word.length());
                else
                        nWordId = ws->getWordId(word.c_str(), word.length());
		if (nWordId < 0)
                        nWordId = hash_string(word.c_str());
                string strWordTag = line.substr(t + 1);
                int nWordTag = 0;
                if (strWordTag == "CP_PP") nWordTag = KB_PP;
                else if (strWordTag == "CPLX") nWordTag = KB_LX;

		m_mapBwsWordtags[nWordId] = nWordTag;
	}
	ifBwsWordtags.close();

	return true;
}

}
